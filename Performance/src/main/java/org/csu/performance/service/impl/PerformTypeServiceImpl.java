package org.csu.performance.service.impl;

import org.csu.performance.DTO.PerformTypeDTO;
import org.csu.performance.VO.PerformTypeVO;
import org.csu.performance.VO.ResultVO;
import org.csu.performance.entity.PerformType;
import org.csu.performance.mapper.PerformMapper;
import org.csu.performance.mapper.PerformTypeMapper;
import org.csu.performance.service.PerformTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 业绩种类Service实现类
 */
@Service
public class PerformTypeServiceImpl implements PerformTypeService {

    @Autowired
    private PerformTypeMapper performTypeMapper;
    
    @Autowired
    private PerformMapper performMapper;

    @Override
    public ResultVO<List<PerformTypeVO>> getTypeList() {
        List<PerformTypeVO> types = performTypeMapper.selectActiveTypes();
        return ResultVO.success("业绩种类列表获取成功", types);
    }

    @Override
    @Transactional
    public ResultVO<PerformTypeVO> createType(PerformTypeDTO typeDTO) {
        // 检查种类名称是否已存在
        Integer count = performTypeMapper.checkTypeNameExists(typeDTO.getPerformTypeName(), null);
        if (count > 0) {
            return ResultVO.error("业绩种类名称已存在");
        }

        // 创建业绩种类实体
        PerformType performType = new PerformType();
        BeanUtils.copyProperties(typeDTO, performType);
        performType.setIsActive(true);
        
        // 自动设置排序值：如果用户没有提供sortOrder，则设置为当前最大值+1
        if (performType.getSortOrder() == null) {
            Integer maxSortOrder = performTypeMapper.getMaxSortOrder();
            performType.setSortOrder(maxSortOrder != null ? maxSortOrder + 1 : 1);
        }

        // 保存业绩种类
        performTypeMapper.insert(performType);

        // 返回业绩种类信息
        return getTypeById(performType.getPerformTypeId());
    }

    @Override
    @Transactional
    public ResultVO<PerformTypeVO> updateType(Long performTypeId, PerformTypeDTO typeDTO) {
        // 检查业绩种类是否存在
        if (!checkTypeExists(performTypeId)) {
            return ResultVO.error("业绩种类不存在");
        }

        // 检查种类名称是否已存在（排除当前种类）
        if (StringUtils.hasText(typeDTO.getPerformTypeName())) {
            Integer count = performTypeMapper.checkTypeNameExists(typeDTO.getPerformTypeName(), performTypeId);
            if (count > 0) {
                return ResultVO.error("业绩种类名称已存在");
            }
        }

        // 获取现有业绩种类信息
        PerformType performType = performTypeMapper.selectById(performTypeId);
        if (performType == null) {
            return ResultVO.error("业绩种类不存在");
        }

        // 只更新用户实际提交的字段，未提交的字段保留原值
        boolean hasUpdate = false;
        
        if (StringUtils.hasText(typeDTO.getPerformTypeName())) {
            performType.setPerformTypeName(typeDTO.getPerformTypeName());
            hasUpdate = true;
        }
        
        if (StringUtils.hasText(typeDTO.getPerformTypeDesc())) {
            performType.setPerformTypeDesc(typeDTO.getPerformTypeDesc());
            hasUpdate = true;
        }
        
        if (typeDTO.getSortOrder() != null) {
            performType.setSortOrder(typeDTO.getSortOrder());
            hasUpdate = true;
        }
        
        // 如果有字段更新，则保存
        if (hasUpdate) {
            performTypeMapper.updateById(performType);
        }

        // 返回更新后的业绩种类信息
        return getTypeById(performTypeId);
    }

    @Override
    @Transactional
    public ResultVO<Void> deleteType(Long performTypeId) {
        return deleteType(performTypeId, false);
    }

    @Override
    @Transactional
    public ResultVO<Void> deleteType(Long performTypeId, boolean forceDelete) {
        // 检查业绩种类是否存在
        if (!checkTypeExists(performTypeId)) {
            return ResultVO.error("业绩种类不存在");
        }

        // 检查业绩种类是否正在使用
        Integer count = performTypeMapper.checkTypeInUse(performTypeId);
        if (count > 0) {
            if (!forceDelete) {
                return ResultVO.error("该业绩种类正在使用中，无法删除。如需强制删除，请使用forceDelete=true参数");
            } else {
                // 强制删除：先删除相关业绩，再删除种类
                performMapper.deleteByPerformTypeId(performTypeId);
            }
        }

        // 删除业绩种类
        performTypeMapper.deleteById(performTypeId);

        return ResultVO.success("业绩种类删除成功");
    }

    @Override
    public boolean checkTypeExists(Long performTypeId) {
        if (performTypeId == null) {
            return false;
        }
        return performTypeMapper.selectById(performTypeId) != null;
    }

    /**
     * 根据ID获取业绩种类信息
     */
    private ResultVO<PerformTypeVO> getTypeById(Long performTypeId) {
        PerformType performType = performTypeMapper.selectById(performTypeId);
        if (performType == null) {
            return ResultVO.error("业绩种类不存在");
        }

        PerformTypeVO typeVO = new PerformTypeVO();
        BeanUtils.copyProperties(performType, typeVO);
        return ResultVO.success("业绩种类获取成功", typeVO);
    }
} 