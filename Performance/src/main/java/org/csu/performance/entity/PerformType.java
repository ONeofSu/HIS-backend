package org.csu.performance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 业绩种类实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("perform_type")
public class PerformType {

    /**
     * 业绩种类主键
     */
    @TableId(value = "perform_type_id", type = IdType.AUTO)
    private Long performTypeId;

    /**
     * 业绩种类名称
     */
    @TableField("perform_type_name")
    private String performTypeName;

    /**
     * 业绩种类描述
     */
    @TableField("perform_type_desc")
    private String performTypeDesc;

    /**
     * 是否启用
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

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
} 