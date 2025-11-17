package org.csu.herb_teaching.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.VO.CourseDetailVO;
import org.csu.herb_teaching.VO.PageVO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.feign.HerbInfoFeignClient;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.service.LabService;
import org.csu.herb_teaching.service.ResourceService;
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

import java.time.LocalDateTime;
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
@DisplayName("CourseController集成测试 - Controller->Service->Dao->Database")
class CourseControllerIT {

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

    @MockBean
    private HerbInfoFeignClient herbInfoFeignClient;

    @Autowired
    private CourseService courseService;

    @BeforeAll
    void initSchema() throws Exception {
        try (Connection conn = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement stmt = conn.createStatement()) {
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
        }
    }

    @Test
    @DisplayName("测试获取课程列表 - Controller集成测试")
    void testGetCourseList_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("集成测试课程");
        courseDTO.setTeacherId(1);
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

    @Test
    @DisplayName("测试获取课程详情 - Controller集成测试")
    void testGetCourseDetail_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);
        when(userFeignClient.getUserInfoById(anyInt())).thenReturn(null);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("测试课程详情");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course created = courseService.createCourse(courseDTO);

        // Act & Assert
        mockMvc.perform(get("/courses/{courseId}", created.getCourseId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.courseId").value(created.getCourseId()));
    }

    @Test
    @DisplayName("测试创建课程 - Controller集成测试")
    void testCreateCourse_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.isUserRealTeacher(1)).thenReturn(true);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("Controller集成测试课程");
        courseDTO.setCoverImageUrl("http://example.com/image.jpg");
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        courseDTO.setTeacherId(1);
        courseDTO.setCourseStartTime(LocalDateTime.now());
        courseDTO.setCourseEndTime(LocalDateTime.now().plusDays(30));
        courseDTO.setCourseDes("这是一门Controller集成测试课程");

        // Act & Assert
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.course").exists())
                .andExpect(jsonPath("$.courseName").value("Controller集成测试课程"));
    }

    @Test
    @DisplayName("测试更新课程 - Controller集成测试")
    void testUpdateCourse_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.isUserRealTeacher(1)).thenReturn(true);

        // 先创建课程
        CourseDTO createDTO = new CourseDTO();
        createDTO.setCourseName("原始课程名");
        createDTO.setTeacherId(1);
        createDTO.setCourseType(1);
        createDTO.setCourseObject(0);
        Course created = courseService.createCourse(createDTO);

        // 更新课程
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setCourseName("更新后的课程名");

        // Act & Assert
        mockMvc.perform(put("/courses/{courseId}", created.getCourseId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.courseName").value("更新后的课程名"));
    }

    @Test
    @DisplayName("测试删除课程 - Controller集成测试")
    void testDeleteCourse_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.isUserRealTeacher(1)).thenReturn(true);

        // 先创建课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("待删除课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course created = courseService.createCourse(courseDTO);

        // Act & Assert
        mockMvc.perform(delete("/courses/{courseId}", created.getCourseId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Course deleted successfully."));
    }

    @Test
    @DisplayName("测试课程评分 - Controller集成测试")
    void testRateCourse_ControllerIntegration() throws Exception {
        // Arrange
        when(userFeignClient.isUserRealTeacher(1)).thenReturn(true);
        when(userFeignClient.getUserIdByToken(anyString())).thenReturn(100);
        when(userFeignClient.isUserExist(100)).thenReturn(true);

        // 先创建课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("评分测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course created = courseService.createCourse(courseDTO);

        // Mock getCourseDetail
        CourseDetailVO detailVO = new CourseDetailVO();
        detailVO.setCourseId(created.getCourseId());
        when(courseService.getCourseDetail(created.getCourseId())).thenReturn(detailVO);

        Map<String, Integer> ratingPayload = new HashMap<>();
        ratingPayload.put("ratingValue", 5);

        // Act & Assert
        mockMvc.perform(post("/courses/{courseId}/ratings", created.getCourseId())
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }
}

