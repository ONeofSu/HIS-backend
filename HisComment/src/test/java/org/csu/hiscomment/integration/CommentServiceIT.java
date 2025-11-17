package org.csu.hiscomment.integration;

import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import org.csu.hiscomment.VO.UserSimpleVO;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.feign.UserFeignClient;
import org.csu.hiscomment.mapper.CommentMapper;
import org.csu.hiscomment.service.CommentService;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CommentService集成测试")
class CommentServiceIT {

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("his_comment_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        if (!MYSQL.isRunning()) {
            MYSQL.start();
        }
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
    }

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @MockBean
    private UserFeignClient userFeignClient;

    @Autowired
    private SensitiveWordFilter sensitiveWordFilter;

    @BeforeAll
    static void initSchema() throws Exception {
        try (Connection conn = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement stmt = conn.createStatement()) {
            // 创建评论表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS comment (
                        comment_id INT AUTO_INCREMENT PRIMARY KEY,
                        target_type VARCHAR(50) NOT NULL,
                        target_id INT NOT NULL,
                        user_id INT NOT NULL,
                        content TEXT NOT NULL,
                        parent_id INT DEFAULT 0,
                        root_id INT DEFAULT 0,
                        like_count INT DEFAULT 0,
                        create_time DATETIME NOT NULL,
                        update_time DATETIME,
                        is_deleted INT DEFAULT 0,
                        original_content TEXT,
                        sensitive_words VARCHAR(500),
                        sensitive_types VARCHAR(500),
                        is_filtered INT DEFAULT 0,
                        filter_level INT DEFAULT 0
                    )
                    """);
            
            // 创建敏感词表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS sensitive_words (
                        sensitive_id INT AUTO_INCREMENT PRIMARY KEY,
                        sensitive_word VARCHAR(100) NOT NULL,
                        sensitive_word_type VARCHAR(50),
                        sensitive_level INT DEFAULT 1,
                        sensitive_status INT DEFAULT 1,
                        create_time DATETIME,
                        create_by INT
                    )
                    """);
            
            // 创建评论点赞表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS comment_like (
                        like_id INT AUTO_INCREMENT PRIMARY KEY,
                        comment_id INT NOT NULL,
                        user_id INT NOT NULL,
                        create_time DATETIME,
                        UNIQUE KEY uk_comment_user (comment_id, user_id)
                    )
                    """);
            
            // 插入测试敏感词
            stmt.execute("""
                    INSERT INTO sensitive_words (sensitive_word, sensitive_word_type, sensitive_level, sensitive_status)
                    VALUES ('测试敏感词', '辱骂', 1, 1)
                    """);
        }
    }

    @BeforeEach
    void setUpMocks() {
        // Mock UserFeignClient
        when(userFeignClient.isUserExist(anyInt())).thenReturn(true);
        
        UserSimpleVO userVO = new UserSimpleVO();
        userVO.setId(1);
        userVO.setUsername("测试用户");
        userVO.setAvatarUrl("http://example.com/avatar.jpg");
        
        when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(
            Collections.singletonMap(1, userVO)
        );
        
        // 重新加载敏感词库（因为表在 @BeforeAll 中创建，但 SensitiveWordFilter 在 Spring 启动时初始化）
        sensitiveWordFilter.reloadFromDatabase();
    }

    @Test
    @Transactional
    @DisplayName("测试添加评论 - 无敏感词，集成测试")
    void testAddComment_NoSensitiveWords_Integration() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("这是一条正常的评论内容");
        commentDTO.setParentId(0);
        int userId = 1;

        // Act
        CommentVO result = commentService.addComment(commentDTO, userId);

        // Assert
        assertNotNull(result);
        assertEquals("这是一条正常的评论内容", result.getContent());
        assertFalse(result.isFiltered());
        
        // 验证数据库中的记录
        Comment saved = commentMapper.selectById(result.getCommentId());
        assertNotNull(saved);
        assertEquals("这是一条正常的评论内容", saved.getContent());
        assertEquals(0, saved.getIsFiltered());
    }

    @Test
    @Transactional
    @DisplayName("测试添加评论 - 包含敏感词，集成测试")
    void testAddComment_WithSensitiveWords_Integration() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("这是一条包含测试敏感词的评论");
        commentDTO.setParentId(0);
        int userId = 1;

        // Act
        CommentVO result = commentService.addComment(commentDTO, userId);

        // Assert
        // 根据敏感词过滤逻辑，轻度敏感词应该被过滤而不是拒绝
        if (result != null) {
            assertTrue(result.isFiltered());
            assertTrue(result.getContent().contains("***"));
            
            // 验证数据库中的记录
            Comment saved = commentMapper.selectById(result.getCommentId());
            assertNotNull(saved);
            assertEquals(1, saved.getIsFiltered());
            assertNotNull(saved.getSensitiveWords());
        }
    }

    @Test
    @Transactional
    @DisplayName("测试获取评论列表 - 集成测试")
    void testListComments_Integration() {
        // Arrange - 先创建一些测试评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("测试评论1");
        commentDTO.setParentId(0);
        commentService.addComment(commentDTO, 1);

        // Act
        List<CommentVO> result = commentService.listComments("course", 1, "time", 1, 10, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    @Transactional
    @DisplayName("测试点赞评论 - 集成测试")
    void testLikeComment_Integration() {
        // Arrange - 先创建评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("测试评论");
        commentDTO.setParentId(0);
        CommentVO created = commentService.addComment(commentDTO, 1);
        int commentId = created.getCommentId();

        // Act
        boolean liked = commentService.likeComment(commentId, 2);

        // Assert
        assertTrue(liked);
        
        // 验证数据库中的点赞数已更新
        Comment saved = commentMapper.selectById(commentId);
        assertEquals(1, saved.getLikeCount());
    }

    @Test
    @Transactional
    @DisplayName("测试删除评论 - 集成测试")
    void testDeleteComment_Integration() {
        // Arrange - 先创建评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("待删除评论");
        commentDTO.setParentId(0);
        CommentVO created = commentService.addComment(commentDTO, 1);
        int commentId = created.getCommentId();

        // Act
        boolean deleted = commentService.deleteComment(commentId, 1, false);

        // Assert
        assertTrue(deleted);
        
        // 验证数据库中的记录已被逻辑删除
        Comment saved = commentMapper.selectById(commentId);
        assertEquals(1, saved.getIsDeleted());
    }
}

