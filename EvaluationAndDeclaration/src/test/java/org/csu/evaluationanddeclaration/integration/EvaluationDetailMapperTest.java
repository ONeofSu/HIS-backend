package org.csu.evaluationanddeclaration.integration;

import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.mapper.EvaluationDetailMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EvaluationDetailMapperTest {

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("herb_rating_test")
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
    private EvaluationDetailMapper evaluationDetailMapper;

    @BeforeAll
    void initSchema() throws Exception {
        // reuse the same schema as the shared dump to stay aligned with production tables
        try (Connection conn = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE evaluation_detail (
                        evaluation_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        evaluation_id BIGINT NOT NULL,
                        indicator_id BIGINT NOT NULL,
                        indicator_score DOUBLE,
                        comment VARCHAR(255),
                        material VARCHAR(255)
                    )
                    """);
        }
    }

    @Test
    @Transactional
    void insertAndQueryEvaluationDetails() {
        EvaluationDetail detail = new EvaluationDetail();
        detail.setEvaluationId(42L);
        detail.setIndicatorId(5L);
        detail.setIndicatorScore(9.5);
        detail.setComment("excellent");
        detail.setMaterial("lab-report.pdf");

        evaluationDetailMapper.insert(detail);

        List<EvaluationDetail> results = evaluationDetailMapper.selectListByEvaluationId(42);

        assertThat(results).hasSize(1);
        EvaluationDetail stored = results.getFirst();
        assertThat(stored.getEvaluationDetailId()).isNotNull();
        assertThat(stored.getIndicatorId()).isEqualTo(5L);
        assertThat(stored.getIndicatorScore()).isEqualTo(9.5);
        assertThat(stored.getComment()).isEqualTo("excellent");
        assertThat(stored.getMaterial()).isEqualTo("lab-report.pdf");
        assertThat(evaluationDetailMapper.count()).isEqualTo(1L);
    }
}
