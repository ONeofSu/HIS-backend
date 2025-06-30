package org.csu.evaluationanddeclaration.service;

import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.entity.HerbRating;
import org.csu.evaluationanddeclaration.entity.HerbRatingDetail;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public interface HerbEvaluationService{
    public List<HerbEvaluation> GetAllHerbEvaluations();
    public HerbEvaluation GetHerbEvaluationById(int id);
    public List<HerbEvaluation> GetHerbEvaluationsByHerbId(int herbId);
    public List<HerbEvaluation> GetHerbEvaluationsByUserId(int userId);
    public List<HerbEvaluation> GetHerbEvaluationsByScore(float minScore,float maxScore);
    public List<HerbEvaluation> GetHerbEvaluationsByDate(Date date1,Date date2);
    public float GetHerbEvaluationScore(int id);

    public int GetUserId(int id);

    public List<EvaluationDetail> GetAllEvaluationDetails(int id);

    //增删改查
    //评价
    public boolean AddHerbEvaluation(HerbEvaluation herbEvaluation);
    public boolean DeleteHerbEvaluation(int id);
    public boolean UpdateHerbEvaluation(HerbEvaluation herbEvaluation);
    public boolean IsHerbExit(int id);

    //评价明细
    public boolean AddDetail(EvaluationDetail detail);
    public boolean DeleteDetail(int id);
    public boolean UpdateDetail(EvaluationDetail detail);
    public boolean IsDetailExit(int id);

    //单个药材评分
    public boolean AddRating(HerbRating rating);
    public boolean DeleteRating(int id);
    public boolean UpdateRating(HerbRating rating);
    public boolean IsRatingExit(int id);

    //单个药材评分明细
    public boolean AddRatingDetail(HerbRatingDetail herbRatingDetail);
    public boolean DeleteRatingDetail(int id);
    public boolean UpdateRatingDetail(HerbRatingDetail herbRatingDetail);
    public boolean IsRatingDetailExit(int id);
}
