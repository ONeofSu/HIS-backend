package org.csu.performance.service.impl;

import org.csu.performance.VO.ResultVO;
import org.csu.performance.service.PerformStatisticsService;
import org.csu.performance.mapper.PerformMapper;
import org.csu.performance.mapper.PerformTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业绩统计Service实现类
 */
@Service
public class PerformStatisticsServiceImpl implements PerformStatisticsService {

    @Autowired
    private PerformMapper performMapper;

    @Autowired
    private PerformTypeMapper performTypeMapper;

    @Override
    public ResultVO<Object> getStatistics(boolean excludeDraft) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 基础统计
        Integer totalCount = excludeDraft ? performMapper.countTotalPerformsExcludeDraft() : performMapper.countTotalPerforms();
        Integer pendingCount = performMapper.countPerformsByStatus(1); // 已提交
        Integer approvedCount = performMapper.countPerformsByStatus(2); // 已通过
        Integer rejectedCount = performMapper.countPerformsByStatus(3); // 已驳回
        
        statistics.put("totalCount", totalCount != null ? totalCount : 0);
        statistics.put("pendingCount", pendingCount != null ? pendingCount : 0);
        statistics.put("approvedCount", approvedCount != null ? approvedCount : 0);
        statistics.put("rejectedCount", rejectedCount != null ? rejectedCount : 0);
        
        // 类型分布
        List<Map<String, Object>> typeDistributionList = excludeDraft ? 
            performMapper.getTypeDistributionExcludeDraft() : performMapper.getTypeDistribution();
        Map<String, Object> typeDistribution = new HashMap<>();
        for (Map<String, Object> item : typeDistributionList) {
            String typeName = (String) item.get("perform_type_name");
            Long count = (Long) item.get("count");
            typeDistribution.put(typeName, count);
        }
        statistics.put("typeDistribution", typeDistribution);
        
        // 月度趋势（最近6个月）
        List<Map<String, Object>> monthlyTrendList = excludeDraft ? 
            performMapper.getMonthlyTrendExcludeDraft() : performMapper.getMonthlyTrend();
        Map<String, Object> monthlyTrend = new HashMap<>();
        for (Map<String, Object> item : monthlyTrendList) {
            String month = (String) item.get("month");
            Long count = (Long) item.get("count");
            monthlyTrend.put(month, count);
        }
        statistics.put("monthlyTrend", monthlyTrend);
        
        return ResultVO.success("统计信息获取成功", statistics);
    }
} 