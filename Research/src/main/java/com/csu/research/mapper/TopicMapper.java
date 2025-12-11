package com.csu.research.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.research.entity.Topic;
import com.csu.research.vo.TopicVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TopicMapper extends BaseMapper<Topic> {

    @Select("""
       select * from topic join team on topic.team_id = team.team_id
       where topic.topic_name like CONCAT('%', #{query}, '%')
       AND topic.topic_isvalid = 1
       """
    )
    List<TopicVo> searchTopic(String query);
}
