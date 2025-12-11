package org.csu.herb_teaching.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.service.CourseService;
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
@DisplayName("ResourceController集成测试 - Controller->Service->Dao->Database")
class ResourceControllerIT {

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

    @Autowired
    private ResourceService resourceService;

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
        }
    }

    @Test
    @DisplayName("测试获取资源 - Controller集成测试")
    void testGetResource_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("资源测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course createdCourse = courseService.createCourse(courseDTO);

        // 创建一个测试资源
        CourseResourceDTO resourceDTO = new CourseResourceDTO();
        resourceDTO.setCourseResourceType(0); // 视频类型
        resourceDTO.setCourseResourceTitle("测试视频资源");
        resourceDTO.setCourseResourceContent("http://example.com/video.mp4");
        resourceDTO.setCourseResourceOrder(1);
        CourseResource createdResource = resourceService.createResource(createdCourse.getCourseId(), resourceDTO);

        // Act & Assert
        mockMvc.perform(get("/resources/{resourceId}", createdResource.getCourseResourceId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.resource").exists())
                .andExpect(jsonPath("$.resource.courseResourceId").value(createdResource.getCourseResourceId()))
                .andExpect(jsonPath("$.resource.courseResourceTitle").value("测试视频资源"))
                .andExpect(jsonPath("$.resource.courseId").value(createdCourse.getCourseId()));
    }

    @Test
    @DisplayName("测试获取资源 - 资源不存在")
    void testGetResource_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/resources/{resourceId}", 99999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Resource not found!"));
    }

    @Test
    @DisplayName("测试更新资源 - Controller集成测试")
    void testUpdateResource_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("更新资源测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course createdCourse = courseService.createCourse(courseDTO);

        // 创建一个测试资源
        CourseResourceDTO createResourceDTO = new CourseResourceDTO();
        createResourceDTO.setCourseResourceType(0); // 视频类型
        createResourceDTO.setCourseResourceTitle("原始资源标题");
        createResourceDTO.setCourseResourceContent("http://example.com/original.mp4");
        createResourceDTO.setCourseResourceOrder(1);
        CourseResource createdResource = resourceService.createResource(createdCourse.getCourseId(), createResourceDTO);

        // 准备更新数据
        CourseResourceDTO updateResourceDTO = new CourseResourceDTO();
        updateResourceDTO.setCourseResourceType(1); // 文件类型
        updateResourceDTO.setCourseResourceTitle("更新后的资源标题");
        updateResourceDTO.setCourseResourceContent("http://example.com/updated.pdf");
        updateResourceDTO.setCourseResourceOrder(2);

        // Act & Assert
        mockMvc.perform(put("/resources/{resourceId}", createdResource.getCourseResourceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResourceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.resource").exists())
                .andExpect(jsonPath("$.resource.courseResourceTitle").value("更新后的资源标题"))
                .andExpect(jsonPath("$.resource.courseResourceType").value(1))
                .andExpect(jsonPath("$.resource.courseResourceOrder").value(2));
    }

    @Test
    @DisplayName("测试更新资源 - 资源不存在")
    void testUpdateResource_NotFound() throws Exception {
        // Arrange
        CourseResourceDTO updateResourceDTO = new CourseResourceDTO();
        updateResourceDTO.setCourseResourceType(0);
        updateResourceDTO.setCourseResourceTitle("不存在的资源");
        updateResourceDTO.setCourseResourceContent("http://example.com/test.mp4");
        updateResourceDTO.setCourseResourceOrder(1);

        // Act & Assert
        mockMvc.perform(put("/resources/{resourceId}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResourceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Resource not found!"));
    }

    @Test
    @DisplayName("测试删除资源 - Controller集成测试")
    void testDeleteResource_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("删除资源测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course createdCourse = courseService.createCourse(courseDTO);

        // 创建一个测试资源
        CourseResourceDTO resourceDTO = new CourseResourceDTO();
        resourceDTO.setCourseResourceType(0); // 视频类型
        resourceDTO.setCourseResourceTitle("待删除资源");
        resourceDTO.setCourseResourceContent("http://example.com/delete.mp4");
        resourceDTO.setCourseResourceOrder(1);
        CourseResource createdResource = resourceService.createResource(createdCourse.getCourseId(), resourceDTO);

        // Act & Assert
        mockMvc.perform(delete("/resources/{resourceId}", createdResource.getCourseResourceId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Resource deleted successfully."));

        // 验证资源已被删除
        mockMvc.perform(get("/resources/{resourceId}", createdResource.getCourseResourceId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Resource not found!"));
    }

    @Test
    @DisplayName("测试删除资源 - 资源不存在")
    void testDeleteResource_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/resources/{resourceId}", 99999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Resource not found!"));
    }
}

