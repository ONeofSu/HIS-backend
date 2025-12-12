package org.csu.evaluationanddeclaration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.csu.evaluationanddeclaration.entity.HerbRatingDetail;

import java.util.List;

public interface HerbRatingDetailMapper extends BaseMapper<HerbRatingDetail> {
    @Select("SELECT COUNT(*) FROM herb_rating_detail")
    Long count();

    @Select("SELECT * FROM herb_rating_detail WHERE herb_evaluation_id = #{evaluationId}")
    List<HerbRatingDetail> findByHerbEvaluationId(Long evaluationId);
    @Select("SELECT * FROM herb_rating_detail WHERE herb_id = #{herbId}")
    List<HerbRatingDetail> findByHerbId(Long herbId);
}
