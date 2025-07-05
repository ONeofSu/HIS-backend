package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("document")
public class Document {
    @TableId(value = "document_id", type = IdType.AUTO)
    private Long documentId;

    @TableField("topic_id")
    private Long topicId;

    @TableField("user_id")
    private int userId;

    @TableField("auth_id")
    private Long authId;

    @TableField("document_name")
    private String documentName;

    @TableField("document_des")
    private String documentDes;

    @TableField("document_type")
    private String documentType;

    @TableField("document_url")
    private String documentUrl;

    @TableField("document_time")
    private LocalDateTime documentTime;

    @TableField("document_isvalid")
    private Boolean documentIsValid;

}
