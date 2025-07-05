package org.csu.performance.service;

import org.csu.performance.VO.ResultVO;

/**
 * 业绩统计Service接口
 */
public interface PerformStatisticsService {

    /**
     * 获取业绩统计信息
     */
    ResultVO<Object> getStatistics();
} 