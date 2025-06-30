package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("lab_resource")
public class LabResource {
    @TableId(value = "lab_resource_id", type = IdType.AUTO)
    private int id;
    
    @TableField("lab_id")
    private int labId;
    
    @TableField("lab_resource_type")
    private int type; // 0:文本 1:图片 2:视频 3:文档
    
    @TableField("lab_resource_order")
    private int resourceOrder;
    
    @TableField("lab_resource_title")
    private String title;
    
    @TableField("lab_resource_content")
    private String content; // 文本内容或文件路径
    
    @TableField("lab_resource_metadata")
    private String metadata; // JSON格式的额外信息
    
    @TableField("lab_resource_time")
    private Timestamp time;
    
    @TableField("lab_resource_isvalid")
    private Boolean isValid;
} 