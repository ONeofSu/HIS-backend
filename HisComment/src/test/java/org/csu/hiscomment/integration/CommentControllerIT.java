package org.csu.hiscomment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.feign.CourseFeignClient;
import org.csu.hiscomment.feign.HerbFeignClient;
import org.csu.hiscomment.feign.UserFeignClient;
import org.csu.hiscomment.service.CommentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CommentController集成测试 - Controller->Service->Dao->Database")
class CommentControllerIT {

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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserFeignClient userFeignClient;

    @MockBean
    private CourseFeignClient courseFeignClient;

    @MockBean
    private HerbFeignClient herbFeignClient;

    @Autowired
    private CommentService commentService;

    @Autowired
    private org.csu.hiscomment.utils.SensitiveWordFilter sensitiveWordFilter;

    @BeforeAll
    static void initSchema() throws Exception {
        if (!MYSQL.isRunning()) {
            MYSQL.start();
        }
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

    @org.junit.jupiter.api.BeforeEach
    void setUpMocks() {
        // Mock UserFeignClient 的返回值
        org.csu.hiscomment.VO.UserSimpleVO userVO = new org.csu.hiscomment.VO.UserSimpleVO();
        userVO.setId(1);
        userVO.setUsername("测试用户");
        userVO.setAvatarUrl("http://example.com/avatar.jpg");
        
        when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(
            java.util.Collections.singletonMap(1, userVO)
        );
        
        // 重新加载敏感词库（因为表在 @BeforeAll 中创建，但 SensitiveWordFilter 在 Spring 启动时初始化）
        sensitiveWordFilter.reloadFromDatabase();
    }

    @Test
    @DisplayName("测试发布评论 - 无敏感词，Controller集成测试")
    void testAddComment_NoSensitiveWords_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(1);
        when(userFeignClient.isUserExist(1)).thenReturn(true);
        when(courseFeignClient.isCourseExist(1)).thenReturn(true);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("这是一条正常的评论内容");
        commentDTO.setParentId(0);

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").value("这是一条正常的评论内容"));
    }

    @Test
    @DisplayName("测试发布评论 - 包含敏感词，Controller集成测试")
    void testAddComment_WithSensitiveWords_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(1);
        when(userFeignClient.isUserExist(1)).thenReturn(true);
        when(courseFeignClient.isCourseExist(1)).thenReturn(true);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("这是一条包含测试敏感词的评论");
        commentDTO.setParentId(0);

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                // 根据敏感词过滤逻辑，轻度敏感词会被过滤，重度会被拒绝
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("测试获取评论列表 - Controller集成测试")
    void testListComments_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(1);
        when(userFeignClient.isUserExist(1)).thenReturn(true);
        when(courseFeignClient.isCourseExist(1)).thenReturn(true);

        // 先创建一条评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("测试评论");
        commentDTO.setParentId(0);
        commentService.addComment(commentDTO, 1);

        // Act & Assert
        mockMvc.perform(get("/comments")
                        .header("Authorization", "Bearer test-token")
                        .param("targetType", "course")
                        .param("targetId", "1")
                        .param("sort", "new")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.list").exists());
    }

    @Test
    @DisplayName("测试点赞评论 - Controller集成测试")
    void testLikeComment_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(1);
        when(userFeignClient.isUserExist(1)).thenReturn(true);
        when(courseFeignClient.isCourseExist(1)).thenReturn(true);

        // 先创建一条评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("测试评论");
        commentDTO.setParentId(0);
        CommentVO created = commentService.addComment(commentDTO, 1);
        
        // 不需要 Mock getCommentDetail，因为 commentService 是真实对象

        // Act & Assert
        mockMvc.perform(post("/comments/{commentId}/like", created.getCommentId())
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    @DisplayName("测试删除评论 - Controller集成测试")
    void testDeleteComment_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(1);
        when(userFeignClient.isUserExist(1)).thenReturn(true);
        when(userFeignClient.isUserAdmin(1)).thenReturn(false);
        when(courseFeignClient.isCourseExist(1)).thenReturn(true);

        // 先创建一条评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTargetType("course");
        commentDTO.setTargetId(1);
        commentDTO.setContent("待删除评论");
        commentDTO.setParentId(0);
        CommentVO created = commentService.addComment(commentDTO, 1);

        // Act & Assert
        mockMvc.perform(delete("/comments/{commentId}", created.getCommentId())
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    @DisplayName("测试检测敏感词 - Controller集成测试")
    void testCheckSensitiveWords_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(1);
        when(userFeignClient.isUserExist(1)).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("content", "这是一条包含测试敏感词的评论");

        // Act & Assert
        mockMvc.perform(post("/comments/check-sensitive")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.hasSensitive").exists());
                // sensitiveWords 可能是 List，如果检测到敏感词则存在
    }
}

