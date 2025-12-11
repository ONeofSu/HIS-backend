package org.csu.performance.service.impl;

import org.csu.performance.DTO.PerformFileDTO;
import org.csu.performance.DTO.PerformFileUpdateDTO;
import org.csu.performance.VO.PerformFileVO;
import org.csu.performance.VO.ResultVO;
import org.csu.performance.entity.Perform;
import org.csu.performance.entity.PerformFile;
import org.csu.performance.mapper.PerformFileMapper;
import org.csu.performance.mapper.PerformMapper;
import org.csu.performance.service.PerformFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 业绩附件Service实现类
 */
@Service
public class PerformFileServiceImpl implements PerformFileService {

    @Autowired
    private PerformFileMapper performFileMapper;

    @Autowired
    private PerformMapper performMapper;

    // 文件存储路径
    private static final String UPLOAD_PATH = "uploads/performances/";

    @Override
    @Transactional
    public ResultVO<PerformFileVO> uploadFile(Long performId, MultipartFile file, String performFileDes, Long uploadBy) {
        // 检查业绩是否存在
        Perform perform = performMapper.selectById(performId);
        if (perform == null) {
            return ResultVO.error("业绩不存在");
        }

        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            return ResultVO.error("文件不能为空");
        }

        // 检查文件大小（限制为50MB）
        if (file.getSize() > 50 * 1024 * 1024) {
            return ResultVO.error("文件大小不能超过50MB");
        }

        try {
            // 创建上传目录
            String uploadDir = UPLOAD_PATH + performId + "/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;
            String filePath = uploadDir + fileName;

            // 保存文件
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());

            // 创建附件记录
            PerformFile performFile = new PerformFile();
            performFile.setPerformId(performId);
            performFile.setPerformFileName(originalFilename != null ? originalFilename : fileName);
            performFile.setPerformFileDes(performFileDes);
            performFile.setPerformFileType(fileExtension.replace(".", ""));
            performFile.setPerformFileUrl(filePath);
            performFile.setFileSize(file.getSize());
            performFile.setPerformFileIsvalid(true);
            performFile.setUploadBy(uploadBy);
            


            performFileMapper.insert(performFile);

            // 返回附件信息
            return getFileById(performFile.getPerformFileId());

        } catch (IOException e) {
            return ResultVO.error("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public ResultVO<List<PerformFileVO>> getFilesByPerformId(Long performId) {
        // 检查业绩是否存在
        if (performMapper.selectById(performId) == null) {
            return ResultVO.error("业绩不存在");
        }

        List<PerformFileVO> files = performFileMapper.selectFilesByPerformId(performId);
        return ResultVO.success("附件列表获取成功", files);
    }

    @Override
    public ResultVO<PerformFileVO> getFileById(Long performFileId) {
        if (!checkFileExists(performFileId)) {
            return ResultVO.error("附件不存在");
        }

        PerformFileVO fileVO = performFileMapper.selectFileDetailById(performFileId);
        if (fileVO == null) {
            return ResultVO.error("附件不存在");
        }

        return ResultVO.success("附件详情获取成功", fileVO);
    }

    @Override
    @Transactional
    public ResultVO<PerformFileVO> updateFile(Long performFileId, PerformFileUpdateDTO performFileUpdateDTO, Long uploadBy) {
        if (!checkFileExists(performFileId)) {
            return ResultVO.error("附件不存在");
        }

        if (!checkFilePermission(performFileId, uploadBy)) {
            return ResultVO.error("无权限操作此附件");
        }

        PerformFile performFile = performFileMapper.selectById(performFileId);
        if (performFile == null) {
            return ResultVO.error("附件不存在");
        }

        // 只更新用户实际提交的字段，未提交的字段保留原值
        boolean hasUpdate = false;
        
        if (performFileUpdateDTO.getPerformFileDes() != null) {
            performFile.setPerformFileDes(performFileUpdateDTO.getPerformFileDes());
            hasUpdate = true;
        }
        
        // 如果有字段更新，则保存
        if (hasUpdate) {
            performFileMapper.updateById(performFile);
        }

        return getFileById(performFileId);
    }

    @Override
    @Transactional
    public ResultVO<Void> deleteFile(Long performFileId, Long uploadBy) {
        if (!checkFileExists(performFileId)) {
            return ResultVO.error("附件不存在");
        }

        if (!checkFilePermission(performFileId, uploadBy)) {
            return ResultVO.error("无权限操作此附件");
        }

        PerformFile performFile = performFileMapper.selectById(performFileId);
        if (performFile == null) {
            return ResultVO.error("附件不存在");
        }

        // 删除物理文件
        try {
            File file = new File(performFile.getPerformFileUrl());
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // 记录日志但不影响删除操作
        }

        // 删除数据库记录
        performFileMapper.deleteById(performFileId);

        return ResultVO.success("附件删除成功");
    }



    @Override
    public boolean checkFileExists(Long performFileId) {
        if (performFileId == null) {
            return false;
        }
        return performFileMapper.selectById(performFileId) != null;
    }

    @Override
    public boolean checkFilePermission(Long performFileId, Long userId) {
        if (performFileId == null || userId == null) {
            return false;
        }
        PerformFile performFile = performFileMapper.selectById(performFileId);
        return performFile != null && performFile.getUploadBy().equals(userId);
    }

    @Override
    @Transactional
    public ResultVO<PerformFileVO> uploadFileByUrl(Long performId, PerformFileDTO performFileDTO, Long uploadBy) {
        // 检查业绩是否存在
        Perform perform = performMapper.selectById(performId);
        if (perform == null) {
            return ResultVO.error("业绩不存在");
        }

        // 检查URL是否为空
        if (performFileDTO.getPerformFileUrl() == null || performFileDTO.getPerformFileUrl().trim().isEmpty()) {
            return ResultVO.error("文件URL不能为空");
        }

        // 检查文件名是否为空
        if (performFileDTO.getPerformFileName() == null || performFileDTO.getPerformFileName().trim().isEmpty()) {
            return ResultVO.error("文件名称不能为空");
        }

        try {
            // 创建附件记录
            PerformFile performFile = new PerformFile();
            performFile.setPerformId(performId);
            performFile.setPerformFileName(performFileDTO.getPerformFileName());
            performFile.setPerformFileDes(performFileDTO.getPerformFileDes());
            performFile.setPerformFileType(performFileDTO.getPerformFileType());
            performFile.setPerformFileUrl(performFileDTO.getPerformFileUrl());
            performFile.setFileSize(performFileDTO.getFileSize());
            performFile.setPerformFileIsvalid(true);
            performFile.setUploadBy(uploadBy);

            performFileMapper.insert(performFile);

            // 返回附件信息
            return getFileById(performFile.getPerformFileId());

        } catch (Exception e) {
            return ResultVO.error("附件上传失败：" + e.getMessage());
        }
    }
} 