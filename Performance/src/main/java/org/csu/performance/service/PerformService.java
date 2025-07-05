package org.csu.performance.service;

import org.csu.performance.DTO.PerformAuditDTO;
import org.csu.performance.DTO.PerformDTO;
import org.csu.performance.VO.PageResultVO;
import org.csu.performance.VO.PerformVO;
import org.csu.performance.VO.ResultVO;

/**
 * 业绩Service接口
 */
public interface PerformService {

    /**
     * 新增业绩
     */
    ResultVO<PerformVO> createPerform(PerformDTO performDTO, Long submitUserId);

    /**
     * 分页查询业绩列表
     */
    ResultVO<PageResultVO<PerformVO>> getPerformList(String keyword, Long performTypeId, 
                                                    Integer performStatus, Integer page, Integer size);

    /**
     * 根据ID查询业绩详情
     */
    ResultVO<PerformVO> getPerformById(Long performId);

    /**
     * 更新业绩信息
     */
    ResultVO<PerformVO> updatePerform(Long performId, PerformDTO performDTO, Long submitUserId);

    /**
     * 提交业绩
     */
    ResultVO<PerformVO> submitPerform(Long performId, Long submitUserId);

    /**
     * 删除业绩
     */
    ResultVO<Void> deletePerform(Long performId, Long submitUserId);

    /**
     * 分页查询待审核业绩列表
     */
    ResultVO<PageResultVO<PerformVO>> getPendingPerformList(Integer page, Integer size);

    /**
     * 审核业绩
     */
    ResultVO<PerformVO> auditPerform(Long performId, PerformAuditDTO auditDTO, Long auditUserId);

    /**
     * 分页查询用户提交的业绩列表
     */
    ResultVO<PageResultVO<PerformVO>> getMyPerformList(Long submitUserId, Integer page, Integer size);

    /**
     * 检查业绩是否存在
     */
    boolean checkPerformExists(Long performId);

    /**
     * 检查用户是否有权限操作业绩
     */
    boolean checkUserPermission(Long performId, Long userId);
} 