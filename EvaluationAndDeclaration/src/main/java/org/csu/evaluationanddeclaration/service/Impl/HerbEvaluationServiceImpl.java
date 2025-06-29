package org.csu.evaluationanddeclaration.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.evaluationanddeclaration.entity.HerbRating;
import org.csu.evaluationanddeclaration.mapper.HerbRatingMapper;
import org.csu.evaluationanddeclaration.service.HerbEvaluationService;
import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.mapper.EvaluationDetailMapper;
import org.csu.evaluationanddeclaration.mapper.HerbEvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HerbEvaluationServiceImpl implements HerbEvaluationService {

    @Autowired
    HerbEvaluationMapper herbEvaluationMapper;

    @Autowired
    private EvaluationDetailMapper evaluationDetailMapper;
    @Autowired
    HerbRatingMapper herbRatingMapper;

    @Override
    public List<HerbEvaluation> GetAllHerbEvaluations() {
        return herbEvaluationMapper.selectList(null);
    }

    @Override
    public HerbEvaluation GetHerbEvaluationById(int id) {
        return herbEvaluationMapper.selectById(id);
    }

    @Override
    public List<HerbEvaluation> GetHerbEvaluationsByHerbId(int herbId) {
        return herbEvaluationMapper.selectList(new QueryWrapper<HerbEvaluation>().eq("herb_id", herbId));
    }

    @Override
    public List<HerbEvaluation> GetHerbEvaluationsByUserId(int userId) {
        return herbEvaluationMapper.selectList(new QueryWrapper<HerbEvaluation>().eq("user_id", userId));
    }

    @Override
    public List<HerbEvaluation> GetHerbEvaluationsByScore(float minScore, float maxScore) {
        if (minScore > maxScore) {
            throw new IllegalArgumentException("最小分数必须小于等于最大分数");
        }

        // 使用 QueryWrapper 构建查询条件
        return herbEvaluationMapper.selectList(new QueryWrapper<HerbEvaluation>()
                .ge("total_score",minScore)  // create_time >= date1
                .le("total_score",maxScore)); // create_time <= date2

    }

    @Override
    public List<HerbEvaluation> GetHerbEvaluationsByDate(Date date1, Date date2) {
        if (date1 != null && date2 != null && date1.after(date2)) {
            throw new IllegalArgumentException("起始日期必须早于结束日期");
        }

        // 使用 QueryWrapper 构建查询条件
        return herbEvaluationMapper.selectList(new QueryWrapper<HerbEvaluation>()
                .ge(date1 != null, "evaluate_time", date1)  // create_time >= date1
                .le(date2 != null, "evaluate_time", date2)); // create_time <= date2

    }

    @Override
    public float GetHerbEvaluationScore(int id) {
        HerbEvaluation evaluation = herbEvaluationMapper.selectById(id);
        return evaluation != null ? evaluation.getTotalScore().floatValue() : 0.0f;
    }

    @Override
    public int GetUserId(int id) {
        HerbEvaluation evaluation = herbEvaluationMapper.selectById(id);
        return evaluation != null ? evaluation.getUserId().intValue():0;    //默认用户ID为0
    }

    @Override
    public  List<EvaluationDetail> GetAllEvaluationDetails(int id) {
        return evaluationDetailMapper.selectListByEvaluationId(id);
    }

    @Override
    public boolean AddHerbEvaluation(HerbEvaluation herbEvaluation) {
        if (herbEvaluation == null || herbEvaluation.getHerbId() <= 0) {
            return false;
        }
        herbEvaluationMapper.insert(herbEvaluation);
        return true;
    }

    @Override
    public boolean DeleteHerbEvaluation(int id) {
        HerbEvaluation evaluation = herbEvaluationMapper.selectById(id);
        if (evaluation == null) {
            return false;
        }
        herbEvaluationMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean UpdateHerbEvaluation(HerbEvaluation herbEvaluation) {
        if (herbEvaluation == null || herbEvaluation.getEvaluationId() <= 0) {
            return false;
        }
        herbEvaluationMapper.updateById(herbEvaluation);
        return true;
    }

    @Override
    public boolean IsHerbExit(int id) {
        return herbEvaluationMapper.selectById(id) != null;
    }

    @Override
    public boolean AddDetail(EvaluationDetail detail) {
        if (detail == null) {
            return false;
        }
        evaluationDetailMapper.insert(detail);
        return true;
    }

    @Override
    public boolean DeleteDetail(int id) {
        EvaluationDetail detail = evaluationDetailMapper.selectById(id);
        if (detail == null) {
            return false;
        }
        evaluationDetailMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean UpdateDetail(EvaluationDetail detail) {
        if (detail == null || detail.getEvaluationDetailId() <= 0) {
            return false;
        }
        evaluationDetailMapper.updateById(detail);
        return true;
    }

    @Override
    public boolean IsDetailExit(int id) {
        return evaluationDetailMapper.selectById(id) != null;
    }

    @Override
    public boolean AddRating(HerbRating rating) {
        if (rating == null) {
            return false;
        }
        herbRatingMapper.insert(rating);
        return true;
    }

    @Override
    public boolean DeleteRating(int id) {
        HerbRating rating = herbRatingMapper.selectById(id);
        if (rating == null) {
            return false;
        }
        herbRatingMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean UpdateRating(HerbRating rating) {
        if (rating == null) {
            return false;
        }
        herbRatingMapper.updateById(rating);
        return true;
    }

    @Override
    public boolean IsRatingExit(int id) {
        return herbRatingMapper.selectById(id) != null;
    }
}
