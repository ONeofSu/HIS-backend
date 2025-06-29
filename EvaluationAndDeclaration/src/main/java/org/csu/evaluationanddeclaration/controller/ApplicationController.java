package org.csu.evaluationanddeclaration.controller;

import org.csu.evaluationanddeclaration.entity.EvaluationApplication;
import org.csu.evaluationanddeclaration.service.Impl.EvaluationApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
public class ApplicationController {
    @Autowired
    EvaluationApplicationServiceImpl evaluationApplicationService;
    @GetMapping("/GetApplicationByEvaluationId")
    public ResponseEntity<?> getApplicationByEvaluationId(
            @RequestParam(name = "evaluationId")int evaluationId){
        ResponseEntity<EvaluationApplication> result = null;
        EvaluationApplication application = evaluationApplicationService.GetApplicationByEvaluationId(evaluationId);
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
}
