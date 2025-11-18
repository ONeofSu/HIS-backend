package org.csu.hiscomment.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.csu.hiscomment.entity.SensitiveWord;
import org.csu.hiscomment.exception.BusinessException;
import org.csu.hiscomment.mapper.SensitiveWordMapper;
import org.csu.hiscomment.service.impl.SensitiveWordServiceImpl;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SensitiveWordServiceImpl单元测试")
class SensitiveWordServiceImplTest {

    @Mock
    private SensitiveWordMapper sensitiveWordMapper;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private SensitiveWordFilter sensitiveWordFilter;

    @InjectMocks
    private SensitiveWordServiceImpl sensitiveWordService;

    private SensitiveWord testSensitiveWord;

    @BeforeEach
    void setUp() {
        testSensitiveWord = new SensitiveWord();
        testSensitiveWord.setSensitiveId(1);
        testSensitiveWord.setSensitiveWord("测试敏感词");
        testSensitiveWord.setSensitiveWordType("辱骂");
        testSensitiveWord.setSensitiveLevel(1);
        testSensitiveWord.setSensitiveStatus(1);
        testSensitiveWord.setCreateTime(new Date());
        testSensitiveWord.setCreateBy(1);
    }

    @Test
    @DisplayName("测试获取所有启用的敏感词")
    void testGetAllEnabledWords() {
        // Arrange
        List<SensitiveWord> words = Arrays.asList(testSensitiveWord);
        when(sensitiveWordMapper.getAllEnabledWords()).thenReturn(words);

        // Act
        List<SensitiveWord> result = sensitiveWordService.getAllEnabledWords();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试敏感词", result.get(0).getSensitiveWord());
        verify(sensitiveWordMapper, times(1)).getAllEnabledWords();
    }

