package org.csu.herb_teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.csu.herb_teaching.entity.Course;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
} 