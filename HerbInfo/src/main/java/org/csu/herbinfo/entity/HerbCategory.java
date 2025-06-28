package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("herb_category")
public class HerbCategory {
    @TableId("category_id")
    private int id;
    @TableField(value = "category_name")
    private String name;
}
