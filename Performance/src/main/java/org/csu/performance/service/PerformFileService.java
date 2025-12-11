package org.csu.performance.service;

import org.csu.performance.DTO.PerformFileDTO;
import org.csu.performance.DTO.PerformFileUpdateDTO;
import org.csu.performance.VO.PerformFileVO;
import org.csu.performance.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 业绩附件Service接口
 */
public interface PerformFileService {

    /**
     * 上传附件（文件上传方式）
     */
    ResultVO<PerformFileVO> uploadFile(Long performId, MultipartFile file, String performFileDes, Long uploadBy);

    /**
     * 上传附件（URL方式）
     */
    ResultVO<PerformFileVO> uploadFileByUrl(Long performId, PerformFileDTO performFileDTO, Long uploadBy);

    /**
     * 获取业绩的附件列表
     */
    ResultVO<List<PerformFileVO>> getFilesByPerformId(Long performId);

    /**
     * 获取附件详情
     */
    ResultVO<PerformFileVO> getFileById(Long performFileId);

    /**
     * 更新附件信息
     */
    ResultVO<PerformFileVO> updateFile(Long performFileId, PerformFileUpdateDTO performFileUpdateDTO, Long uploadBy);

    /**
     * 删除附件
     */
    ResultVO<Void> deleteFile(Long performFileId, Long uploadBy);



    /**
     * 检查附件是否存在
     */
    boolean checkFileExists(Long performFileId);

    /**
     * 检查用户是否有权限操作附件
     */
    boolean checkFilePermission(Long performFileId, Long userId);
} 