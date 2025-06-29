package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("team_member")
public class TeamMember {
    @TableId(value = "team_member_id", type = IdType.AUTO)
    private Long teamMemberId;

    @TableField("team_id")
    private Long teamId;

    @TableField("user_id")
    private Long userId;

    @TableField("team_member_name")
    private String teamMemberName;

    @TableField("team_member_des")
    private String team_member_des;

    @TableField("team_member_iscaptain")
    private Short teamMemberIsCaptain;

}
