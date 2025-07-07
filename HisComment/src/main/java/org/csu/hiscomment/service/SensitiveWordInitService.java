package org.csu.hiscomment.service;

import org.csu.hiscomment.entity.SensitiveWord;
import org.csu.hiscomment.mapper.SensitiveWordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.PostConstruct;
import java.util.Date;

/**
 * 敏感词初始化服务
 * 确保数据库中有默认的敏感词数据
 */
@Slf4j
@Service
public class SensitiveWordInitService {
    
    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    
    @PostConstruct
    public void initDefaultSensitiveWords() {
        try {
            // 检查数据库中是否已有敏感词
            long count = sensitiveWordMapper.selectCount(null);
            if (count == 0) {
                log.info("数据库中没有敏感词数据，开始初始化默认敏感词...");
                initDefaultWords();
                log.info("默认敏感词初始化完成");
            } else {
                log.info("数据库中已有 {} 个敏感词，跳过初始化", count);
            }
        } catch (Exception e) {
            log.error("初始化默认敏感词失败", e);
        }
    }
    
    /**
     * 初始化默认敏感词
     */
    private void initDefaultWords() {
        // 政治敏感词
        addDefaultWord("反对国家", "POLITICAL", 3);
        addDefaultWord("颠覆政权", "POLITICAL", 3);
        addDefaultWord("分裂国家", "POLITICAL", 3);
        addDefaultWord("台独", "POLITICAL", 3);
        addDefaultWord("藏独", "POLITICAL", 3);
        addDefaultWord("疆独", "POLITICAL", 3);
        addDefaultWord("港独", "POLITICAL", 3);
        addDefaultWord("法轮功", "POLITICAL", 3);
        addDefaultWord("六四", "POLITICAL", 3);
        addDefaultWord("天安门事件", "POLITICAL", 3);
        addDefaultWord("政治敏感", "POLITICAL", 3);
        addDefaultWord("反动", "POLITICAL", 3);
        addDefaultWord("反革命", "POLITICAL", 3);
        
        // 恐怖活动相关
        addDefaultWord("恐怖主义", "TERRORISM", 3);
        addDefaultWord("恐怖分子", "TERRORISM", 3);
        addDefaultWord("炸弹", "TERRORISM", 3);
        addDefaultWord("爆炸", "TERRORISM", 3);
        addDefaultWord("自杀式袭击", "TERRORISM", 3);
        addDefaultWord("极端组织", "TERRORISM", 3);
        addDefaultWord("ISIS", "TERRORISM", 3);
        addDefaultWord("基地组织", "TERRORISM", 3);
        addDefaultWord("恐怖袭击", "TERRORISM", 3);
        addDefaultWord("暴力", "TERRORISM", 2);
        addDefaultWord("血腥", "TERRORISM", 2);
        
        // 极端思想
        addDefaultWord("极端主义", "EXTREMIST", 3);
        addDefaultWord("种族主义", "EXTREMIST", 3);
        addDefaultWord("纳粹", "EXTREMIST", 3);
        addDefaultWord("法西斯", "EXTREMIST", 3);
        addDefaultWord("仇恨言论", "EXTREMIST", 2);
        addDefaultWord("歧视", "EXTREMIST", 2);
        addDefaultWord("排外", "EXTREMIST", 2);
        addDefaultWord("民族仇恨", "EXTREMIST", 3);
        addDefaultWord("宗教极端", "EXTREMIST", 3);
        addDefaultWord("邪教", "EXTREMIST", 3);
        
        // 辱骂词汇
        addDefaultWord("傻逼", "ABUSE", 1);
        addDefaultWord("狗屎", "ABUSE", 1);
        addDefaultWord("混蛋", "ABUSE", 1);
        addDefaultWord("王八蛋", "ABUSE", 1);
        addDefaultWord("贱人", "ABUSE", 1);
        addDefaultWord("婊子", "ABUSE", 1);
        addDefaultWord("狗娘养的", "ABUSE", 1);
        addDefaultWord("去死", "ABUSE", 1);
        addDefaultWord("该死", "ABUSE", 1);
        addDefaultWord("垃圾", "ABUSE", 1);
        addDefaultWord("废物", "ABUSE", 1);
        addDefaultWord("白痴", "ABUSE", 1);
        addDefaultWord("智障", "ABUSE", 1);
        addDefaultWord("脑残", "ABUSE", 1);
        
        // 违法内容
        addDefaultWord("毒品", "ILLEGAL", 3);
        addDefaultWord("大麻", "ILLEGAL", 3);
        addDefaultWord("海洛因", "ILLEGAL", 3);
        addDefaultWord("冰毒", "ILLEGAL", 3);
        addDefaultWord("摇头丸", "ILLEGAL", 3);
        addDefaultWord("色情", "ILLEGAL", 3);
        addDefaultWord("黄色", "ILLEGAL", 2);
        addDefaultWord("赌博", "ILLEGAL", 3);
        addDefaultWord("博彩", "ILLEGAL", 3);
        addDefaultWord("私服", "ILLEGAL", 2);
        addDefaultWord("外挂", "ILLEGAL", 2);
        addDefaultWord("盗版", "ILLEGAL", 2);
        addDefaultWord("破解", "ILLEGAL", 2);
        
        // 垃圾信息
        addDefaultWord("代刷", "SPAM", 1);
        addDefaultWord("刷单", "SPAM", 1);
        addDefaultWord("刷钻", "SPAM", 1);
        addDefaultWord("刷信誉", "SPAM", 1);
        addDefaultWord("刷流量", "SPAM", 1);
        addDefaultWord("刷粉丝", "SPAM", 1);
        addDefaultWord("代考", "SPAM", 1);
        addDefaultWord("代写", "SPAM", 1);
        addDefaultWord("代做", "SPAM", 1);
        addDefaultWord("代发", "SPAM", 1);
        addDefaultWord("群发", "SPAM", 1);
        addDefaultWord("推广", "SPAM", 1);
    }
    
    /**
     * 添加默认敏感词
     */
    private void addDefaultWord(String word, String wordType, Integer level) {
        try {
            SensitiveWord sensitiveWord = new SensitiveWord();
            sensitiveWord.setSensitiveWord(word);
            sensitiveWord.setSensitiveWordType(wordType);
            sensitiveWord.setSensitiveLevel(level);
            sensitiveWord.setSensitiveStatus(1); // 默认启用
            sensitiveWord.setCreateTime(new Date());
            sensitiveWord.setCreateBy(0); // 系统内置
            sensitiveWordMapper.insert(sensitiveWord);
        } catch (Exception e) {
            log.warn("添加默认敏感词失败: {}", word, e);
        }
    }
} 