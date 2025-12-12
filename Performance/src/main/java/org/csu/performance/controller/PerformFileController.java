package org.csu.performance.controller;

import org.csu.performance.DTO.PerformFileDTO;
import org.csu.performance.DTO.PerformFileUpdateDTO;
import org.csu.performance.VO.PerformFileVO;
import org.csu.performance.VO.ResultVO;
import org.csu.performance.service.PerformFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.csu.performance.feign.UserFeignClient;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 业绩附件Controller
 */
@RestController
@RequestMapping("/files")
@Validated
public class PerformFileController {

    @Autowired
    private PerformFileService performFileService;
    
    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 为业绩新增附件 (教师权限)
     */
    @PostMapping("/performances/{performId}")
    public ResultVO<PerformFileVO> uploadFile(@PathVariable @NotNull Long performId,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam(required = false) String performFileDes,
                                            HttpServletRequest request) {
        Long uploadBy = getUserIdFromRequest(request);
        if (uploadBy == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performFileService.uploadFile(performId, file, performFileDes, uploadBy);
    }

    /**
     * 为业绩新增附件（URL方式）(教师权限)
     */
    @PostMapping("/performances/{performId}/url")
    public ResultVO<PerformFileVO> uploadFileByUrl(@PathVariable @NotNull Long performId,
                                                  @Valid @RequestBody PerformFileDTO performFileDTO,
                                                  HttpServletRequest request) {
        Long uploadBy = getUserIdFromRequest(request);
        if (uploadBy == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performFileService.uploadFileByUrl(performId, performFileDTO, uploadBy);
    }

    /**
     * 获取业绩的附件列表
     */
    @GetMapping("/performances/{performId}")
    public ResultVO<List<PerformFileVO>> getFilesByPerformId(@PathVariable @NotNull Long performId) {
        return performFileService.getFilesByPerformId(performId);
    }

    /**
     * 获取单个附件详情
     */
    @GetMapping("/{performFileId}")
    public ResultVO<PerformFileVO> getFileById(@PathVariable @NotNull Long performFileId) {
        return performFileService.getFileById(performFileId);
    }

    /**
     * 更新附件信息 (教师权限)
     */
    @PutMapping("/{performFileId}")
    public ResultVO<PerformFileVO> updateFile(@PathVariable @NotNull Long performFileId,
                                            @Valid @RequestBody PerformFileUpdateDTO performFileUpdateDTO,
                                            HttpServletRequest request) {
        Long uploadBy = getUserIdFromRequest(request);
        if (uploadBy == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performFileService.updateFile(performFileId, performFileUpdateDTO, uploadBy);
    }

    /**
     * 删除附件 (教师权限)
     */
    @DeleteMapping("/{performFileId}")
    public ResultVO<Void> deleteFile(@PathVariable @NotNull Long performFileId,
                                   HttpServletRequest request) {
        Long uploadBy = getUserIdFromRequest(request);
        if (uploadBy == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performFileService.deleteFile(performFileId, uploadBy);
    }



    /**
     * 从JWT token中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 通过Feign客户端获取用户ID
            return (long) userFeignClient.getUserIdByToken(token);
        } catch (Exception e) {
            return null;
        }
    }
} 