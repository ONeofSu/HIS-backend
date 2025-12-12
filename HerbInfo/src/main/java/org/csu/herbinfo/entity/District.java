package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("district")
public class District {
    @TableId("district_id")
    private int id;
    @TableField(value = "district_name")
    private String name;
}
