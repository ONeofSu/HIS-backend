# Stub（插桩）测试指南

## 一、Stub（插桩）的好处

### 1.1 核心优势

#### ✅ 1. **批量操作自然支持**
- **问题**：Mock测试中，批量获取用户信息等操作设置复杂
- **Stub解决**：Stub实现自然支持批量操作
- **示例**：
```java
// Mock方式：需要手动构建Map
Map<Integer, UserSimpleVO> userMap = new HashMap<>();
userMap.put(100, user1);
userMap.put(200, user2);
when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(userMap);

// Stub方式：自动处理批量请求
userFeignClientStub.addUser(100, "user1", "...", false);
userFeignClientStub.addUser(200, "user2", "...", false);
// 批量获取时，自动返回所有已添加的用户
```

#### ✅ 2. **数据一致性保证**
- **问题**：Mock测试中，不同方法返回的数据可能不一致
- **Stub解决**：Stub实现维护统一的数据状态
- **示例**：
```java
// Stub实现中，用户数据是统一的
userFeignClientStub.addUser(100, "testUser", "...", false);
// isUserExist(100) 返回 true
// isUserAdmin(100) 返回 false
// getUserSimpleInfoBatch([100]) 返回包含该用户的Map
// 数据自动保持一致
```

#### ✅ 3. **Token映射管理**
- **问题**：Mock测试中，Token到用户ID的映射需要手动管理
- **Stub解决**：Stub实现提供了Token映射管理功能
- **示例**：
```java
// Stub方式：统一管理Token映射
userFeignClientStub.addUser(100, "user1", "...", false);
userFeignClientStub.setTokenMapping("Bearer token-100", 100);
// getUserIdByToken("Bearer token-100") 自动返回 100
```

#### ✅ 4. **代码可读性更好**
- **问题**：Mock测试需要大量的`when().thenReturn()`设置
- **Stub解决**：测试代码更简洁，意图更清晰
- **对比**：
```java
// Mock方式：代码冗长
when(userFeignClient.getUserIdByToken("Bearer token")).thenReturn(100);
when(userFeignClient.isUserExist(100)).thenReturn(true);
when(courseFeignClient.isCourseExist(1)).thenReturn(true);
when(herbFeignClient.isHerbExist(1)).thenReturn(true);

// Stub方式：简洁明了
userFeignClientStub.addUser(100, "user1", "...", false);
userFeignClientStub.setTokenMapping("Bearer token", 100);
courseFeignClientStub.addCourse(1);
herbFeignClientStub.addHerb(1);
```

## 二、如何测试看效果

### 2.1 运行Stub测试

#### 方法1：运行所有Stub测试
```bash
# 进入HisComment目录
cd HisComment

# 运行所有Stub集成测试
mvn test -Dtest="*StubIT"
```

#### 方法2：运行特定的Stub测试类
```bash
# 运行CommentControllerStubIT
mvn test -Dtest="CommentControllerStubIT"
```

#### 方法3：运行特定的测试方法
```bash
# 运行发布评论的Stub测试
mvn test -Dtest="CommentControllerStubIT#testAddComment_Course_WithStub"
```

### 2.2 对比测试效果

#### 步骤1：运行Stub测试
```bash
cd HisComment
mvn test -Dtest="CommentControllerStubIT" -X
```

**预期输出**：
```
[INFO] Running org.csu.hiscomment.integration.CommentControllerStubIT
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.2 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

#### 步骤2：运行Mock测试（对比）
```bash
mvn test -Dtest="CommentControllerIT" -X
```

**预期输出**：
```
[INFO] Running org.csu.hiscomment.integration.CommentControllerIT
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.8 s
```

### 2.3 查看测试报告

```bash
# 生成测试报告
mvn surefire-report:report

# 查看报告
# 报告位置：target/site/surefire-report.html
```

### 2.4 实际运行示例

#### 示例1：测试发布评论功能

**运行命令**：
```bash
cd HisComment
mvn test -Dtest="CommentControllerStubIT#testAddComment_Course_WithStub"
```

**查看输出**：
- ✅ 测试通过：说明Stub正确模拟了用户和课程验证
- ✅ 数据一致性：Stub自动维护了数据的一致性

#### 示例2：测试批量获取用户信息

**运行命令**：
```bash
mvn test -Dtest="CommentControllerStubIT#testListComments_WithStub"
```

**查看输出**：
- ✅ 测试通过：说明Stub正确支持了批量获取用户信息
- ✅ 批量操作：Stub自动处理批量请求，返回所有已添加的用户

#### 示例3：测试敏感词过滤

**运行命令**：
```bash
mvn test -Dtest="CommentControllerStubIT#testAddComment_WithSensitiveWords_WithStub"
```

**查看输出**：
- ✅ 测试通过：说明Stub不影响敏感词过滤功能
- ✅ 功能验证：验证了完整的评论发布流程（包括敏感词过滤）

## 三、Stub vs Mock 实际对比

### 3.1 批量操作对比

#### 场景：获取评论列表（需要批量获取用户信息）

**Mock方式**：
```java
// 需要手动构建Map，设置复杂
Map<Integer, UserSimpleVO> userMap = new HashMap<>();
UserSimpleVO user1 = new UserSimpleVO();
user1.setId(100);
user1.setUsername("user1");
userMap.put(100, user1);
// ... 更多用户
when(userFeignClient.getUserSimpleInfoBatch(anyList())).thenReturn(userMap);
```

**Stub方式**：
```java
// 一次设置，批量操作自动支持
userFeignClientStub.addUser(100, "user1", "...", false);
userFeignClientStub.addUser(200, "user2", "...", false);
// 批量获取时，自动返回所有已添加的用户
```

### 3.2 执行时间对比

| 测试类型 | 单个测试用例 | 完整测试套件 |
|---------|------------|-------------|
| Stub测试 | ~60ms | ~600ms (6个用例) |
| Mock测试 | ~40ms | ~400ms (6个用例) |
| 真实依赖测试 | ~250ms | ~6s (需要外部服务) |

### 3.3 代码复杂度对比

| 操作类型 | Stub方式 | Mock方式 |
|---------|---------|---------|
| 单个用户操作 | 1行代码 | 1-2行代码 |
| 批量用户操作 | 1行代码 | 5-10行代码 |
| Token映射 | 1行代码 | 1行代码 |
| 数据一致性 | 自动保证 | 需要手动维护 |

## 四、快速开始

### 4.1 运行第一个Stub测试

```bash
# 1. 进入HisComment目录
cd HisComment

# 2. 确保Docker Desktop运行中（集成测试需要）
docker ps

# 3. 运行Stub测试
mvn test -Dtest="CommentControllerStubIT#testAddComment_Course_WithStub"

# 4. 查看测试结果
# 应该看到：Tests run: 1, Failures: 0, Errors: 0
```

### 4.2 查看Stub实现代码

```bash
# 查看UserFeignClientStub实现
cat src/test/java/org/csu/hiscomment/stub/UserFeignClientStub.java

# 查看如何使用Stub
cat src/test/java/org/csu/hiscomment/integration/CommentControllerStubIT.java
```

### 4.3 修改Stub数据看效果

编辑 `UserFeignClientStub.java`，修改初始化数据：
```java
private void initTestData() {
    // 修改这里的测试数据
    UserSimpleVO user1 = new UserSimpleVO();
    user1.setId(100);
    user1.setUsername("修改后的用户名"); // 修改这里
    // ...
}
```

然后重新运行测试，查看效果变化。

---

**文档生成时间**：2025-11-20  
**适用模块**：HisComment  
**测试框架**：JUnit 5 + Testcontainers + Stub实现

