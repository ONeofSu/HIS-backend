# HerbTeaching 模块测试指南

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
确保已进入 `HerbTeaching` 目录：
```bash
cd HerbTeaching
```

### 1. 运行所有测试
```bash
mvn clean test
```
- 运行所有单元测试和集成测试
- 生成 JaCoCo 覆盖率报告

### 2. 只运行单元测试
```bash
mvn test -Dtest="*Test"
```
- 不依赖 Docker
- 快速执行，适合开发时快速验证

### 3. 只运行集成测试
```bash
mvn test -Dtest="*IT"
```
- **需要 Docker Desktop 运行中**
- 使用 Testcontainers 启动 MySQL 容器

### 4. 运行特定测试类
```bash
# 运行课程服务单元测试
mvn test -Dtest="CourseServiceImplTest"

# 运行课程控制器集成测试
mvn test -Dtest="CourseControllerIT"

# 运行多个测试类
mvn test -Dtest="CourseServiceImplTest,LabServiceImplTest"
```

### 5. 运行特定测试方法
```bash
mvn test -Dtest="CourseServiceImplTest#testGetCourseList"
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
- **位置**：`src/test/java/org/csu/herb_teaching/service/`

### 2. 集成测试（Integration Tests）
- **命名规则**：`*IT.java`
- **特点**：使用 Testcontainers 启动真实 MySQL 数据库，测试完整调用链
- **位置**：`src/test/java/org/csu/herb_teaching/integration/`

---

## 已实现的测试

### 单元测试

#### 1. `CourseServiceImplTest.java` - 课程服务单元测试
**测试覆盖功能**：
- ✅ 获取课程列表（含关键词/无关键词）
- ✅ 获取课程详情（正常/不存在）
- ✅ 创建课程（正常/课程名重复/教师ID无效）
- ✅ 更新课程（正常/不存在）
- ✅ 删除课程（正常/不存在）
- ✅ 课程评分（正常/评分值无效/更新已有评分）
- ✅ 收藏课程（正常/课程不存在/已收藏）
- ✅ 取消收藏
- ✅ 添加中草药到课程（正常/中草药不存在）
- ✅ 检查用户是否已评分
- ✅ 检查用户是否已收藏

**测试方法数**：17 个

#### 2. `LabServiceImplTest.java` - 实验服务单元测试
**测试覆盖功能**：
- ✅ 根据课程ID获取实验列表（正常/无实验）
- ✅ 根据ID获取实验（正常/不存在）
- ✅ 创建实验（正常/课程不存在/已有实验自动分配顺序）
- ✅ 更新实验（正常/不存在/部分字段更新）
- ✅ 删除实验（正常/不存在）

**测试方法数**：12 个

#### 3. `ResourceServiceImplTest.java` - 资源服务单元测试
**测试覆盖功能**：
- ✅ 根据课程ID获取资源列表（正常/无资源）
- ✅ 根据ID获取资源（正常/不存在）
- ✅ 创建资源（正常/课程不存在/已有资源自动分配顺序）
- ✅ 更新资源（正常/不存在/部分字段更新/更新顺序）
- ✅ 删除资源（正常/不存在）

**测试方法数**：12 个

### 集成测试

#### 1. `CourseServiceIT.java` - 课程服务集成测试
**测试覆盖功能**：
- ✅ 创建课程 - 集成测试（Service -> Dao -> Database）
- ✅ 获取课程列表 - 集成测试
- ✅ 更新课程 - 集成测试
- ✅ 删除课程 - 集成测试

**测试方法数**：4 个

#### 2. `CourseControllerIT.java` - 课程控制器集成测试
**测试覆盖功能**：
- ✅ 获取课程列表 - Controller集成测试（Controller -> Service -> Dao -> Database）
- ✅ 获取课程详情 - Controller集成测试
- ✅ 创建课程 - Controller集成测试
- ✅ 更新课程 - Controller集成测试
- ✅ 删除课程 - Controller集成测试
- ✅ 课程评分 - Controller集成测试

**测试方法数**：6 个


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
- ❌ HerbInfoService（HerbInfoFeignClient 已 Mock）

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
- Service.impl：71% 指令，61% 分支 ✅
- Controller：0% ❌（需要增加 Controller 测试）
- 总体：27% ❌（需要提升）

---

## 故障排查

### 问题 1：Docker 环境错误
```
ERROR: Could not find a valid Docker environment
```
**解决方案**：
1. 确认 Docker Desktop 已启动
2. 运行 `docker ps` 验证 Docker 是否正常工作
3. 如果只是运行单元测试，使用 `mvn test -Dtest="*Test"` 跳过集成测试

### 问题 2：端口占用
```
Port already in use
```
**解决方案**：
- Testcontainers 会自动分配端口，通常不会冲突
- 如仍有问题，检查是否有其他 MySQL 实例运行

### 问题 3：测试超时
```
Tests run: X, Failures: 0, Errors: Y
```
**解决方案**：
1. 检查网络连接（首次下载镜像可能较慢）
2. 增加超时时间（在 `pom.xml` 中配置 `testcontainers.reuse.enable=true`）

### 问题 4：Maven 依赖下载失败
```
Could not resolve dependencies
```
**解决方案**：
1. 检查网络连接
2. 清理本地 Maven 仓库：`mvn dependency:purge-local-repository`
3. 重新下载：`mvn clean install -U`

### 问题 5：编译错误
```
找不到符号
```
**解决方案**：
1. 清理项目：`mvn clean`
2. 重新编译：`mvn compile`
3. 检查 JDK 版本是否为 21

---

## 测试文件结构

```
HerbTeaching/
├── pom.xml                          # Maven 配置（含测试依赖）
└── src/
    └── test/
        └── java/
            └── org/
                └── csu/
                    └── herb_teaching/
                        ├── HerbTeachingApplicationTests.java  # Spring Boot 默认测试
                        ├── integration/                       # 集成测试目录
                        │   ├── CourseControllerIT.java       # Controller 集成测试
                        │   └── CourseServiceIT.java          # Service 集成测试
                        └── service/                           # 单元测试目录
                            ├── CourseServiceImplTest.java    # 课程服务单元测试
                            ├── LabServiceImplTest.java       # 实验服务单元测试
                            └── ResourceServiceImplTest.java  # 资源服务单元测试
```

---

## 关键测试点覆盖情况

根据测试计划，以下关键测试点已覆盖：

### ✅ 已覆盖
- ✅ **获取课程**：`CourseServiceImplTest#testGetCourseList`
- ✅ **查看课程详情**：`CourseServiceImplTest#testGetCourseDetail`
- ✅ **课程学习记录**：通过课程评分和收藏功能测试间接覆盖

### ⚠️ 待完善
- ⚠️ Controller 层覆盖率 0%，需要运行集成测试并确保测试通过
- ⚠️ 需要增加更多边界条件和异常场景测试
- ⚠️ 需要提升分支覆盖率至 70%

---

## 快速参考

| 测试类型 | 命令 | 需要 Docker |
|---------|------|------------|
| 所有测试 | `mvn clean test` | ✅ |
| 仅单元测试 | `mvn test -Dtest="*Test"` | ❌ |
| 仅集成测试 | `mvn test -Dtest="*IT"` | ✅ |
| 特定测试类 | `mvn test -Dtest="ClassName"` | 取决于测试类型 |
| 跳过测试 | `mvn package -DskipTests` | ❌ |