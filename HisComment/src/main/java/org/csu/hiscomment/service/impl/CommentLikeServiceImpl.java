package org.csu.hiscomment.service.impl;

import org.csu.hiscomment.entity.CommentLike;
import org.csu.hiscomment.mapper.CommentLikeMapper;
import org.csu.hiscomment.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {
    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Override
    public boolean like(int commentId, int userId) {
        // 判断是否已点赞
        if (isLiked(commentId, userId)) {
            return false;
        }
        CommentLike like = new CommentLike();
        like.setCommentId(commentId);
        like.setUserId(userId);
        like.setCreateTime(new java.util.Date());
        return commentLikeMapper.insert(like) > 0;
    }

    @Override
    public boolean unlike(int commentId, int userId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<CommentLike> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("comment_id", commentId).eq("user_id", userId);
        return commentLikeMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean isLiked(int commentId, int userId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<CommentLike> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("comment_id", commentId).eq("user_id", userId);
        return commentLikeMapper.selectCount(wrapper) > 0;
    }
} 