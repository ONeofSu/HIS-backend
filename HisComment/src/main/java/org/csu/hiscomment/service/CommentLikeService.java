package org.csu.hiscomment.service;
 
public interface CommentLikeService {
    boolean like(int commentId, int userId);
    boolean unlike(int commentId, int userId);
    boolean isLiked(int commentId, int userId);
} 