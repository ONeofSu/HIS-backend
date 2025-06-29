package org.csu.evaluationanddeclaration.controller;

import org.csu.evaluationanddeclaration.entity.*;
import org.csu.evaluationanddeclaration.mapper.*;
import org.csu.evaluationanddeclaration.service.Impl.EvaluationApplicationServiceImpl;
import org.csu.evaluationanddeclaration.service.Impl.HerbEvaluationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class EvaluationController {
    float indicator1Weight = 0.25f;
    float indicator2Weight = 0.25f;
    float indicator3Weight = 0.25f;
    float indicator4Weight = 0.25f;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    // 测试完成后重置自增ID
//    @AfterEach // JUnit5 每次测试后执行
    public void resetAutoIncrement(String table,int cnt) {
        jdbcTemplate.execute("ALTER TABLE "+ table + " AUTO_INCREMENT = "+cnt);
    }

    @Autowired
    EvaluationIndicatorMapper indicatorMapper;

//    public EvaluationController(){
//        List<Double>weights = new ArrayList<>();
////        weights[0] = indicatorMapper.getAllIndicator().at
//        for(EvaluationIndicator var:indicatorMapper.getAllIndicator()){
//            weights.add(var.getWeight());
//        }
//        indicator1Weight = weights.getFirst().floatValue();
//        weights.removeFirst();
//        indicator2Weight = weights.getFirst().floatValue();
//        weights.removeFirst();
//        indicator3Weight = weights.getFirst().floatValue();
//        weights.removeFirst();
//        indicator4Weight = weights.getFirst().floatValue();
//        weights.removeFirst();
//    }

    @Autowired
    HerbEvaluationServiceImpl evaluationService;

    @Autowired
    EvaluationApplicationServiceImpl applicationService;

    @GetMapping("/herbEvaluations/{herbId}")
    public ResponseEntity<?> GetHerbEvaluationsByHerbName(@PathVariable int herbId){
        ResponseEntity<List<HerbEvaluation>> result = null;
        List<HerbEvaluation> herbEvaluations = evaluationService.GetHerbEvaluationsByHerbId(herbId);
        if(herbEvaluations.isEmpty()){
//            return result.status(490).body("暂无评价");
            return result.ok(Map.of(
                    "message","暂无评价",
                    "code",-1
            ));
        }
        return result.ok(Map.of(
                "Evaluations",herbEvaluations,
                "code",0
        ));
    }

    @GetMapping("/GetEvaluationsBetweenScore")
    public ResponseEntity<?> GetHerbEvaluationsBetweenDates(
            @RequestParam(name="minScore")Float minScore,
            @RequestParam(name="maxScore")Float maxScore){
        ResponseEntity<List<HerbEvaluation>> result = null;
        System.out.println("minScore: "+minScore);
        System.out.println("maxScore: "+maxScore);
        List<HerbEvaluation> herbEvaluations = evaluationService.GetHerbEvaluationsByScore(minScore,maxScore);
        if(herbEvaluations.isEmpty()){
//            return result.status(490).body("这个分数段内无评价");
            return result.ok(Map.of(
                    "message","这个分数段内无评价",
                    "code",-1
            ));
        }
//        return result.ok(herbEvaluations);
        return result.ok(Map.of(
                "Evaluations",herbEvaluations,
                "code",0
        ));
    }


    @GetMapping("/GetEvaluationsBetweenDates")
    public ResponseEntity<?> GetHerbEvaluationsBetweenDates(
            @RequestParam(name="startDate")String startDate,
            @RequestParam(name="endDate")String endDate){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1,date2;
        ResponseEntity<List<HerbEvaluation>> result = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate1 = LocalDateTime.parse(startDate,formatter);
        LocalDateTime localDate2 = LocalDateTime.parse(endDate,formatter);

//        try{
//            date1 = sdf.parse(startDate);
//            date2 = sdf.parse(endDate);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        date1 = Date.from(localDate1.atZone(ZoneId.systemDefault()).toInstant());
        date2 = Date.from(localDate2.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("date1: "+date1);
        System.out.println("date2: "+date2);
        List<HerbEvaluation> herbEvaluations = evaluationService.GetHerbEvaluationsByDate(date1,date2);
        for (HerbEvaluation h: herbEvaluations) {
            Instant instant = h.getEvaluateTime().toInstant();
            h.formatTime(Date.from(instant.atZone(ZoneId.systemDefault()).toInstant()));
        }
        if(herbEvaluations.isEmpty()){
//            return result.status(490).body("这段时间内无评价");
            return result.ok(Map.of(
                    "message","这段时间内无评价",
                    "code",-1
            ));
        }
        return result.ok(Map.of(
                "Evaluations",herbEvaluations,
                "code",0
        ));
    }

    @GetMapping("/GetEvaluationsByUserId")
    public ResponseEntity<?> GetHerbEvaluationsByUserId(
            @RequestParam(name = "userId")int userId){
        ResponseEntity<List<HerbEvaluation>> result = null;
        List<HerbEvaluation> herbEvaluations = evaluationService.GetHerbEvaluationsByUserId(userId);
        if(herbEvaluations.isEmpty()){
//            return result.status(490).body("该用户暂无评价");
            return result.ok(Map.of(
                    "message","该用户暂无评价",
                    "code",-1
            ));
        }
        return result.ok(Map.of(
                "Evaluations",herbEvaluations,
                "code",0
        ));
    }

    @GetMapping("/GetEvaluationDetailsByEvaluationId")
    public ResponseEntity<?> GetEvaluationDetailsByEvaluationId(
            @RequestParam(name = "evaluationId")int evaluationId){
        ResponseEntity<List<EvaluationDetail>> result = null;
        List<EvaluationDetail> details = evaluationService.GetAllEvaluationDetails(evaluationId);
        if(details.isEmpty()){
            return result.ok(Map.of(
                    "message","暂无评价",
                    "code",-1
            ));
        }
        return result.ok(Map.of(
                "EvaluationDetails",details,
                "code",0
        ));
    }

    @PostMapping("/DoComment")
    public ResponseEntity<?> doComment(
            @RequestParam(name = "herbId")int herbId,
            @RequestParam(name = "commentTime")String commentTime,
            @RequestParam(name = "userId")int userId,
            @RequestParam(name = "evaluationState")String evaluationState,
            @RequestParam(name = "comment1")String comment1,
            @RequestParam(name = "material1")String material1,
            @RequestParam(name = "score1")float score1,
            @RequestParam(name = "comment2")String comment2,
            @RequestParam(name = "material2")String material2,
            @RequestParam(name = "score2")float score2,
            @RequestParam(name = "comment3")String comment3,
            @RequestParam(name = "material3")String material3,
            @RequestParam(name = "score3")float score3,
            @RequestParam(name = "comment4")String comment4,
            @RequestParam(name = "material4")String material4,
            @RequestParam(name = "score4")float score4
    ){
        ResponseEntity<?>result = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate1 = LocalDateTime.parse(commentTime,formatter);

        Date date = Date.from(localDate1.atZone(ZoneId.systemDefault()).toInstant());

        //创建实体
        HerbEvaluation evaluation = new HerbEvaluation();
        EvaluationApplication application = new EvaluationApplication();
        EvaluationDetail detail1 = new EvaluationDetail();
        EvaluationDetail detail2 = new EvaluationDetail();
        EvaluationDetail detail3 = new EvaluationDetail();
        EvaluationDetail detail4 = new EvaluationDetail();
        HerbRating rating;

        //todo:检查各种参数是否合法，例如：herb_id是否存在数据库中

        //评价
        evaluation.setHerbId((long) herbId);
        evaluation.setUserId((long)userId);
        evaluation.setEvaluateTime(date);
        evaluation.setAuditorId((long)100);
        evaluation.setEvaluationState(evaluationState);

        float totalScore = indicator1Weight*score1 + indicator2Weight*score2 + indicator3Weight*score3 + indicator4Weight*score4;
        evaluation.setTotalScore(totalScore);

        //重置自增
        resetAutoIncrement("herb_evaluation", herbEvaluationMapper.count().intValue());

        evaluationService.AddHerbEvaluation(evaluation);
        Long evaluationId = evaluation.getEvaluationId();

        //指标1~4

        //重置自增
        resetAutoIncrement("evaluation_detail", evaluationDetailMapper.count().intValue());

        detail1.setEvaluationId(evaluationId);
        detail1.setIndicatorId((long)1);
        detail1.setComment(comment1);
        detail1.setMaterial(material1);
        detail1.setIndicatorScore((double)score1);
        evaluationService.AddDetail(detail1);


        // 设置第二个指标
        detail2.setEvaluationId(evaluationId);
        detail2.setIndicatorId((long) 2); // 指标ID为2
        detail2.setComment(comment2);   // 对应的评语
        detail2.setMaterial(material2); // 对应的材料
        detail2.setIndicatorScore((double) score2); // 对应的得分
        evaluationService.AddDetail(detail2);

// 设置第三个指标
        detail3.setEvaluationId(evaluationId);
        detail3.setIndicatorId((long) 3); // 指标ID为3
        detail3.setComment(comment3);   // 对应的评语
        detail3.setMaterial(material3); // 对应的材料
        detail3.setIndicatorScore((double) score3); // 对应的得分
        evaluationService.AddDetail(detail3);

// 设置第四个指标
        detail4.setEvaluationId(evaluationId);
        detail4.setIndicatorId((long) 4); // 指标ID为4
        detail4.setComment(comment4);   // 对应的评语
        detail4.setMaterial(material4); // 对应的材料
        detail4.setIndicatorScore((double) score4); // 对应的得分
        evaluationService.AddDetail(detail4);

        //审核
        //重置自增
        resetAutoIncrement("evaluation_application", evaluationApplicationMapper.count().intValue());

        application.setApplicationState(evaluationState);
        application.setApplicationType((long)3);
        application.setEvaluationId(evaluationId);
        application.setUserId((long)100);
        applicationService.AddApplication(application);

        //单个药材评分
        //重置自增
        resetAutoIncrement("herb_rating", herbRatingMapper.count().intValue());

        rating = herbRatingMapper.selectByHerbId(herbId);
        if(rating == null){
            rating = new HerbRating();
            rating.setTotalScore((double) totalScore);
            rating.setHerbId((long) herbId);
            evaluationService.AddRating(rating);
        }
        else{
            int cnt = 1;
            for(HerbEvaluation e : herbEvaluationMapper.getEvaluationsByHerbId(herbId)){
                totalScore += e.getTotalScore().floatValue();
                cnt++;
            }
            totalScore /= cnt;
            rating.setTotalScore((double) totalScore);
            evaluationService.UpdateRating(rating);
        }
        return result.ok(Map.of(
                "code",0
        ));
    }
}
