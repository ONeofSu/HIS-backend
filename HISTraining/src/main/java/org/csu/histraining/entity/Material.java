package org.csu.histraining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;


@Data
@TableName("material")
public class Material {
    @TableId(value = "material_id",type = IdType.AUTO)
    private int id;
    @TableField(value = "material_title")
    private String title;
    @TableField(value = "material_type")
    private String type;
    @TableField(value = "material_des")
    private String des;
    @TableField(value = "herb_id")
    private int herbId;
    @TableField(value = "user_id")
    private int userId;
    @TableField(value = "material_time")
    private Timestamp time;
    @TableField(value = "use_count")
    private int count;
    @TableField("material_isvalid")
    private boolean isvalid;
}
