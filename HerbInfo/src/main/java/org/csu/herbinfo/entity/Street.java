package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("street")
public class Street {
    @TableId("street_id")
    private int streetId;
    @TableField(value = "district_id")
    private int districtId;
    @TableField(value = "street_name")
    private String name;
}
