package org.csu.hiscomment.service.impl;

import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.mapper.CommentMapper;
import org.csu.hiscomment.service.CommentService;
import org.csu.hiscomment.service.CommentLikeService;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentLikeService commentLikeService;
    @Autowired
    private org.csu.hiscomment.feign.UserFeignClient userFeignClient;
    @Autowired
    private org.csu.hiscomment.mapper.CommentLikeMapper commentLikeMapper;
    @Autowired
    private SensitiveWordFilter sensitiveWordFilter;

    @Override
    public CommentVO addComment(CommentDTO dto, int userId) {
        // 0. 校验用户是否存在
        if (!userFeignClient.isUserExist(userId)) {
            return null;
        }
        
        // 1. 敏感词检测和过滤
        String originalContent = dto.getContent();
        SensitiveWordFilter.SensitiveCheckResult checkResult = sensitiveWordFilter.checkSensitiveWords(originalContent);
        
        String filteredContent = originalContent;
        int filterLevel = 0;
        boolean isFiltered = false;
        
        if (checkResult.hasSensitive()) {
            // 基于敏感级别的处理策略
            List<String> sensitiveWords = checkResult.sensitiveWords();
            List<SensitiveWordFilter.SensitiveType> types = checkResult.sensitiveTypes();
            
            // 检查是否有重度敏感词（级别3）
            boolean hasHighLevelSensitive = sensitiveWords.stream()
                .anyMatch(word -> sensitiveWordFilter.getSensitiveWordLevel(word) >= 3);
            
            // 检查是否有政治/恐怖/极端敏感词（无论级别）
            boolean hasSevereTypeSensitive = types.stream().anyMatch(type -> 
                type == SensitiveWordFilter.SensitiveType.POLITICAL || 
                type == SensitiveWordFilter.SensitiveType.TERRORISM || 
                type == SensitiveWordFilter.SensitiveType.EXTREMIST);
            
            if (hasHighLevelSensitive || hasSevereTypeSensitive) {
                // 重度敏感词或政治/恐怖/极端敏感词，直接拒绝发布
                log.warn("检测到重度敏感内容，拒绝发布评论。敏感词: {}, 类型: {}", 
                    checkResult.getSensitiveWordsString(), checkResult.getSensitiveTypesString());
                return null;
            } else {
                // 轻度敏感词，进行过滤
                filteredContent = sensitiveWordFilter.filterSensitiveWords(originalContent);
                isFiltered = true;
                
                // 根据敏感词数量和级别确定过滤级别
                int maxLevel = sensitiveWords.stream()
                    .mapToInt(word -> sensitiveWordFilter.getSensitiveWordLevel(word))
                    .max()
                    .orElse(1);
                
                int sensitiveCount = sensitiveWords.size();
                
                // 综合考虑敏感词数量和级别
                if (maxLevel == 1 && sensitiveCount <= 3) {
                    filterLevel = 1; // 轻度
                } else if (maxLevel == 2 && sensitiveCount <= 2) {
                    filterLevel = 2; // 中度
                } else if (maxLevel == 1 && sensitiveCount > 3) {
                    filterLevel = 2; // 轻度敏感词过多，升级为中度
                } else {
                    filterLevel = 3; // 重度
                }
                
                log.info("评论包含轻度敏感内容，已过滤。敏感词: {}, 过滤级别: {}", 
                    checkResult.getSensitiveWordsString(), filterLevel);
            }
        }
        
        // 2. 组装Comment对象
        Comment comment = new Comment();
        comment.setTargetType(dto.getTargetType());
        comment.setTargetId(dto.getTargetId());
        comment.setUserId(userId);
        comment.setContent(filteredContent);
        // 只有在检测到敏感词且进行了过滤时，才保存原始内容
        if (isFiltered) {
            comment.setOriginalContent(originalContent);
        } else {
            comment.setOriginalContent(null); // 无敏感词时，original_content为null
        }
        comment.setSensitiveWords(checkResult.getSensitiveWordsString());
        comment.setSensitiveTypes(checkResult.getSensitiveTypesString());
        comment.setIsFiltered(isFiltered ? 1 : 0);
        comment.setFilterLevel(filterLevel);
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
        vo.setFiltered(isFiltered);
        vo.setFilterLevel(filterLevel);
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
            if (comment == null) return false;
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
            if (comment == null) return false;
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

    @Override
    public int filterExistingComments() {
        // 获取所有未过滤且未删除的评论
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("is_filtered", 0)
               .eq("is_deleted", 0)
               .isNotNull("content")
               .ne("content", "");
        List<Comment> comments = commentMapper.selectList(wrapper);
        int filteredCount = 0;
        for (Comment comment : comments) {
            if (filterComment(comment.getCommentId())) {
                filteredCount++;
            }
        }
        return filteredCount;
    }
    
    @Override
    public boolean filterComment(int commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() == 1) {
            return false;
        }
        if (comment.getIsFiltered() == 1) {
            // 已过滤，直接返回
            return false;
        }
        SensitiveWordFilter.SensitiveCheckResult checkResult = sensitiveWordFilter.checkSensitiveWords(comment.getContent());
        if (checkResult.hasSensitive()) {
            // 基于敏感级别的处理策略
            List<String> sensitiveWords = checkResult.sensitiveWords();
            List<SensitiveWordFilter.SensitiveType> types = checkResult.sensitiveTypes();
            
            // 检查是否有重度敏感词（级别3）
            boolean hasHighLevelSensitive = sensitiveWords.stream()
                .anyMatch(word -> sensitiveWordFilter.getSensitiveWordLevel(word) >= 3);
            
            // 检查是否有政治/恐怖/极端敏感词（无论级别）
            boolean hasSevereTypeSensitive = types.stream().anyMatch(type -> 
                type == SensitiveWordFilter.SensitiveType.POLITICAL || 
                type == SensitiveWordFilter.SensitiveType.TERRORISM || 
                type == SensitiveWordFilter.SensitiveType.EXTREMIST);
            
            if (hasHighLevelSensitive || hasSevereTypeSensitive) {
                // 重度敏感词或政治/恐怖/极端敏感词，标记为删除
                comment.setIsDeleted(1);
                comment.setUpdateTime(new Date());
                log.warn("历史评论包含重度敏感内容，标记为已删除。评论ID: {}, 敏感词: {}, 类型: {}", 
                    comment.getCommentId(), checkResult.getSensitiveWordsString(), checkResult.getSensitiveTypesString());
            } else {
                // 轻度敏感词，进行过滤
                String filteredContent = sensitiveWordFilter.filterSensitiveWords(comment.getContent());
                comment.setContent(filteredContent);
                comment.setOriginalContent(comment.getContent()); // 保存原始内容
                comment.setSensitiveWords(checkResult.getSensitiveWordsString());
                comment.setSensitiveTypes(checkResult.getSensitiveTypesString());
                comment.setIsFiltered(1);
                
                // 根据敏感词数量和级别确定过滤级别
                int maxLevel = sensitiveWords.stream()
                    .mapToInt(word -> sensitiveWordFilter.getSensitiveWordLevel(word))
                    .max()
                    .orElse(1);
                
                int sensitiveCount = sensitiveWords.size();
                
                // 综合考虑敏感词数量和级别
                if (maxLevel == 1 && sensitiveCount <= 3) {
                    comment.setFilterLevel(1); // 轻度
                } else if (maxLevel == 2 && sensitiveCount <= 2) {
                    comment.setFilterLevel(2); // 中度
                } else if (maxLevel == 1 && sensitiveCount > 3) {
                    comment.setFilterLevel(2); // 轻度敏感词过多，升级为中度
                } else {
                    comment.setFilterLevel(3); // 重度
                }
                
                comment.setUpdateTime(new Date());
                log.info("历史评论包含轻度敏感内容，已过滤。评论ID: {}, 敏感词: {}, 过滤级别: {}", 
                    comment.getCommentId(), checkResult.getSensitiveWordsString(), comment.getFilterLevel());
            }
            
            int result = commentMapper.updateById(comment);
            return result > 0;
        } else {
            // 没有敏感词也要标记为已过滤
            comment.setIsFiltered(1);
            comment.setUpdateTime(new java.util.Date());
            int result = commentMapper.updateById(comment);
            return result > 0;
        }
    }
    
    @Override
    public int getCommentsNeedFilterCount() {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("is_filtered", 0)
               .eq("is_deleted", 0)
               .isNotNull("content")
               .ne("content", "");
        return commentMapper.selectCount(wrapper).intValue();
    }
} 