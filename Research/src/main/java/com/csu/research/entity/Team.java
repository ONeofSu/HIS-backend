package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("team")
public class Team {
    @TableId(value = "team_id", type = IdType.AUTO)
    private Long teamId;

    @TableField("team_name")
    private String teamName;

    @TableField("team_time")
    private LocalDateTime teamTime;

    @TableField("team_des")
    private String teamDes;
}
