package org.csu.performance.DTO;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 业绩附件DTO
 */
@Data
public class PerformFileDTO {

    /**
     * 附件描述
     */
    private String performFileDes;
    
    /**
     * 文件URL（用于URL方式上传）
     */
    @NotBlank(message = "文件URL不能为空")
    private String performFileUrl;
    
    /**
     * 文件名称（用于URL方式上传）
     */
    @NotBlank(message = "文件名称不能为空")
    private String performFileName;
    
    /**
     * 文件类型（用于URL方式上传）
     */
    private String performFileType;
    
    /**
     * 文件大小（用于URL方式上传）
     */
    private Long fileSize;
} 