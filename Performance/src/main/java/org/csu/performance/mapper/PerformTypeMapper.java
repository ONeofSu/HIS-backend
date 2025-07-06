package org.csu.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.performance.VO.PerformTypeVO;

import java.util.List;

/**
 * 业绩种类Mapper接口
 */
@Mapper
public interface PerformTypeMapper extends BaseMapper<org.csu.performance.entity.PerformType> {

    /**
     * 查询所有启用的业绩种类
     */
    List<PerformTypeVO> selectActiveTypes();

    /**
     * 检查业绩种类名称是否存在
     */
    Integer checkTypeNameExists(@Param("performTypeName") String performTypeName, @Param("performTypeId") Long performTypeId);

    /**
     * 检查业绩种类是否正在使用
     */
    Integer checkTypeInUse(@Param("performTypeId") Long performTypeId);
    
    /**
     * 获取最大排序值
     */
    Integer getMaxSortOrder();
} 