package org.csu.evaluationanddeclaration.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.entity.HerbRating;
import org.csu.evaluationanddeclaration.entity.HerbRatingDetail;
import org.csu.evaluationanddeclaration.mapper.EvaluationDetailMapper;
import org.csu.evaluationanddeclaration.mapper.HerbEvaluationMapper;
import org.csu.evaluationanddeclaration.mapper.HerbRatingDetailMapper;
import org.csu.evaluationanddeclaration.mapper.HerbRatingMapper;
import org.csu.evaluationanddeclaration.service.Impl.HerbEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HerbEvaluationServiceImplTest {

    @Mock
    private HerbEvaluationMapper herbEvaluationMapper;
    @Mock
    private EvaluationDetailMapper evaluationDetailMapper;
    @Mock
    private HerbRatingMapper herbRatingMapper;
    @Mock
    private HerbRatingDetailMapper herbRatingDetailMapper;

    @InjectMocks
    private HerbEvaluationServiceImpl service;

    @Test
    void getHerbEvaluationsByScoreRejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.GetHerbEvaluationsByScore(10f, 5f));
        verifyNoInteractions(herbEvaluationMapper);
    }

    @Test
    void getHerbEvaluationsByScoreDelegatesToMapper() {
        HerbEvaluation evaluation = new HerbEvaluation();
        when(herbEvaluationMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(evaluation));

        List<HerbEvaluation> results = service.GetHerbEvaluationsByScore(1f, 3f);

        assertEquals(1, results.size());
        assertSame(evaluation, results.getFirst());
        verify(herbEvaluationMapper).selectList(any(QueryWrapper.class));
    }

    @Test
    void getHerbEvaluationsByDateRejectsInvalidRange() {
        Date later = new Date(2_000);
        Date earlier = new Date(1_000);

        assertThrows(IllegalArgumentException.class, () -> service.GetHerbEvaluationsByDate(later, earlier));
        verifyNoInteractions(herbEvaluationMapper);
    }

    @Test
    void addHerbEvaluationRejectsNullOrInvalidHerbId() {
        HerbEvaluation invalid = new HerbEvaluation();
        invalid.setHerbId(0L);

        assertFalse(service.AddHerbEvaluation(null));
        assertFalse(service.AddHerbEvaluation(invalid));
        verifyNoInteractions(herbEvaluationMapper);
    }

    @Test
    void addHerbEvaluationPersistsWhenValid() {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setHerbId(5L);

        assertTrue(service.AddHerbEvaluation(evaluation));
        verify(herbEvaluationMapper).insert(evaluation);
    }

    @Test
    void deleteHerbEvaluationHandlesMissingRecord() {
        when(herbEvaluationMapper.selectById(9)).thenReturn(null);

        assertFalse(service.DeleteHerbEvaluation(9));
        verify(herbEvaluationMapper).selectById(9);
        verify(herbEvaluationMapper, never()).deleteById(anyInt());
    }

    @Test
    void deleteHerbEvaluationRemovesExistingRecord() {
        HerbEvaluation evaluation = new HerbEvaluation();
        when(herbEvaluationMapper.selectById(3)).thenReturn(evaluation);

        assertTrue(service.DeleteHerbEvaluation(3));
        verify(herbEvaluationMapper).deleteById(3);
    }

    @Test
    void updateHerbEvaluationRejectsInvalidPayload() {
        HerbEvaluation invalid = new HerbEvaluation();
        invalid.setEvaluationId(0L);

        assertFalse(service.UpdateHerbEvaluation(null));
        assertFalse(service.UpdateHerbEvaluation(invalid));
        verify(herbEvaluationMapper, never()).updateById(any(HerbEvaluation.class));
    }

    @Test
    void updateHerbEvaluationDelegatesWhenValid() {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setEvaluationId(7L);

        assertTrue(service.UpdateHerbEvaluation(evaluation));
        verify(herbEvaluationMapper).updateById(evaluation);
    }

    @Test
    void addRatingDetailGuardsAgainstNull() {
        assertFalse(service.AddRatingDetail(null));
        verifyNoInteractions(herbRatingDetailMapper);
    }

    @Test
    void addRatingDetailPersistsWhenValid() {
        HerbRatingDetail detail = new HerbRatingDetail();

        assertTrue(service.AddRatingDetail(detail));
        verify(herbRatingDetailMapper).insert(detail);
    }

    @Test
    void updateRatingDetailValidatesInput() {
        assertFalse(service.UpdateRatingDetail(null));
        verify(herbRatingDetailMapper, never()).updateById(any(HerbRatingDetail.class));
    }

    @Test
    void updateRatingDetailUpdatesWhenValid() {
        HerbRatingDetail detail = new HerbRatingDetail();

        assertTrue(service.UpdateRatingDetail(detail));
        verify(herbRatingDetailMapper).updateById(detail);
    }

    @Test
    void getHerbEvaluationScoreDefaultsToZeroWhenNotFound() {
        when(herbEvaluationMapper.selectById(4)).thenReturn(null);

        assertEquals(0.0f, service.GetHerbEvaluationScore(4));
    }

    @Test
    void getHerbEvaluationScoreReturnsStoredScore() {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setTotalScore(8.5f);
        when(herbEvaluationMapper.selectById(11)).thenReturn(evaluation);

        assertEquals(8.5f, service.GetHerbEvaluationScore(11));
    }

    @Test
    void isHerbExitUsesMapper() {
        when(herbEvaluationMapper.selectById(6)).thenReturn(null).thenReturn(new HerbEvaluation());

        assertFalse(service.IsHerbExit(6));
        assertTrue(service.IsHerbExit(6));
        verify(herbEvaluationMapper, times(2)).selectById(6);
    }

    @Test
    void getAllEvaluationDetailsDelegatesToMapper() {
        when(evaluationDetailMapper.selectListByEvaluationId(2)).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), service.GetAllEvaluationDetails(2));
        verify(evaluationDetailMapper).selectListByEvaluationId(2);
    }

    @Test
    void addRatingRejectsNull() {
        assertFalse(service.AddRating(null));
        verifyNoInteractions(herbRatingMapper);
    }

    @Test
    void addRatingInsertsWhenValid() {
        HerbRating rating = new HerbRating();

        assertTrue(service.AddRating(rating));
        verify(herbRatingMapper).insert(rating);
    }
}
