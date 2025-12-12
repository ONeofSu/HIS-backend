package org.csu.evaluationanddeclaration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.entity.EvaluationIndicator;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface EvaluationIndicatorMapper extends BaseMapper<EvaluationIndicator> {
    @Select("SELECT * FROM evaluation_indicator")
    List<EvaluationIndicator> getAllIndicator();
    @Select("SELECT COUNT(*) FROM evaluation_indicator")
    Long count();
}