    @Test
    @DisplayName("测试根据类型获取敏感词")
    void testGetWordsByType() {
        // Arrange
        String wordType = "辱骂";
        List<SensitiveWord> words = Arrays.asList(testSensitiveWord);
        when(sensitiveWordMapper.getWordsByType(wordType)).thenReturn(words);

        // Act
        List<SensitiveWord> result = sensitiveWordService.getWordsByType(wordType);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sensitiveWordMapper, times(1)).getWordsByType(wordType);
    }

    @Test
    @DisplayName("测试根据级别获取敏感词")
    void testGetWordsByLevel() {
        // Arrange
        int minLevel = 1;
        List<SensitiveWord> words = Arrays.asList(testSensitiveWord);
        when(sensitiveWordMapper.getWordsByLevel(minLevel)).thenReturn(words);

        // Act
        List<SensitiveWord> result = sensitiveWordService.getWordsByLevel(minLevel);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sensitiveWordMapper, times(1)).getWordsByLevel(minLevel);
    }

    @Test
    @DisplayName("测试添加敏感词 - 正常情况")
    void testAddSensitiveWord_Success() {
        // Arrange
        when(sensitiveWordMapper.insert(any(SensitiveWord.class))).thenReturn(1);
        when(applicationContext.getBean(SensitiveWordFilter.class)).thenReturn(sensitiveWordFilter);
        doNothing().when(sensitiveWordFilter).reloadFromDatabase();

        // Act
        boolean result = sensitiveWordService.addSensitiveWord(testSensitiveWord);

        // Assert
        assertTrue(result);
        assertEquals(1, testSensitiveWord.getSensitiveStatus()); // 应该默认启用
        assertNotNull(testSensitiveWord.getCreateTime());
        verify(sensitiveWordMapper, times(1)).insert(any(SensitiveWord.class));
        verify(sensitiveWordFilter, times(1)).reloadFromDatabase();
    }

    @Test
    @DisplayName("测试更新敏感词 - 正常情况")
    void testUpdateSensitiveWord_Success() {
        // Arrange
        SensitiveWord updateWord = new SensitiveWord();
        updateWord.setSensitiveId(1);
        updateWord.setSensitiveWord("更新后的敏感词");
        updateWord.setSensitiveWordType("违法内容");
        updateWord.setSensitiveLevel(2);

        when(sensitiveWordMapper.selectById(1)).thenReturn(testSensitiveWord);
        when(sensitiveWordMapper.updateById(any(SensitiveWord.class))).thenReturn(1);
        when(applicationContext.getBean(SensitiveWordFilter.class)).thenReturn(sensitiveWordFilter);
        doNothing().when(sensitiveWordFilter).reloadFromDatabase();

        // Act
        boolean result = sensitiveWordService.updateSensitiveWord(updateWord);

        // Assert
        assertTrue(result);
        verify(sensitiveWordMapper, times(1)).updateById(any(SensitiveWord.class));
        verify(sensitiveWordFilter, times(1)).reloadFromDatabase();
    }

    @Test
    @DisplayName("测试更新敏感词 - 不存在")
    void testUpdateSensitiveWord_NotFound() {
        // Arrange
        SensitiveWord updateWord = new SensitiveWord();
        updateWord.setSensitiveId(999);

        when(sensitiveWordMapper.selectById(999)).thenReturn(null);

        // Act
        boolean result = sensitiveWordService.updateSensitiveWord(updateWord);

        // Assert
        assertFalse(result);
        verify(sensitiveWordMapper, never()).updateById(any(SensitiveWord.class));
    }

    @Test
    @DisplayName("测试删除敏感词 - 正常情况")
    void testDeleteSensitiveWord_Success() {
        // Arrange
        int id = 1;
        when(sensitiveWordMapper.deleteById(id)).thenReturn(1);
        when(applicationContext.getBean(SensitiveWordFilter.class)).thenReturn(sensitiveWordFilter);
        doNothing().when(sensitiveWordFilter).reloadFromDatabase();

        // Act
        boolean result = sensitiveWordService.deleteSensitiveWord(id);

        // Assert
        assertTrue(result);
        verify(sensitiveWordMapper, times(1)).deleteById(id);
        verify(sensitiveWordFilter, times(1)).reloadFromDatabase();
    }

    @Test
    @DisplayName("测试切换敏感词状态 - 正常情况")
    void testToggleSensitiveWordStatus_Success() {
        // Arrange
        int id = 1;
        int newStatus = 0; // 禁用

        when(sensitiveWordMapper.selectById(id)).thenReturn(testSensitiveWord);
        when(sensitiveWordMapper.update(any(), any(UpdateWrapper.class))).thenReturn(1);
        when(applicationContext.getBean(SensitiveWordFilter.class)).thenReturn(sensitiveWordFilter);
        doNothing().when(sensitiveWordFilter).reloadFromDatabase();

        // Act
        boolean result = sensitiveWordService.toggleSensitiveWordStatus(id, newStatus);

        // Assert
        assertTrue(result);
        verify(sensitiveWordMapper, times(1)).update(any(), any(UpdateWrapper.class));
        verify(sensitiveWordFilter, times(1)).reloadFromDatabase();
    }

    @Test
    @DisplayName("测试切换敏感词状态 - 敏感词不存在")
    void testToggleSensitiveWordStatus_NotFound() {
        // Arrange
        int id = 999;
        int newStatus = 0;

        when(sensitiveWordMapper.selectById(id)).thenReturn(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            sensitiveWordService.toggleSensitiveWordStatus(id, newStatus);
        });
        verify(sensitiveWordMapper, never()).update(any(), any(UpdateWrapper.class));
    }

    @Test
    @DisplayName("测试切换敏感词状态 - 状态相同")
    void testToggleSensitiveWordStatus_SameStatus() {
        // Arrange
        int id = 1;
        int status = 1; // 与当前状态相同

        when(sensitiveWordMapper.selectById(id)).thenReturn(testSensitiveWord);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            sensitiveWordService.toggleSensitiveWordStatus(id, status);
        });
        verify(sensitiveWordMapper, never()).update(any(), any(UpdateWrapper.class));
    }

    @Test
    @DisplayName("测试重新加载敏感词库")
    void testReloadSensitiveWords() {
        // Arrange
        when(applicationContext.getBean(SensitiveWordFilter.class)).thenReturn(sensitiveWordFilter);
        doNothing().when(sensitiveWordFilter).reloadFromDatabase();

        // Act
        sensitiveWordService.reloadSensitiveWords();

        // Assert
        verify(sensitiveWordFilter, times(1)).reloadFromDatabase();
    }
}

