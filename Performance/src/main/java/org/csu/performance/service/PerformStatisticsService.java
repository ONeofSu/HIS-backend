package org.csu.performance.service;

import org.csu.performance.VO.ResultVO;

/**
 * 业绩统计Service接口
 */
public interface PerformStatisticsService {

    /**
     * 获取业绩统计信息
     * @param excludeDraft 是否排除草稿状态的数据
     */
    ResultVO<Object> getStatistics(boolean excludeDraft);
} 