# HisComment 模块测试指南

## 📋 目录
- [测试分类说明](#测试分类说明)
- [已实现的测试](#已实现的测试)
- [运行方法](#运行方法)
- [外部条件要求](#外部条件要求)
- [覆盖率报告](#覆盖率报告)
- [故障排查](#故障排查)

---
---

## 运行方法

### 前置条件
确保已进入 `HisComment` 目录：
```bash
cd HisComment
```

### 1. 运行所有测试
```bash
mvn clean test
```
- 运行所有单元测试和集成测试
- 生成 JaCoCo 覆盖率报告
- **需要 Docker Desktop 运行中**

### 2. 只运行单元测试
```bash
mvn test -Dtest="*Test"
```
- 不依赖 Docker
- 快速执行，适合开发时快速验证
- 包含敏感词过滤机制的核心测试

### 3. 只运行集成测试
```bash
mvn test -Dtest="*IT"
```
- **需要 Docker Desktop 运行中**
- 使用 Testcontainers 启动 MySQL 容器
- 测试完整调用链和数据库交互
- **覆盖关键测试点：敏感词过滤机制验证**

### 4. 运行特定测试类
```bash
# 运行评论服务单元测试
mvn test -Dtest="CommentServiceImplTest"

# 运行敏感词过滤器单元测试（关键测试点）
mvn test -Dtest="SensitiveWordFilterTest"

# 运行评论控制器集成测试（关键测试点）
mvn test -Dtest="CommentControllerIT"

# 运行多个测试类
mvn test -Dtest="CommentServiceImplTest,SensitiveWordFilterTest"
```

### 5. 运行特定测试方法
```bash
# 运行敏感词过滤测试（关键测试点）
mvn test -Dtest="CommentServiceImplTest#testAddComment_WithSensitiveWords_ShouldFilter"

# 运行集成测试中的敏感词过滤
mvn test -Dtest="CommentServiceIT#testAddComment_WithSensitiveWords_Integration"
```

### 6. 跳过测试（仅编译打包）
```bash
mvn clean package -DskipTests
```
---

## 测试分类说明

### 1. 单元测试（Unit Tests）
- **命名规则**：`*Test.java`
- **特点**：使用 Mockito 隔离依赖，快速执行
- **位置**：
  - `src/test/java/org/csu/hiscomment/service/`
  - `src/test/java/org/csu/hiscomment/utils/`

### 2. 集成测试（Integration Tests）
- **命名规则**：`*IT.java`
- **特点**：使用 Testcontainers 启动真实 MySQL 数据库，测试完整调用链
- **位置**：`src/test/java/org/csu/hiscomment/integration/`

---

## 已实现的测试

### 单元测试

#### 1. `CommentServiceImplTest.java` - 评论服务单元测试
**测试覆盖功能**：
- ✅ 添加评论 - 无敏感词，正常情况
- ✅ 添加评论 - 包含轻度敏感词，应过滤
- ✅ 添加评论 - 包含重度敏感词，应拒绝发布
- ✅ 添加评论 - 包含政治敏感词，应拒绝发布
- ✅ 添加评论 - 用户不存在
- ✅ 添加评论 - 回复评论
- ✅ 点赞评论（正常/已点赞）
- ✅ 取消点赞
- ✅ 删除评论（正常/管理员删除/非本人且非管理员）
- ✅ 过滤评论（包含轻度敏感词/无敏感词）
- ✅ 获取需要过滤的评论数量

**测试方法数**：12 个

#### 2. `SensitiveWordFilterTest.java` - 敏感词过滤器工具类单元测试
**测试覆盖功能**：
- ✅ 检查敏感词 - 无敏感词
- ✅ 检查敏感词 - 包含敏感词
- ✅ 过滤敏感词 - 无敏感词
- ✅ 过滤敏感词 - 包含敏感词
- ✅ 获取敏感词级别
- ✅ 检查空文本
- ✅ 过滤空文本
- ✅ 重新加载敏感词库
- ✅ 多个敏感词场景

**测试方法数**：9 个  
**关键测试点**：敏感词检测和过滤机制（DFA 算法）

#### 3. `SensitiveWordServiceImplTest.java` - 敏感词服务单元测试
**测试覆盖功能**：
- ✅ 获取所有启用的敏感词
- ✅ 根据类型获取敏感词
- ✅ 根据级别获取敏感词
- ✅ 添加敏感词 - 正常情况
- ✅ 更新敏感词（正常/不存在）
- ✅ 删除敏感词
- ✅ 切换敏感词状态（正常/不存在/状态相同）
- ✅ 重新加载敏感词库

**测试方法数**：9 个

#### 4. `CommentLikeServiceImplTest.java` - 评论点赞服务单元测试
**测试覆盖功能**：
- ✅ 点赞 - 正常情况
- ✅ 点赞 - 已点赞
- ✅ 取消点赞（正常/未点赞）
- ✅ 检查是否已点赞（已点赞/未点赞）

**测试方法数**：6 个

### 集成测试

#### 1. `CommentServiceIT.java` - 评论服务集成测试
**测试覆盖功能**：
- ✅ 添加评论 - 无敏感词，集成测试（Service -> Dao -> Database）
- ✅ 添加评论 - 包含敏感词，集成测试
- ✅ 获取评论列表 - 集成测试
- ✅ 点赞评论 - 集成测试
- ✅ 删除评论 - 集成测试

**测试方法数**：5 个  
**关键测试点**：
- ✅ **发布内容无敏感词**：测试正常发布流程
- ✅ **发布内容有敏感词（验证敏感词过滤机制）**：测试敏感词检测和过滤

#### 2. `CommentControllerIT.java` - 评论控制器集成测试
**测试覆盖功能**：
- ✅ 发布评论 - 无敏感词，Controller集成测试（Controller -> Service -> Dao -> Database）
- ✅ 发布评论 - 包含敏感词，Controller集成测试
- ✅ 获取评论列表 - Controller集成测试
- ✅ 点赞评论 - Controller集成测试
- ✅ 删除评论 - Controller集成测试
- ✅ 检测敏感词 - Controller集成测试

**测试方法数**：6 个  
**关键测试点**：
- ✅ **发布内容无敏感词**：完整 Controller 调用链测试
- ✅ **发布内容有敏感词（验证敏感词过滤机制）**：完整敏感词过滤流程测试


---

## 外部条件要求

### 必需条件

#### 1. Java 环境
- **版本**：Java 21
- **检查命令**：`java -version`
- **作用**：运行测试和执行代码

#### 2. Maven 环境
- **版本**：Maven 3.6+
- **检查命令**：`mvn -version`
- **作用**：构建项目和运行测试

### 集成测试专用条件

#### 3. Docker Desktop（仅集成测试需要）
- **用途**：Testcontainers 需要 Docker 启动 MySQL 测试容器
- **检查命令**：`docker ps`
- **启动方法**：
  - Windows：在开始菜单搜索 "Docker Desktop" 并启动
  - 等待 Docker 完全启动（系统托盘图标不再闪烁）
- **注意**：
  - 单元测试（`*Test.java`）**不需要** Docker
  - 只有集成测试（`*IT.java`）需要 Docker
  - **关键测试点（敏感词过滤）在单元测试和集成测试中都有覆盖**

#### 4. 网络连接
- **用途**：Maven 下载依赖（首次运行）
- **注意**：集成测试会从 Docker Hub 下载 MySQL 镜像（首次运行）

### 非必需条件（运行时不需要）

以下服务在测试中已被 Mock，**不需要实际运行**：
- ❌ Eureka Server（服务注册中心）
- ❌ MySQL 数据库（使用 Testcontainers 临时容器）
- ❌ Redis（未使用）
- ❌ RabbitMQ（未使用）
- ❌ UserService（UserFeignClient 已 Mock）
- ❌ CourseService（CourseFeignClient 已 Mock）
- ❌ HerbService（HerbFeignClient 已 Mock）

---

## 覆盖率报告

### 查看覆盖率报告

#### 1. 生成并查看报告
```bash
mvn clean test
```
报告位置：`target/site/jacoco/index.html`

#### 2. 在浏览器中打开
```bash
# Windows PowerShell
Start-Process target\site\jacoco\index.html

# Windows CMD
start target\site\jacoco\index.html
```

### 覆盖率指标说明

- **Instructions（指令）**：执行的字节码指令覆盖率
- **Branches（分支）**：if/else、switch 等分支覆盖率
- **Lines（行）**：代码行覆盖率
- **Methods（方法）**：方法覆盖率
- **Classes（类）**：类覆盖率

### 目标覆盖率

根据测试计划要求：
- **指令覆盖率**：≥ 70%
- **分支覆盖率**：≥ 70%
- **行覆盖率**：≥ 70%

当前覆盖率（参考）：
- Service.impl：较高覆盖率 ✅
- Utils（敏感词过滤器）：已充分测试 ✅
- Controller：需要运行集成测试确认 ✅

---

## 故障排查

### 问题 1：Docker 环境错误
```
ERROR: Could not find a valid Docker environment
```
**解决方案**：
1. 确认 Docker Desktop 已启动
2. 运行 `docker ps` 验证 Docker 是否正常工作
3. 如果只是运行单元测试（包含敏感词过滤测试），使用 `mvn test -Dtest="*Test"` 跳过集成测试

### 问题 2：敏感词库为空警告
```
WARN: 数据库中没有找到敏感词，请检查敏感词数据是否正确导入
```
**原因**：集成测试中，`SensitiveWordFilter` 的 `@PostConstruct` 可能在数据库表创建前执行。

**解决方案**：
- 已在 `CommentServiceIT` 和 `CommentControllerIT` 中通过 `@BeforeEach` 调用 `sensitiveWordFilter.reloadFromDatabase()` 解决
- 确保集成测试正确设置了数据库初始数据

### 问题 3：Feign Client 服务不可用
```
FeignException$ServiceUnavailable: Load balancer does not contain an instance
```
**原因**：集成测试中尝试调用真实的 Feign Client。

**解决方案**：
- 已在测试中使用 `@MockBean` 模拟所有 Feign Client
- 确保在 `@BeforeEach` 中正确设置 Mock 行为

### 问题 4：端口占用
```
Port already in use
```
**解决方案**：
- Testcontainers 会自动分配端口，通常不会冲突
- 如仍有问题，检查是否有其他 MySQL 实例运行

### 问题 5：测试超时
```
Tests run: X, Failures: 0, Errors: Y
```
**解决方案**：
1. 检查网络连接（首次下载镜像可能较慢）
2. 增加超时时间（在 `pom.xml` 中配置 `testcontainers.reuse.enable=true`）

### 问题 6：敏感词过滤不工作
```
expected: <true> but was: <false>
```
**原因**：敏感词库未正确加载。

**解决方案**：
1. 检查集成测试中是否正确初始化了 `sensitive_words` 表
2. 确认在 `@BeforeEach` 中调用了 `sensitiveWordFilter.reloadFromDatabase()`
3. 检查测试数据中是否包含有效的敏感词记录

---

## 测试文件结构

```
HisComment/
├── pom.xml                          # Maven 配置（含测试依赖）
└── src/
    └── test/
        └── java/
            └── org/
                └── csu/
                    └── hiscomment/
                        ├── HisCommentApplicationTests.java  # Spring Boot 默认测试
                        ├── integration/                     # 集成测试目录
                        │   ├── CommentControllerIT.java    # Controller 集成测试（关键测试点）
                        │   └── CommentServiceIT.java       # Service 集成测试（关键测试点）
                        ├── service/                         # 单元测试目录
                        │   ├── CommentServiceImplTest.java       # 评论服务单元测试（关键测试点）
                        │   ├── CommentLikeServiceImplTest.java   # 点赞服务单元测试
                        │   └── SensitiveWordServiceImplTest.java # 敏感词服务单元测试
                        └── utils/                           # 工具类单元测试
                            └── SensitiveWordFilterTest.java       # 敏感词过滤器单元测试（关键测试点）
```

---

## 关键测试点覆盖情况

根据测试计划，以下关键测试点已充分覆盖：

### ✅ 已覆盖

#### 1. 发布内容无敏感词 ✅
**单元测试**：
- `CommentServiceImplTest#testAddComment_NoSensitiveWords`
- `SensitiveWordFilterTest#testCheckSensitiveWords_NoSensitiveWords`

**集成测试**：
- `CommentServiceIT#testAddComment_NoSensitiveWords_Integration`
- `CommentControllerIT#testAddComment_NoSensitiveWords_ControllerIntegration`

#### 2. 发布内容有敏感词（验证敏感词过滤机制）✅
**单元测试**：
- `CommentServiceImplTest#testAddComment_WithMildSensitiveWords_ShouldFilter` - 轻度敏感词过滤
- `CommentServiceImplTest#testAddComment_WithSevereSensitiveWords_ShouldReject` - 重度敏感词拒绝
- `CommentServiceImplTest#testAddComment_WithPoliticalSensitiveWords_ShouldReject` - 政治敏感词拒绝
- `SensitiveWordFilterTest#testCheckSensitiveWords_WithSensitiveWords` - 敏感词检测
- `SensitiveWordFilterTest#testFilterSensitiveWords_WithSensitiveWords` - 敏感词过滤

**集成测试**：
- `CommentServiceIT#testAddComment_WithSensitiveWords_Integration` - 完整集成测试
- `CommentControllerIT#testAddComment_WithSensitiveWords_ControllerIntegration` - Controller 层集成测试
- `CommentControllerIT#testCheckSensitiveWords_ControllerIntegration` - 敏感词检测接口测试

### 敏感词过滤机制说明

1. **DFA 算法**：使用 `SensitiveWordFilter` 工具类实现高效的敏感词检测
2. **敏感词级别**：
   - **轻度敏感词**：标记 `isFiltered=1`，内容可发布但会被标记
   - **重度敏感词**：拒绝发布，返回错误
   - **政治敏感词**：拒绝发布，返回错误
3. **数据库加载**：从 `sensitive_words` 表加载敏感词库，支持动态重载
4. **测试覆盖**：单元测试和集成测试都充分验证了过滤机制

---

## 快速参考

| 测试类型 | 命令 | 需要 Docker | 覆盖关键测试点 |
|---------|------|------------|--------------|
| 所有测试 | `mvn clean test` | ✅ | ✅ |
| 仅单元测试 | `mvn test -Dtest="*Test"` | ❌ | ✅ 敏感词过滤 |
| 仅集成测试 | `mvn test -Dtest="*IT"` | ✅ | ✅ 敏感词过滤 |
| 敏感词测试 | `mvn test -Dtest="*SensitiveWord*"` | ❌ | ✅ 敏感词过滤 |
| 跳过测试 | `mvn package -DskipTests` | ❌ | ❌ |
