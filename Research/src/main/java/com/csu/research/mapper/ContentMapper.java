package com.csu.research.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.research.entity.Content;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContentMapper extends BaseMapper<Content> {
    @Select("SELECT * FROM content JOIN topic ON content.topic_id = topic.topic_id " +
            "WHERE content.content_name LIKE CONCAT('%',#{query},'%') " +
            "AND content.content_isvalid=1 " +
            "AND topic.topic_isvalid=1;")
    List<Content> selectByQuery(String query);
}
