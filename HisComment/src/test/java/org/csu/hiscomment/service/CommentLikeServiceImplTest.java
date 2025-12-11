package org.csu.hiscomment.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.hiscomment.entity.CommentLike;
import org.csu.hiscomment.mapper.CommentLikeMapper;
import org.csu.hiscomment.service.impl.CommentLikeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentLikeServiceImpl单元测试")
class CommentLikeServiceImplTest {

    @Mock
    private CommentLikeMapper commentLikeMapper;

    @InjectMocks
    private CommentLikeServiceImpl commentLikeService;

    private CommentLike testCommentLike;

    @BeforeEach
    void setUp() {
        testCommentLike = new CommentLike();
        testCommentLike.setLikeId(1);
        testCommentLike.setCommentId(1);
        testCommentLike.setUserId(100);
        testCommentLike.setCreateTime(new Date());
    }

    @Test
    @DisplayName("测试点赞 - 正常情况")
    void testLike_Success() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(commentLikeMapper.insert(any(CommentLike.class))).thenReturn(1);

        // Act
        boolean result = commentLikeService.like(commentId, userId);

        // Assert
        assertTrue(result);
        verify(commentLikeMapper, times(1)).insert(any(CommentLike.class));
    }

    @Test
    @DisplayName("测试点赞 - 已点赞")
    void testLike_AlreadyLiked() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // Act
        boolean result = commentLikeService.like(commentId, userId);

        // Assert
        assertFalse(result);
        verify(commentLikeMapper, never()).insert(any(CommentLike.class));
    }

    @Test
    @DisplayName("测试取消点赞 - 正常情况")
    void testUnlike_Success() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeMapper.delete(any(QueryWrapper.class))).thenReturn(1);

        // Act
        boolean result = commentLikeService.unlike(commentId, userId);

        // Assert
        assertTrue(result);
        verify(commentLikeMapper, times(1)).delete(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试取消点赞 - 未点赞")
    void testUnlike_NotLiked() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeMapper.delete(any(QueryWrapper.class))).thenReturn(0);

        // Act
        boolean result = commentLikeService.unlike(commentId, userId);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("测试检查是否已点赞 - 已点赞")
    void testIsLiked_True() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // Act
        boolean result = commentLikeService.isLiked(commentId, userId);

        // Assert
        assertTrue(result);
        verify(commentLikeMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试检查是否已点赞 - 未点赞")
    void testIsLiked_False() {
        // Arrange
        int commentId = 1;
        int userId = 100;

        when(commentLikeMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        // Act
        boolean result = commentLikeService.isLiked(commentId, userId);

        // Assert
        assertFalse(result);
    }
}

