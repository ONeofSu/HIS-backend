package org.csu.herb_teaching.integration;

import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.feign.HerbInfoFeignClient;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.mapper.CourseMapper;
import org.csu.herb_teaching.service.CourseService;
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

import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CourseService集成测试")
class CourseServiceIT {

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
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @MockBean
    private UserFeignClient userFeignClient;

    @MockBean
    private HerbInfoFeignClient herbInfoFeignClient;

    @BeforeEach
    void setUpMocks() {
        // Mock UserFeignClient - 所有教师ID都返回true
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);
    }

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

    @Test
    @Transactional
    @DisplayName("测试创建课程 - 集成测试")
    void testCreateCourse_Integration() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("集成测试课程");
        courseDTO.setCoverImageUrl("http://example.com/image.jpg");
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        courseDTO.setTeacherId(1);
        courseDTO.setCourseStartTime(LocalDateTime.now());
        courseDTO.setCourseEndTime(LocalDateTime.now().plusDays(30));
        courseDTO.setCourseDes("这是一门集成测试课程");

        // Act
        Course result = courseService.createCourse(courseDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result.getCourseId() > 0);
        assertEquals("集成测试课程", result.getCourseName());
        
        // 验证数据库中的记录
        Course saved = courseMapper.selectById(result.getCourseId());
        assertNotNull(saved);
        assertEquals("集成测试课程", saved.getCourseName());
    }

    @Test
    @Transactional
    @DisplayName("测试获取课程列表 - 集成测试")
    void testGetCourseList_Integration() {
        // Arrange - 先创建一些测试数据
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("测试课程1");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        courseService.createCourse(courseDTO);

        // Act
        var result = courseService.getCourseList(1, 10, null, 0, 0);

        // Assert
        assertNotNull(result);
        assertTrue(result.getTotal() > 0);
    }

    @Test
    @Transactional
    @DisplayName("测试更新课程 - 集成测试")
    void testUpdateCourse_Integration() {
        // Arrange - 先创建课程
        CourseDTO createDTO = new CourseDTO();
        createDTO.setCourseName("原始课程名");
        createDTO.setTeacherId(1);
        createDTO.setCourseType(1);
        createDTO.setCourseObject(0);
        Course created = courseService.createCourse(createDTO);

        // 更新课程
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setCourseId(created.getCourseId());
        updateDTO.setCourseName("更新后的课程名");

        // Act
        Course updated = courseService.updateCourse(updateDTO);

        // Assert
        assertNotNull(updated);
        assertEquals("更新后的课程名", updated.getCourseName());
        
        // 验证数据库中的记录
        Course saved = courseMapper.selectById(created.getCourseId());
        assertEquals("更新后的课程名", saved.getCourseName());
    }

    @Test
    @Transactional
    @DisplayName("测试删除课程 - 集成测试")
    void testDeleteCourse_Integration() {
        // Arrange - 先创建课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("待删除课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course created = courseService.createCourse(courseDTO);
        int courseId = created.getCourseId();

        // Act
        boolean deleted = courseService.deleteCourse(courseId);

        // Assert
        assertTrue(deleted);
        
        // 验证数据库中的记录已被删除
        Course saved = courseMapper.selectById(courseId);
        assertNull(saved);
    }
}

