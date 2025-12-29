package org.csu.histraining.localIntegrationTest;

import org.csu.histraining.DTO.FeedbackDTO;
import org.csu.histraining.VO.FeedbackVO;
import org.csu.histraining.entity.Feedback;
import org.csu.histraining.entity.Material;
import org.csu.histraining.mapper.FeedbackMapper;
import org.csu.histraining.mapper.MaterialMapper;
import org.csu.histraining.service.FeedbackService;
import org.csu.histraining.service.MaterialService;
import org.csu.histraining.service.UserService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FeedbackService 本地集成测试
 * 使用真实的 Spring 容器、真实数据库、真实依赖进行联调测试
 */
@SpringBootTest
@ActiveProfiles("test")  // 使用测试配置文件
@Transactional  // 每个测试方法执行后自动回滚,保持数据库清洁
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FeedbackServiceLocalIntegrationTest {

    @Autowired
    private FeedbackService feedbackService;  // 真实 Service

    @Autowired
    private MaterialService materialService;  // 真实依赖

    @Autowired
    private UserService userService;  // 真实依赖

    @Autowired
    private FeedbackMapper feedbackMapper;  // 真实 Mapper

    @Autowired
    private MaterialMapper materialMapper;

    private static int testFeedbackId;

    @BeforeEach
    void setUp() {
        // 可以在这里插入测试所需的基础数据
        // 由于使用了 @Transactional,每次测试后会自动回滚
    }

    @Test
    @Order(1)
    @DisplayName("本地集成测试 - 添加反馈(正常流程)")
    void testAddFeedback_WithRealDependencies() {
        // 1. 验证前置条件:教材和用户在数据库中存在
        assertTrue(materialService.isMaterialIdExist(1),
                "测试教材应存在于数据库");
        assertTrue(userService.isUserIdExist(1),
                "测试用户应存在于数据库");

        // 2. 构造反馈数据
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(1);
        feedback.setContent("这是一条本地集成测试的真实反馈内容");
        feedback.setRating(5);
        feedback.setTime(new Timestamp(System.currentTimeMillis()));

        // 3. 调用业务方法(会真实写入数据库)
        int feedbackId = feedbackService.addFeedback(feedback);
        testFeedbackId = feedbackId;

        // 4. 验证返回结果
        assertTrue(feedbackId > 0, "应返回有效的反馈 ID");

        // 5. 从数据库查询验证
        Feedback savedFeedback = feedbackMapper.selectById(feedbackId);
        assertNotNull(savedFeedback, "反馈应成功保存到数据库");
        assertEquals("这是一条本地集成测试的真实反馈内容",
                savedFeedback.getContent());
        assertEquals(1, savedFeedback.getMaterialId());
        assertEquals(1, savedFeedback.getUserId());
        assertEquals(5, savedFeedback.getRating());

        System.out.println("✓ 反馈成功添加,ID: " + feedbackId);
    }

    @Test
    @Order(2)
    @DisplayName("本地集成测试 - 查询反馈并转换 VO")
    void testGetFeedbackAndTransferToVO() {
        // 1. 先添加测试数据
        Feedback feedback = new Feedback();
        feedback.setMaterialId(2);
        feedback.setUserId(2);
        feedback.setContent("测试 VO 转换的反馈");
        feedback.setRating(4);
        feedback.setTime(new Timestamp(System.currentTimeMillis()));

        int feedbackId = feedbackService.addFeedback(feedback);

        // 2. 通过 Service 查询
        Feedback retrieved = feedbackService.getFeedbackById(feedbackId);
        assertNotNull(retrieved, "应能查询到刚添加的反馈");

        // 3. 转换为 VO(会调用 UserService.getUsernameById)
        FeedbackVO vo = feedbackService.transferToFeedbackVO(retrieved);

        // 4. 验证 VO 中的数据完整性
        assertNotNull(vo);
        assertEquals(feedbackId, vo.getId());
        assertEquals("测试 VO 转换的反馈", vo.getContent());
        assertEquals(2, vo.getMaterialId());
        assertEquals(2, vo.getUserId());
        assertEquals(4, vo.getRating());

        // 5. 关键验证:用户名应从真实 UserService 获取
        String expectedUsername = userService.getUsernameById(2);
        assertEquals(expectedUsername, vo.getUserName(),
                "VO 中的用户名应与数据库中的用户名一致");

        // 6. 验证用户名不为空(确实调用了 UserService)
        assertNotNull(vo.getUserName(), "用户名不应为空");
        assertFalse(vo.getUserName().isEmpty(), "用户名不应为空字符串");

        System.out.println("✓ VO 转换成功,用户名: " + vo.getUserName());
    }

    @Test
    @Order(3)
    @DisplayName("本地集成测试 - DTO 转换与保存")
    void testTransferDTOAndSave() {
        // 1. 构造 DTO(模拟前端传入的数据)
        FeedbackDTO dto = new FeedbackDTO();
        dto.setMaterialId(3);
        dto.setContent("通过 DTO 提交的反馈内容");
        dto.setRating(5);

        int userId = 3;

        // 2. DTO 转 Entity
        Feedback feedback = feedbackService.transferFeedbackDTOToFeedback(dto, userId);

        // 3. 验证转换结果
        assertNotNull(feedback);
        assertEquals(3, feedback.getMaterialId());
        assertEquals(3, feedback.getUserId());
        assertEquals("通过 DTO 提交的反馈内容", feedback.getContent());
        assertEquals(5, feedback.getRating());
        assertNotNull(feedback.getTime(), "时间戳应自动设置");

        // 4. 保存到数据库
        int feedbackId = feedbackService.addFeedback(feedback);
        assertTrue(feedbackId > 0);

        // 5. 验证数据库中的记录
        Feedback saved = feedbackMapper.selectById(feedbackId);
        assertNotNull(saved);
        assertEquals(dto.getContent(), saved.getContent());

        System.out.println("✓ DTO 转换并保存成功");
    }

    @Test
    @Order(4)
    @DisplayName("本地集成测试 - 教材不存在时的验证")
    void testAddFeedback_InvalidMaterial() {
        Feedback feedback = new Feedback();
        feedback.setMaterialId(9999);  // 不存在的教材 ID
        feedback.setUserId(1);
        feedback.setContent("测试教材验证");
        feedback.setRating(4);

        // MaterialService 会返回 false
        int result = feedbackService.addFeedback(feedback);

        assertEquals(-1, result, "教材不存在时应返回 -1");

        System.out.println("✓ 教材验证逻辑正常工作");
    }

    @Test
    @Order(5)
    @DisplayName("本地集成测试 - 用户不存在时的验证")
    void testAddFeedback_InvalidUser() {
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(9999);  // 不存在的用户 ID
        feedback.setContent("测试用户验证");
        feedback.setRating(4);

        // UserService 会返回 false
        int result = feedbackService.addFeedback(feedback);

        assertEquals(-1, result, "用户不存在时应返回 -1");

        System.out.println("✓ 用户验证逻辑正常工作");
    }

    @Test
    @Order(6)
    @DisplayName("本地集成测试 - 按用户查询反馈列表")
    void testGetFeedbackByUserId() {
        int userId = 1;

        // 1. 为指定用户添加多条反馈
        for (int i = 0; i < 3; i++) {
            Feedback feedback = new Feedback();
            feedback.setMaterialId(1);
            feedback.setUserId(userId);
            feedback.setContent("用户1的反馈-" + (i + 1));
            feedback.setRating(4 + i % 2);
            feedback.setTime(new Timestamp(System.currentTimeMillis()));
            feedbackService.addFeedback(feedback);
        }

        // 2. 查询该用户的所有反馈
        List<Feedback> userFeedbacks = feedbackService.getFeedbackByUserId(userId);

        // 3. 验证结果
        assertNotNull(userFeedbacks);
        assertTrue(userFeedbacks.size() >= 3,
                "应至少查询到3条反馈(可能包含之前测试添加的)");

        // 4. 验证所有反馈都属于该用户
        assertTrue(userFeedbacks.stream()
                        .allMatch(f -> f.getUserId() == userId),
                "所有反馈应属于指定用户");

        System.out.println("✓ 查询到用户 " + userId + " 的 " +
                userFeedbacks.size() + " 条反馈");
    }

    @Test
    @Order(7)
    @DisplayName("本地集成测试 - 批量转换 VO 列表")
    void testTransferToFeedbackVOList() {
        // 1. 查询所有反馈
        List<Feedback> allFeedbacks = feedbackService.getAllFeedback();
        assertFalse(allFeedbacks.isEmpty(), "应有测试数据");

        // 2. 批量转换为 VO
        List<FeedbackVO> vos = feedbackService.transferToFeedbackVOList(allFeedbacks);

        // 3. 验证转换结果
        assertEquals(allFeedbacks.size(), vos.size(),
                "VO 列表大小应与原列表一致");

        // 4. 验证每个 VO 的用户名都正确填充
        for (FeedbackVO vo : vos) {
            assertNotNull(vo.getUserName(), "用户名不应为空");

            // 验证用户名确实来自数据库
            String expectedUsername = userService.getUsernameById(vo.getUserId());
            assertEquals(expectedUsername, vo.getUserName(),
                    "VO 中的用户名应与数据库一致");
        }

        System.out.println("✓ 成功转换 " + vos.size() + " 个 VO,所有用户名正确填充");
    }

    @Test
    @Order(8)
    @DisplayName("本地集成测试 - 删除反馈")
    void testDeleteFeedback() {
        // 1. 先添加一条反馈
        Feedback feedback = new Feedback();
        feedback.setMaterialId(1);
        feedback.setUserId(1);
        feedback.setContent("待删除的反馈");
        feedback.setRating(3);
        feedback.setTime(new Timestamp(System.currentTimeMillis()));

        int feedbackId = feedbackService.addFeedback(feedback);

        // 2. 确认存在
        assertTrue(feedbackService.isFeedbackIdExist(feedbackId),
                "反馈应存在于数据库");

        // 3. 执行删除
        boolean deleted = feedbackService.deleteFeedback(feedbackId);
        assertTrue(deleted, "删除操作应成功");

        // 4. 验证已删除
        assertFalse(feedbackService.isFeedbackIdExist(feedbackId),
                "反馈应已从数据库删除");

        // 5. 尝试再次查询应返回 null
        Feedback deletedFeedback = feedbackMapper.selectById(feedbackId);
        assertNull(deletedFeedback, "已删除的反馈应查询不到");

        System.out.println("✓ 反馈删除成功");
    }

    @Test
    @Order(9)
    @DisplayName("本地集成测试 - 跨模块数据一致性验证")
    void testCrossModuleDataConsistency() {
        // 1. 通过 MaterialService 获取教材信息
        Material material = materialService.getMaterialById(1);
        assertNotNull(material, "教材应存在");

        // 2. 添加该教材的反馈
        Feedback feedback = new Feedback();
        feedback.setMaterialId(material.getId());
        feedback.setUserId(1);
        feedback.setContent("跨模块测试反馈");
        feedback.setRating(5);
        feedback.setTime(new Timestamp(System.currentTimeMillis()));

        int feedbackId = feedbackService.addFeedback(feedback);

        // 3. 查询反馈并转换 VO
        Feedback saved = feedbackService.getFeedbackById(feedbackId);
        FeedbackVO vo = feedbackService.transferToFeedbackVO(saved);

        // 4. 验证 VO 中的教材 ID 与 Material 对象一致
        assertEquals(material.getId(), vo.getMaterialId(),
                "VO 中的教材 ID 应与 Material 对象一致");

        // 5. 验证用户信息一致性
        String username = userService.getUsernameById(1);
        assertEquals(username, vo.getUserName(),
                "用户名应跨模块保持一致");

        System.out.println("✓ 跨模块数据一致性验证通过");
        System.out.println("  教材: " + material.getTitle());
        System.out.println("  用户: " + username);
        System.out.println("  反馈: " + vo.getContent());
    }

    @Test
    @Order(10)
    @DisplayName("本地集成测试 - 并发场景模拟")
    void testConcurrentFeedbackSubmission() throws InterruptedException {
        // 模拟多个用户同时提交反馈
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int userId = (i % 3) + 1;  // 使用用户 1-3
            threads[i] = new Thread(() -> {
                Feedback feedback = new Feedback();
                feedback.setMaterialId(1);
                feedback.setUserId(userId);
                feedback.setContent("并发测试反馈-" + Thread.currentThread().getName());
                feedback.setRating(4);
                feedback.setTime(new Timestamp(System.currentTimeMillis()));

                int feedbackId = feedbackService.addFeedback(feedback);
                assertTrue(feedbackId > 0, "并发提交应成功");
            });
            threads[i].start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // 验证所有反馈都已保存
        List<Feedback> allFeedbacks = feedbackService.getAllFeedback();
        assertTrue(allFeedbacks.size() >= threadCount,
                "并发提交的反馈应全部保存");

        System.out.println("✓ 并发场景测试通过,共提交 " + threadCount + " 条反馈");
    }
}
