package org.csu.hiscomment.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import org.csu.hiscomment.VO.UserSimpleVO;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.feign.UserFeignClient;
import org.csu.hiscomment.mapper.CommentLikeMapper;
import org.csu.hiscomment.mapper.CommentMapper;
import org.csu.hiscomment.service.CommentLikeService;
import org.csu.hiscomment.service.impl.CommentServiceImpl;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentServiceImpl单元测试")
class CommentServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentLikeService commentLikeService;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private CommentLikeMapper commentLikeMapper;

    @Mock
    private SensitiveWordFilter sensitiveWordFilter;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private CommentDTO testCommentDTO;
    private UserSimpleVO testUserVO;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setCommentId(1);
        testComment.setTargetType("course");
        testComment.setTargetId(1);
        testComment.setUserId(100);
        testComment.setContent("这是一条测试评论");
        testComment.setParentId(0);
        testComment.setRootId(1);
        testComment.setLikeCount(0);
        testComment.setCreateTime(new Date());
        testComment.setUpdateTime(new Date());
        testComment.setIsDeleted(0);
        testComment.setIsFiltered(0);
        testComment.setFilterLevel(0);

        testCommentDTO = new CommentDTO();
        testCommentDTO.setTargetType("course");
        testCommentDTO.setTargetId(1);
        testCommentDTO.setContent("这是一条测试评论");
        testCommentDTO.setParentId(0);

        testUserVO = new UserSimpleVO();
        testUserVO.setId(100);
        testUserVO.setUsername("测试用户");
        testUserVO.setAvatarUrl("http://example.com/avatar.jpg");
    }

    @Test
    @DisplayName("测试添加评论 - 无敏感词，正常情况")
    void testAddComment_NoSensitiveWords_Success() {
        // Arrange
        int userId = 100;
        SensitiveWordFilter.SensitiveCheckResult noSensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(false, null, null);

        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(sensitiveWordFilter.checkSensitiveWords(anyString())).thenReturn(noSensitiveResult);
        when(commentMapper.insert(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setCommentId(1);
            return 1;
        });
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);
        when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(
            Collections.singletonMap(userId, testUserVO)
        );

        // Act
        CommentVO result = commentService.addComment(testCommentDTO, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCommentId());
        assertEquals("这是一条测试评论", result.getContent());
        assertFalse(result.isFiltered());
        assertEquals(0, result.getFilterLevel());
        verify(sensitiveWordFilter, times(1)).checkSensitiveWords(anyString());
        verify(commentMapper, times(1)).insert(any(Comment.class));
    }

    @Test
    @DisplayName("测试添加评论 - 包含轻度敏感词，应过滤")
    void testAddComment_WithMildSensitiveWords_Filtered() {
        // Arrange
        int userId = 100;
        String contentWithSensitive = "这是一条包含敏感词的评论";
        List<String> sensitiveWords = Arrays.asList("敏感词");
        List<SensitiveWordFilter.SensitiveType> sensitiveTypes = 
            Arrays.asList(SensitiveWordFilter.SensitiveType.ABUSE);
        
        SensitiveWordFilter.SensitiveCheckResult sensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(true, sensitiveWords, sensitiveTypes);

        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(sensitiveWordFilter.checkSensitiveWords(contentWithSensitive)).thenReturn(sensitiveResult);
        when(sensitiveWordFilter.getSensitiveWordLevel("敏感词")).thenReturn(1);
        when(sensitiveWordFilter.filterSensitiveWords(contentWithSensitive)).thenReturn("这是一条包含***的评论");
        when(commentMapper.insert(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setCommentId(1);
            return 1;
        });
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);
        when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(
            Collections.singletonMap(userId, testUserVO)
        );

        testCommentDTO.setContent(contentWithSensitive);

        // Act
        CommentVO result = commentService.addComment(testCommentDTO, userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isFiltered());
        assertEquals(1, result.getFilterLevel()); // 轻度过滤
        verify(sensitiveWordFilter, times(1)).filterSensitiveWords(contentWithSensitive);
        verify(commentMapper, times(1)).insert(any(Comment.class));
    }

    @Test
    @DisplayName("测试添加评论 - 包含重度敏感词，应拒绝发布")
    void testAddComment_WithHighLevelSensitiveWords_Rejected() {
        // Arrange
        int userId = 100;
        String contentWithHighSensitive = "这是一条包含重度敏感词的评论";
        List<String> sensitiveWords = Arrays.asList("重度敏感词");
        List<SensitiveWordFilter.SensitiveType> sensitiveTypes = 
            Arrays.asList(SensitiveWordFilter.SensitiveType.ABUSE);
        
        SensitiveWordFilter.SensitiveCheckResult sensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(true, sensitiveWords, sensitiveTypes);

        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(sensitiveWordFilter.checkSensitiveWords(contentWithHighSensitive)).thenReturn(sensitiveResult);
        when(sensitiveWordFilter.getSensitiveWordLevel("重度敏感词")).thenReturn(3); // 重度

        testCommentDTO.setContent(contentWithHighSensitive);

        // Act
        CommentVO result = commentService.addComment(testCommentDTO, userId);

        // Assert
        assertNull(result); // 应该被拒绝
        verify(sensitiveWordFilter, never()).filterSensitiveWords(anyString());
        verify(commentMapper, never()).insert(any(Comment.class));
    }

    @Test
    @DisplayName("测试添加评论 - 包含政治敏感词，应拒绝发布")
    void testAddComment_WithPoliticalSensitiveWords_Rejected() {
        // Arrange
        int userId = 100;
        String contentWithPolitical = "这是一条包含政治敏感词的评论";
        List<String> sensitiveWords = Arrays.asList("政治敏感词");
        List<SensitiveWordFilter.SensitiveType> sensitiveTypes = 
            Arrays.asList(SensitiveWordFilter.SensitiveType.POLITICAL);
        
        SensitiveWordFilter.SensitiveCheckResult sensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(true, sensitiveWords, sensitiveTypes);

        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(sensitiveWordFilter.checkSensitiveWords(contentWithPolitical)).thenReturn(sensitiveResult);
        when(sensitiveWordFilter.getSensitiveWordLevel("政治敏感词")).thenReturn(1); // 即使级别低，但类型严重

        testCommentDTO.setContent(contentWithPolitical);

        // Act
        CommentVO result = commentService.addComment(testCommentDTO, userId);

        // Assert
        assertNull(result); // 应该被拒绝
        verify(commentMapper, never()).insert(any(Comment.class));
    }

    @Test
    @DisplayName("测试添加评论 - 用户不存在")
    void testAddComment_UserNotExist() {
        // Arrange
        int userId = 999;
        when(userFeignClient.isUserExist(userId)).thenReturn(false);

        // Act
        CommentVO result = commentService.addComment(testCommentDTO, userId);

        // Assert
        assertNull(result);
        verify(commentMapper, never()).insert(any(Comment.class));
    }

    @Test
    @DisplayName("测试添加评论 - 回复评论")
    void testAddComment_ReplyComment() {
        // Arrange
        int userId = 100;
        int parentId = 1;
        Comment parentComment = new Comment();
        parentComment.setCommentId(parentId);
        parentComment.setRootId(1);

        testCommentDTO.setParentId(parentId);
        SensitiveWordFilter.SensitiveCheckResult noSensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(false, null, null);

        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(sensitiveWordFilter.checkSensitiveWords(anyString())).thenReturn(noSensitiveResult);
        when(commentMapper.selectById(parentId)).thenReturn(parentComment);
        when(commentMapper.insert(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setCommentId(2);
            return 1;
        });
        when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(
            Collections.singletonMap(userId, testUserVO)
        );

        // Act
        CommentVO result = commentService.addComment(testCommentDTO, userId);

        // Assert
        assertNotNull(result);
        assertEquals(parentId, result.getParentId());
        assertEquals(1, result.getRootId()); // rootId应该与父评论相同
        verify(commentMapper, times(1)).selectById(parentId);
        verify(commentMapper, times(1)).insert(any(Comment.class));
        verify(commentMapper, never()).updateById(any(Comment.class)); // 回复评论不需要更新rootId
    }

    @Test
    @DisplayName("测试点赞评论 - 正常情况")
    void testLikeComment_Success() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeService.isLiked(commentId, userId)).thenReturn(false);
        when(commentLikeService.like(commentId, userId)).thenReturn(true);
        when(commentMapper.selectById(commentId)).thenReturn(testComment);
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // Act
        boolean result = commentService.likeComment(commentId, userId);

        // Assert
        assertTrue(result);
        verify(commentLikeService, times(1)).like(commentId, userId);
        verify(commentMapper, times(1)).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试点赞评论 - 已点赞")
    void testLikeComment_AlreadyLiked() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeService.isLiked(commentId, userId)).thenReturn(true);

        // Act
        boolean result = commentService.likeComment(commentId, userId);

        // Assert
        assertFalse(result);
        verify(commentLikeService, never()).like(anyInt(), anyInt());
    }

    @Test
    @DisplayName("测试取消点赞 - 正常情况")
    void testUnlikeComment_Success() {
        // Arrange
        int commentId = 1;
        int userId = 100;
        testComment.setLikeCount(5);

        when(commentLikeService.isLiked(commentId, userId)).thenReturn(true);
        when(commentLikeService.unlike(commentId, userId)).thenReturn(true);
        when(commentMapper.selectById(commentId)).thenReturn(testComment);
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // Act
        boolean result = commentService.unlikeComment(commentId, userId);

        // Assert
        assertTrue(result);
        verify(commentLikeService, times(1)).unlike(commentId, userId);
        verify(commentMapper, times(1)).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试删除评论 - 正常情况")
    void testDeleteComment_Success() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentMapper.selectById(commentId)).thenReturn(testComment);
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // Act
        boolean result = commentService.deleteComment(commentId, userId, false);

        // Assert
        assertTrue(result);
        verify(commentMapper, times(1)).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试删除评论 - 管理员删除")
    void testDeleteComment_AdminDelete() {
        // Arrange
        int commentId = 1;
        int adminId = 999; // 管理员ID与评论用户ID不同

        when(commentMapper.selectById(commentId)).thenReturn(testComment);
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // Act
        boolean result = commentService.deleteComment(commentId, adminId, true);

        // Assert
        assertTrue(result); // 管理员可以删除任何评论
        verify(commentMapper, times(1)).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试删除评论 - 非本人且非管理员")
    void testDeleteComment_Unauthorized() {
        // Arrange
        int commentId = 1;
        int otherUserId = 999;

        when(commentMapper.selectById(commentId)).thenReturn(testComment);

        // Act
        boolean result = commentService.deleteComment(commentId, otherUserId, false);

        // Assert
        assertFalse(result);
        verify(commentMapper, never()).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试过滤评论 - 包含轻度敏感词")
    void testFilterComment_WithMildSensitiveWords() {
        // Arrange
        int commentId = 1;
        testComment.setIsFiltered(0);
        testComment.setContent("这是一条包含敏感词的评论");
        
        List<String> sensitiveWords = Arrays.asList("敏感词");
        List<SensitiveWordFilter.SensitiveType> sensitiveTypes = 
            Arrays.asList(SensitiveWordFilter.SensitiveType.ABUSE);
        
        SensitiveWordFilter.SensitiveCheckResult sensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(true, sensitiveWords, sensitiveTypes);

        when(commentMapper.selectById(commentId)).thenReturn(testComment);
        when(sensitiveWordFilter.checkSensitiveWords(anyString())).thenReturn(sensitiveResult);
        when(sensitiveWordFilter.getSensitiveWordLevel("敏感词")).thenReturn(1);
        when(sensitiveWordFilter.filterSensitiveWords(anyString())).thenReturn("这是一条包含***的评论");
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // Act
        boolean result = commentService.filterComment(commentId);

        // Assert
        assertTrue(result);
        verify(sensitiveWordFilter, times(1)).filterSensitiveWords(anyString());
        verify(commentMapper, times(1)).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试过滤评论 - 无敏感词")
    void testFilterComment_NoSensitiveWords() {
        // Arrange
        int commentId = 1;
        testComment.setIsFiltered(0);
        
        SensitiveWordFilter.SensitiveCheckResult noSensitiveResult = 
            new SensitiveWordFilter.SensitiveCheckResult(false, null, null);

        when(commentMapper.selectById(commentId)).thenReturn(testComment);
        when(sensitiveWordFilter.checkSensitiveWords(anyString())).thenReturn(noSensitiveResult);
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // Act
        boolean result = commentService.filterComment(commentId);

        // Assert
        assertTrue(result);
        verify(sensitiveWordFilter, never()).filterSensitiveWords(anyString());
        verify(commentMapper, times(1)).updateById(any(Comment.class));
    }

    @Test
    @DisplayName("测试获取需要过滤的评论数量")
    void testGetCommentsNeedFilterCount() {
        // Arrange
        when(commentMapper.selectCount(any(QueryWrapper.class))).thenReturn(5L);

        // Act
        int result = commentService.getCommentsNeedFilterCount();

        // Assert
        assertEquals(5, result);
        verify(commentMapper, times(1)).selectCount(any(QueryWrapper.class));
    }
}

