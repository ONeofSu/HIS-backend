package org.csu.herb_teaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course_herb_link")
public class CourseHerbLink {
    @TableId(value = "link_id", type = IdType.AUTO)
    private int linkId;
    @TableField("course_id")
    private int courseId;
    @TableField("herb_id")
    private int herbId;
} 