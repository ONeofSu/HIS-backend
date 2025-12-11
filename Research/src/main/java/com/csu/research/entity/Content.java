package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("content")
public class Content {
    @TableId(value = "content_id", type = IdType.AUTO)
    private Long contentId;

    @TableField("topic_id")
    private Long topicId;

    @TableField("user_id")
    private int userId;

    @TableField("content_type_id")
    private Long contentTypeId;

    @TableField("auth_id")
    private Long authId;

    @TableField("content_name")
    private String contentName;

    @TableField("content_des")
    private String contentDes;

    @TableField("content_time")
    private LocalDateTime contentTime;

    @TableField("content_isvalid")
    private boolean contentIsValid;
}
