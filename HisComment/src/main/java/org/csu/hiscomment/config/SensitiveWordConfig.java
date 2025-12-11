package org.csu.hiscomment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 敏感词过滤配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sensitive.word")
public class SensitiveWordConfig {
    
    /**
     * 是否启用敏感词过滤
     */
    private boolean enabled = true;
    

    
    /**
     * 敏感词替换符
     */
    private String replacement = "***";
    
    /**
     * 是否记录敏感词日志
     */
    private boolean logSensitiveWords = true;
    
    /**
     * 严重敏感词处理策略：REJECT-拒绝发布，FILTER-过滤后发布
     */
    private String severeStrategy = "REJECT";
    
    /**
     * 轻度敏感词处理策略：REJECT-拒绝发布，FILTER-过滤后发布
     */
    private String mildStrategy = "FILTER";
    
    /**
     * 是否启用实时检测API
     */
    private boolean enableCheckApi = true;
    

    
    /**
     * 敏感词检测阈值（超过此数量拒绝发布）
     */
    private int maxSensitiveWords = 10;
} 