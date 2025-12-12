package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("growth_audit")
public class GrowthAudit {
    @TableId(value = "audit_id",type = IdType.AUTO)
    private Long auditId;
    private Integer growthId;
    @TableField(value = "user_id")
    private Integer auditorUserId;
    private Integer auditResult;
    private String auditDes;
    private LocalDateTime auditTime;
}
