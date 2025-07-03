package org.csu.histraining.service;

import org.csu.histraining.entity.LiveRoom;
import org.csu.histraining.mapper.LiveRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface LiveStreamService {
    public LiveRoom createLiveRoom(LiveRoom liveRoom);
    public LiveRoom startLiveStream(Long roomId);
    public LiveRoom endLiveStream(Long roomId);
    public List<LiveRoom> getActiveLiveRooms(int page,int size);
    public List<LiveRoom> getHotLiveRooms(int limit);

    public void addViewCount(Long roomId);
    public boolean isStreamKeyValid(String streamKey,String token,String expire);

    public void handleStreamPublish(String app,String stream);
    public void handleStreamClose(String app,String stream);
    public Map<String, Object> getSrsServerInfo();
}
