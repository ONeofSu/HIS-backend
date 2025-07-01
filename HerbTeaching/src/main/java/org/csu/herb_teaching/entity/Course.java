package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId(value = "course_id", type = IdType.AUTO)
    private int courseId;
    @TableField("course_name")
    private String courseName;
    @TableField("cover_image_url")
    private String coverImageUrl;
    @TableField("course_type")
    private int courseType;
    @TableField("course_object")
    private int courseObject;
    @TableField("teacher_id")
    private int teacherId;
    @TableField("course_start_time")
    private LocalDateTime courseStartTime;
    @TableField("course_end_time")
    private LocalDateTime courseEndTime;
    @TableField("course_des")
    private String courseDes;
    @TableField("course_average_rating")
    private BigDecimal courseAverageRating;
    @TableField("course_rating_count")
    private int courseRatingCount;
}