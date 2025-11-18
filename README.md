# EmiyaOJ - Online Judge System

基于 Java 21 和 Spring Boot 3.5.5 的在线评测系统，集成沙箱环境进行安全的代码执行。

## 系统架构

### 技术栈
- **后端框架**: Spring Boot 3.5.5
- **Java版本**: Java 21
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **ORM框架**: MyBatis-Plus 3.5.13
- **API文档**: Knife4j (Swagger) 4.4.0
- **HTTP客户端**: Spring WebFlux WebClient
- **安全框架**: Spring Security

### 核心功能
1. **题目管理**: 题目的创建、查询、分页列表
2. **代码提交**: 支持多语言代码提交
3. **自动判题**: 异步编译和执行，自动比对输出
4. **沙箱集成**: 与外部沙箱服务集成，确保代码安全执行
5. **多语言支持**: C, C++, Java, Python, Go

## 快速开始

### 前置条件
- Java 21
- MySQL 8.0+
- Redis
- 沙箱服务运行于 10.30.22.1:10000

### 数据库初始化

1. 创建数据库:
```sql
CREATE DATABASE `emiya-oj` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库脚本:
```bash
# RBAC权限系统表
mysql -u root -p emiya-oj < database/rbac_schema.sql
mysql -u root -p emiya-oj < database/rbac_init_data.sql

# OJ系统表
mysql -u root -p emiya-oj < database/oj_schema.sql
mysql -u root -p emiya-oj < database/oj_init_data.sql
```

### 配置文件

编辑 `oj-service/src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://10.30.22.1:3306/emiya-oj
    username: root
    password: your_password
  data:
    redis:
      host: 10.30.22.1
      port: 6379

# 沙箱配置
sandbox:
  url: http://10.30.22.1:10000
  connection-timeout: 10000
  read-timeout: 300000
  enabled: true
```

### 编译和运行

```bash
# 编译项目
mvn clean package -DskipTests

# 运行服务
java -jar oj-service/target/oj-service-0.0.1-SNAPSHOT.jar
```

服务默认运行在 `http://localhost:8080`

### API文档

启动服务后，访问 Swagger UI:
- Knife4j UI: http://localhost:8080/doc.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## API 接口

### 语言管理
- `GET /language/list` - 获取支持的编程语言列表

### 题目管理
- `GET /problem/page` - 分页查询题目列表
- `GET /problem/{id}` - 获取题目详情

### 代码提交
- `POST /submission/submit` - 提交代码
- `GET /submission/list` - 查询提交列表
- `GET /submission/{id}` - 获取提交详情

### 示例: 提交代码

```bash
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() { int a, b; scanf(\"%d %d\", &a, &b); printf(\"%d\", a+b); return 0; }"
  }'
```

