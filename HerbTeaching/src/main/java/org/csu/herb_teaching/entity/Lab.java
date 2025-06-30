package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("lab")
public class Lab {
    @TableId(value = "lab_id", type = IdType.AUTO)
    private int id;
    @TableField(value = "course_id")
    private int courseId;
    @TableField(value = "user_id")
    private int userId;
    @TableField(value = "lab_submit_time")
    private Timestamp submitTime;
    @TableField(value = "lab_end_time")
    private Timestamp endTime;
    @TableField(value = "lab_des")
    private String des;
}
