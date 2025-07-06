package org.csu.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.performance.VO.PerformFileVO;

import java.util.List;

/**
 * 业绩附件Mapper接口
 */
@Mapper
public interface PerformFileMapper extends BaseMapper<org.csu.performance.entity.PerformFile> {

    /**
     * 根据业绩ID查询附件列表
     */
    List<PerformFileVO> selectFilesByPerformId(@Param("performId") Long performId);

    /**
     * 根据ID查询附件详情（包含业绩名称）
     */
    PerformFileVO selectFileDetailById(@Param("performFileId") Long performFileId);

    /**
     * 统计业绩的附件数量
     */
    Integer countFilesByPerformId(@Param("performId") Long performId);
} 