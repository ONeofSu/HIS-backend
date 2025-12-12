package org.csu.evaluationanddeclaration.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.evaluationanddeclaration.entity.EvaluationApplication;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.mapper.EvaluationApplicationMapper;
import org.csu.evaluationanddeclaration.mapper.HerbEvaluationMapper;
import org.csu.evaluationanddeclaration.service.EvaluationApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EvaluationApplicationServiceImpl implements EvaluationApplicationService {

    @Autowired
    HerbEvaluationMapper herbEvaluationMapper;
    @Autowired
    EvaluationApplicationMapper evaluationApplicationMapper;
    @Override
    public List<EvaluationApplication> GetAllApplications() {
        return evaluationApplicationMapper.selectList(null);
    }

    @Override
    public EvaluationApplication GetApplicationById(int id) {
        return evaluationApplicationMapper.selectById(id);
    }

    @Override
    public HerbEvaluation GetEvaluation(int id) {

        int Id = evaluationApplicationMapper.selectById(id) != null ?evaluationApplicationMapper.selectById(id).getApplicationId().intValue():0;
        return herbEvaluationMapper.getEvaluationById(Id);
    }


    @Override
    public String GetApplicationState(int id) {
        return evaluationApplicationMapper.selectById(id) != null ?evaluationApplicationMapper.selectById(id).getApplicationState():null;
    }

    @Override
    public EvaluationApplication GetApplicationByEvaluationId(int id) {
        return evaluationApplicationMapper.selectOne(new QueryWrapper<EvaluationApplication>().eq("evaluation_id", id));
    }

    @Override
    public boolean AddApplication(EvaluationApplication evaluationApplication) {
        if (evaluationApplication == null) {
            return false;
        }
        evaluationApplicationMapper.insert(evaluationApplication);
        return true;
    }

    @Override
    public boolean DeleteApplication(int id) {
        EvaluationApplication application = evaluationApplicationMapper.selectById(id);
        if (application == null) {
            return false;
        }
        evaluationApplicationMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean UpdateApplication(EvaluationApplication evaluationApplication) {
        if (evaluationApplication == null || evaluationApplication.getEvaluationId() <= 0) {
            return false;
        }
        evaluationApplicationMapper.updateById(evaluationApplication);
        return true;
    }

    @Override
    public boolean IsHApplicationExit(int id) {
        return evaluationApplicationMapper.selectById(id) != null;
    }
}
