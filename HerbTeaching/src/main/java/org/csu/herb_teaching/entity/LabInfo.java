package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lab_info")
public class LabInfo {
    @TableId(value = "lab_info_id", type = IdType.AUTO)
    private int id;
    @TableField(value = "lab_id")
    private int labId;
    @TableField(value = "herb_id")
    private int herbId;
    @TableField(value = "lab_info_goal")
    private String goal;
    @TableField(value = "lab_info_summary")
    private String summary;
    @TableField(value = "lab_info_isvalid")
    private Boolean isValid;
}