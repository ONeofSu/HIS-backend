package org.csu.hiscomment.service;

import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import java.util.List;
import org.csu.hiscomment.entity.Comment;

public interface CommentService {
    CommentVO addComment(CommentDTO dto, int userId);
    List<CommentVO> listComments(String targetType, int targetId, String sort, int page, int size, Integer userId);
    boolean likeComment(int commentId, int userId);
    boolean unlikeComment(int commentId, int userId);
    boolean deleteComment(int commentId, int userId, boolean isAdmin);
    CommentVO getCommentDetail(int commentId, Integer userId);
    Comment getCommentById(int commentId);
    
    // 敏感词过滤相关方法
    /**
     * 批量过滤已存在的评论
     */
    int filterExistingComments();
    
    /**
     * 过滤指定评论
     */
    boolean filterComment(int commentId);
    
    /**
     * 获取需要过滤的评论数量
     */
    int getCommentsNeedFilterCount();
} 