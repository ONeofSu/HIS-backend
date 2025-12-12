package org.csu.evaluationanddeclaration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;

import java.util.List;


public interface HerbEvaluationMapper extends BaseMapper<HerbEvaluation> {
    @Select("SELECT * FROM herb_evaluation WHERE evaluation_id = #{evaluationId}")
    HerbEvaluation getEvaluationById(int evaluationId);
    @Select("SELECT COUNT(*) FROM herb_evaluation")
    Long count();
    @Select("SELECT * FROM herb_evaluation WHERE herb_id = #{herbId}")
    List<HerbEvaluation> getEvaluationsByHerbId(int herbId);

    @Select("SELECT * FROM herb_evaluation WHERE herb_id = #{herbId} AND evaluation_state = '审核通过' ")
    List<HerbEvaluation> getAuditedEvaluationsByHerbId(int herbId);

}
