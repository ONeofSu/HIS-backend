package org.csu.evaluationanddeclaration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.csu.evaluationanddeclaration.entity.HerbRating;
import org.springframework.stereotype.Repository;


public interface HerbRatingMapper extends BaseMapper<HerbRating> {
    @Select("SELECT COUNT(*) FROM herb_rating")
    Long count();

    @Select("SELECT * FROM herb_rating WHERE herb_id = #{herbId}")
    HerbRating selectByHerbId(int herbId);

}
