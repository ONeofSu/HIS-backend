package org.csu.histraining.service;

import org.csu.histraining.DTO.FeedbackDTO;
import org.csu.histraining.VO.FeedbackVO;
import org.csu.histraining.entity.Feedback;
import org.csu.histraining.mapper.FeedbackMapper;
import org.csu.histraining.service.MaterialService;
import org.csu.histraining.service.UserService;
import org.csu.histraining.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackMapper feedbackMapper;

    @Mock
    private MaterialService materialService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback validFeedback;
    private FeedbackDTO validFeedbackDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        validFeedback = new Feedback();
        validFeedback.setId(1);
        validFeedback.setMaterialId(100);
        validFeedback.setUserId(200);
        validFeedback.setContent("Great material!");
        validFeedback.setRating(5);
        validFeedback.setTime(new Timestamp(System.currentTimeMillis()));

        validFeedbackDTO = new FeedbackDTO();
        validFeedbackDTO.setMaterialId(100);
        validFeedbackDTO.setContent("Great material!");
        validFeedbackDTO.setRating(5);
    }

    // ============ addFeedback 测试 ============

    @Test
    void addFeedback_WithValidInput_ShouldReturnFeedbackId() {
        // Arrange
        when(materialService.isMaterialIdExist(100)).thenReturn(true);
        when(userService.isUserIdExist(200)).thenReturn(true);
        doAnswer(invocation -> {
            Feedback feedback = invocation.getArgument(0);
            feedback.setId(1); // 模拟数据库生成ID
            return null;
        }).when(feedbackMapper).insert(any(Feedback.class));

        // Act
        int result = feedbackService.addFeedback(validFeedback);

        // Assert
        assertEquals(1, result);
        verify(feedbackMapper).insert(validFeedback);
    }

    @Test
    void addFeedback_WithInvalidMaterialId_ShouldReturnMinusOne() {
        // Arrange
        when(materialService.isMaterialIdExist(100)).thenReturn(false);

        // Act
        int result = feedbackService.addFeedback(validFeedback);

        // Assert
        assertEquals(-1, result);
        verify(feedbackMapper, never()).insert(any(Feedback.class));
    }

    @Test
    void addFeedback_WithInvalidUserId_ShouldReturnMinusOne() {
        // Arrange
        when(materialService.isMaterialIdExist(100)).thenReturn(true);
        when(userService.isUserIdExist(200)).thenReturn(false);

        // Act
        int result = feedbackService.addFeedback(validFeedback);

        // Assert
        assertEquals(-1, result);
        verify(feedbackMapper, never()).insert(any(Feedback.class));
    }

    @Test
    void addFeedback_WithNullContent_ShouldReturnMinusOne() {
        // Arrange
        validFeedback.setContent(null);
        when(materialService.isMaterialIdExist(100)).thenReturn(true);
        when(userService.isUserIdExist(200)).thenReturn(true);

        // Act
        int result = feedbackService.addFeedback(validFeedback);

        // Assert
        assertEquals(-1, result);
        verify(feedbackMapper, never()).insert(any(Feedback.class));
    }

    @Test
    void addFeedback_WithEmptyContent_ShouldReturnMinusOne() {
        // Arrange
        validFeedback.setContent("");
        when(materialService.isMaterialIdExist(100)).thenReturn(true);
        when(userService.isUserIdExist(200)).thenReturn(true);

        // Act
        int result = feedbackService.addFeedback(validFeedback);

        // Assert
        assertEquals(-1, result);
        verify(feedbackMapper, never()).insert(any(Feedback.class));
    }

    // ============ getFeedbackById 测试 ============

    @Test
    void getFeedbackById_WithExistingId_ShouldReturnFeedback() {
        // Arrange
        when(feedbackMapper.selectById(1)).thenReturn(validFeedback);

        // Act
        Feedback result = feedbackService.getFeedbackById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Great material!", result.getContent());
        verify(feedbackMapper).selectById(1);
    }

    @Test
    void getFeedbackById_WithNonExistingId_ShouldReturnNull() {
        // Arrange
        when(feedbackMapper.selectById(999)).thenReturn(null);

        // Act
        Feedback result = feedbackService.getFeedbackById(999);

        // Assert
        assertNull(result);
        verify(feedbackMapper).selectById(999);
    }

    // ============ getAllFeedback 测试 ============

    @Test
    void getAllFeedback_ShouldReturnAllFeedbacks() {
        // Arrange
        Feedback feedback2 = new Feedback();
        feedback2.setId(2);
        List<Feedback> expectedList = Arrays.asList(validFeedback, feedback2);
        when(feedbackMapper.selectList(null)).thenReturn(expectedList);

        // Act
        List<Feedback> result = feedbackService.getAllFeedback();

        // Assert
        assertEquals(2, result.size());
        verify(feedbackMapper).selectList(null);
    }

    // ============ getFeedbackByUserId 测试 ============

    @Test
    void getFeedbackByUserId_ShouldReturnUserFeedbacks() {
        // Arrange
        List<Feedback> expectedList = Arrays.asList(validFeedback);
        when(feedbackMapper.selectList(any())).thenReturn(expectedList);

        // Act
        List<Feedback> result = feedbackService.getFeedbackByUserId(200);

        // Assert
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getUserId());
        verify(feedbackMapper).selectList(any());
    }

    // ============ isFeedbackIdExist 测试 ============

    @Test
    void isFeedbackIdExist_WithExistingId_ShouldReturnTrue() {
        // Arrange
        when(feedbackMapper.selectById(1)).thenReturn(validFeedback);

        // Act
        boolean result = feedbackService.isFeedbackIdExist(1);

        // Assert
        assertTrue(result);
        verify(feedbackMapper).selectById(1);
    }

    @Test
    void isFeedbackIdExist_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(feedbackMapper.selectById(999)).thenReturn(null);

        // Act
        boolean result = feedbackService.isFeedbackIdExist(999);

        // Assert
        assertFalse(result);
        verify(feedbackMapper).selectById(999);
    }

    // ============ deleteFeedback 测试 ============

    @Test
    void deleteFeedback_WithExistingId_ShouldReturnTrue() {
        // Arrange
        when(feedbackMapper.selectById(1)).thenReturn(validFeedback);
        doNothing().when(feedbackMapper).deleteById(1);

        // Act
        boolean result = feedbackService.deleteFeedback(1);

        // Assert
        assertTrue(result);
        verify(feedbackMapper).selectById(1);
        verify(feedbackMapper).deleteById(1);
    }

    @Test
    void deleteFeedback_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(feedbackMapper.selectById(999)).thenReturn(null);

        // Act
        boolean result = feedbackService.deleteFeedback(999);

        // Assert
        assertFalse(result);
        verify(feedbackMapper).selectById(999);
        verify(feedbackMapper, never()).deleteById(anyInt());
    }

    // ============ transferFeedbackDTOToFeedback 测试 ============

    @Test
    void transferFeedbackDTOToFeedback_ShouldCorrectlyTransferFields() {
        // Act
        Feedback result = feedbackService.transferFeedbackDTOToFeedback(validFeedbackDTO, 200);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getMaterialId());
        assertEquals(200, result.getUserId());
        assertEquals("Great material!", result.getContent());
        assertEquals(5, result.getRating());
        assertNotNull(result.getTime());
    }

    @Test
    void transferFeedbackDTOToFeedback_TimestampShouldBeRecent() {
        // Act
        Feedback result = feedbackService.transferFeedbackDTOToFeedback(validFeedbackDTO, 200);

        // Assert
        long currentTime = System.currentTimeMillis();
        long feedbackTime = result.getTime().getTime();
        assertTrue(currentTime - feedbackTime < 1000); // 时间差应该小于1秒
    }

    // ============ transferToFeedbackVO 测试 ============

    @Test
    void transferToFeedbackVO_ShouldCorrectlyTransferAllFields() {
        // Arrange
        when(userService.getUsernameById(200)).thenReturn("TestUser");

        // Act
        FeedbackVO result = feedbackService.transferToFeedbackVO(validFeedback);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(100, result.getMaterialId());
        assertEquals(200, result.getUserId());
        assertEquals("Great material!", result.getContent());
        assertEquals(5, result.getRating());
        assertEquals("TestUser", result.getUserName());
        assertNotNull(result.getTime());
        verify(userService).getUsernameById(200);
    }

    // ============ transferToFeedbackVOList 测试 ============

    @Test
    void transferToFeedbackVOList_WithMultipleFeedbacks_ShouldReturnCorrectList() {
        // Arrange
        Feedback feedback2 = new Feedback();
        feedback2.setId(2);
        feedback2.setUserId(201);
        feedback2.setMaterialId(101);
        feedback2.setContent("Another feedback");
        feedback2.setRating(4);

        List<Feedback> feedbackList = Arrays.asList(validFeedback, feedback2);

        when(userService.getUsernameById(200)).thenReturn("User1");
        when(userService.getUsernameById(201)).thenReturn("User2");

        // Act
        List<FeedbackVO> result = feedbackService.transferToFeedbackVOList(feedbackList);

        // Assert
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getUserName());
        assertEquals("User2", result.get(1).getUserName());
        verify(userService, times(2)).getUsernameById(anyInt());
    }

    @Test
    void transferToFeedbackVOList_WithEmptyList_ShouldReturnEmptyList() {
        // Arrange
        List<Feedback> emptyList = Arrays.asList();

        // Act
        List<FeedbackVO> result = feedbackService.transferToFeedbackVOList(emptyList);

        // Assert
        assertTrue(result.isEmpty());
        verify(userService, never()).getUsernameById(anyInt());
    }
}

