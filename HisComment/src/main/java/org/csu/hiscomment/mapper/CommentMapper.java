package org.csu.hiscomment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.hiscomment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
} 