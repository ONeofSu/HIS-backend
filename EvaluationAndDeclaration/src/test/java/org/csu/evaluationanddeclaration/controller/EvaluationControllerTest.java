package org.csu.evaluationanddeclaration.controller;

import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.entity.HerbRating;
import org.csu.evaluationanddeclaration.service.Impl.EvaluationApplicationServiceImpl;
import org.csu.evaluationanddeclaration.service.Impl.HerbEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EvaluationController.class)
class EvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HerbEvaluationServiceImpl evaluationService;
    @MockBean
    private EvaluationApplicationServiceImpl applicationService;
    @MockBean
    private JdbcTemplate jdbcTemplate;

    // mappers mocked because controller autowires them directly
    @MockBean private org.csu.evaluationanddeclaration.mapper.HerbEvaluationMapper herbEvaluationMapper;
    @MockBean private org.csu.evaluationanddeclaration.mapper.HerbRatingMapper herbRatingMapper;
    @MockBean private org.csu.evaluationanddeclaration.mapper.EvaluationApplicationMapper evaluationApplicationMapper;
    @MockBean private org.csu.evaluationanddeclaration.mapper.EvaluationDetailMapper evaluationDetailMapper;
    @MockBean private org.csu.evaluationanddeclaration.mapper.EvaluationIndicatorMapper evaluationIndicatorMapper;
    @MockBean private org.csu.evaluationanddeclaration.mapper.HerbRatingDetailMapper herbRatingDetailMapper;

    @Test
    void getHerbEvaluationsByHerbIdReturnsEmptyMessage() throws Exception {
        when(evaluationService.GetHerbEvaluationsByHerbId(10)).thenReturn(List.of());

        mockMvc.perform(get("/GetHerbEvaluationsByHerbId").param("herbId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("暂无评价"));
    }

    @Test
    void getHerbEvaluationsByHerbIdReturnsData() throws Exception {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setEvaluationId(3L);
        when(evaluationService.GetHerbEvaluationsByHerbId(1)).thenReturn(List.of(evaluation));

        mockMvc.perform(get("/GetHerbEvaluationsByHerbId").param("herbId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.Evaluations[0].evaluationId").value(3));
    }

    @Test
    void getEvaluationsBetweenScoreReturnsEmptyMessage() throws Exception {
        when(evaluationService.GetHerbEvaluationsByScore(1f, 2f)).thenReturn(List.of());

        mockMvc.perform(get("/GetEvaluationsBetweenScore")
                        .param("minScore", "1")
                        .param("maxScore", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("这个分数段内无评价"));
    }

    @Test
    void getEvaluationsBetweenScoreReturnsData() throws Exception {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setTotalScore(2.5f);
        when(evaluationService.GetHerbEvaluationsByScore(1f, 3f)).thenReturn(List.of(evaluation));

        mockMvc.perform(get("/GetEvaluationsBetweenScore")
                        .param("minScore", "1")
                        .param("maxScore", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.Evaluations[0].totalScore").value(2.5));
    }

    @Test
    void getEvaluationsBetweenDatesFormatsAndReturnsData() throws Exception {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setEvaluationId(2L);
        evaluation.setEvaluateTime(Date.from(Instant.parse("2024-01-01T00:00:00Z")));

        when(evaluationService.GetHerbEvaluationsByDate(any(Date.class), any(Date.class)))
                .thenReturn(List.of(evaluation));

        mockMvc.perform(get("/GetEvaluationsBetweenDates")
                        .param("startDate", "2024-01-01 00:00:00")
                        .param("endDate", "2024-01-02 00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.Evaluations[0].evaluationId").value(2));

        verify(evaluationService).GetHerbEvaluationsByDate(any(Date.class), any(Date.class));
    }

    @Test
    void getEvaluationsByUserIdReturnsEmptyMessage() throws Exception {
        when(evaluationService.GetHerbEvaluationsByUserId(3)).thenReturn(List.of());

        mockMvc.perform(get("/GetEvaluationsByUserId").param("userId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("该用户暂无评价"));
    }

    @Test
    void getEvaluationDetailsReturnsData() throws Exception {
        EvaluationDetail detail = new EvaluationDetail();
        detail.setIndicatorId(1L);
        when(evaluationService.GetAllEvaluationDetails(5)).thenReturn(List.of(detail));

        mockMvc.perform(get("/GetEvaluationDetailsByEvaluationId").param("evaluationId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.EvaluationDetails[0].indicatorId").value(1));
    }

    @Test
    void getHerbEvaluationScoreByHerbIdReturnsErrorWhenMissing() throws Exception {
        when(herbRatingMapper.selectByHerbId(99)).thenReturn(null);

        mockMvc.perform(get("/GetHerbEvaluationScoreByHerbId").param("herbId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("不存在该herbId"));
    }

    @Test
    void getHerbEvaluationScoreByHerbIdReturnsScore() throws Exception {
        HerbRating rating = new HerbRating();
        rating.setTotalScore(7.5);
        when(herbRatingMapper.selectByHerbId(8)).thenReturn(rating);

        mockMvc.perform(get("/GetHerbEvaluationScoreByHerbId").param("herbId", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.herbScore").value(7.5));
    }
}
