package org.csu.evaluationanddeclaration.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.core.io.FileSystemResource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
class EvaluationControllerIT {

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("herb_rating")
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

    @BeforeAll
    void loadSchemaAndData() throws Exception {
        Path sqlPath = Path.of("..", "Database", "his_herb_rating2025_07_08_19_35_10.sql").toAbsolutePath();
        try (Connection connection = DriverManager.getConnection(
                MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword())) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource(sqlPath));
        }
    }

    @Test
    void getEvaluationsBetweenScoreReturnsSeededRows() throws Exception {
        mockMvc.perform(get("/GetEvaluationsBetweenScore")
                        .param("minScore", "70")
                        .param("maxScore", "95"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.Evaluations[*].evaluationId", containsInAnyOrder(1, 2)));
    }

    @Test
    void getEvaluationsBetweenDatesReturnsExpectedWindow() throws Exception {
        mockMvc.perform(get("/GetEvaluationsBetweenDates")
                        .param("startDate", "2025-06-24 00:00:00")
                        .param("endDate", "2025-06-29 23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.Evaluations[*].evaluationId", containsInAnyOrder(1, 2)));
    }

    @Test
    void getEvaluationDetailsReturnsSixIndicators() throws Exception {
        mockMvc.perform(get("/GetEvaluationDetailsByEvaluationId")
                        .param("evaluationId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.EvaluationDetails", hasSize(6)));
    }

    @Test
    void getHerbEvaluationScoreByHerbIdReturnsAggregatedScore() throws Exception {
        mockMvc.perform(get("/GetHerbEvaluationScoreByHerbId")
                        .param("herbId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.herbScore").value(80.00));
    }
}
