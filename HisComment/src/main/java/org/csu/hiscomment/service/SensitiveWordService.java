package org.csu.hiscomment.service;

import org.csu.hiscomment.entity.SensitiveWord;
import java.util.List;

public interface SensitiveWordService {
    
    /**
     * 获取所有启用的敏感词
     */
    List<SensitiveWord> getAllEnabledWords();
    
    /**
     * 根据类型获取敏感词
     */
    List<SensitiveWord> getWordsByType(String wordType);
    
    /**
     * 根据级别获取敏感词
     */
    List<SensitiveWord> getWordsByLevel(int minLevel);
    
    /**
     * 添加敏感词
     */
    boolean addSensitiveWord(SensitiveWord sensitiveWord);
    
    /**
     * 更新敏感词
     */
    boolean updateSensitiveWord(SensitiveWord sensitiveWord);
    
    /**
     * 删除敏感词
     */
    boolean deleteSensitiveWord(int id);
    
    /**
     * 启用/禁用敏感词
     */
    boolean toggleSensitiveWordStatus(int id, int status);
    
    /**
     * 重新加载敏感词库
     */
    void reloadSensitiveWords();
} 