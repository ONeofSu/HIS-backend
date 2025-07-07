package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("herb_growth")
public class HerbGrowth {
    @TableId("growth_id")
    private int id;
    @TableField(value = "herb_id")
    private int herbId;
    @TableField(value = "batch_code")
    private String batchCode;
    private double wet;
    private double temperature;
    @TableField(value = "growth_des")
    private String des;
    @TableField(value = "growth_longitude")
    private double longitude;
    @TableField(value = "growth_latitude")
    private double latitude;
    @TableField(value = "user_id")
    private int userId;
    @TableField(value = "growth_time")
    private Timestamp recordTime;
    @TableField(value = "growth_img")
    private String imgUrl;
    private int growthAuditStatus;
}
