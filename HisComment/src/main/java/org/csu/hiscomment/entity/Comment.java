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
} 