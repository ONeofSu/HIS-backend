package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("content_block")
public class ContentBlock {

    @TableId(value="content_block_id",type= IdType.AUTO)
    private Long contentBlockId;

    @TableField("content_id")
    private Long contentId;

    @TableField("content_block_type")
    private Integer contentBlockType;

    @TableField("content_block_order")
    private Integer contentBlockOrder;

    @TableField("content_block_des")
    private String contentBlockDes;

    @TableField("content_block_url")
    private String contentBlockUrl;

    @TableField("content_block_isvalid")
    private boolean contentBlockIsValid;
}
