package org.csu.performance.controller;

import org.csu.performance.VO.ResultVO;
import org.csu.performance.service.PerformStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业绩统计Controller
 */
@RestController
@RequestMapping("/performances")
public class PerformStatisticsController {

    @Autowired
    private PerformStatisticsService performStatisticsService;

    /**
     * 获取业绩统计信息
     */
    @GetMapping("/statistics")
    public ResultVO<Object> getStatistics() {
        return performStatisticsService.getStatistics();
    }
} 