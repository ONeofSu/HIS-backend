package org.csu.herb_teaching.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.config.StubTestConfiguration;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.feign.HerbInfoFeignClient;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.stub.HerbInfoFeignClientStub;
import org.csu.herb_teaching.stub.UserFeignClientStub;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CourseController 桩集成测试
 * 使用 Stub 实现设置 Mock 行为，验证接口正确性
 * 
 * 与 CourseControllerIT 的区别：
 * - 使用 Stub 实现来设置 Mock 行为，而不是直接使用 when().thenReturn()
 * - Stub 实现提供了更真实的模拟行为和数据一致性
 * - 可以测试 Stub 与真实依赖的差异
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(StubTestConfiguration.class)
@DisplayName("CourseController桩集成测试 - 使用Stub实现设置Mock行为")
class CourseControllerStubIT {

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("herb_teaching_stub_test")
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
    private HerbInfoFeignClient herbInfoFeignClient;

    @Autowired
    private UserFeignClientStub userFeignClientStub;

    @Autowired
    private HerbInfoFeignClientStub herbInfoFeignClientStub;

    @Autowired
    private CourseService courseService;

    @BeforeAll
    void initSchema() throws Exception {
        if (!MYSQL.isRunning()) {
            MYSQL.start();
        }
        try (Connection conn = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement stmt = conn.createStatement()) {
            // 创建课程表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS course (
                        course_id INT AUTO_INCREMENT PRIMARY KEY,
                        course_name VARCHAR(255) NOT NULL,
                        cover_image_url VARCHAR(500),
                        course_type INT DEFAULT 0,
                        course_object INT DEFAULT 0,
                        teacher_id INT NOT NULL,
                        course_start_time DATETIME,
                        course_end_time DATETIME,
                        course_des TEXT,
                        course_average_rating DECIMAL(3,2) DEFAULT 0.00,
                        course_rating_count INT DEFAULT 0
                    )
                    """);
            // 创建实验表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS lab (
                        lab_id INT AUTO_INCREMENT PRIMARY KEY,
                        course_id INT NOT NULL,
                        lab_name VARCHAR(100) NOT NULL,
                        lab_steps TEXT,
                        lab_order INT DEFAULT 0,
                        INDEX idx_course_id (course_id)
                    )
                    """);
            // 创建课程资源表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS course_resource (
                        course_resource_id INT AUTO_INCREMENT PRIMARY KEY,
                        course_id INT NOT NULL,
                        course_resource_type INT NOT NULL,
                        course_resource_order INT DEFAULT 0,
                        course_resource_title VARCHAR(100),
                        course_resource_content TEXT,
                        course_resource_metadata JSON,
                        course_resource_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                        course_resource_isvalid TINYINT(1) DEFAULT 1,
                        INDEX idx_course_id (course_id)
                    )
                    """);
            // 创建课程药材关联表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS course_herb_link (
                        link_id INT AUTO_INCREMENT PRIMARY KEY,
                        course_id INT NOT NULL,
                        herb_id INT NOT NULL,
                        UNIQUE KEY uk_course_herb (course_id, herb_id)
                    )
                    """);
            // 创建课程评分表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS course_rating (
                        rating_id INT AUTO_INCREMENT PRIMARY KEY,
                        course_id INT NOT NULL,
                        user_id INT NOT NULL,
                        rating_value INT NOT NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        UNIQUE KEY uk_course_user_rating (course_id, user_id)
                    )
                    """);
            // 创建用户课程收藏表
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS user_course_collection (
                        collection_id INT AUTO_INCREMENT PRIMARY KEY,
                        course_id INT NOT NULL,
                        user_id INT NOT NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        UNIQUE KEY uk_course_user_collection (course_id, user_id)
                    )
                    """);
        }
    }

    @BeforeEach
    void setUp() {
        // 重置 Stub 数据
        userFeignClientStub.clearAll();
        herbInfoFeignClientStub.clearAll();
        
        // 使用 Stub 实现来设置 Mock 行为
        setupMockBehavior();
    }
    
    private void setupMockBehavior() {
        // 设置 UserFeignClient Mock 行为，使用 Stub 实现
        when(userFeignClient.getUsernameById(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.getUsernameById(userId);
        });
        when(userFeignClient.getAvatarById(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.getAvatarById(userId);
        });
        when(userFeignClient.getUserInfoById(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.getUserInfoById(userId);
        });
        when(userFeignClient.isUserRealTeacher(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.isUserRealTeacher(userId);
        });
        when(userFeignClient.isUserExist(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return userFeignClientStub.isUserExist(userId);
        });
        when(userFeignClient.getUserIdByToken(anyString())).thenAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return userFeignClientStub.getUserIdByToken(token);
        });
        
        // 设置 HerbInfoFeignClient Mock 行为，使用 Stub 实现
        when(herbInfoFeignClient.getHerbInfoById(anyInt())).thenAnswer(invocation -> {
            int herbId = invocation.getArgument(0);
            return herbInfoFeignClientStub.getHerbInfoById(herbId);
        });
        when(herbInfoFeignClient.getHerbInfoByName(anyString())).thenAnswer(invocation -> {
            String herbName = invocation.getArgument(0);
            return herbInfoFeignClientStub.getHerbInfoByName(herbName);
        });
        when(herbInfoFeignClient.getAllHerbs()).thenAnswer(invocation -> {
            return herbInfoFeignClientStub.getAllHerbs();
        });
    }

    @Test
    @DisplayName("桩测试：创建课程 - 使用Stub验证教师身份")
    void testCreateCourse_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据
        userFeignClientStub.addUser(200, "teacherUser", "http://example.com/avatar.jpg", true);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("桩测试课程");
        courseDTO.setTeacherId(200); // 使用Stub中的教师ID
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        courseDTO.setCourseDes("这是一个使用Stub的测试课程");

        // Act & Assert
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                // Controller 返回结构中没有 data，直接包含 course 和 courseName 字段
                .andExpect(jsonPath("$.courseName").value("桩测试课程"))
                .andExpect(jsonPath("$.course.courseName").value("桩测试课程"));
    }

    @Test
    @DisplayName("桩测试：创建课程 - 非教师用户应被拒绝")
    void testCreateCourse_NonTeacher_WithStub() throws Exception {
        // Arrange - 使用Stub设置非教师用户
        userFeignClientStub.addUser(100, "normalUser", "http://example.com/avatar.jpg", false);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("非教师课程");
        courseDTO.setTeacherId(100); // 非教师用户
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);

        // Act & Assert
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("teacherId不是有效教师"));
    }

    @Test
    @DisplayName("桩测试：课程评分 - 使用Stub验证用户存在")
    void testRateCourse_WithStub() throws Exception {
        // Arrange - 设置Stub数据
        // 评分用户
        userFeignClientStub.addUser(100, "testUser", "http://example.com/avatar.jpg", false);
        userFeignClientStub.addToken("test-token", 100);
        // 授课教师（用于创建课程时的教师校验）
        userFeignClientStub.addUser(200, "teacherUser", "http://example.com/avatar.jpg", true);

        // 先通过 Service 创建一个课程（会调用 isUserRealTeacher 校验）
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("评分测试课程");
        courseDTO.setTeacherId(200);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course course = courseService.createCourse(courseDTO);
        int courseId = course.getCourseId();

        // 评分请求 Payload
        Map<String, Integer> ratingPayload = new HashMap<>();
        ratingPayload.put("ratingValue", 4);

        // Act & Assert - 使用Stub验证用户存在 + 控制器评分逻辑
        mockMvc.perform(post("/courses/{courseId}/ratings", courseId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.courseRating.ratingValue").value(4));
    }

    @Test
    @DisplayName("桩测试：添加中草药到课程 - 使用Stub验证中草药存在")
    void testAddHerbToCourse_WithStub() throws Exception {
        // Arrange - 设置Stub数据
        userFeignClientStub.addUser(200, "teacherUser", "http://example.com/avatar.jpg", true);
        herbInfoFeignClientStub.addHerb(1, "人参", "珍贵中药材", "补气药");
        
        // 先通过 Controller 创建一个课程，避免直接调用 Service 返回 null 的问题
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("中草药测试课程-存在");
        courseDTO.setTeacherId(200);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);

        String createResponse = mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        @SuppressWarnings("unchecked")
        Map<String, Object> createBody = objectMapper.readValue(createResponse, Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> courseMap = (Map<String, Object>) createBody.get("course");
        int courseId = (Integer) courseMap.get("courseId");

        // Act & Assert - 使用Stub验证中草药存在
        mockMvc.perform(post("/courses/{courseId}/herbs/{herbId}", courseId, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Herb added to course successfully."));
    }

    @Test
    @DisplayName("桩测试：添加中草药到课程 - 中草药不存在应被拒绝")
    void testAddHerbToCourse_HerbNotExist_WithStub() throws Exception {
        // Arrange - 设置Stub数据（不添加中草药）
        userFeignClientStub.addUser(200, "teacherUser", "http://example.com/avatar.jpg", true);
        // 不添加中草药，模拟中草药不存在的情况
        
        // 先通过 Controller 创建一个课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("中草药测试课程-不存在");
        courseDTO.setTeacherId(200);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);

        String createResponse = mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        @SuppressWarnings("unchecked")
        Map<String, Object> createBody = objectMapper.readValue(createResponse, Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> courseMap = (Map<String, Object>) createBody.get("course");
        int courseId = (Integer) courseMap.get("courseId");

        // Act & Assert - 使用Stub验证中草药不存在
        mockMvc.perform(post("/courses/{courseId}/herbs/{herbId}", courseId, 999)) // 不存在的herbId
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("中草药不存在"));
    }

    @Test
    @DisplayName("桩测试：获取课程列表 - 验证Stub不影响查询功能")
    void testGetCourseList_WithStub() throws Exception {
        // Arrange - 使用Stub设置测试数据
        userFeignClientStub.addUser(200, "teacherUser", "http://example.com/avatar.jpg", true);
        
        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("桩测试列表课程");
        courseDTO.setTeacherId(200);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        courseService.createCourse(courseDTO);

        // Act & Assert
        mockMvc.perform(get("/courses")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.total").exists());
    }
}

