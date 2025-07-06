package org.csu.performance.controller;

import org.csu.performance.DTO.PerformAuditDTO;
import org.csu.performance.DTO.PerformDTO;
import org.csu.performance.VO.PageResultVO;
import org.csu.performance.VO.PerformVO;
import org.csu.performance.VO.ResultVO;
import org.csu.performance.service.PerformService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.csu.performance.feign.UserFeignClient;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 业绩管理Controller
 */
@RestController
@RequestMapping("/performances")
@Validated
public class PerformController {

    @Autowired
    private PerformService performService;
    
    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 新增业绩 (教师权限)
     */
    @PostMapping
    public ResultVO<PerformVO> createPerform(@Valid @RequestBody PerformDTO performDTO, 
                                           HttpServletRequest request) {
        Long submitUserId = getUserIdFromRequest(request);
        if (submitUserId == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performService.createPerform(performDTO, submitUserId);
    }

    /**
     * 获取业绩列表（分页和筛选）
     */
    @GetMapping
    public ResultVO<PageResultVO<PerformVO>> getPerformList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long performTypeId,
            @RequestParam(required = false) Integer performStatus,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return performService.getPerformList(keyword, performTypeId, performStatus, page, size);
    }

    /**
     * 获取单个业绩详情
     */
    @GetMapping("/{performId}")
    public ResultVO<PerformVO> getPerformById(@PathVariable @NotNull Long performId) {
        return performService.getPerformById(performId);
    }

    /**
     * 更新业绩信息 (教师权限)
     */
    @PutMapping("/{performId}")
    public ResultVO<PerformVO> updatePerform(@PathVariable @NotNull Long performId,
                                           @Valid @RequestBody PerformDTO performDTO,
                                           HttpServletRequest request) {
        Long submitUserId = getUserIdFromRequest(request);
        if (submitUserId == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performService.updatePerform(performId, performDTO, submitUserId);
    }

    /**
     * 提交业绩 (教师权限)
     */
    @PostMapping("/{performId}/submit")
    public ResultVO<PerformVO> submitPerform(@PathVariable @NotNull Long performId,
                                           HttpServletRequest request) {
        Long submitUserId = getUserIdFromRequest(request);
        if (submitUserId == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performService.submitPerform(performId, submitUserId);
    }

    /**
     * 删除业绩 (教师权限)
     */
    @DeleteMapping("/{performId}")
    public ResultVO<Void> deletePerform(@PathVariable @NotNull Long performId,
                                       HttpServletRequest request) {
        Long submitUserId = getUserIdFromRequest(request);
        if (submitUserId == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performService.deletePerform(performId, submitUserId);
    }

    /**
     * 获取待审核业绩列表 (管理员权限)
     */
    @GetMapping("/pending")
    public ResultVO<PageResultVO<PerformVO>> getPendingPerformList(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        // 权限验证：仅管理员可访问
        if (!isAdmin(request)) {
            return ResultVO.error("权限不足，仅管理员可访问");
        }
        return performService.getPendingPerformList(page, size);
    }

    /**
     * 审核业绩 (管理员权限)
     */
    @PostMapping("/{performId}/audit")
    public ResultVO<PerformVO> auditPerform(@PathVariable @NotNull Long performId,
                                          @Valid @RequestBody PerformAuditDTO auditDTO,
                                          HttpServletRequest request) {
        // 权限验证：仅管理员可访问
        if (!isAdmin(request)) {
            return ResultVO.error("权限不足，仅管理员可访问");
        }
        Long auditUserId = getUserIdFromRequest(request);
        if (auditUserId == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performService.auditPerform(performId, auditDTO, auditUserId);
    }

    /**
     * 获取用户提交的业绩列表
     */
    @GetMapping("/my")
    public ResultVO<PageResultVO<PerformVO>> getMyPerformList(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        Long submitUserId = getUserIdFromRequest(request);
        if (submitUserId == null) {
            return ResultVO.error("用户ID不能为空");
        }
        return performService.getMyPerformList(submitUserId, page, size);
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

    /**
     * 检查是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 通过Feign客户端获取用户角色等级
            Integer userRoleLevel = userFeignClient.getUserRoleLevelByToken(token);
            return userRoleLevel != null && userRoleLevel == 3; // 3表示管理员
        } catch (Exception e) {
            return false;
        }
    }
} 