package org.csu.hiscomment.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private String targetType;
    private int targetId;
    private String content;
    private int parentId; // 一级评论为0，回复评论为被回复评论ID
} 