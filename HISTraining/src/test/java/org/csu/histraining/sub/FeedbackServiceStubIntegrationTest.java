package org.csu.histraining.sub;

import org.csu.histraining.DTO.FeedbackDTO;
import org.csu.histraining.VO.FeedbackVO;
import org.csu.histraining.entity.Feedback;
import org.csu.histraining.service.impl.FeedbackServiceImpl;
//import org.csu.histraining.sub.FeedbackMapperStub;
//import org.csu.histraining.sub.MaterialServiceStub;
//import org.csu.histraining.sub.UserServiceStub;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * FeedbackServiceImpl 桩集成测试
 * 使用 Stub 模拟所有外部依赖，验证业务逻辑正确性
 */
class FeedbackServiceStubIntegrationTest {

    private FeedbackServiceImpl feedbackService;
    private FeedbackMapperStub feedbackMapperStub;
    private MaterialServiceStub materialServiceStub;
    private UserServiceStub userServiceStub;

    @BeforeEach
    void setUp() {
        // 初始化所有 Stub
        feedbackMapperStub = new FeedbackMapperStub();
        materialServiceStub = new MaterialServiceStub();
        userServiceStub = new UserServiceStub();

        // 使用 Stub 组装被测服务
        feedbackService = new FeedbackServiceImpl(
                feedbackMapperStub,
                materialServiceStub,
                userServiceStub
        );
    }

    @Test
    @DisplayName("测试添加反馈 - 正常流程")
    void testAddFeedback_Success() {
        // 准备测试数据
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1); // Stub 中存在的教材 ID
        feedback.setUserId(1);      // Stub 中存在的用户 ID
        feedback.setContent("这是一条测试反馈");
        feedback.setRating(5);

        // 执行业务方法
        int feedbackId = feedbackService.addFeedback(feedback);

        // 验证结果
        assertTrue(feedbackId > 0, "反馈添加应返回正数 ID");

        // 验证数据是否正确保存
        Feedback saved = feedbackService.getFeedbackById(feedbackId);
        assertNotNull(saved);
        assertEquals("这是一条测试反馈", saved.getContent());
        assertEquals(1, saved.getMaterialId());
        assertEquals(1, saved.getUserId());
    }

    @Test
    @DisplayName("测试添加反馈 - 教材不存在")
    void testAddFeedback_InvalidMaterial() {
        Feedback feedback = new Feedback();
        feedback.setMaterialId(999); // 不存在的教材 ID
        feedback.setUserId(1);
        feedback.setContent("测试内容");

        int result = feedbackService.addFeedback(feedback);

        assertEquals(-1, result, "教材不存在时应返回 -1");
    }

    @Test
    @DisplayName("测试添加反馈 - 用户不存在")
    void testAddFeedback_InvalidUser() {
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(999); // 不存在的用户 ID
        feedback.setContent("测试内容");

        int result = feedbackService.addFeedback(feedback);

        assertEquals(-1, result, "用户不存在时应返回 -1");
    }

    @Test
    @DisplayName("测试添加反馈 - 内容为空")
    void testAddFeedback_EmptyContent() {
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(1);
        feedback.setContent(""); // 空内容

        int result = feedbackService.addFeedback(feedback);

        assertEquals(-1, result, "内容为空时应返回 -1");
    }

    @Test
    @DisplayName("测试 DTO 转实体")
    void testTransferFeedbackDTOToFeedback() {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setMaterialId(2);
        dto.setContent("DTO 转换测试");
        dto.setRating(4);

        Feedback feedback = feedbackService.transferFeedbackDTOToFeedback(dto, 2);

        assertNotNull(feedback);
        assertEquals(2, feedback.getMaterialId());
        assertEquals(2, feedback.getUserId());
        assertEquals("DTO 转换测试", feedback.getContent());
        assertEquals(4, feedback.getRating());
        assertNotNull(feedback.getTime());
    }

    @Test
    @DisplayName("测试实体转 VO - 验证外部依赖调用")
    void testTransferToFeedbackVO() {
        // 先添加一条反馈
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(2);
        feedback.setContent("VO 转换测试");
        feedback.setRating(5);

        int id = feedbackService.addFeedback(feedback);
        Feedback saved = feedbackService.getFeedbackById(id);

        // 转换为 VO
        FeedbackVO vo = feedbackService.transferToFeedbackVO(saved);

        // 验证基本属性
        assertNotNull(vo);
        assertEquals(id, vo.getId());
        assertEquals("VO 转换测试", vo.getContent());

        // 关键验证：UserName 应来自 UserServiceStub
        assertEquals("测试用户B", vo.getUserName(),
                "用户名应从 UserServiceStub 获取");
    }

    @Test
    @DisplayName("测试查询用户所有反馈")
    void testGetFeedbackByUserId() {
        // 添加多条反馈
        for (int i = 0; i < 3; i++) {
            Feedback feedback = new Feedback();
            feedback.setMaterialId(1);
            feedback.setUserId(1);
            feedback.setContent("反馈内容-" + i);
            feedback.setRating(4);
            feedbackService.addFeedback(feedback);
        }

        // 查询用户反馈
        List<Feedback> feedbacks = feedbackService.getFeedbackByUserId(1);

        assertEquals(3, feedbacks.size(), "应返回 3 条反馈");
    }

    @Test
    @DisplayName("测试删除反馈")
    void testDeleteFeedback() {
        // 先添加
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(1);
        feedback.setContent("待删除的反馈");
        int id = feedbackService.addFeedback(feedback);

        // 确认存在
        assertTrue(feedbackService.isFeedbackIdExist(id));

        // 执行删除
        boolean deleted = feedbackService.deleteFeedback(id);
        assertTrue(deleted, "删除应成功");

        // 验证已删除
        assertFalse(feedbackService.isFeedbackIdExist(id),
                "删除后反馈应不存在");
    }

    @Test
    @DisplayName("测试转换反馈列表为 VO 列表")
    void testTransferToFeedbackVOList() {
        // 添加测试数据
        for (int i = 1; i <= 2; i++) {
            Feedback feedback = new Feedback();
            feedback.setMaterialId(1);
            feedback.setUserId(i);
            feedback.setContent("批量转换测试-" + i);
            feedbackService.addFeedback(feedback);
        }

        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        List<FeedbackVO> vos = feedbackService.transferToFeedbackVOList(feedbacks);

        assertEquals(feedbacks.size(), vos.size());
        // 验证用户名正确填充
        assertTrue(vos.stream()
                        .allMatch(vo -> vo.getUserName() != null && !vo.getUserName().isEmpty()),
                "所有 VO 应包含用户名");
    }
}
