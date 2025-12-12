package org.csu.hiscomment.utils;

import org.csu.hiscomment.entity.SensitiveWord;
import org.csu.hiscomment.service.SensitiveWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SensitiveWordFilter单元测试")
class SensitiveWordFilterTest {

    @Mock
    private SensitiveWordService sensitiveWordService;

    @InjectMocks
    private SensitiveWordFilter sensitiveWordFilter;

    private List<SensitiveWord> testSensitiveWords;

    @BeforeEach
    void setUp() {
        // 准备测试敏感词数据
        testSensitiveWords = new ArrayList<>();
        
        SensitiveWord word1 = new SensitiveWord();
        word1.setSensitiveWord("测试敏感词");
        word1.setSensitiveWordType("辱骂");
        word1.setSensitiveLevel(1);
        word1.setSensitiveStatus(1);
        testSensitiveWords.add(word1);

        SensitiveWord word2 = new SensitiveWord();
        word2.setSensitiveWord("重度敏感");
        word2.setSensitiveWordType("违法内容");
        word2.setSensitiveLevel(3);
        word2.setSensitiveStatus(1);
        testSensitiveWords.add(word2);

        SensitiveWord word3 = new SensitiveWord();
        word3.setSensitiveWord("政治敏感");
        word3.setSensitiveWordType("政治敏感");
        word3.setSensitiveLevel(1);
        word3.setSensitiveStatus(1);
        testSensitiveWords.add(word3);
    }

    @Test
    @DisplayName("测试检查敏感词 - 无敏感词")
    void testCheckSensitiveWords_NoSensitive() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(Collections.emptyList());
        sensitiveWordFilter.init();

        String text = "这是一条正常的评论内容";

        // Act
        SensitiveWordFilter.SensitiveCheckResult result = sensitiveWordFilter.checkSensitiveWords(text);

        // Assert
        assertNotNull(result);
        assertFalse(result.hasSensitive());
        assertNull(result.sensitiveWords());
    }

    @Test
    @DisplayName("测试检查敏感词 - 包含敏感词")
    void testCheckSensitiveWords_WithSensitive() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        String text = "这是一条包含测试敏感词的评论";

        // Act
        SensitiveWordFilter.SensitiveCheckResult result = sensitiveWordFilter.checkSensitiveWords(text);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasSensitive());
        assertNotNull(result.sensitiveWords());
        assertTrue(result.sensitiveWords().contains("测试敏感词"));
    }

    @Test
    @DisplayName("测试过滤敏感词 - 无敏感词")
    void testFilterSensitiveWords_NoSensitive() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(Collections.emptyList());
        sensitiveWordFilter.init();

        String text = "这是一条正常的评论内容";

        // Act
        String result = sensitiveWordFilter.filterSensitiveWords(text);

        // Assert
        assertEquals(text, result); // 应该保持不变
    }

    @Test
    @DisplayName("测试过滤敏感词 - 包含敏感词")
    void testFilterSensitiveWords_WithSensitive() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        String text = "这是一条包含测试敏感词的评论";

        // Act
        String result = sensitiveWordFilter.filterSensitiveWords(text);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("***")); // 敏感词应该被替换为***
        assertFalse(result.contains("测试敏感词")); // 原始敏感词不应该存在
    }

    @Test
    @DisplayName("测试获取敏感词级别")
    void testGetSensitiveWordLevel() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        // Act
        int level1 = sensitiveWordFilter.getSensitiveWordLevel("测试敏感词");
        int level2 = sensitiveWordFilter.getSensitiveWordLevel("重度敏感");
        int level3 = sensitiveWordFilter.getSensitiveWordLevel("不存在的词");

        // Assert
        assertEquals(1, level1);
        assertEquals(3, level2);
        assertEquals(1, level3); // 不存在的词返回默认级别1
    }

    @Test
    @DisplayName("测试检查空文本")
    void testCheckSensitiveWords_EmptyText() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        // Act
        SensitiveWordFilter.SensitiveCheckResult result1 = sensitiveWordFilter.checkSensitiveWords("");
        SensitiveWordFilter.SensitiveCheckResult result2 = sensitiveWordFilter.checkSensitiveWords(null);

        // Assert
        assertNotNull(result1);
        assertFalse(result1.hasSensitive());
        assertNotNull(result2);
        assertFalse(result2.hasSensitive());
    }

    @Test
    @DisplayName("测试过滤空文本")
    void testFilterSensitiveWords_EmptyText() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        // Act
        String result1 = sensitiveWordFilter.filterSensitiveWords("");
        String result2 = sensitiveWordFilter.filterSensitiveWords(null);

        // Assert
        assertEquals("", result1);
        assertNull(result2);
    }

    @Test
    @DisplayName("测试重新加载敏感词库")
    void testReloadFromDatabase() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        // 准备新的敏感词列表
        List<SensitiveWord> newWords = new ArrayList<>();
        SensitiveWord newWord = new SensitiveWord();
        newWord.setSensitiveWord("新敏感词");
        newWord.setSensitiveWordType("垃圾信息");
        newWord.setSensitiveLevel(2);
        newWord.setSensitiveStatus(1);
        newWords.add(newWord);

        when(sensitiveWordService.getAllEnabledWords()).thenReturn(newWords);

        // Act
        sensitiveWordFilter.reloadFromDatabase();

        // Assert
        SensitiveWordFilter.SensitiveCheckResult result = sensitiveWordFilter.checkSensitiveWords("这是一条包含新敏感词的评论");
        assertTrue(result.hasSensitive());
        assertTrue(result.sensitiveWords().contains("新敏感词"));
    }

    @Test
    @DisplayName("测试多个敏感词")
    void testMultipleSensitiveWords() {
        // Arrange
        when(sensitiveWordService.getAllEnabledWords()).thenReturn(testSensitiveWords);
        sensitiveWordFilter.init();

        String text = "这是一条包含测试敏感词和重度敏感的评论";

        // Act
        SensitiveWordFilter.SensitiveCheckResult result = sensitiveWordFilter.checkSensitiveWords(text);
        String filtered = sensitiveWordFilter.filterSensitiveWords(text);

        // Assert
        assertTrue(result.hasSensitive());
        assertTrue(result.sensitiveWords().size() >= 2);
        assertTrue(filtered.contains("***"));
    }
}

