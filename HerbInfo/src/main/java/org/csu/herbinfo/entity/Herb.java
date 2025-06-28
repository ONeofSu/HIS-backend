package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("herb")
public class Herb {
    @TableId("herb_id")
    private int id;
    @TableField(value = "herb_name")
    private String name;
    @TableField(value = "herb_origin")
    private String origin;
    @TableField(value = "herb_img")
    private String image;
    @TableField(value = "herb_des1")
    private String des1;
    @TableField(value = "herb_des2")
    private String des2;
    @TableField(value = "herb_isvalid")
    private boolean isvalid;
}
