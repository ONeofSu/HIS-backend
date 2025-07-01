package org.csu.herb_teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.csu.herb_teaching.entity.Lab;

@Mapper
public interface LabMapper extends BaseMapper<Lab> {
    @Select("SELECT MAX(lab_order) FROM lab WHERE course_id = #{courseId}")
    Integer selectMaxLabOrderByCourseId(@Param("courseId") int courseId);
} 