响应:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "submissionId": 123,
    "message": "提交成功，正在判题中..."
  }
}
```

## 判题流程

1. **接收提交**: 用户提交代码，系统创建提交记录，状态设为 `Pending`
2. **异步判题**: 后台线程开始处理
3. **编译阶段** (如需要):
   - 将源代码写入沙箱
   - 执行编译命令
   - 检查编译结果
4. **执行阶段**:
   - 对每个测试用例:
     - 准备输入数据
     - 在沙箱中执行程序
     - 收集输出结果
     - 比对预期输出
5. **结果汇总**: 计算通过率、最大时间、最大内存等
6. **更新状态**: 更新提交记录状态

## 判题状态

- `Pending` - 等待判题
- `Judging` - 判题中
- `Accepted` - 通过
- `Wrong Answer` - 答案错误
- `Time Limit Exceeded` - 时间超限
- `Memory Limit Exceeded` - 内存超限
- `Output Limit Exceeded` - 输出超限
- `Runtime Error` - 运行时错误
- `Compile Error` - 编译错误
- `System Error` - 系统错误

## 沙箱集成

系统集成了外部沙箱服务来安全执行用户代码。沙箱提供以下功能:

- **资源限制**: CPU时间、内存、栈空间、进程数
- **文件隔离**: 独立的文件系统环境
- **输入输出**: 通过文件或管道传递
- **编译执行**: 支持多种语言的编译和执行

### 沙箱 API

客户端类: `com.emiyaoj.service.sandbox.client.SandboxClient`

主要方法:
- `run(SandboxRequest)` - 执行代码
- `uploadFile(filename, content)` - 上传文件
- `downloadFile(fileId)` - 下载文件
- `deleteFile(fileId)` - 删除文件

## 支持的编程语言

| 语言 | 版本 | 编译 | 文件扩展名 |
|------|------|------|-----------|
| C | gcc-11 | 是 | .c |
| C++ | g++-11 | 是 | .cpp |
| Java | openjdk-21 | 是 | .java |
| Python | python-3.11 | 否 | .py |
| Go | go-1.21 | 是 | .go |

## 项目结构

```
EmiyaOJ/
├── database/                    # 数据库脚本
│   ├── rbac_schema.sql         # RBAC权限表结构
│   ├── rbac_init_data.sql      # RBAC初始数据
│   ├── oj_schema.sql           # OJ系统表结构
│   └── oj_init_data.sql        # OJ初始数据
├── oj-common/                   # 公共模块
│   └── src/main/java/com/emiyaoj/common/
│       ├── config/             # 通用配置
│       ├── domain/             # 通用领域对象
│       ├── exception/          # 异常定义
│       └── utils/              # 工具类
└── oj-service/                  # 服务模块
    └── src/main/java/com/emiyaoj/service/
        ├── controller/         # REST控制器
        ├── domain/
        │   ├── constant/       # 常量定义
        │   ├── dto/            # 数据传输对象
        │   ├── pojo/           # 实体类
        │   └── vo/             # 视图对象
        ├── mapper/             # MyBatis映射器
        ├── sandbox/            # 沙箱集成
        │   ├── client/         # 沙箱客户端
        │   ├── config/         # 沙箱配置
        │   └── dto/            # 沙箱DTO
        └── service/            # 业务服务
            └── impl/           # 服务实现
```

## 开发指南

### 添加新的编程语言

1. 在数据库中插入语言记录:
```sql
INSERT INTO `language` (`name`, `version`, `compile_command`, `execute_command`, 
    `source_file_ext`, `executable_ext`, `is_compiled`, `time_limit_multiplier`, 
    `memory_limit_multiplier`, `status`) 
VALUES ('Rust', 'rustc-1.70', 'rustc -O {source} -o {executable}', 
    '{executable}', '.rs', '', 1, 1.0, 1.0, 1);
```

2. 确保沙箱环境支持该语言的编译器/解释器

### 添加新题目

1. 准备题目描述、输入输出说明、样例
2. 创建测试用例（输入和预期输出）
3. 通过API或直接在数据库中插入

### 扩展判题逻辑

修改 `JudgeServiceImpl` 类:
- `compile()` - 编译逻辑
- `runSingleTest()` - 单个测试用例执行
- `compareOutput()` - 输出比对逻辑

## 性能优化

1. **异步判题**: 使用 `@Async` 注解实现异步处理
2. **数据库索引**: 关键字段已添加索引
3. **分页查询**: 使用 MyBatis-Plus 分页插件
4. **连接池**: 默认使用 HikariCP 连接池
5. **缓存**: 可使用 Redis 缓存常用数据

## 安全考虑

1. **沙箱隔离**: 所有用户代码在隔离的沙箱环境中执行
2. **资源限制**: 严格限制CPU、内存、时间等资源
3. **输入验证**: 对用户提交的代码和数据进行验证
4. **权限控制**: 基于 Spring Security 的 RBAC 权限系统

## 故障排查

### 编译失败
- 检查沙箱服务是否正常运行
- 验证编译命令配置是否正确
- 查看沙箱返回的编译错误信息

### 判题超时
- 增加 `sandbox.read-timeout` 配置
- 检查测试用例的时间限制是否合理
- 验证沙箱服务性能

### 数据库连接失败
- 确认 MySQL 服务运行正常
- 检查数据库连接配置
- 验证用户名密码是否正确

## 许可证

本项目使用 MIT 许可证。

## 贡献

欢迎提交 Issue 和 Pull Request!

## 联系方式

如有问题，请创建 Issue 或联系维护者。
