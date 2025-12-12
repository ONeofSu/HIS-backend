package com.csu.research.controller;

import com.csu.research.DTO.TopicDTO;
import com.csu.research.entity.Topic;
import com.csu.research.service.AuthService;
import com.csu.research.service.TeamService;
import com.csu.research.service.TopicService;
import com.csu.research.service.UserService;
import com.csu.research.vo.TopicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topics")
public class TopicController {
    @Autowired
    TopicService topicService;
    @Autowired
    TeamService teamService;
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    @GetMapping("/{topicId}") // 根据课题id课题信息
    public ResponseEntity<?> findTopicByTopicId(
            @PathVariable Long topicId) {
        if(!topicService.isTopicExist(topicId)) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "invalid topicId")
            );
        }
        try {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                    "topic", topicService.findTopicByTopicId(topicId))
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", e.getMessage())
            );
        }

    }

    @PostMapping("/update/{topicId}") // 更新课题
    public ResponseEntity<?> updateTopic(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable Long topicId,
                                         @RequestBody TopicDTO topicDTO) throws Exception {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId==-1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!teamService.isExistTeam(topicDTO.getTeamId())) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "the team doesn't exist")
            );
        }
        if(!topicService.isTopicExist(topicId)){
            return ResponseEntity.ok(
                    Map.of("code", -3,
                            "message", "the topic doesn't exist")
            );
        }

        if(!authService.isQualifiedToHandleTopic(topicId, userId) &&
                !userService.isAdmin(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message", "you are not qualified update this topic")
            );
        }

        Topic topic = topicService.findTopicByTopicId(topicId);
        if(!topic.getTopicName().equals(topicDTO.getName()) && topicService.isTopicExist(topicDTO.getName())){
            return ResponseEntity.ok(
                    Map.of("code", -5,
                            "message", "the topic name already exist")
            );
        }
        topic = topicService.transferDTOToTopic(topicDTO);
        topic.setTopicId(topicId);
        if (topicService.updateTopic(topic)) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "topic", topicService.findTopicByTopicId(topic.getTopicId()))
            );
        }
        return ResponseEntity.ok(
                Map.of("code", -6,
                        "message", "update failed")
        );
    }

    @GetMapping("/search") // 搜索课题
    public ResponseEntity<?> searchTopic(@RequestParam String query) {
        return ResponseEntity.ok(
          Map.of("code", 0,
                  "topics", topicService.searchTopic(query))
        );
    }

    @PostMapping("/add") // 添加课题
    public ResponseEntity<?> addTopic(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody TopicDTO topicDTO) throws Exception {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId==-1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!teamService.isExistTeam(topicDTO.getTeamId())) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "the team doesn't exist")
            );
        }
        if(topicService.isTopicExist(topicDTO.getName())){
            return ResponseEntity.ok(
                    Map.of("code", -3,
                            "message", "the topic name already exist")
            );
        }

        Topic topic = topicService.transferDTOToTopic(topicDTO);
        if(!authService.isQualifiedToHandleTopic(topic,userId)
                && !userService.isAdmin(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message", "you are not qualified add this topic by this team")
            );
        }

        topic = topicService.addTopic(topic);
        if (topic != null) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "topic", topicService.findTopicByTopicId(topic.getTopicId()))
            );
        }
        return ResponseEntity.ok(Map.of(
                "code", -5,
                "message", "fail to add the topic"
        ));

    }

    //删除课题
    @DeleteMapping("/del/{topicId}")
    public ResponseEntity<?> delTopic(@RequestHeader("Authorization") String authHeader,
                                      @PathVariable Long topicId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId==-1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!topicService.isTopicExist(topicId)) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "invalid topicId")
            );
        }

        if(!authService.isQualifiedToHandleTopic(topicId,userId) &&
        !userService.isAdmin(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message", "you are not qualified delete this topic")
            );
        }

        if (topicService.delTopic(topicId)) {
            return ResponseEntity.ok(
              Map.of("code", 0,
                      "success", true)
            );
        }
        return ResponseEntity.ok(
              Map.of("code", -4,
                      "message", "fail to delete the topic")
        );
    }

//    //获得特定用户课题
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<?> findTopicByUserId(@PathVariable int userId,
//                                               @RequestParam(defaultValue = "1") int page,
//                                               @RequestParam(defaultValue = "10") int size) {
//        if(!userService.isUserIdExist(userId)){
//            return ResponseEntity.ok(
//                    Map.of("code",-1,
//                            "message", "invalid user id")
//            );
//        }
//
//        List<Topic> topics = topicService.getTopicsByUserId(userId, page, size);
//        List<TopicVo> topicVos = topicService.transferTopicToVO(topics);
//        return ResponseEntity.ok(
//                Map.of("code",0,
//                        "topics", topicVos)
//        );
//    }
//
//    //获得用户自身选题
//    @GetMapping("/user/me")
//    public ResponseEntity<?> findMyTopic(@RequestHeader("Authorization") String authHeader,
//                                         @RequestParam(defaultValue = "1") int page,
//                                         @RequestParam(defaultValue = "10") int size) {
//        String token = authHeader.substring(7);
//        int userId = userService.getUserId(token);
//        if(userId==-1){
//            return ResponseEntity.ok(
//                    Map.of("code",-1,
//                            "message", "user not found")
//            );
//        }
//
//        List<Topic> topics = topicService.getTopicsByUserId(userId, page, size);
//        List<TopicVo> topicVos = topicService.transferTopicToVO(topics);
//        return ResponseEntity.ok(
//                Map.of("code",0,
//                        "topics", topicVos)
//        );
//    }

    //获得所有选题
    @GetMapping("/all")
    public ResponseEntity<?> findAllTopic(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        List<Topic> topics = topicService.getAllTopics(page, size);
        List<TopicVo> topicVos = topicService.transferTopicToVO(topics);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "topics", topicVos)
        );
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> findTopicByTeam(@PathVariable Long teamId,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        if(!teamService.isExistTeam(teamId)) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "invalid teamId")
            );
        }
        List<Topic> topics = topicService.getAllTopicsByTeamId(teamId,page, size);
        List<TopicVo> topicVos = topicService.transferTopicToVO(topics);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "topics", topicVos)
        );
    }
}
