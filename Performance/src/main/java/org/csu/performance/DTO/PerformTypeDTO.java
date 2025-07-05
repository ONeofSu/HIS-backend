package org.csu.performance.DTO;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 业绩种类DTO
 */
@Data
public class PerformTypeDTO {

    /**
     * 业绩种类ID（更新时使用）
     */
    private Long performTypeId;

    /**
     * 业绩种类名称
     */
    @NotBlank(message = "业绩种类名称不能为空")
    private String performTypeName;

    /**
     * 业绩种类描述
     */
    private String performTypeDesc;

    /**
     * 排序
     */
    private Integer sortOrder;
} 