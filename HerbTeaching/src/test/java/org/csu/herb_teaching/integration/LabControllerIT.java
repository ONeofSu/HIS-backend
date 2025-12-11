package org.csu.herb_teaching.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.service.LabService;
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
@DisplayName("LabController集成测试 - Controller->Service->Dao->Database")
class LabControllerIT {

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
    private LabService labService;

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
        }
    }

    @Test
    @DisplayName("测试获取实验 - Controller集成测试")
    void testGetLab_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("实验测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course createdCourse = courseService.createCourse(courseDTO);

        // 创建一个测试实验
        LabDTO labDTO = new LabDTO();
        labDTO.setLabName("测试实验");
        labDTO.setLabSteps("步骤1：准备材料\n步骤2：开始实验\n步骤3：记录结果");
        labDTO.setLabOrder(1);
        Lab createdLab = labService.createLab(createdCourse.getCourseId(), labDTO);

        // Act & Assert
        mockMvc.perform(get("/labs/{labId}", createdLab.getLabId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.lab").exists())
                .andExpect(jsonPath("$.lab.labId").value(createdLab.getLabId()))
                .andExpect(jsonPath("$.lab.labName").value("测试实验"))
                .andExpect(jsonPath("$.lab.courseId").value(createdCourse.getCourseId()));
    }

    @Test
    @DisplayName("测试获取实验 - 实验不存在")
    void testGetLab_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/labs/{labId}", 99999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Lab not found!"));
    }

    @Test
    @DisplayName("测试更新实验 - Controller集成测试")
    void testUpdateLab_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("更新实验测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course createdCourse = courseService.createCourse(courseDTO);

        // 创建一个测试实验
        LabDTO createLabDTO = new LabDTO();
        createLabDTO.setLabName("原始实验名");
        createLabDTO.setLabSteps("原始步骤");
        createLabDTO.setLabOrder(1);
        Lab createdLab = labService.createLab(createdCourse.getCourseId(), createLabDTO);

        // 准备更新数据
        LabDTO updateLabDTO = new LabDTO();
        updateLabDTO.setLabName("更新后的实验名");
        updateLabDTO.setLabSteps("更新后的步骤：步骤1\n步骤2\n步骤3");
        updateLabDTO.setLabOrder(2);

        // Act & Assert
        mockMvc.perform(put("/labs/{labId}", createdLab.getLabId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateLabDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.lab").exists())
                .andExpect(jsonPath("$.lab.labName").value("更新后的实验名"))
                .andExpect(jsonPath("$.lab.labOrder").value(2));
    }

    @Test
    @DisplayName("测试更新实验 - 实验不存在")
    void testUpdateLab_NotFound() throws Exception {
        // Arrange
        LabDTO updateLabDTO = new LabDTO();
        updateLabDTO.setLabName("不存在的实验");
        updateLabDTO.setLabSteps("测试步骤");
        updateLabDTO.setLabOrder(1);

        // Act & Assert
        mockMvc.perform(put("/labs/{labId}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateLabDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Lab not found!"));
    }

    @Test
    @DisplayName("测试删除实验 - Controller集成测试")
    void testDeleteLab_ControllerIntegration() throws Exception {
        // Arrange - Mock Feign Client
        when(userFeignClient.isUserRealTeacher(anyInt())).thenReturn(true);

        // 先创建一个测试课程
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("删除实验测试课程");
        courseDTO.setTeacherId(1);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);
        Course createdCourse = courseService.createCourse(courseDTO);

        // 创建一个测试实验
        LabDTO labDTO = new LabDTO();
        labDTO.setLabName("待删除实验");
        labDTO.setLabSteps("测试步骤");
        labDTO.setLabOrder(1);
        Lab createdLab = labService.createLab(createdCourse.getCourseId(), labDTO);

        // Act & Assert
        mockMvc.perform(delete("/labs/{labId}", createdLab.getLabId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Lab deleted successfully."));

        // 验证实验已被删除
        mockMvc.perform(get("/labs/{labId}", createdLab.getLabId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Lab not found!"));
    }

    @Test
    @DisplayName("测试删除实验 - 实验不存在")
    void testDeleteLab_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/labs/{labId}", 99999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("Lab not found!"));
    }
}
