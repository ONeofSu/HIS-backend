package org.csu.performance.VO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业绩附件VO
 */
@Data
public class PerformFileVO {

    /**
     * 文档主键
     */
    private Long performFileId;

    /**
     * 所属业绩
     */
    private Long performId;

    /**
     * 文档名称
     */
    private String performFileName;

    /**
     * 文档描述
     */
    private String performFileDes;

    /**
     * 文档类型
     */
    private String performFileType;

    /**
     * 文件URL
     */
    private String performFileUrl;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 是否有效
     */
    private Boolean performFileIsvalid;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 上传人ID
     */
    private Long uploadBy;

    /**
     * 业绩名称（用于附件详情）
     */
    private String performName;
} 