package org.csu.histraining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.csu.histraining.entity.LiveRoom;
import org.csu.histraining.entity.LiveStream;
import org.csu.histraining.mapper.LiveRoomMapper;
import org.csu.histraining.mapper.LiveStreamMapper;
import org.csu.histraining.service.LiveStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LiveStreamImpl implements LiveStreamService {
    @Autowired
    LiveRoomMapper liveRoomMapper;
    @Autowired
    LiveStreamMapper liveStreamMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${live.srs.server-url}")
    private String srsServerUrl;

    @Value("${live.srs.api-url}")
    private String srsApiUrl;

    @Value("${live.srs.http-flv-url}")
    private String httpFlvUrl;

    @Value("${live.srs.hls-url}")
    private String hlsUrl;

    @Value("${live.push.key-check-enabled}")
    private boolean keyCheckEnabled;

    @Value("${live.push.auth-expire}")
    private long authExpire;

    @Value("${live.push.auth-key}")
    private String authKey;

    private RestTemplate restTemplate = new RestTemplate();

    //生成密钥
    private String generateStreamKey(int userId){
        String baseKey = userId + "_" + System.currentTimeMillis();
        return DigestUtils.md5DigestAsHex(baseKey.getBytes());
    }

    //生成推流地址
    private String generatePushUrl(String streamKey){
        StringBuilder sb = new StringBuilder(srsServerUrl);
        sb.append("/").append(streamKey);

        //推流验证
        if(keyCheckEnabled){
            long expireTimestamp = System.currentTimeMillis() / 1000 + authExpire;
            String authString = streamKey + "-" + expireTimestamp + "-" + authKey;
            String authToken = DigestUtils.md5DigestAsHex(authString.getBytes());

            sb.append("?auth_key=").append(authToken)
                    .append("&expire=").append(expireTimestamp);
        }

        return sb.toString();
    }

    //直播间创建
    @Override
    @Transactional
    public LiveRoom createLiveRoom(LiveRoom liveRoom) {
        //推流秘钥生成
        String streamKey = generateStreamKey(liveRoom.getUserId());
        liveRoom.setStreamKey(streamKey);

        String pushUrl = generatePushUrl(streamKey);
        liveRoom.setStreamUrl(pushUrl);
        liveRoom.setHlsUrl(hlsUrl + "/" + streamKey + ".m3u8");
        liveRoom.setFlvUrl(httpFlvUrl + "/" + streamKey + ".flv");

        liveRoom.setStatus(LiveRoom.NOT_LIVE);
        liveRoom.setViewCount(0L);
        liveRoom.setCreatedAt(LocalDateTime.now());
        liveRoom.setUpdatedAt(LocalDateTime.now());

        liveRoomMapper.insert(liveRoom);
        return liveRoom;
    }

    @Override
    @Transactional
    public LiveRoom startLiveStream(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null){
            throw new IllegalArgumentException("直播间不存在");
        }

        //更新状态
        liveRoom.setStartTime(LocalDateTime.now());
        liveRoom.setStatus(LiveRoom.LIVE);
        liveRoomMapper.updateById(liveRoom);

        //创建直播流
        LiveStream liveStream = new LiveStream();
        liveStream.setRoomId(roomId);
        liveStream.setStreamId(liveRoom.getStreamKey());
        liveStream.setProtocol("rtmp");
        liveStream.setStatus(LiveStream.ACTIVE);
        liveStream.setCreatedAt(LocalDateTime.now());
        liveStream.setUpdatedAt(LocalDateTime.now());
        liveStreamMapper.insert(liveStream);
        redisTemplate.opsForSet().add("his_live:active_rooms", String.valueOf(roomId));
        return liveRoom;
    }

    @Override
    @Transactional
    public LiveRoom endLiveStream(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null || liveRoom.getStatus() != LiveRoom.LIVE){
            throw new IllegalArgumentException("直播间不存在或未开播");
        }

        //更新直播间状态
        liveRoom.setEndTime(LocalDateTime.now());
        liveRoom.setStatus(LiveRoom.LIVE_END);
        liveRoomMapper.updateById(liveRoom);

        //更新直播流状态
        QueryWrapper<LiveStream> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", roomId).eq("live_stream_status",1);

        LiveStream liveStream = liveStreamMapper.selectOne(queryWrapper);
        if(liveStream!=null){
            liveStream.setStatus(LiveStream.END);
            liveStream.setUpdatedAt(LocalDateTime.now());
            liveStreamMapper.updateById(liveStream);
        }
        redisTemplate.opsForSet().remove("his_live:active_rooms", String.valueOf(roomId));
        return liveRoom;
    }

    @Override
    public List<LiveRoom> getActiveLiveRooms(int page, int size) {
        Page<LiveRoom> pageParam = new Page<>(page,size);
        QueryWrapper<LiveRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_status",LiveRoom.LIVE).orderByDesc("live_room_view_count");
        return liveRoomMapper.selectPage(pageParam,queryWrapper).getRecords();
    }

    @Override
    public List<LiveRoom> getHotLiveRooms(int limit) {
        return liveRoomMapper.findHotLiveRooms(limit);
    }

    @Override
    public void addViewCount(Long roomId) {
        String key = "live:room:" + roomId + ":view_count";
        redisTemplate.opsForValue().increment(key);

        if (Math.random() < 0.25) {  // 25%概率同步，减少数据库压力
            String countStr = redisTemplate.opsForValue().get(key);
            if (countStr != null) {
                long count = Long.parseLong(countStr);

                LiveRoom room = new LiveRoom();
                room.setId(roomId);
                room.setViewCount(count);
                liveRoomMapper.updateById(room);
            }
        }
    }

    @Override
    public boolean isStreamKeyValid(String streamKey, String token, String expire) {
        if(!keyCheckEnabled){
            return true;
        }

        try {
            long expireTimestamp = Long.parseLong(expire);
            long currentTime = System.currentTimeMillis() / 1000;

            // 检查是否过期
            if (currentTime > expireTimestamp) {
                return false;
            }

            // 验证token
            String authString = streamKey + "-" + expire + "-" + authKey;
            String calculatedToken = DigestUtils.md5DigestAsHex(authString.getBytes());

            return calculatedToken.equals(token);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("验证推流秘钥失败");
            return false;
        }
    }

    @Override
    public void handleStreamPublish(String app, String stream) {
        try{
            QueryWrapper<LiveRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stream_key", stream);

            LiveRoom liveRoom = liveRoomMapper.selectOne(queryWrapper);
            if(liveRoom!=null && liveRoom.getStatus() == LiveRoom.NOT_LIVE){
                startLiveStream(liveRoom.getId());
                log.info("直播流发布成功: app={}, stream={}, roomId={}", app, stream, liveRoom.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("处理流发布回调异常");
        }
    }

    @Override
    public void handleStreamClose(String app, String stream) {
        try {
            QueryWrapper<LiveRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stream_key", stream);

            LiveRoom liveRoom = liveRoomMapper.selectOne(queryWrapper);
            if(liveRoom!=null && liveRoom.getStatus() == LiveRoom.LIVE){
                endLiveStream(liveRoom.getId());
                log.info("直播流关闭: app={}, stream={}, roomId={}", app, stream, liveRoom.getId());
            }
        }catch (Exception e){
            log.error("处理流关闭回调异常", e);
        }
    }

    @Override
    public Map<String, Object> getSrsServerInfo() {
        try {
            String url = srsApiUrl + "/v1/summaries";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("获取SRS服务器信息异常", e);
            return Collections.emptyMap();
        }
    }
}
