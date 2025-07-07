package org.csu.hiscomment.utils;

import org.csu.hiscomment.config.SensitiveWordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.csu.hiscomment.entity.SensitiveWord;
import org.csu.hiscomment.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * 敏感词过滤器
 * 使用DFA算法实现高效的敏感词检测
 */
@Slf4j
@Component
public class SensitiveWordFilter {
    
    // DFA算法的根节点
    private Map<Character, Object> sensitiveWordMap;
    
    // 敏感词类型映射表（敏感词 -> 类型）
    private Map<String, SensitiveType> sensitiveWordTypeMap;
    
    // 敏感词级别映射表（敏感词 -> 级别）
    private Map<String, Integer> sensitiveWordLevelMap;
    
    // 敏感词替换符
    private static final String REPLACEMENT = "***";
    
    // 敏感词服务
    @Autowired
    private SensitiveWordService sensitiveWordService;
    
    // 是否使用数据库存储
    @Autowired
    private SensitiveWordConfig config;
    
    // 敏感词分类
    @lombok.Getter
    public enum SensitiveType {
        POLITICAL("政治敏感"),      // 反对国家、政治敏感
        TERRORISM("恐怖活动"),      // 恐怖活动相关
        EXTREMIST("极端思想"),      // 极端思想
        ABUSE("辱骂"),             // 辱骂、人身攻击
        ILLEGAL("违法内容"),        // 其他违法内容
        SPAM("垃圾信息");           // 垃圾信息、广告
        
        private final String description;
        
        SensitiveType(String description) {
            this.description = description;
        }
    }
    
    @PostConstruct
    public void init() {
        sensitiveWordMap = new HashMap<>();
        sensitiveWordTypeMap = new HashMap<>();
        sensitiveWordLevelMap = new HashMap<>();
        loadSensitiveWordsFromDatabase();
        
        if (sensitiveWordMap.isEmpty()) {
            log.error("敏感词库初始化失败！");
            log.error("请检查以下配置：");
            log.error("1. 数据库连接是否正常");
            log.error("2. 敏感词数据是否已正确导入");
            log.error("3. 应用配置是否正确");
        } else {
            log.info("敏感词过滤器初始化完成，共加载 {} 个敏感词", sensitiveWordMap.size());
        }
    }
    

    
    /**
     * 从数据库加载敏感词库
     */
    private void loadSensitiveWordsFromDatabase() {
        try {
            if (sensitiveWordService != null) {
                List<SensitiveWord> words = sensitiveWordService.getAllEnabledWords();
                if (words.isEmpty()) {
                    log.warn("数据库中没有找到敏感词，请检查敏感词数据是否正确导入");
                    log.warn("建议执行敏感词初始化脚本或通过管理界面添加敏感词");
                } else {
                    for (SensitiveWord word : words) {
                        addSensitiveWord(word.getSensitiveWord());
                        // 同时建立敏感词类型和级别映射
                        SensitiveType type = mapDatabaseTypeToEnum(word.getSensitiveWordType());
                        sensitiveWordTypeMap.put(word.getSensitiveWord(), type);
                        sensitiveWordLevelMap.put(word.getSensitiveWord(), word.getSensitiveLevel());
                        log.debug("加载敏感词: {} -> {} (级别{})", word.getSensitiveWord(), type, word.getSensitiveLevel());
                    }
                    log.info("从数据库加载了 {} 个敏感词", words.size());
                    log.info("敏感词类型映射: {}", sensitiveWordTypeMap);
                }
            } else {
                log.error("敏感词服务不可用，请检查服务配置");
            }
        } catch (Exception e) {
            log.error("从数据库加载敏感词失败", e);
            log.error("请检查数据库连接和敏感词表结构");
        }
    }
    

    
    /**
     * 重新从数据库加载敏感词库
     */
    public void reloadFromDatabase() {
        sensitiveWordMap.clear();
        sensitiveWordTypeMap.clear();
        sensitiveWordLevelMap.clear();
        loadSensitiveWordsFromDatabase();
    }

    
    
