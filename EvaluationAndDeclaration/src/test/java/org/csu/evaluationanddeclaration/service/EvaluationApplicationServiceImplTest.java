package org.csu.evaluationanddeclaration.service;

import org.csu.evaluationanddeclaration.entity.EvaluationApplication;
import org.csu.evaluationanddeclaration.entity.HerbEvaluation;
import org.csu.evaluationanddeclaration.mapper.EvaluationApplicationMapper;
import org.csu.evaluationanddeclaration.mapper.HerbEvaluationMapper;
import org.csu.evaluationanddeclaration.service.Impl.EvaluationApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * EvaluationApplicationServiceImpl 类的单元测试
 * 
 * 该测试类使用 Mockito 框架对 EvaluationApplicationServiceImpl 进行单元测试，
 * 主要测试服务层的各种业务逻辑，包括获取、添加、删除和更新申请等功能。
 * 
 * Mockito 是一个流行的 Java 单元测试框架，它允许创建和配置模拟对象(Mock Objects)。
 * 使用 Mockito 可以隔离被测试的类，避免对数据库等外部依赖的直接调用，使测试更加高效和可靠。
 */
@ExtendWith(MockitoExtension.class)
class EvaluationApplicationServiceImplTest {

    /**
     * 使用 @Mock 注解创建 HerbEvaluationMapper 的模拟对象
     * 
     * @Mock 的作用：
     * 1. 创建一个 HerbEvaluationMapper 接口的模拟实现
     * 2. 允许我们预定义这个对象的方法行为（如 when(...).thenReturn(...)）
     * 3. 可以验证方法调用（如 verify(...)）
     * 4. 避免真实访问数据库，提高测试速度和可靠性
     */
    @Mock
    private HerbEvaluationMapper herbEvaluationMapper;

    /**
     * 使用 @Mock 注解创建 EvaluationApplicationMapper 的模拟对象
     * 
     * @Mock 的作用：
     * 1. 创建一个 EvaluationApplicationMapper 接口的模拟实现
     * 2. 允许我们预定义这个对象的方法行为（如 when(...).thenReturn(...)）
     * 3. 可以验证方法调用（如 verify(...)）
     * 4. 避免真实访问数据库，提高测试速度和可靠性
     */
    @Mock
    private EvaluationApplicationMapper evaluationApplicationMapper;

    /**
     * 使用 @InjectMocks 注解创建 EvaluationApplicationServiceImpl 的实例
     * 
     * @InjectMocks 的作用：
     * 1. 创建 EvaluationApplicationServiceImpl 的实例
     * 2. 自动将标记为 @Mock 的对象注入到该实例中
     * 3. 实现了依赖注入的自动化，避免手动设置依赖关系
     * 4. 确保被测试的类使用的是模拟对象而不是真实对象
     */
    @InjectMocks
    private EvaluationApplicationServiceImpl service;

    /**
     * 测试获取所有申请的方法
     * 验证服务能否正确调用 Mapper 并返回预期结果
     */
    @Test
    void getAllApplicationsReturnsMapperResults() {
        EvaluationApplication first = new EvaluationApplication();
        first.setApplicationId(1L);
        EvaluationApplication second = new EvaluationApplication();
        second.setApplicationId(2L);
        List<EvaluationApplication> expected = List.of(first, second);
        when(evaluationApplicationMapper.selectList(null)).thenReturn(expected);

        List<EvaluationApplication> results = service.GetAllApplications();

        assertEquals(expected, results);
        verify(evaluationApplicationMapper).selectList(null);
    }

    @Test
    void getApplicationByIdReturnsMapperResult() {
        EvaluationApplication application = new EvaluationApplication();
        when(evaluationApplicationMapper.selectById(5)).thenReturn(application);

        assertSame(application, service.GetApplicationById(5));
        verify(evaluationApplicationMapper).selectById(5);
    }

    @Test
    void getApplicationStateReturnsValueWhenPresent() {
        EvaluationApplication application = new EvaluationApplication();
        application.setApplicationState("pending");
        when(evaluationApplicationMapper.selectById(6)).thenReturn(application);

        assertEquals("pending", service.GetApplicationState(6));
        verify(evaluationApplicationMapper, atLeastOnce()).selectById(6);
    }

    @Test
    void getApplicationStateReturnsNullWhenMissing() {
        when(evaluationApplicationMapper.selectById(7)).thenReturn(null);

        assertNull(service.GetApplicationState(7));
        verify(evaluationApplicationMapper, atLeastOnce()).selectById(7);
    }

    @Test
    void getApplicationByEvaluationIdQueriesByColumn() {
        EvaluationApplication application = new EvaluationApplication();
        when(evaluationApplicationMapper.selectOne(any())).thenReturn(application);

        assertSame(application, service.GetApplicationByEvaluationId(9));
        verify(evaluationApplicationMapper).selectOne(any());
    }

