package org.csu.evaluationanddeclaration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csu.evaluationanddeclaration.entity.EvaluationApplication;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface EvaluationApplicationMapper extends BaseMapper<EvaluationApplication> {
//    @Select("""
//        SELECT
//    """)
//    @Results({
//
//    })
//    HerbEvaluation GetHerbEvaluationBy
    @Select("SELECT COUNT(*) FROM evaluation_application")
    Long count();

    @Select("SELECT * FROM evaluation_application WHERE application_id = #{applicationId}")
    EvaluationApplication getApplicationById(Long applicationId);

    @Select("SELECT * FROM evaluation_application WHERE application_state = #{applicationState}")
    List<EvaluationApplication> getApplications(String applicationState);


}
