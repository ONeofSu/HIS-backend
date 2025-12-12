package org.csu.performance.VO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业绩种类VO
 */
@Data
public class PerformTypeVO {

    /**
     * 业绩种类主键
     */
    private Long performTypeId;

    /**
     * 业绩种类名称
     */
    private String performTypeName;

    /**
     * 业绩种类描述
     */
    private String performTypeDesc;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
} 