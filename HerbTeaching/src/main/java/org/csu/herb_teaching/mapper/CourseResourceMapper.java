package org.csu.herb_teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.csu.herb_teaching.entity.CourseResource;

@Mapper
public interface CourseResourceMapper extends BaseMapper<CourseResource> {
    @Select("SELECT MAX(course_resource_order) FROM course_resource WHERE course_id = #{courseId}")
    Integer selectMaxCourseResourceOrderByCourseId(@Param("courseId") int courseId);
} 