package org.csu.evaluationanddeclaration.controller;

import org.csu.evaluationanddeclaration.entity.*;
import org.csu.evaluationanddeclaration.mapper.*;
import org.csu.evaluationanddeclaration.service.Impl.EvaluationApplicationServiceImpl;
import org.csu.evaluationanddeclaration.service.Impl.HerbEvaluationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ApplicationController {
    @Autowired
    HerbEvaluationServiceImpl evaluationService;
    @Autowired
    EvaluationApplicationServiceImpl applicationService;
    //mapper
    @Autowired
    HerbEvaluationMapper herbEvaluationMapper;
    @Autowired
    HerbRatingMapper herbRatingMapper;
    @Autowired
    EvaluationApplicationMapper evaluationApplicationMapper;
    @Autowired
    EvaluationDetailMapper evaluationDetailMapper;
    @Autowired
    EvaluationIndicatorMapper evaluationIndicatorMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HerbRatingDetailMapper herbRatingDetailMapper;

    public void resetAutoIncrement(String table,int cnt) {
        jdbcTemplate.execute("ALTER TABLE "+ table + " AUTO_INCREMENT = "+cnt);
    }
    @GetMapping("/GetApplicationByEvaluationId")
    public ResponseEntity<?> getApplicationByEvaluationId(
            @RequestParam(name = "evaluationId")int evaluationId){
        ResponseEntity<EvaluationApplication> result = null;
        EvaluationApplication application = applicationService.GetApplicationByEvaluationId(evaluationId);
        if(application == null){
//            return result.status(490).body("这个分数段内无评价");
            return result.ok(Map.of(
                    "message", "无评价",
                    "code",-1));
        }
        return result.ok(Map.of(
                "code",0,
                "Application",application
        ));
    }
    @GetMapping("/GetUnAuditedApplications")
    public ResponseEntity<?>GetUnAuditedApplications(){
        ResponseEntity<?> result = null;
        List<EvaluationApplication> unAuditedApplication = evaluationApplicationMapper.getApplications("审核中");
        if(unAuditedApplication.isEmpty()){
            return result.ok(Map.of(
                    "message", "暂无未审核的评论",
                    "code", -1
            ));
        }
        return result.ok(Map.of(
                "code",0,
                "applications",unAuditedApplication
        ));
    }

    @PostMapping("/DoAudit")
    public ResponseEntity<?> DoAuith(
            @RequestParam(name = "applicationId")int applicationId
    ){
        ResponseEntity<?> result = null;
        EvaluationApplication application = evaluationApplicationMapper.getApplicationById((long) applicationId);
        if(application==null||!evaluationApplicationMapper.getApplications("审核中").contains(application)){
            return result.ok(Map.of(
                    "code",-1,
                    "message","该申请不存在或已审核"
            ));
        }
        else{
//            application.setApplicationState("审核通过");
//            applicationService.UpdateApplication(application);
            HerbEvaluation evaluation = herbEvaluationMapper.getEvaluationById(application.getEvaluationId().intValue());
//            evaluation.setEvaluationState("审核通过");
//            evaluationService.UpdateHerbEvaluation(evaluation);

            Long evaluationId = evaluation.getEvaluationId();
            Long herbId = evaluation.getHerbId();

            resetAutoIncrement("herb_rating",herbRatingMapper.count().intValue());
            resetAutoIncrement("herb_rating_detail",herbRatingDetailMapper.count().intValue());
            HerbRating rating = herbRatingMapper.selectByHerbId(herbId.intValue());
            if(rating == null){
                //更新herb_rating
                rating = new HerbRating();
                rating.setTotalScore(evaluation.getTotalScore().doubleValue());
                rating.setHerbId(herbId);
                evaluationService.AddRating(rating);

                //更新herb_rating_detail
                HerbRatingDetail detail1 = new HerbRatingDetail();
                HerbRatingDetail detail2 = new HerbRatingDetail();
                HerbRatingDetail detail3 = new HerbRatingDetail();
                HerbRatingDetail detail4 = new HerbRatingDetail();
                HerbRatingDetail detail5 = new HerbRatingDetail();
                HerbRatingDetail detail6 = new HerbRatingDetail();

                detail1.setIndicatorId((long)1);
                detail2.setIndicatorId((long)2);
                detail3.setIndicatorId((long)3);
                detail4.setIndicatorId((long)4);
                detail5.setIndicatorId((long)5);
                detail6.setIndicatorId((long)6);

                detail1.setHerbId(herbId);
                detail2.setHerbId(herbId);
                detail3.setHerbId(herbId);
                detail4.setHerbId(herbId);
                detail5.setHerbId(herbId);
                detail6.setHerbId(herbId);

                detail1.setHerbEvaluationId(evaluationId);
                detail2.setHerbEvaluationId(evaluationId);
                detail3.setHerbEvaluationId(evaluationId);
                detail4.setHerbEvaluationId(evaluationId);
                detail5.setHerbEvaluationId(evaluationId);
                detail6.setHerbEvaluationId(evaluationId);

                //todo:detail评分
                List<EvaluationDetail> details= evaluationDetailMapper.selectListByEvaluationId(evaluationId.intValue());
                detail1.setAvgScore(details.getFirst().getIndicatorScore());
                details.removeFirst();
                detail2.setAvgScore(details.getFirst().getIndicatorScore());
                details.removeFirst();
                detail3.setAvgScore(details.getFirst().getIndicatorScore());
                details.removeFirst();
                detail4.setAvgScore(details.getFirst().getIndicatorScore());
                details.removeFirst();
                detail5.setAvgScore(details.getFirst().getIndicatorScore());
                details.removeFirst();
                detail6.setAvgScore(details.getFirst().getIndicatorScore());
                details.removeFirst();

                evaluationService.AddRatingDetail(detail1);
                evaluationService.AddRatingDetail(detail2);
                evaluationService.AddRatingDetail(detail3);
                evaluationService.AddRatingDetail(detail4);
                evaluationService.AddRatingDetail(detail5);
                evaluationService.AddRatingDetail(detail6);

            }
            else{
                int cnt = 1;
                float score = evaluation.getTotalScore();
                for(HerbEvaluation e : herbEvaluationMapper.getAuditedEvaluationsByHerbId(herbId.intValue())){
                    score+=e.getTotalScore();
                    cnt++;
                }
                System.out.println("rating:score = "+score+"/="+cnt);
                score/=cnt;
                rating.setTotalScore((double)score);
                evaluationService.UpdateRating(rating);

                List<EvaluationDetail> details= evaluationDetailMapper.selectListByEvaluationId(evaluationId.intValue());

                List<HerbRatingDetail> ratingDetails = herbRatingDetailMapper.findByHerbId(herbId);
                for(EvaluationDetail detail : details){
                    HerbRatingDetail ratingDetail= ratingDetails.removeFirst();
                    System.out.println("ratingDetail:newscore = "+detail.getIndicatorScore()+"/"+cnt+"+"+ratingDetail.getAvgScore()+"*"+(cnt-1)+"/"+cnt);
                    double sscore=detail.getIndicatorScore()/cnt+ratingDetail.getAvgScore()*(cnt-1)/cnt;
                    ratingDetail.setAvgScore(sscore);
                    evaluationService.UpdateRatingDetail(ratingDetail);
                }
            }
            application.setApplicationState("审核通过");
            evaluation.setEvaluationState("审核通过");
            evaluationService.UpdateHerbEvaluation(evaluation);
            applicationService.UpdateApplication(application);

            return result.ok(Map.of(
                    "code",0,
                    "message","已通过该评论的审核"
            ));
        }

    }

}
