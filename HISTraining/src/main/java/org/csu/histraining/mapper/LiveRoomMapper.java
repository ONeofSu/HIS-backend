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
    @Select("SELECT * FROM live_room WHERE live_room_status = 1 ORDER BY live_room_view_count DESC LIMIT #{limit}")
    List<LiveRoom> findHotLiveRooms(@Param("limit") int limit);
}
