package org.csu.histraining.controller;

import org.csu.histraining.DTO.FeedbackDTO;
import org.csu.histraining.VO.FeedbackVO;
import org.csu.histraining.entity.Feedback;
import org.csu.histraining.service.FeedbackService;
import org.csu.histraining.service.HerbService;
import org.csu.histraining.service.MaterialService;
import org.csu.histraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    MaterialService materialService;
    @Autowired
    UserService userService;
    @Autowired
    HerbService herbService;

    @PostMapping("/feedback/info")
    public ResponseEntity<?> addFeedbackInfo(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody FeedbackDTO feedbackDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!materialService.isMaterialIdExist(feedbackDTO.getMaterialId())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid materialId")
            );
        }

        Feedback feedback = feedbackService.transferFeedbackDTOToFeedback(feedbackDTO, userId);

        int id = feedbackService.addFeedback(feedback);
        if(id <= 0){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","failed to add , there may be errors on your input")
            );
        }

        feedback = feedbackService.getFeedbackById(id);
        FeedbackVO feedbackVO = feedbackService.transferToFeedbackVO(feedback);
        return ResponseEntity.ok(Map.of("code",0,"feedback",feedbackVO));
    }

    @GetMapping("/feedback/me")
    public ResponseEntity<?> getFeedbackOnMe(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<Feedback> feedbacks = feedbackService.getFeedbackByUserId(userId);
        List<FeedbackVO> feedbackVOS = feedbackService.transferToFeedbackVOList(feedbacks);
        return ResponseEntity.ok(Map.of("code",0,"feedbacks",feedbackVOS));
    }

    @DeleteMapping("/feedback/{feedbackId}")
    public ResponseEntity<?> deleteFeedback(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable int feedbackId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        if(feedback == null){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the feedback does not exist")
            );
        }

        if(feedback.getUserId() != userId){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't delete the feedback made by others")
            );
        }

        if(!feedbackService.deleteFeedback(feedbackId)){
            return ResponseEntity.internalServerError().body("error to delete");
        }

        FeedbackVO feedbackVO = feedbackService.transferToFeedbackVO(feedback);
        return ResponseEntity.ok(Map.of("code",0,"feedback",feedbackVO));
    }
}
