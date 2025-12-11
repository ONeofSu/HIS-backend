package org.csu.hiscomment.controller;

import org.csu.hiscomment.config.SensitiveWordConfig;
import org.csu.hiscomment.schedule.SensitiveWordSchedule;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.csu.hiscomment.entity.SensitiveWord;
import org.csu.hiscomment.service.SensitiveWordService;
import org.csu.hiscomment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * 敏感词管理控制器
 * 提供敏感词库管理功能
 */
@RestController
@RequestMapping("/admin/sensitive-words")
public class SensitiveWordController {
    
    @Autowired
    private SensitiveWordFilter sensitiveWordFilter;
    
    @Autowired
    private SensitiveWordConfig sensitiveWordConfig;
    
    @Autowired
    private SensitiveWordService sensitiveWordService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private SensitiveWordSchedule sensitiveWordSchedule;
    
    @Autowired
    private org.csu.hiscomment.feign.UserFeignClient userFeignClient;
    
    /**
     * 检测文本中的敏感词
     */
    @PostMapping("/check")
    public ResponseEntity<?> checkSensitiveWords(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", -1);
            result.put("message", "内容不能为空");
            return ResponseEntity.ok(result);
        }
        
        SensitiveWordFilter.SensitiveCheckResult checkResult = sensitiveWordFilter.checkSensitiveWords(content);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("hasSensitive", checkResult.hasSensitive());
        result.put("sensitiveWords", checkResult.sensitiveWords());
        result.put("sensitiveTypes", checkResult.sensitiveTypes());
        result.put("sensitiveWordsString", checkResult.getSensitiveWordsString());
        result.put("sensitiveTypesString", checkResult.getSensitiveTypesString());
        
        if (checkResult.hasSensitive()) {
            String filteredContent = sensitiveWordFilter.filterSensitiveWords(content);
            result.put("filteredContent", filteredContent);
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取敏感词过滤配置
     */
    @GetMapping("/config")
    public ResponseEntity<?> getConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", sensitiveWordConfig);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 更新敏感词过滤配置
     */
    @PutMapping("/config")
    public ResponseEntity<?> updateConfig(@RequestBody SensitiveWordConfig config) {
        // 这里可以添加配置更新逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "配置更新成功");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取敏感词列表
     */
    @GetMapping("/list")
    public ResponseEntity<?> getSensitiveWords(
            @RequestParam(required = false) String wordType,
            @RequestParam(required = false) Integer level,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> result = new HashMap<>();
        List<SensitiveWord> words;
        
        if (wordType != null) {
            words = sensitiveWordService.getWordsByType(wordType);
        } else if (level != null) {
            words = sensitiveWordService.getWordsByLevel(level);
        } else {
            words = sensitiveWordService.getAllEnabledWords();
        }
        
        result.put("code", 0);
        result.put("data", words);
        result.put("total", words.size());
        return ResponseEntity.ok(result);
    }
    
    /**
     * 添加敏感词
     */
    @PostMapping("/add")
    public ResponseEntity<?> addSensitiveWord(@RequestBody SensitiveWord sensitiveWord, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取用户ID
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
        // 验证用户权限（管理员权限已在网关层验证）
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        
        // 设置创建人
        sensitiveWord.setCreateBy(userId);
        
        boolean success = sensitiveWordService.addSensitiveWord(sensitiveWord);
        if (success) {
            result.put("code", 0);
            result.put("message", "敏感词添加成功");
        } else {
            result.put("code", -1);
            result.put("message", "敏感词添加失败");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 更新敏感词
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateSensitiveWord(@RequestBody SensitiveWord sensitiveWord, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取用户ID
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
        // 验证用户权限（管理员权限已在网关层验证）
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        
        boolean success = sensitiveWordService.updateSensitiveWord(sensitiveWord);
        if (success) {
            result.put("code", 0);
            result.put("message", "敏感词更新成功");
        } else {
            result.put("code", -1);
            result.put("message", "敏感词更新失败");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 删除敏感词
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSensitiveWord(@PathVariable int id, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取用户ID
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
        // 验证用户权限（管理员权限已在网关层验证）
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        
        boolean success = sensitiveWordService.deleteSensitiveWord(id);
        if (success) {
            result.put("code", 0);
            result.put("message", "敏感词删除成功");
        } else {
            result.put("code", -1);
            result.put("message", "敏感词删除失败");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 启用/禁用敏感词
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> toggleSensitiveWordStatus(@PathVariable int id, @RequestParam int status, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取用户ID
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
        // 验证用户权限（管理员权限已在网关层验证）
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        
        boolean success = sensitiveWordService.toggleSensitiveWordStatus(id, status);
        if (success) {
            result.put("code", 0);
            result.put("message", status == 1 ? "敏感词启用成功" : "敏感词禁用成功");
        } else {
            result.put("code", -1);
            result.put("message", "操作失败");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 重新加载敏感词库
     */
    @PostMapping("/reload")
    public ResponseEntity<?> reloadSensitiveWords() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            sensitiveWordService.reloadSensitiveWords();
            result.put("code", 0);
            result.put("message", "敏感词库重新加载成功");
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "敏感词库重新加载失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 手动触发敏感词检查任务（Windows开发环境）
     */
    @PostMapping("/manual-check")
    public ResponseEntity<?> manualCheckSensitiveWords() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            sensitiveWordSchedule.manualCheckExistingComments();
            result.put("code", 0);
            result.put("message", "敏感词检查任务执行成功");
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "敏感词检查任务执行失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 手动触发敏感词库重新加载（Windows开发环境）
     */
    @PostMapping("/manual-reload")
    public ResponseEntity<?> manualReloadSensitiveWords() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            sensitiveWordSchedule.manualReloadSensitiveWords();
            result.put("code", 0);
            result.put("message", "敏感词库重新加载成功");
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "敏感词库重新加载失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 批量过滤已存在的评论
     */
    @PostMapping("/filter-comments")
    public ResponseEntity<?> filterExistingComments() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int filteredCount = commentService.filterExistingComments();
            result.put("code", 0);
            result.put("message", "批量过滤完成");
            result.put("filteredCount", filteredCount);
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "批量过滤失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 过滤指定评论
     */
    @PostMapping("/filter-comment/{commentId}")
    public ResponseEntity<?> filterComment(@PathVariable int commentId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = commentService.filterComment(commentId);
            if (success) {
                result.put("code", 0);
                result.put("message", "评论过滤成功");
            } else {
                result.put("code", -1);
                result.put("message", "评论过滤失败或无需过滤");
            }
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "评论过滤失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取需要过滤的评论数量
     */
    @GetMapping("/comments-need-filter")
    public ResponseEntity<?> getCommentsNeedFilterCount() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int count = commentService.getCommentsNeedFilterCount();
            result.put("code", 0);
            result.put("count", count);
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "获取数量失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取敏感词统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("enabled", sensitiveWordConfig.isEnabled());
        stats.put("replacement", sensitiveWordConfig.getReplacement());
        stats.put("severeStrategy", sensitiveWordConfig.getSevereStrategy());
        stats.put("mildStrategy", sensitiveWordConfig.getMildStrategy());
        stats.put("maxSensitiveWords", sensitiveWordConfig.getMaxSensitiveWords());
        
        result.put("data", stats);
        return ResponseEntity.ok(result);
    }
} 