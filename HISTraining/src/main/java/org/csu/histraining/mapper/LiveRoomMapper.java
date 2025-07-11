package org.csu.histraining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.csu.histraining.entity.LiveRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {
    // 自定义查询方法
    @Select("SELECT " +
            "live_room_id AS id, " +
            "live_room_title AS title, " +
            "live_room_cover_url AS coverUrl, "+
            "live_room_status AS status, " +
            "stream_key, " +
            "stream_url, " +
            "hls_url, " +
            "flv_url, " +
            "live_room_view_count AS viewCount, " +
            "live_room_like_count AS likeCount, " +
            "live_room_start_time AS startTime, " +
            "live_room_end_time AS endTime, " +
            "live_room_creat_time AS createAt," +
            "live_room_update_time AS updateAt " +
            "FROM live_room WHERE live_room_status = 1 ORDER BY live_room_view_count DESC LIMIT #{limit}")
    List<LiveRoom> findHotLiveRooms(@Param("limit") int limit);
}
