package org.csu.histraining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("content")
public class Content {
    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final int FILE_TYPE = 2;
    public static final int ERROR_TYPE = -1;

    @TableId(value = "content_id",type = IdType.AUTO)
    private int id;
    @TableField(value = "material_id")
    private int materialId;
    @TableField(value = "content_type")
    private int type;
    @TableField(value = "content_order")
    private int sortOrder;
    @TableField(value = "content_des")
    private String des;
    @TableField(value = "content_url")
    private String url;
    @TableField(value = "content_isvalid")
    private boolean isvalid;
}
