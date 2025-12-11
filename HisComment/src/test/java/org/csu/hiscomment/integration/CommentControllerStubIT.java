package org.csu.hiscomment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.config.StubTestConfiguration;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.feign.CourseFeignClient;
import org.csu.hiscomment.feign.HerbFeignClient;
import org.csu.hiscomment.feign.UserFeignClient;
import org.csu.hiscomment.service.CommentService;
import org.csu.hiscomment.stub.CourseFeignClientStub;
import org.csu.hiscomment.stub.HerbFeignClientStub;
import org.csu.hiscomment.stub.UserFeignClientStub;
import org.csu.hiscomment.utils.SensitiveWordFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CommentController 桩集成测试
 * 使用 Stub 实现设置 Mock 行为，验证接口正确性
 * 
 * 与 CommentControllerIT 的区别：
 * - 使用 Stub 实现来设置 Mock 行为，而不是直接使用 when().thenReturn()
 * - Stub 实现提供了更真实的模拟行为和数据一致性
 * - 可以测试 Stub 与真实依赖的差异
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(StubTestConfiguration.class)
@DisplayName("CommentController桩集成测试 - 使用Stub实现设置Mock行为")
class CommentControllerStubIT {

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("his_comment_stub_test")
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
    private UserFeignClientStub userFeignClientStub;

    @Autowired
    private CourseFeignClientStub courseFeignClientStub;