    /**
     * 测试通过ID获取草药评估信息的方法
     * 验证当申请存在时，能否正确获取关联的草药评估信息
     */
    @Test
    void getEvaluationFetchesHerbEvaluationUsingMapper() {
        EvaluationApplication application = new EvaluationApplication();
        application.setApplicationId(9L);
        application.setEvaluationId(42L);
        when(evaluationApplicationMapper.selectById(3)).thenReturn(application);
        HerbEvaluation evaluation = new HerbEvaluation();
        evaluation.setEvaluationId(9L);
        when(herbEvaluationMapper.getEvaluationById(9)).thenReturn(evaluation);

        HerbEvaluation result = service.GetEvaluation(3);

        assertSame(evaluation, result);
        verify(evaluationApplicationMapper, atLeastOnce()).selectById(3);
        verify(herbEvaluationMapper).getEvaluationById(9);
    }

    /**
     * 测试当申请不存在时获取草药评估信息的方法
     * 验证当申请不存在时，方法应返回 null
     */
    @Test
    void getEvaluationReturnsNullWhenApplicationMissing() {
        when(evaluationApplicationMapper.selectById(7)).thenReturn(null);
        when(herbEvaluationMapper.getEvaluationById(0)).thenReturn(null);

        HerbEvaluation result = service.GetEvaluation(7);

        assertNull(result);
        verify(evaluationApplicationMapper, atLeastOnce()).selectById(7);
        verify(herbEvaluationMapper).getEvaluationById(0);
    }

    /**
     * 测试添加申请时传入空值的情况
     * 验证当传入 null 时，方法应返回 false 并且不调用任何 Mapper 方法
     */
    @Test
    void addApplicationReturnsFalseWhenInputNull() {
        assertFalse(service.AddApplication(null));
        verifyNoInteractions(evaluationApplicationMapper, herbEvaluationMapper);
    }

    /**
     * 测试正常添加申请的情况
     * 验证当传入有效申请对象时，方法应返回 true 并调用相应的 Mapper 方法
     */
    @Test
    void addApplicationDelegatesToMapper() {
        EvaluationApplication application = new EvaluationApplication();

        boolean saved = service.AddApplication(application);

        assertTrue(saved);
        verify(evaluationApplicationMapper).insert(application);
        verifyNoInteractions(herbEvaluationMapper);
    }

    /**
     * 测试删除不存在的申请
     * 验证当尝试删除不存在的申请时，方法应返回 false 并且不会调用删除方法
     */
    @Test
    void deleteApplicationReturnsFalseWhenMissing() {
        when(evaluationApplicationMapper.selectById(4)).thenReturn(null);

        boolean deleted = service.DeleteApplication(4);

        assertFalse(deleted);
        verify(evaluationApplicationMapper).selectById(4);
        verify(evaluationApplicationMapper, never()).deleteById(anyInt());
    }

    /**
     * 测试删除存在的申请
     * 验证当申请存在时，能成功删除并返回 true
     */
    @Test
    void deleteApplicationRemovesExistingApplication() {
        EvaluationApplication application = new EvaluationApplication();
        when(evaluationApplicationMapper.selectById(5)).thenReturn(application);

        boolean deleted = service.DeleteApplication(5);

        assertTrue(deleted);
        verify(evaluationApplicationMapper).selectById(5);
        verify(evaluationApplicationMapper).deleteById(5);
    }

    /**
     * 测试更新申请时的无效输入处理
     * 验证当传入 null 或无效的申请对象时，方法应返回 false 并且不调用更新方法
     */
    @Test
    void updateApplicationRejectsInvalidPayload() {
        EvaluationApplication missingEvaluationId = new EvaluationApplication();
        missingEvaluationId.setEvaluationId(0L);

        assertFalse(service.UpdateApplication(null));
        assertFalse(service.UpdateApplication(missingEvaluationId));
        verify(evaluationApplicationMapper, never()).updateById(org.mockito.Mockito.<EvaluationApplication>any());
    }

    /**
     * 测试正常更新申请的情况
     * 验证当传入有效的申请对象时，方法应返回 true 并调用相应的更新方法
     */
    @Test
    void updateApplicationDelegatesToMapper() {
        EvaluationApplication application = new EvaluationApplication();
        application.setEvaluationId(11L);

        boolean updated = service.UpdateApplication(application);

        assertTrue(updated);
        verify(evaluationApplicationMapper).updateById(application);
    }

    /**
     * 测试检查申请是否存在的方法
     * 验证该方法能正确判断申请是否存在
     */
    @Test
    void isHApplicationExitUsesMapper() {
        when(evaluationApplicationMapper.selectById(12)).thenReturn(null).thenReturn(new EvaluationApplication());

        assertFalse(service.IsHApplicationExit(12));
        assertTrue(service.IsHApplicationExit(12));
        verify(evaluationApplicationMapper, times(2)).selectById(12);
    }
}
