package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course_resource")
public class CourseResource {
    @TableId(value = "course_resource_id", type = IdType.AUTO)
    private int courseResourceId;
    @TableField("course_id")
    private int courseId;
    @TableField("course_resource_type")
    private int courseResourceType;
    @TableField("course_resource_order")
    private int courseResourceOrder;
    @TableField("course_resource_title")
    private String courseResourceTitle;
    @TableField("course_resource_content")
    private String courseResourceContent;
    @TableField("course_resource_metadata")
    private String courseResourceMetadata;
    @TableField("course_resource_time")
    private LocalDateTime courseResourceTime;
    @TableField("course_resource_isvalid")
    private boolean courseResourceIsvalid;
    @TableField(exist = false)
    private String courseResourceTypeName;

    public String getCourseResourceTypeName() {
        switch (courseResourceType) {
            case 0: return "视频";
            case 1: return "文件";
            default: return "未知";
        }
    }
} 