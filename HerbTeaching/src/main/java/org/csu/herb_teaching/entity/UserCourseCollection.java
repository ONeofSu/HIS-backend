package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_course_collection")
public class UserCourseCollection {
    @TableId(value = "collection_id", type = IdType.AUTO)
    private int collectionId;
    @TableField("course_id")
    private int courseId;
    @TableField("user_id")
    private int userId;
    @TableField("created_at")
    private LocalDateTime createdAt;
} 