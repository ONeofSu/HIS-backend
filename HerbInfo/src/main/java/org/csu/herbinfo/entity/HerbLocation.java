package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("herb_location")
public class HerbLocation {
    @TableId("location_id")
    private int id;
    @TableField(value = "herb_id")
    private int herbId;
    @TableField(value = "location_count")
    private int count;
    @TableField(value = "district_id")
    private String districtId;
    @TableField(value = "street_id")
    private String streetId;
    @TableField(value = "location_longitude")
    private double longitude;
    @TableField(value = "location_latitude")
    private double latitude;
}
