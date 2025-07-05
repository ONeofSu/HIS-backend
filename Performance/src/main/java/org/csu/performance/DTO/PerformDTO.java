package org.csu.performance.DTO;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 业绩DTO
 */
@Data
public class PerformDTO {

    /**
     * 业绩ID（更新时使用）
     */
    private Long performId;

    /**
     * 业绩(工作)名称
     */
    @NotBlank(message = "业绩名称不能为空")
    private String performName;

    /**
     * 业绩内容
     */
    private String performContent;

    /**
     * 业绩类型
     */
    private Long performTypeId;

    /**
     * 提交日期
     */
    private LocalDateTime performTime;
} 