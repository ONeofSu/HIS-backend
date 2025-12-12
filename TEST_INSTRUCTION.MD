# 测试指南

## 单元测试

验证最小的代码单元是否按照预期工作，针对Service层的方法、工具类。

需要满足隔离性：必须使用Mock框架来模拟外部依赖（如DAO层，其他service, 外部API）。

Mock框架使用Mockito

我们应该假设被测试对象的外部依赖是没有问题的，而只考虑本身是否存在逻辑错误。

应该重点关注业务逻辑的计算是否正确，引入边界条件，验证是否按照预期抛出异常等。

## 集成测试

验证多个组件组合在一起时能否协同工作。

包括内部集成和外部集成。

内部集成是Controller->Service->Dao->Database的调用流；

外部集成是服务与外部系统（数据库、消息队列、缓存或其他微服务）的交互。

对于内部集成，使用SpringBootTest+MockMVC

对于外部集成，使Testcontainers，动态启动一个真实数据库，在Docker容器中运行测试，测完即焚

## 白盒测试

白盒测试是一种方法论，单元测试和集成测试都属于白盒测试

此时我们应该重点关注：
1. 代码覆盖率（行覆盖，分支覆盖）
    - 行覆盖是比较简单的指标，只关心这行代码是否至少被执行过一次，100%的行覆盖可能并不代表测试彻底 
    - 分支覆盖要求所有控制流每个分支（true/false都至少被执行多一次） 
    - 使用JaCoCo生成报告

2. 路径覆盖，这是比较复杂，但是更为彻底的测试，要求一个方法中所有可能的执行路基都要被测试一遍

```java
public boolean canSubmitOrder(User user, Order order) {
    // 路径 1: 非法用户
    if (user == null || !user.isActive()) {
        return false;
    }

    // 路径 2 & 3: 高级会员或大额订单
    if (user.isPremium() || order.getTotalAmount() > 1000) {
        return true;
    }

    // 路径 4 & 5: 库存检查
    if (inventoryService.checkStock(order)) {
        return true;
    }

    return false;
}
```

3. 内部状态：很多方法的价值不在于返回了什么，而是在于改变了什么，我们的目标是验证这些副作用是否预期发生
```java
@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private EmailService emailService;

    public void deactivateUser(Long userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        // 改变 1: 改变 user 对象的内部状态
        user.setActive(false);

        // 改变 2: 改变数据库状态
        userRepo.save(user);

        // 改变 3: 触发外部系统
        emailService.sendDeactivationEmail(user.getEmail());
    }
}
```
```java
@Mock private UserRepository userRepo;
@Mock private EmailService emailService;
@InjectMocks private UserService userService;

@Test
public void testDeactivateUser() {
    // 准备 (Arrange)
    User fakeUser = new User();
    fakeUser.setActive(true); // 初始状态
    fakeUser.setEmail("test@example.com");
    when(userRepo.findById(1L)).thenReturn(Optional.of(fakeUser));

    // 执行 (Act)
    userService.deactivateUser(1L);

    // 断言内部状态 (Assert)

    // 验证 1: user 对象的状态是否在方法内部被修改？
    // 我们需要捕获传递给 save 方法的 user 对象
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    // 验证 2: 数据库交互状态 (save 是否被调用? 传入的参数是否正确?)
    verify(userRepo).save(userCaptor.capture());
    User savedUser = userCaptor.getValue();
    assertFalse(savedUser.isActive()); // 核心断言：内部状态变了！

    // 验证 3: 外部系统交互状态 (EmailService 是否被正确调用?)
    verify(emailService).sendDeactivationEmail("test@example.com");
}
```

