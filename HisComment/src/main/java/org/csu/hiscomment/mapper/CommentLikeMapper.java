package org.csu.hiscomment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.hiscomment.entity.CommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    @Select({
        "<script>",
        "SELECT comment_id FROM comment_like ",
        "WHERE user_id = #{userId} ",
        "AND comment_id IN ",
        "<foreach collection='commentIds' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    List<Integer> getLikedCommentIds(@Param("userId") int userId, @Param("commentIds") List<Integer> commentIds);
} 