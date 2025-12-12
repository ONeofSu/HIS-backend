package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course_rating")
public class CourseRating {
    @TableId(value = "rating_id", type = IdType.AUTO)
    private int ratingId;
    @TableField("course_id")
    private int courseId;
    @TableField("user_id")
    private int userId;
    @TableField("rating_value")
    private int ratingValue;
    @TableField("created_at")
    private LocalDateTime createdAt;
} 