package org.csu.performance.VO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 业绩VO
 */
@Data
public class PerformVO {

    /**
     * 业绩主键
     */
    private Long performId;

    /**
     * 业绩(工作)名称
     */
    private String performName;

    /**
     * 业绩内容
     */
    private String performContent;

    /**
     * 业绩类型ID
     */
    private Long performTypeId;

    /**
     * 业绩类型名称
     */
    private String performTypeName;

    /**
     * 业绩状态
     */
    private Integer performStatus;

    /**
     * 业绩状态名称
     */
    private String performStatusName;

    /**
     * 提交日期
     */
    private LocalDateTime performTime;

    /**
     * 审核评语
     */
    private String performComment;

    /**
     * 提交人ID
     */
    private Long submitUserId;

    /**
     * 提交人姓名
     */
    private String submitUserName;

    /**
     * 提交人角色
     */
    private String submitUserRole;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    private Long auditBy;

    /**
     * 附件数量
     */
    private Integer fileCount;

    /**
     * 附件列表
     */
    private List<PerformFileVO> files;
} 