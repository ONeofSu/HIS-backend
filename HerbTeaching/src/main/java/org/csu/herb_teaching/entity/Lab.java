package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lab")
public class Lab {
    @TableId(value = "lab_id", type = IdType.AUTO)
    private int labId;
    @TableField("course_id")
    private int courseId;
    @TableField("lab_name")
    private String labName;
    @TableField("lab_steps")
    private String labSteps;
    @TableField("lab_order")
    private int labOrder;
}
