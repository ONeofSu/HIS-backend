package org.csu.hiscomment.service.impl;

import org.csu.hiscomment.entity.SensitiveWord;
import org.csu.hiscomment.mapper.SensitiveWordMapper;
import org.csu.hiscomment.service.SensitiveWordService;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.csu.hiscomment.exception.BusinessException;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {
    
    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public List<SensitiveWord> getAllEnabledWords() {
        return sensitiveWordMapper.getAllEnabledWords();
    }
    
    @Override
    public List<SensitiveWord> getWordsByType(String wordType) {
        return sensitiveWordMapper.getWordsByType(wordType);
    }
    
    @Override
    public List<SensitiveWord> getWordsByLevel(int minLevel) {
        return sensitiveWordMapper.getWordsByLevel(minLevel);
    }
    
    @Override
    public boolean addSensitiveWord(SensitiveWord sensitiveWord) {
        sensitiveWord.setCreateTime(new Date());
        sensitiveWord.setSensitiveStatus(1); // 默认启用
        boolean result = sensitiveWordMapper.insert(sensitiveWord) > 0;
        if (result) {
            // 重新加载敏感词库
            reloadSensitiveWords();
        }
        return result;
    }
    
    @Override
    public boolean updateSensitiveWord(SensitiveWord sensitiveWord) {
        SensitiveWord oldWord = sensitiveWordMapper.selectById(sensitiveWord.getSensitiveId());
        if (oldWord == null) return false;

        // 只更新前端传来的字段
        if (sensitiveWord.getSensitiveWord() != null) oldWord.setSensitiveWord(sensitiveWord.getSensitiveWord());
        if (sensitiveWord.getSensitiveWordType() != null) oldWord.setSensitiveWordType(sensitiveWord.getSensitiveWordType());
        if (sensitiveWord.getSensitiveLevel() != 0) oldWord.setSensitiveLevel(sensitiveWord.getSensitiveLevel());
        if (sensitiveWord.getSensitiveStatus() != null) oldWord.setSensitiveStatus(sensitiveWord.getSensitiveStatus());
        // createBy、createTime等字段不允许更新

        boolean result = sensitiveWordMapper.updateById(oldWord) > 0;
        if (result) {
            // 重新加载敏感词库
            reloadSensitiveWords();
        }
        return result;
    }
    
    @Override
    public boolean deleteSensitiveWord(int id) {
        boolean result = sensitiveWordMapper.deleteById(id) > 0;
        if (result) {
            // 重新加载敏感词库
            reloadSensitiveWords();
        }
        return result;
    }
    
    @Override
    public boolean toggleSensitiveWordStatus(int id, int status) {
        SensitiveWord word = sensitiveWordMapper.selectById(id);
        if (word == null) {
            throw new BusinessException("敏感词不存在");
        }
        if (word.getSensitiveStatus() != null && word.getSensitiveStatus() == status) {
            throw new BusinessException("敏感词已" + (status == 1 ? "启用" : "禁用") + "，无法重复操作");
        }
        UpdateWrapper<SensitiveWord> wrapper = new UpdateWrapper<>();
        wrapper.eq("sensitive_id", id).set("sensitive_status", status);
        boolean result = sensitiveWordMapper.update(null, wrapper) > 0;
        if (result) {
            reloadSensitiveWords();
        }
        return result;
    }
    
    @Override
    public void reloadSensitiveWords() {
        // 通过ApplicationContext获取SensitiveWordFilter，避免循环依赖
        SensitiveWordFilter filter = applicationContext.getBean(SensitiveWordFilter.class);
        filter.reloadFromDatabase();
    }
} 