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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest(name = "{2}")
    @MethodSource("validDateRanges")
    void getHerbEvaluationsByDateReturnsListWhenRangeValid(Date start, Date end, String caseId) {
        HerbEvaluation evaluation = new HerbEvaluation();
        when(herbEvaluationMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(evaluation));

        List<HerbEvaluation> results = service.GetHerbEvaluationsByDate(start, end);

        assertEquals(List.of(evaluation), results, caseId + " should return mapper result");
        verify(herbEvaluationMapper).selectList(any(QueryWrapper.class));
    }

    @Test
    void getHerbEvaluationsByDateRejectsInvalidRange() {
        Date start = isoDate("2023-10-05");
        Date end = isoDate("2023-10-01");

        assertThrows(IllegalArgumentException.class, () -> service.GetHerbEvaluationsByDate(start, end));
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

    private static Stream<Arguments> validDateRanges() {
        return Stream.of(
                Arguments.of(null, null, "TC-01"),
                Arguments.of(isoDate("2023-10-01"), null, "TC-02"),
                Arguments.of(isoDate("2023-10-01"), isoDate("2023-10-05"), "TC-03")
        );
    }

    private static Date isoDate(String iso) {
        return Date.from(LocalDate.parse(iso).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    @Test
    void addHerbEvaluationPersistsWhenValid() {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setHerbId(5L);

        assertTrue(service.AddHerbEvaluation(evaluation));
        verify(herbEvaluationMapper).insert(evaluation);
    }

    @Test
    void getAllHerbEvaluationsDelegatesToMapper() {
        when(herbEvaluationMapper.selectList(null)).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), service.GetAllHerbEvaluations());
        verify(herbEvaluationMapper).selectList(null);
    }

    @Test
    void getHerbEvaluationByIdReturnsMapperValue() {
        HerbEvaluation evaluation = new HerbEvaluation();
        when(herbEvaluationMapper.selectById(8)).thenReturn(evaluation);

        assertSame(evaluation, service.GetHerbEvaluationById(8));
        verify(herbEvaluationMapper).selectById(8);
    }

    @Test
    void getHerbEvaluationsByHerbIdFiltersByColumn() {
        HerbEvaluation evaluation = new HerbEvaluation();
        when(herbEvaluationMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(evaluation));

        List<HerbEvaluation> results = service.GetHerbEvaluationsByHerbId(12);

        assertEquals(List.of(evaluation), results);
        verify(herbEvaluationMapper).selectList(any(QueryWrapper.class));
    }

    @Test
    void getHerbEvaluationsByUserIdFiltersByColumn() {
        HerbEvaluation evaluation = new HerbEvaluation();
        when(herbEvaluationMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(evaluation));

        assertEquals(List.of(evaluation), service.GetHerbEvaluationsByUserId(33));
        verify(herbEvaluationMapper).selectList(any(QueryWrapper.class));
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
    void getUserIdReturnsZeroWhenMissing() {
        when(herbEvaluationMapper.selectById(21)).thenReturn(null);

        assertEquals(0, service.GetUserId(21));
    }

    @Test
    void getUserIdReturnsStoredValue() {
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setUserId(31L);
        when(herbEvaluationMapper.selectById(31)).thenReturn(evaluation);

        assertEquals(31, service.GetUserId(31));
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

    @Test
    void deleteRatingHandlesMissingRecord() {
        when(herbRatingMapper.selectById(15)).thenReturn(null);

        assertFalse(service.DeleteRating(15));
        verify(herbRatingMapper).selectById(15);
        verify(herbRatingMapper, never()).deleteById(anyInt());
    }

    @Test
    void deleteRatingRemovesRecordWhenPresent() {
        when(herbRatingMapper.selectById(16)).thenReturn(new HerbRating());

        assertTrue(service.DeleteRating(16));
        verify(herbRatingMapper).deleteById(16);
    }

    @Test
    void updateRatingValidatesNullInput() {
        assertFalse(service.UpdateRating(null));
        verify(herbRatingMapper, never()).updateById(any(HerbRating.class));
    }

    @Test
    void updateRatingPersistsWhenValid() {
        HerbRating rating = new HerbRating();

        assertTrue(service.UpdateRating(rating));
        verify(herbRatingMapper).updateById(rating);
    }

    @Test
    void isRatingExitReflectsMapperPresence() {
        when(herbRatingMapper.selectById(19)).thenReturn(null).thenReturn(new HerbRating());

        assertFalse(service.IsRatingExit(19));
        assertTrue(service.IsRatingExit(19));
        verify(herbRatingMapper, times(2)).selectById(19);
    }

    @Test
    void deleteRatingDetailReturnsFalseWhenMissing() {
        when(herbRatingDetailMapper.selectById(25)).thenReturn(null);

        assertFalse(service.DeleteRatingDetail(25));
        verify(herbRatingDetailMapper).selectById(25);
        verify(herbRatingDetailMapper, never()).deleteById(anyInt());
    }

    @Test
    void deleteRatingDetailDeletesWhenPresent() {
        when(herbRatingDetailMapper.selectById(26)).thenReturn(new HerbRatingDetail());

        assertTrue(service.DeleteRatingDetail(26));
        verify(herbRatingDetailMapper).deleteById(26);
    }

    @Test
    void isRatingDetailExitMirrorsMapperResult() {
        when(herbRatingDetailMapper.selectById(27)).thenReturn(null).thenReturn(new HerbRatingDetail());

        assertFalse(service.IsRatingDetailExit(27));
        assertTrue(service.IsRatingDetailExit(27));
        verify(herbRatingDetailMapper, times(2)).selectById(27);
    }

    @Test
    void addDetailRejectsNullDetail() {
        assertFalse(service.AddDetail(null));
        verifyNoInteractions(evaluationDetailMapper);
    }

    @Test
    void addDetailInsertsWhenValid() {
        EvaluationDetail detail = new EvaluationDetail();

        assertTrue(service.AddDetail(detail));
        verify(evaluationDetailMapper).insert(detail);
    }

    @Test
    void deleteDetailReturnsFalseWhenMissing() {
        when(evaluationDetailMapper.selectById(41)).thenReturn(null);

        assertFalse(service.DeleteDetail(41));
        verify(evaluationDetailMapper).selectById(41);
        verify(evaluationDetailMapper, never()).deleteById(anyInt());
    }

    @Test
    void deleteDetailDeletesWhenPresent() {
        when(evaluationDetailMapper.selectById(42)).thenReturn(new EvaluationDetail());

        assertTrue(service.DeleteDetail(42));
        verify(evaluationDetailMapper).deleteById(42);
    }

    @Test
    void updateDetailValidatesId() {
        EvaluationDetail detail = new EvaluationDetail();
        detail.setEvaluationDetailId(0L);

        assertFalse(service.UpdateDetail(null));
        assertFalse(service.UpdateDetail(detail));
        verify(evaluationDetailMapper, never()).updateById(any(EvaluationDetail.class));
    }

    @Test
    void updateDetailPersistsWhenValid() {
        EvaluationDetail detail = new EvaluationDetail();
        detail.setEvaluationDetailId(7L);

        assertTrue(service.UpdateDetail(detail));
        verify(evaluationDetailMapper).updateById(detail);
    }

    @Test
    void isDetailExitReflectsMapperResult() {
        when(evaluationDetailMapper.selectById(43)).thenReturn(null).thenReturn(new EvaluationDetail());

        assertFalse(service.IsDetailExit(43));
        assertTrue(service.IsDetailExit(43));
        verify(evaluationDetailMapper, times(2)).selectById(43);
    }
}
