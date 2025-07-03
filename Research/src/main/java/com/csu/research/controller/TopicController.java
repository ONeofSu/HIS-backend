package com.csu.research.controller;

import com.csu.research.entity.Topic;
import com.csu.research.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    TopicService topicService;

    @GetMapping("/{topicId}") // 根据课题id课题信息
    public ResponseEntity<?> findTopicByTopicId(
            @PathVariable Long topicId
    ) {
        log.info("/topic/{}", topicId);
        try {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                    "topic", topicService.findTopicByTopicId(topicId))
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", e.getMessage())
            );
        }

    }

    @PostMapping("/update") // 更新课题
    public ResponseEntity<?> updateTopic(@RequestBody Topic topic) throws Exception {
        if (topicService.updateTopic(topic)) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "topic", topicService.findTopicByTopicId(topic.getTopicId()))
            );
        }
        return ResponseEntity.ok(
                Map.of("code", -1,
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
    public ResponseEntity<?> addTopic(@RequestBody Topic topic) throws Exception {
        Boolean b = topicService.addTopic(topic);
        if (b) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "topic", topicService.findTopicByTopicId(topic.getTopicId()))
            );
        }
        return ResponseEntity.ok(Map.of(
                "code", -1,
                "message", "fail to add the topic"
        ));

    }

    @DeleteMapping("/del/{topicId}")
    public ResponseEntity<?> delTopic(@PathVariable Long topicId) {
        if (topicService.delTopic(topicId)) {
            return ResponseEntity.ok(
              Map.of("code", 0,
                      "success", true)
            );
        }
        return ResponseEntity.ok(
              Map.of("code", -1,
                      "message", "fail to delete the topic")
        );
    }
}
