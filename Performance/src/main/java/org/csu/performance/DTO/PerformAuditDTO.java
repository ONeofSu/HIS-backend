package org.csu.performance.DTO;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 业绩审核DTO
 */
@Data
public class PerformAuditDTO {

    /**
     * 审核结果：2-通过，3-驳回
     */
    @NotNull(message = "审核结果不能为空")
    private Integer auditResult;

    /**
     * 审核评语
     */
    private String performComment;
} 