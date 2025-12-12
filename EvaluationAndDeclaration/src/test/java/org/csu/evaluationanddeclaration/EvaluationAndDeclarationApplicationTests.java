package org.csu.evaluationanddeclaration;

import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.csu.evaluationanddeclaration.mapper.EvaluationDetailMapper;
import org.csu.evaluationanddeclaration.service.HerbEvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EvaluationAndDeclarationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private HerbEvaluationService herbEvaluationService;

    @MockBean
    private EvaluationDetailMapper evaluationDetailMapper;

    @Test
    public void testGetAllEvaluationDetails() {
        // 假设已知一个有效的 evaluationId
        int evaluationId = 1;

        EvaluationDetail detail = new EvaluationDetail();
        detail.setEvaluationId((long) evaluationId);
        List<EvaluationDetail> mockedDetails = List.of(detail);
        when(evaluationDetailMapper.selectListByEvaluationId(evaluationId)).thenReturn(mockedDetails);

        List<EvaluationDetail> details = herbEvaluationService.GetAllEvaluationDetails(evaluationId);

        assertNotNull(details, "返回的评价明细列表不应为 null");
        assertEquals(mockedDetails, details, "评价明细结果应与预期一致");
        verify(evaluationDetailMapper).selectListByEvaluationId(evaluationId);

    }

}
