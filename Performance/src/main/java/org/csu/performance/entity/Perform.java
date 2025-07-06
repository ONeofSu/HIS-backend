package org.csu.performance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 业绩实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("perform")
public class Perform {

    /**
     * 业绩主键
     */
    @TableId(value = "perform_id", type = IdType.AUTO)
    private Long performId;

    /**
     * 业绩(工作)名称
     */
    @TableField("perform_name")
    private String performName;

    /**
     * 业绩内容
     */
    @TableField("perform_content")
    private String performContent;

    /**
     * 业绩类型
     */
    @TableField("perform_type_id")
    private Long performTypeId;

    /**
     * 业绩状态:0:草稿1:已提交2:已通过3:已驳回
     */
    @TableField("perform_status")
    private Integer performStatus;

    /**
     * 提交日期
     */
    @TableField("perform_time")
    private LocalDateTime performTime;

    /**
     * 审核评语
     */
    @TableField("perform_comment")
    private String performComment;

    /**
     * 提交人ID
     */
    @TableField("submit_user_id")
    private Long submitUserId;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @TableField("audit_by")
    private Long auditBy;
} 