    @Autowired
    private HerbFeignClientStub herbFeignClientStub;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SensitiveWordFilter sensitiveWordFilter;

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
                        filter_level INT DEFAULT 0,
                        INDEX idx_target (target_type, target_id),
                        INDEX idx_user_id (user_id)
                    )
                    """);
            // 创建评论点赞表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS comment_like (
                        like_id INT AUTO_INCREMENT PRIMARY KEY,
                        comment_id INT NOT NULL,
                        user_id INT NOT NULL,
                        create_time DATETIME,
                        UNIQUE KEY uk_comment_user (comment_id, user_id),
                        INDEX idx_comment_id (comment_id)
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
                        create_by INT,
                        UNIQUE KEY uk_word (sensitive_word)
                    )
                    """);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        // 重置 Stub 数据
        userFeignClientStub.clearAll();
        courseFeignClientStub.clearAll();
        herbFeignClientStub.clearAll();

        // 使用 Stub 实现来设置 Mock 行为
        setupMockBehavior();

        // 初始化敏感词数据
        try (Connection conn = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM sensitive_words");
            stmt.execute("INSERT INTO sensitive_words (sensitive_word, sensitive_word_type, sensitive_level, sensitive_status) VALUES " +
                    "('测试敏感词', '轻度', 1, 1), " +
                    "('禁止词', '重度', 2, 1), " +
                    "('政治敏感', '政治', 3, 1)");
        }
        // 重新加载敏感词库
        sensitiveWordFilter.reloadFromDatabase();
    }
    
    private void setupMockBehavior() {
        // 设置 UserFeignClient Mock 行为，使用 Stub 实现
        when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenAnswer(invocation -> {
            List<Integer> userIdList = invocation.getArgument(0);
            return userFeignClientStub.getUserSimpleInfoBatch(userIdList);
        });
        when(userFeignClient.isUserExist(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.isUserExist(userId);
        });
        when(userFeignClient.isUserAdmin(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.isUserAdmin(userId);
        });
        when(userFeignClient.getUserIdByToken(anyString())).thenAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return userFeignClientStub.getUserIdByToken(token);
        });
        
        // 设置 CourseFeignClient Mock 行为，使用 Stub 实现
        when(courseFeignClient.isCourseExist(anyInt())).thenAnswer(invocation -> {
            int courseId = invocation.getArgument(0);
            return courseFeignClientStub.isCourseExist(courseId);
        });
        
        // 设置 HerbFeignClient Mock 行为，使用 Stub 实现
        when(herbFeignClient.isHerbExist(anyInt())).thenAnswer(invocation -> {
            int herbId = invocation.getArgument(0);
            return herbFeignClientStub.isHerbExist(herbId);
        });
    }

    @Test
    @DisplayName("桩测试：发布课程评论 - 使用Stub验证用户和课程存在")
    void testAddComment_Course_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据
        userFeignClientStub.addUser(100, "testUser", "http://example.com/avatar.jpg", false);
        userFeignClientStub.setTokenMapping("Bearer valid-token-100", 100);
        courseFeignClientStub.addCourse(1);

        CommentDTO dto = new CommentDTO();
        dto.setTargetType("course");
        dto.setTargetId(1);
        dto.setContent("这是一条测试评论");

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer valid-token-100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.content").value("这是一条测试评论"));
    }

    @Test
    @DisplayName("桩测试：发布课程评论 - 用户不存在应被拒绝")
    void testAddComment_UserNotExist_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据（不添加用户）
        courseFeignClientStub.addCourse(1);
        userFeignClientStub.setTokenMapping("Bearer invalid-token", 999); // 不存在的用户

        CommentDTO dto = new CommentDTO();
        dto.setTargetType("course");
        dto.setTargetId(1);
        dto.setContent("这是一条测试评论");

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    @Test
    @DisplayName("桩测试：发布课程评论 - 课程不存在应被拒绝")
    void testAddComment_CourseNotExist_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据（不添加课程）
        userFeignClientStub.addUser(100, "testUser", "http://example.com/avatar.jpg", false);
        userFeignClientStub.setTokenMapping("Bearer valid-token-100", 100);
        // 不添加课程，模拟课程不存在

        CommentDTO dto = new CommentDTO();
        dto.setTargetType("course");
        dto.setTargetId(999); // 不存在的课程ID
        dto.setContent("这是一条测试评论");

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer valid-token-100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("课程不存在"));
    }

    @Test
    @DisplayName("桩测试：发布中草药评论 - 使用Stub验证中草药存在")
    void testAddComment_Herb_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据
        userFeignClientStub.addUser(100, "testUser", "http://example.com/avatar.jpg", false);
        userFeignClientStub.setTokenMapping("Bearer valid-token-100", 100);
        herbFeignClientStub.addHerb(1);

        CommentDTO dto = new CommentDTO();
        dto.setTargetType("herb");
        dto.setTargetId(1);
        dto.setContent("这是一条中草药评论");

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer valid-token-100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.content").value("这是一条中草药评论"));
    }

    @Test
    @DisplayName("桩测试：发布评论 - 包含敏感词应被过滤")
    void testAddComment_WithSensitiveWords_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据
        userFeignClientStub.addUser(100, "testUser", "http://example.com/avatar.jpg", false);
        userFeignClientStub.setTokenMapping("Bearer valid-token-100", 100);
        courseFeignClientStub.addCourse(1);

        CommentDTO dto = new CommentDTO();
        dto.setTargetType("course");
        dto.setTargetId(1);
        dto.setContent("这是一条包含测试敏感词的评论"); // 包含轻度敏感词

        // Act & Assert
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer valid-token-100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.filtered").value(true)); // 应被标记为已过滤
    }

    @Test
    @DisplayName("桩测试：获取评论列表 - 使用Stub批量获取用户信息")
    void testListComments_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据
        userFeignClientStub.addUser(100, "testUser", "http://example.com/avatar.jpg", false);
        courseFeignClientStub.addCourse(1);

        // 先创建一条评论
        CommentDTO dto = new CommentDTO();
        dto.setTargetType("course");
        dto.setTargetId(1);
        dto.setContent("测试评论");
        commentService.addComment(dto, 100);

        // Act & Assert
        mockMvc.perform(get("/comments")
                        .param("targetType", "course")
                        .param("targetId", "1")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list[0].content").value("测试评论"));
    }
}

