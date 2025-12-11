package org.csu.histraining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("feedback")
public class Feedback {
    @TableId(value = "feedback_id",type = IdType.AUTO)
    private int id;
    @TableField(value = "material_id")
    private int materialId;
    @TableField(value = "user_id")
    private int userId;
    @TableField(value = "feedback_content")
    private String content;
    @TableField(value = "submit_time")
    private Timestamp time;
    @TableField(value = "rating")
    private int rating;
}
