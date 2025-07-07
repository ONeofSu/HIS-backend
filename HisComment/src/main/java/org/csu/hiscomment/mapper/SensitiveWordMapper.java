package org.csu.hiscomment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.hiscomment.entity.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {
    
    /**
     * 获取所有启用的敏感词
     */
    @Select("SELECT sensitive_id, sensitive_word, sensitive_word_type, sensitive_level, sensitive_status, create_time, create_by FROM sensitive_words WHERE sensitive_status = 1 ORDER BY sensitive_level DESC, sensitive_word")
    List<SensitiveWord> getAllEnabledWords();
    
    /**
     * 根据类型获取敏感词
     */
    @Select("SELECT sensitive_id, sensitive_word, sensitive_word_type, sensitive_level, sensitive_status, create_time, create_by FROM sensitive_words WHERE sensitive_status = 1 AND sensitive_word_type = #{wordType}")
    List<SensitiveWord> getWordsByType(String wordType);
    
    /**
     * 根据级别获取敏感词
     */
    @Select("SELECT sensitive_id, sensitive_word, sensitive_word_type, sensitive_level, sensitive_status, create_time, create_by FROM sensitive_words WHERE sensitive_status = 1 AND sensitive_level >= #{minLevel}")
    List<SensitiveWord> getWordsByLevel(int minLevel);
} 