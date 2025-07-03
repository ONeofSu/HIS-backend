package org.csu.hiscomment.service.impl;

import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.mapper.CommentMapper;
import org.csu.hiscomment.service.CommentService;
import org.csu.hiscomment.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentLikeService commentLikeService;
    @Autowired
    private org.csu.hiscomment.feign.UserFeignClient userFeignClient;
    @Autowired
    private org.csu.hiscomment.mapper.CommentLikeMapper commentLikeMapper;

    @Override
    public CommentVO addComment(CommentDTO dto, int userId) {
        // 0. 校验用户是否存在
        if (!userFeignClient.isUserExist(userId)) {
            return null;
        }
        // 1. 校验用户、目标存在性（略，实际可远程调用）
        // 2. 组装Comment对象
        Comment comment = new Comment();
        comment.setTargetType(dto.getTargetType());
        comment.setTargetId(dto.getTargetId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId());
        comment.setLikeCount(0);
        comment.setIsDeleted(0);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());

        // 3. 设置rootId
        if (dto.getParentId() == 0) {
            // 一级评论，rootId先临时设0，插入后再更新为自身ID
            comment.setRootId(0);
            commentMapper.insert(comment);
            comment.setRootId(comment.getCommentId());
            commentMapper.updateById(comment);
        } else {
            // 二级评论，rootId为父评论的rootId
            Comment parent = commentMapper.selectById(dto.getParentId());
            comment.setRootId(parent.getRootId());
            commentMapper.insert(comment);
        }

        // 4. 组装VO返回（用户信息可远程查）
        CommentVO vo = new CommentVO();
        vo.setCommentId(comment.getCommentId());
        vo.setUserId(comment.getUserId());
        vo.setContent(comment.getContent());
        vo.setParentId(comment.getParentId());
        vo.setRootId(comment.getRootId());
        vo.setLikeCount(comment.getLikeCount());
        vo.setCreateTime(comment.getCreateTime());
        vo.setMine(true); // 自己发布
        vo.setLiked(false);
        vo.setChildren(null);
        // 查询用户信息
        org.csu.hiscomment.VO.UserSimpleVO userVO = userFeignClient.getUserSimpleInfoBatch(java.util.Collections.singletonList(userId)).get(userId);
        vo.setUserName(userVO != null ? userVO.getUsername() : "");
        vo.setUserAvatar(userVO != null ? userVO.getAvatarUrl() : "");
        return vo;
    }

    @Override
    public List<CommentVO> listComments(String targetType, int targetId, String sort, int page, int size, Integer userId) {
        // 1. 分页查询一级评论
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("target_type", targetType)
                .eq("target_id", targetId)
                .eq("parent_id", 0)
                .eq("is_deleted", 0);
        if ("hot".equals(sort)) {
            wrapper.orderByDesc("like_count").orderByDesc("create_time");
        } else {
            wrapper.orderByDesc("create_time");
        }
        Page<Comment> pageObj = new Page<>(page, size);
        commentMapper.selectPage(pageObj, wrapper);
        List<Comment> records = pageObj.getRecords();
        List<CommentVO> voList = new ArrayList<>();
        // 收集所有userId和commentId
        HashSet<Integer> userIdSet = new HashSet<>();
        List<Integer> commentIdList = new ArrayList<>();
        List<Comment> allChildren = new ArrayList<>();
        for (Comment comment : records) {
            userIdSet.add(comment.getUserId());
            commentIdList.add(comment.getCommentId());
            // 查询子评论
            QueryWrapper<Comment> childWrapper = new QueryWrapper<>();
            childWrapper.eq("root_id", comment.getCommentId())
                        .ne("parent_id", 0)
                        .eq("is_deleted", 0)
                        .orderByAsc("create_time");
            List<Comment> children = commentMapper.selectList(childWrapper);
            allChildren.addAll(children);
            for (Comment child : children) {
                userIdSet.add(child.getUserId());
                commentIdList.add(child.getCommentId());
            }
        }
        // 批量查用户信息
        java.util.Map<Integer, org.csu.hiscomment.VO.UserSimpleVO> userInfoMap = userFeignClient.getUserSimpleInfoBatch(new ArrayList<>(userIdSet));
        // 批量查点赞状态
        List<Integer> likedCommentIds = new ArrayList<>();
        if (userId != null && !commentIdList.isEmpty()) {
            likedCommentIds = commentLikeMapper.getLikedCommentIds(userId, commentIdList);
        }
        // 组装VO
        int idx = 0;
        for (Comment comment : records) {
            CommentVO vo = new CommentVO();
            vo.setCommentId(comment.getCommentId());
            vo.setUserId(comment.getUserId());
            vo.setContent(comment.getContent());
            vo.setParentId(comment.getParentId());
            vo.setRootId(comment.getRootId());
            vo.setLikeCount(comment.getLikeCount());
            vo.setCreateTime(comment.getCreateTime());
            vo.setMine(userId != null && userId == comment.getUserId());
            vo.setLiked(userId != null && likedCommentIds.contains(comment.getCommentId()));
            org.csu.hiscomment.VO.UserSimpleVO userVO = userInfoMap.get(comment.getUserId());
            vo.setUserName(userVO != null ? userVO.getUsername() : "");
            vo.setUserAvatar(userVO != null ? userVO.getAvatarUrl() : "");
            // 子评论
            List<CommentVO> childVOList = new ArrayList<>();
            for (Comment child : allChildren) {
                if (child.getRootId() == comment.getCommentId()) {
                    CommentVO childVO = new CommentVO();
                    childVO.setCommentId(child.getCommentId());
                    childVO.setUserId(child.getUserId());
                    childVO.setContent(child.getContent());
                    childVO.setParentId(child.getParentId());
                    childVO.setRootId(child.getRootId());
                    childVO.setLikeCount(child.getLikeCount());
                    childVO.setCreateTime(child.getCreateTime());
                    childVO.setMine(userId != null && userId == child.getUserId());
                    childVO.setLiked(userId != null && likedCommentIds.contains(child.getCommentId()));
                    org.csu.hiscomment.VO.UserSimpleVO childUserVO = userInfoMap.get(child.getUserId());
                    childVO.setUserName(childUserVO != null ? childUserVO.getUsername() : "");
                    childVO.setUserAvatar(childUserVO != null ? childUserVO.getAvatarUrl() : "");
                    childVO.setChildren(null);
                    childVOList.add(childVO);
                }
            }
            vo.setChildren(childVOList);
            voList.add(vo);
            idx++;
        }
        return voList;
    }

    @Override
    public boolean likeComment(int commentId, int userId) {
        // 判断是否已点赞
        if (commentLikeService.isLiked(commentId, userId)) {
            return false;
        }
        boolean liked = commentLikeService.like(commentId, userId);
        if (liked) {
            // 点赞成功，更新评论表like_count
            Comment comment = commentMapper.selectById(commentId);
            comment.setLikeCount(comment.getLikeCount() + 1);
            commentMapper.updateById(comment);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlikeComment(int commentId, int userId) {
        if (!commentLikeService.isLiked(commentId, userId)) {
            return false;
        }
        boolean unliked = commentLikeService.unlike(commentId, userId);
        if (unliked) {
            // 取消点赞成功，更新评论表like_count
            Comment comment = commentMapper.selectById(commentId);
            comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
            commentMapper.updateById(comment);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteComment(int commentId, int userId, boolean isAdmin) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() == 1) {
            return false;
        }
        // 只有本人或管理员可删
        if (!isAdmin && comment.getUserId() != userId) {
            return false;
        }
        // 逻辑删除本条评论
        comment.setIsDeleted(1);
        commentMapper.updateById(comment);
        // 如果是一级评论，删除所有二级评论
        if (comment.getParentId() == 0) {
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("root_id", commentId).ne("parent_id", 0);
            List<Comment> children = commentMapper.selectList(wrapper);
            for (Comment child : children) {
                child.setIsDeleted(1);
                commentMapper.updateById(child);
            }
        }
        return true;
    }

    @Override
    public CommentVO getCommentDetail(int commentId, Integer userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() == 1) {
            return null;
        }
        CommentVO vo = new CommentVO();
        vo.setCommentId(comment.getCommentId());
        vo.setUserId(comment.getUserId());
        vo.setContent(comment.getContent());
        vo.setParentId(comment.getParentId());
        vo.setRootId(comment.getRootId());
        vo.setLikeCount(comment.getLikeCount());
        vo.setCreateTime(comment.getCreateTime());
        vo.setMine(userId != null && userId == comment.getUserId());
        if (userId != null) {
            List<Integer> liked = commentLikeMapper.getLikedCommentIds(userId, java.util.Collections.singletonList(commentId));
            vo.setLiked(liked.contains(commentId));
        } else {
            vo.setLiked(false);
        }
        // 用户信息
        java.util.Map<Integer, org.csu.hiscomment.VO.UserSimpleVO> userInfoMap = userFeignClient.getUserSimpleInfoBatch(java.util.Collections.singletonList(comment.getUserId()));
        org.csu.hiscomment.VO.UserSimpleVO userVO = userInfoMap.get(comment.getUserId());
        vo.setUserName(userVO != null ? userVO.getUsername() : "");
        vo.setUserAvatar(userVO != null ? userVO.getAvatarUrl() : "");
        // 查询所有二级评论
        java.util.List<CommentVO> childVOList = new java.util.ArrayList<>();
        QueryWrapper<Comment> childWrapper = new QueryWrapper<>();
        childWrapper.eq("root_id", commentId)
                    .ne("parent_id", 0)
                    .eq("is_deleted", 0)
                    .orderByAsc("create_time");
        java.util.List<Comment> children = commentMapper.selectList(childWrapper);
        if (!children.isEmpty()) {
            // 批量查用户信息
            java.util.Set<Integer> childUserIds = new java.util.HashSet<>();
            for (Comment child : children) {
                childUserIds.add(child.getUserId());
            }
            java.util.Map<Integer, org.csu.hiscomment.VO.UserSimpleVO> childUserInfoMap = userFeignClient.getUserSimpleInfoBatch(new java.util.ArrayList<>(childUserIds));
            java.util.List<Integer> childCommentIds = new java.util.ArrayList<>();
            for (Comment child : children) {
                childCommentIds.add(child.getCommentId());
            }
            java.util.List<Integer> likedChildIds = (userId != null && !childCommentIds.isEmpty()) ? commentLikeMapper.getLikedCommentIds(userId, childCommentIds) : new java.util.ArrayList<>();
            for (Comment child : children) {
                CommentVO childVO = new CommentVO();
                childVO.setCommentId(child.getCommentId());
                childVO.setUserId(child.getUserId());
                childVO.setContent(child.getContent());
                childVO.setParentId(child.getParentId());
                childVO.setRootId(child.getRootId());
                childVO.setLikeCount(child.getLikeCount());
                childVO.setCreateTime(child.getCreateTime());
                childVO.setMine(userId != null && userId == child.getUserId());
                childVO.setLiked(userId != null && likedChildIds.contains(child.getCommentId()));
                org.csu.hiscomment.VO.UserSimpleVO childUserVO = childUserInfoMap.get(child.getUserId());
                childVO.setUserName(childUserVO != null ? childUserVO.getUsername() : "");
                childVO.setUserAvatar(childUserVO != null ? childUserVO.getAvatarUrl() : "");
                childVO.setChildren(null);
                childVOList.add(childVO);
            }
        }
        vo.setChildren(childVOList);
        return vo;
    }

    @Override
    public Comment getCommentById(int commentId) {
        return commentMapper.selectById(commentId);
    }
} 