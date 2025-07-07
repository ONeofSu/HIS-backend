package org.csu.hiscomment.schedule;

import org.csu.hiscomment.service.CommentService;
import org.csu.hiscomment.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * 敏感词任务（Windows开发环境版本）
 * 
 * 注意：在Windows开发环境下，定时任务配置复杂且不必要
 * 建议使用手动触发或API调用的方式
 */
@Slf4j
@Component
public class SensitiveWordSchedule {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private SensitiveWordService sensitiveWordService;
    
    /**
     * 手动触发敏感词检查任务
     * 可以通过API接口调用
     */
    public void manualCheckExistingComments() {
        log.info("手动触发敏感词检查任务");
        
        try {
            int needFilterCount = commentService.getCommentsNeedFilterCount();
            if (needFilterCount > 0) {
                log.info("发现 {} 条评论需要重新过滤", needFilterCount);
                
                // 批量过滤评论
                int filteredCount = commentService.filterExistingComments();
                log.info("成功过滤 {} 条评论", filteredCount);
            } else {
                log.info("没有发现需要过滤的评论");
            }
        } catch (Exception e) {
            log.error("敏感词检查任务执行失败", e);
            throw new RuntimeException("敏感词检查任务执行失败", e);
        }
    }
    
    /**
     * 手动触发敏感词库重新加载
     * 可以通过API接口调用
     */
    public void manualReloadSensitiveWords() {
        log.info("手动触发敏感词库重新加载");
        
        try {
            sensitiveWordService.reloadSensitiveWords();
            log.info("敏感词库重新加载完成");
        } catch (Exception e) {
            log.error("敏感词库重新加载失败", e);
            throw new RuntimeException("敏感词库重新加载失败", e);
        }
    }
} 