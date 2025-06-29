package org.csu.evaluationanddeclaration;

import org.csu.evaluationanddeclaration.service.HerbEvaluationService;
import org.csu.evaluationanddeclaration.entity.EvaluationDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EvaluationAndDeclarationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private HerbEvaluationService herbEvaluationService;

    @Test
    public void testGetAllEvaluationDetails() {
        // 假设已知一个有效的 evaluationId
        int evaluationId = 1;

        List<EvaluationDetail> details = herbEvaluationService.GetAllEvaluationDetails(evaluationId);

//        assertNotNull(details, "返回的评价明细列表不应为 null");
//        assertFalse(details.isEmpty(), "评价明细列表不应为空");

        // 打印结果便于观察
        for (EvaluationDetail detail : details) {
            System.out.println(detail);
        }
    }

}
