package org.csu.hiscomment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private int commentId;
    private String targetType;
    private int targetId;
    private int userId;
    private String content;
    private int parentId;
    private int rootId;
    private int likeCount;
    private Date createTime;
    private Date updateTime;
    private int isDeleted;
    
    // 敏感词检测相关字段
    private String originalContent;    // 原始内容（未过滤）
    private String sensitiveWords;     // 检测到的敏感词，逗号分隔
    private String sensitiveTypes;     // 敏感词类型，逗号分隔
    private int isFiltered;            // 是否被过滤过（0-否，1-是）
    private int filterLevel;           // 过滤级别（0-无，1-轻度，2-中度，3-重度）
} 