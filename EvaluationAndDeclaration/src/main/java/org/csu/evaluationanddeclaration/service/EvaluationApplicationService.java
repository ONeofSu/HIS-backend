package org.csu.evaluationanddeclaration.service;


import org.csu.evaluationanddeclaration.entity.EvaluationApplication;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EvaluationApplicationService {
    public List<EvaluationApplication>GetAllApplications();
    public EvaluationApplication GetApplicationById(int id);
    public HerbEvaluation GetEvaluation(int id);
    public int GetApplicationType(int id);
    public String GetApplicationState(int id);
    public EvaluationApplication GetApplicationByEvaluationId(int id);

    //增删改查
    //审核
    public boolean AddApplication(EvaluationApplication evaluationApplication);
    public boolean DeleteApplication(int id);
    public boolean UpdateApplication(EvaluationApplication evaluationApplication);
    public boolean IsHApplicationExit(int id);
}
