package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("course")
public class Course {
    @TableId(value="course_id", type = IdType.AUTO)
    private int id;
    @TableField(value="course_name")
    private String name;
    @TableField(value="course_start_time")
    private Timestamp startTime;
    @TableField(value="course_end_time")
    private Timestamp endTime;
    @TableField(value="course_des")
    private String des;
    @TableField(value="course_type")
    private int courseType;
    @TableField(value="course_object")
    private int courseObject;
}