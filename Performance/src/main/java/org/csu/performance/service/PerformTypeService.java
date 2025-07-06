package org.csu.performance.service;

import org.csu.performance.DTO.PerformTypeDTO;
import org.csu.performance.VO.PerformTypeVO;
import org.csu.performance.VO.ResultVO;

import java.util.List;

/**
 * 业绩种类Service接口
 */
public interface PerformTypeService {

    /**
     * 获取业绩种类列表
     */
    ResultVO<List<PerformTypeVO>> getTypeList();

    /**
     * 新增业绩种类
     */
    ResultVO<PerformTypeVO> createType(PerformTypeDTO typeDTO);

    /**
     * 更新业绩种类
     */
    ResultVO<PerformTypeVO> updateType(Long performTypeId, PerformTypeDTO typeDTO);

    /**
     * 删除业绩种类
     */
    ResultVO<Void> deleteType(Long performTypeId);

    /**
     * 删除业绩种类（支持强制删除）
     */
    ResultVO<Void> deleteType(Long performTypeId, boolean forceDelete);

    /**
     * 检查业绩种类是否存在
     */
    boolean checkTypeExists(Long performTypeId);
} 