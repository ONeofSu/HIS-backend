package org.csu.herb_teaching.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.service.CourseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UserController集成测试 - Controller->Service->Dao->Database")
class UserControllerIT {

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("herb_teaching_test")
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

    @Test
    @DisplayName("测试获取用户收藏的课程列表 - Controller集成测试")
    void testGetCollectedCourses_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 创建测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("用户收藏测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course created = courseService.createCourse(courseDTO);

        // 收藏课程
        courseService.collectCourse(created.getCourseId(), 100);

        // Act & Assert
        mockMvc.perform(get("/users/{userId}/collections", 100)
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses.list").exists());
    }

    @Test
    @DisplayName("测试获取用户收藏的课程列表 - 无收藏")
    void testGetCollectedCourses_NoCollections() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users/{userId}/collections", 999)
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.courses").exists());
    }
}