    /**
     * 添加敏感词到DFA树
     */
    private void addSensitiveWord(String word) {
        if (!StringUtils.hasText(word)) {
            return;
        }
        
        Map<Character, Object> currentMap = sensitiveWordMap;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            // 使用computeIfAbsent简化代码，并进行类型安全的转换
            @SuppressWarnings("unchecked")
            Map<Character, Object> nextMap = (Map<Character, Object>) currentMap.computeIfAbsent(c, k -> new HashMap<>());
            currentMap = nextMap;
            
            // 如果是最后一个字符，则设置为结束标识
            if (i == word.length() - 1) {
                currentMap.put('\0', true);
            }
        }
    }
    
    /**
     * 检查文本是否包含敏感词
     * @param text 待检查的文本
     * @return 检查结果
     */
    public SensitiveCheckResult checkSensitiveWords(String text) {
        if (!StringUtils.hasText(text)) {
            return new SensitiveCheckResult(false, null, null);
        }
        
        // 检查敏感词库是否已加载
        if (sensitiveWordMap.isEmpty()) {
            log.warn("敏感词库为空，无法进行敏感词检测");
            log.warn("请检查数据库连接和敏感词数据是否正确导入");
            return new SensitiveCheckResult(false, null, null);
        }
        
        List<String> foundWords = new ArrayList<>();
        List<SensitiveType> foundTypes = new ArrayList<>();
        
        for (int i = 0; i < text.length(); i++) {
            int length = checkSensitiveWord(text, i);
            if (length > 0) {
                String word = text.substring(i, i + length);
                foundWords.add(word);
                // 从数据库获取敏感词类型，而不是硬编码分类
                SensitiveType type = getSensitiveWordTypeFromDatabase(word);
                foundTypes.add(type);
                log.debug("检测到敏感词: {} -> {} (级别{})", word, type, getSensitiveWordLevel(word));
                i += length - 1; // 跳过已检查的字符
            }
        }
        
        boolean hasSensitive = !foundWords.isEmpty();
        return new SensitiveCheckResult(hasSensitive, foundWords, foundTypes);
    }
    
    /**
     * 检查从指定位置开始的敏感词
     * @param text 文本
     * @param beginIndex 开始位置
     * @return 敏感词长度，如果不是敏感词返回0
     */
    private int checkSensitiveWord(String text, int beginIndex) {
        Map<Character, Object> currentMap = sensitiveWordMap;
        int wordLength = 0;
        
        for (int i = beginIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            
            // 进行类型安全的转换
            @SuppressWarnings("unchecked")
            Map<Character, Object> nextMap = (Map<Character, Object>) currentMap.get(c);
            currentMap = nextMap;
            
            if (currentMap == null) {
                break;
            }
            
            wordLength++;
            
            // 如果到达敏感词结尾
            if (currentMap.containsKey('\0')) {
                return wordLength;
            }
        }
        
        return 0;
    }
    
    /**
     * 过滤敏感词，用***替换
     * @param text 原始文本
     * @return 过滤后的文本
     */
    public String filterSensitiveWords(String text) {
        if (!StringUtils.hasText(text)) {
            return text;
        }
        
        // 检查敏感词库是否已加载
        if (sensitiveWordMap.isEmpty()) {
            log.warn("敏感词库为空，无法进行敏感词过滤");
            log.warn("请检查数据库连接和敏感词数据是否正确导入");
            return text; // 返回原文本，不进行过滤
        }
        
        StringBuilder result = new StringBuilder(text);
        int offset = 0;
        
        for (int i = 0; i < text.length(); i++) {
            int length = checkSensitiveWord(text, i);
            if (length > 0) {
                result.replace(i + offset, i + offset + length, REPLACEMENT);
                offset += REPLACEMENT.length() - length;
                i += length - 1;
            }
        }
        
        return result.toString();
    }
    
    /**
     * 获取敏感词类型
     * @param word 敏感词
     * @return 敏感词类型
     */
    private SensitiveType getSensitiveWordTypeFromDatabase(String word) {
        // 从内存映射表中获取类型
        SensitiveType type = sensitiveWordTypeMap.get(word);
        if (type != null) {
            return type;
        }
        
        // 如果映射表中没有找到，使用默认分类逻辑
        return classifySensitiveWord(word);
    }
    
    /**
     * 获取敏感词级别
     * @param word 敏感词
     * @return 敏感词级别（1-轻度，2-中度，3-重度）
     */
    public int getSensitiveWordLevel(String word) {
        // 从内存映射表中获取级别
        Integer level = sensitiveWordLevelMap.get(word);
        if (level != null) {
            return level;
        }
        
        // 如果映射表中没有找到，返回默认级别1
        return 1;
    }
    
    /**
     * 将数据库中的类型映射到枚举
     * @param dbType 数据库中的类型
     * @return 枚举类型
     */
    private SensitiveType mapDatabaseTypeToEnum(String dbType) {
        if (dbType == null) {
            return SensitiveType.SPAM;
        }
        
        switch (dbType) {
            case "政治敏感":
            case "POLITICAL":
                return SensitiveType.POLITICAL;
            case "暴力恐怖":
            case "恐怖活动":
            case "TERRORISM":
                return SensitiveType.TERRORISM;
            case "极端思想":
            case "EXTREMIST":
                return SensitiveType.EXTREMIST;
            case "辱骂攻击":
            case "辱骂":
            case "ABUSE":
                return SensitiveType.ABUSE;
            case "其他":
            case "违法内容":
            case "ILLEGAL":
                return SensitiveType.ILLEGAL;
            case "垃圾信息":
            case "SPAM":
                return SensitiveType.SPAM;
            default:
                log.warn("未知的敏感词类型: {}, 默认归类为垃圾信息", dbType);
                return SensitiveType.SPAM;
        }
    }
    
    /**
     * 分类敏感词类型（备用方法）
     * @param word 敏感词
     * @return 敏感词类型
     */
    private SensitiveType classifySensitiveWord(String word) {
        // 这里可以根据敏感词的特征进行分类
        // 实际项目中可以维护一个敏感词分类映射表
        if (word.contains("反对") || word.contains("政治") || word.contains("台独") || 
            word.contains("藏独") || word.contains("疆独") || word.contains("港独")) {
            return SensitiveType.POLITICAL;
        } else if (word.contains("恐怖") || word.contains("爆炸") || word.contains("袭击")) {
            return SensitiveType.TERRORISM;
        } else if (word.contains("极端") || word.contains("纳粹") || word.contains("法西斯")) {
            return SensitiveType.EXTREMIST;
        } else if (word.contains("傻") || word.contains("狗") || word.contains("贱") || 
                   word.contains("死") || word.contains("垃圾")) {
            return SensitiveType.ABUSE;
        } else if (word.contains("毒品") || word.contains("色情") || word.contains("赌博")) {
            return SensitiveType.ILLEGAL;
        } else {
            return SensitiveType.SPAM;
        }
    }
    
    /**
     * 敏感词检查结果
     */
    public record SensitiveCheckResult(
        boolean hasSensitive,
        List<String> sensitiveWords,
        List<SensitiveType> sensitiveTypes
    ) {
        public String getSensitiveWordsString() {
            return sensitiveWords != null ? String.join(",", sensitiveWords) : "";
        }
        
        public String getSensitiveTypesString() {
            if (sensitiveTypes == null) return "";
            StringBuilder sb = new StringBuilder();
            for (SensitiveType type : sensitiveTypes) {
                if (!sb.isEmpty()) sb.append(",");
                sb.append(type.getDescription());
            }
            return sb.toString();
        }
        
        /**
         * 检查是否包含重度敏感词（级别3）
         */
        public boolean hasHighLevelSensitive() {
            if (sensitiveWords == null || sensitiveWords.isEmpty()) {
                return false;
            }
            // 这里需要访问SensitiveWordFilter实例来获取级别
            // 由于record的限制，我们将在CommentServiceImpl中实现这个逻辑
            return false;
        }
    }
} 