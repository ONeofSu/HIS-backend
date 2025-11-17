package org.csu.evaluationanddeclaration.controller;

import org.csu.evaluationanddeclaration.entity.EvaluationApplication;
import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.entity.HerbRating;
import org.csu.evaluationanddeclaration.entity.HerbRatingDetail;
import org.csu.evaluationanddeclaration.mapper.EvaluationApplicationMapper;
import org.csu.evaluationanddeclaration.mapper.EvaluationDetailMapper;
import org.csu.evaluationanddeclaration.mapper.EvaluationIndicatorMapper;
import org.csu.evaluationanddeclaration.mapper.HerbEvaluationMapper;
import org.csu.evaluationanddeclaration.mapper.HerbRatingDetailMapper;
import org.csu.evaluationanddeclaration.mapper.HerbRatingMapper;
import org.csu.evaluationanddeclaration.service.Impl.EvaluationApplicationServiceImpl;
import org.csu.evaluationanddeclaration.service.Impl.HerbEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HerbEvaluationServiceImpl evaluationService;
    @MockBean
    private EvaluationApplicationServiceImpl applicationService;
    @MockBean
    private HerbEvaluationMapper herbEvaluationMapper;
    @MockBean
    private HerbRatingMapper herbRatingMapper;
    @MockBean
    private EvaluationApplicationMapper evaluationApplicationMapper;
    @MockBean
    private EvaluationDetailMapper evaluationDetailMapper;
    @MockBean
    private EvaluationIndicatorMapper evaluationIndicatorMapper;
    @MockBean
    private JdbcTemplate jdbcTemplate;
    @MockBean
    private HerbRatingDetailMapper herbRatingDetailMapper;

    @Test
    void getApplicationByEvaluationIdReturnsNotFoundWhenMissing() throws Exception {
        when(applicationService.GetApplicationByEvaluationId(1)).thenReturn(null);

        mockMvc.perform(get("/GetApplicationByEvaluationId").param("evaluationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("无评价"));
    }

    @Test
    void getApplicationByEvaluationIdReturnsApplication() throws Exception {
        EvaluationApplication application = new EvaluationApplication();
        application.setApplicationId(2L);
        application.setEvaluationId(3L);
        when(applicationService.GetApplicationByEvaluationId(3)).thenReturn(application);

        mockMvc.perform(get("/GetApplicationByEvaluationId").param("evaluationId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.Application.applicationId").value(2))
                .andExpect(jsonPath("$.Application.evaluationId").value(3));
    }

    @Test
    void getUnAuditedApplicationsReturnsEmptyMessage() throws Exception {
        when(evaluationApplicationMapper.getApplications("审核中")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/GetUnAuditedApplications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("暂无未审核的评论"));
    }

    @Test
    void doAuditReturnsErrorWhenApplicationMissing() throws Exception {
        when(evaluationApplicationMapper.getApplicationById(1L)).thenReturn(null);
        when(evaluationApplicationMapper.getApplications("审核中")).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/DoAudit").param("applicationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("该申请不存在或已审核"));
    }

    @Test
    void doAuditCreatesRatingWhenNoExistingRating() throws Exception {
        EvaluationApplication application = new EvaluationApplication();
        application.setApplicationId(1L);
        application.setEvaluationId(100L);
        application.setApplicationState("审核中");

        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setEvaluationId(100L);
        evaluation.setHerbId(5L);
        evaluation.setTotalScore(9.0f);

        when(evaluationApplicationMapper.getApplicationById(1L)).thenReturn(application);
        when(evaluationApplicationMapper.getApplications("审核中"))
                .thenReturn(new LinkedList<>(List.of(application)));
        when(herbEvaluationMapper.getEvaluationById(100)).thenReturn(evaluation);
        when(herbRatingMapper.count()).thenReturn(0L);
        when(herbRatingDetailMapper.count()).thenReturn(0L);
        when(herbRatingMapper.selectByHerbId(5)).thenReturn(null);
        when(evaluationDetailMapper.selectListByEvaluationId(100))
                .thenReturn(new LinkedList<>(List.of(
                        detailWithScore(1), detailWithScore(2), detailWithScore(3),
                        detailWithScore(4), detailWithScore(5), detailWithScore(6)
                )));

        mockMvc.perform(post("/DoAudit").param("applicationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("已通过该评论的审核"));

        verify(evaluationService).AddRating(argThat(rating ->
                rating.getHerbId().equals(5L) && rating.getTotalScore().equals(9.0)
        ));
        verify(evaluationService, times(6)).AddRatingDetail(any(HerbRatingDetail.class));
        verify(evaluationService).UpdateHerbEvaluation(evaluation);
        verify(applicationService).UpdateApplication(application);
    }

    @Test
    void doAuditUpdatesExistingRatingAndDetails() throws Exception {
        EvaluationApplication application = new EvaluationApplication();
        application.setApplicationId(2L);
        application.setEvaluationId(200L);
        application.setApplicationState("审核中");

        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setEvaluationId(200L);
        evaluation.setHerbId(10L);
        evaluation.setTotalScore(9.0f);

        HerbRating existingRating = new HerbRating();
        existingRating.setHerbId(10L);
        existingRating.setTotalScore(5.0);

        when(evaluationApplicationMapper.getApplicationById(2L)).thenReturn(application);
        when(evaluationApplicationMapper.getApplications("审核中"))
                .thenReturn(new LinkedList<>(List.of(application)));
        when(herbEvaluationMapper.getEvaluationById(200)).thenReturn(evaluation);
        when(herbRatingMapper.count()).thenReturn(1L);
        when(herbRatingDetailMapper.count()).thenReturn(1L);
        when(herbRatingMapper.selectByHerbId(10)).thenReturn(existingRating);
        when(herbEvaluationMapper.getAuditedEvaluationsByHerbId(10))
                .thenReturn(List.of(makeEvaluationWithScore(7f), makeEvaluationWithScore(5f)));

        when(evaluationDetailMapper.selectListByEvaluationId(200))
                .thenReturn(new LinkedList<>(List.of(
                        detailWithScore(10), detailWithScore(8), detailWithScore(4)
                )));

        when(herbRatingDetailMapper.findByHerbId(10L))
                .thenReturn(new LinkedList<>(List.of(
                        ratingDetailWithAvg(4.0), ratingDetailWithAvg(6.0), ratingDetailWithAvg(7.0)
                )));

        mockMvc.perform(post("/DoAudit").param("applicationId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("已通过该评论的审核"));

        ArgumentCaptor<HerbRating> ratingCaptor = ArgumentCaptor.forClass(HerbRating.class);
        verify(evaluationService).UpdateRating(ratingCaptor.capture());
        assertThat(ratingCaptor.getValue().getTotalScore()).isEqualTo(7.0);

        ArgumentCaptor<HerbRatingDetail> detailCaptor = ArgumentCaptor.forClass(HerbRatingDetail.class);
        verify(evaluationService, times(3)).UpdateRatingDetail(detailCaptor.capture());
        List<HerbRatingDetail> updatedDetails = detailCaptor.getAllValues();
        assertThat(updatedDetails.get(0).getAvgScore()).isCloseTo(6.0, withinTolerance());
        assertThat(updatedDetails.get(1).getAvgScore()).isCloseTo(6.6667, withinTolerance());
        assertThat(updatedDetails.get(2).getAvgScore()).isCloseTo(6.0, withinTolerance());
    }

    private EvaluationDetail detailWithScore(double score) {
        EvaluationDetail detail = new EvaluationDetail();
        detail.setIndicatorScore(score);
        return detail;
    }

    private HerbRatingDetail ratingDetailWithAvg(double avg) {
        HerbRatingDetail detail = new HerbRatingDetail();
        detail.setAvgScore(avg);
        return detail;
    }

    private HerbEvaluation makeEvaluationWithScore(float score) {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setTotalScore(score);
        return evaluation;
    }

    private org.assertj.core.data.Offset<Double> withinTolerance() {
        return org.assertj.core.data.Offset.offset(1e-4);
    }
}
