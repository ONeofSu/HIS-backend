package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class ContentType {
    @TableId(value = "content_type_id", type = IdType.AUTO)
    private Long contentTypeId;

    @TableField("content_type_name")
    private String contentTypeName;
}
