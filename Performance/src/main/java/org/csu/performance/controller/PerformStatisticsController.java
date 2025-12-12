package org.csu.performance.controller;

import org.csu.performance.VO.ResultVO;
import org.csu.performance.service.PerformStatisticsService;
import org.csu.performance.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 业绩统计Controller
 */
@RestController
@RequestMapping("/performances")
public class PerformStatisticsController {

    @Autowired
    private PerformStatisticsService performStatisticsService;
    
    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 获取业绩统计信息
     */
    @GetMapping("/statistics")
    public ResultVO<Object> getStatistics(HttpServletRequest request) {
        // 获取用户角色
        Integer userRoleLevel = getUserRoleLevel(request);
        
        // 如果是管理员（角色等级为3或4），统计不包含草稿数据
        boolean excludeDraft = (userRoleLevel != null && (userRoleLevel == 3 || userRoleLevel == 4));
        
        return performStatisticsService.getStatistics(excludeDraft);
    }
    
    /**
     * 从JWT token中获取用户角色等级
     */
    private Integer getUserRoleLevel(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 通过Feign客户端获取用户角色等级
            return userFeignClient.getUserRoleLevelByToken(token);
        } catch (Exception e) {
            return null;
        }
    }
} 