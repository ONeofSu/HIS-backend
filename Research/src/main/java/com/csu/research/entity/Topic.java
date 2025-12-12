package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("topic")
public class Topic {
    @TableId(value = "topic_id", type = IdType.AUTO)
    private Long topicId;

    @TableField("topic_name")
    private String topicName;

    @TableField("team_id")
    private Long teamId;

    @TableField("topic_start_time")
    private LocalDateTime topicStartTime;

    @TableField("topic_end_time")
    private LocalDateTime topicEndTime;

    @TableField("topic_des")
    private String topicDes;

    @TableField("topic_status")
    private Integer topicStatus;

    @TableField("topic_isvalid")
    private boolean topicIsvalid;

}
