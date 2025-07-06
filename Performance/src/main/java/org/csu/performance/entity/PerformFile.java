package org.csu.performance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 业绩附件实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("perform_file")
public class PerformFile {

    /**
     * 文档主键
     */
    @TableId(value = "perform_file_id", type = IdType.AUTO)
    private Long performFileId;

    /**
     * 所属业绩
     */
    @TableField("perform_id")
    private Long performId;

    /**
     * 文档名称
     */
    @TableField("perform_file_name")
    private String performFileName;

    /**
     * 文档描述
     */
    @TableField("perform_file_des")
    private String performFileDes;

    /**
     * 文档类型
     */
    @TableField("perform_file_type")
    private String performFileType;

    /**
     * 文件URL
     */
    @TableField("perform_file_url")
    private String performFileUrl;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 是否有效
     */
    @TableField("perform_file_isvalid")
    private Boolean performFileIsvalid;

    /**
     * 上传时间
     */
    @TableField(value = "upload_time", fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;

    /**
     * 上传人ID
     */
    @TableField("upload_by")
    private Long uploadBy;
} 