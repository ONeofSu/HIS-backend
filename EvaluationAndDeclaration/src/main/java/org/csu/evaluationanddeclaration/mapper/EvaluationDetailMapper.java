package org.csu.evaluationanddeclaration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface EvaluationDetailMapper extends BaseMapper<EvaluationDetail> {

    @Select("SELECT * FROM evaluation_detail WHERE evaluation_id = #{evaluationId}")
    List<EvaluationDetail> selectListByEvaluationId(int evaluationId);
    @Select("SELECT COUNT(*) FROM evaluation_detail")
    Long count();
}
