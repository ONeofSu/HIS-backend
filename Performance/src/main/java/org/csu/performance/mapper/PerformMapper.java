package org.csu.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.performance.VO.PerformVO;

import java.util.List;
import java.util.Map;

/**
 * 业绩Mapper接口
 */
@Mapper
public interface PerformMapper extends BaseMapper<org.csu.performance.entity.Perform> {

    /**
     * 分页查询业绩列表（包含类型名称和用户信息）
     */
    IPage<PerformVO> selectPerformPage(Page<PerformVO> page, 
                                      @Param("keyword") String keyword,
                                      @Param("performTypeId") Long performTypeId,
                                      @Param("performStatus") Integer performStatus);

    /**
     * 分页查询业绩列表（排除草稿状态）
     */
    IPage<PerformVO> selectPerformPageExcludeDraft(Page<PerformVO> page, 
                                                  @Param("keyword") String keyword,
                                                  @Param("performTypeId") Long performTypeId,
                                                  @Param("performStatus") Integer performStatus);

    /**
     * 分页查询待审核业绩列表
     */
    IPage<PerformVO> selectPendingPerformPage(Page<PerformVO> page);

    /**
     * 分页查询用户提交的业绩列表
     */
    IPage<PerformVO> selectMyPerformPage(Page<PerformVO> page, @Param("submitUserId") Long submitUserId);

    /**
     * 根据ID查询业绩详情（包含类型名称和用户信息）
     */
    PerformVO selectPerformDetailById(@Param("performId") Long performId);

    /**
     * 检查业绩名称是否存在
     */
    Integer checkPerformNameExists(@Param("performName") String performName, @Param("performId") Long performId);

    /**
     * 统计总业绩数量
     */
    Integer countTotalPerforms();

    /**
     * 统计总业绩数量（排除草稿状态）
     */
    Integer countTotalPerformsExcludeDraft();

    /**
     * 根据状态统计业绩数量
     */
    Integer countPerformsByStatus(@Param("performStatus") Integer performStatus);

    /**
     * 获取类型分布统计
     */
    List<Map<String, Object>> getTypeDistribution();

    /**
     * 获取类型分布统计（排除草稿状态）
     */
    List<Map<String, Object>> getTypeDistributionExcludeDraft();

    /**
     * 获取月度趋势统计
     */
    List<Map<String, Object>> getMonthlyTrend();

    /**
     * 获取月度趋势统计（排除草稿状态）
     */
    List<Map<String, Object>> getMonthlyTrendExcludeDraft();

    /**
     * 根据业绩类型ID删除相关业绩
     */
    Integer deleteByPerformTypeId(@Param("performTypeId") Long performTypeId);
} 