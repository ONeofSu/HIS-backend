package org.csu.herb_teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.csu.herb_teaching.entity.CourseRating;

@Mapper
public interface CourseRatingMapper extends BaseMapper<CourseRating> {

    @Select("SELECT AVG(rating) FROM course_rating WHERE course_id = #{courseId}")
    Double getAverageRatingByCourseId(Long courseId);

} 