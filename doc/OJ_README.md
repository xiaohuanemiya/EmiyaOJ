# EmiyaOJ - Online Judge System

## 项目简介

EmiyaOJ 是一个基于 Spring Boot 的在线判题系统，支持 C 和 C++ 语言的代码提交和自动判题。

## 技术栈

- **后端框架**: Spring Boot 3.5.5
- **Java版本**: 21
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis-Plus 3.5.13
- **判题引擎**: go-judge
- **API文档**: Knife4j (Swagger)
- **缓存**: Redis

## 核心功能

### 已实现功能

1. **语言管理**
   - 支持 C 和 C++ 语言
   - 可配置编译命令和执行参数
   - 支持时间和内存限制倍数

2. **题目管理**
   - 题目的增删改查
   - 难度分级（简单、中等、困难）
   - 题目标签系统
   - 测试用例管理

3. **代码提交与判题**
   - 异步判题系统
   - 支持编译型语言（C/C++）
   - 多测试用例批量运行
   - 详细的判题结果反馈
   - 判题状态：
     - Pending（等待中）
     - Judging（判题中）
     - Accepted（通过）
     - Wrong Answer（答案错误）
     - Time Limit Exceeded（超时）
     - Memory Limit Exceeded（内存超限）
     - Runtime Error（运行时错误）
     - Compile Error（编译错误）
     - System Error（系统错误）

4. **提交记录管理**
   - 提交历史查询
   - 按题目/用户筛选
   - 通过率统计

## 系统架构

### 目录结构

```
EmiyaOJ/
├── oj-common/          # 公共模块
│   ├── config/         # 配置类
│   ├── constant/       # 常量定义
│   ├── domain/         # 通用领域对象
│   ├── exception/      # 异常处理
│   ├── properties/     # 配置属性
│   └── utils/          # 工具类
├── oj-service/         # 服务模块
│   ├── controller/     # 控制器层
│   ├── service/        # 服务层
│   ├── mapper/         # 数据访问层
│   ├── domain/         # 领域对象
│   │   ├── pojo/       # 实体类
│   │   ├── dto/        # 数据传输对象
│   │   └── vo/         # 视图对象
│   └── util/           # 判题工具类
└── database/           # 数据库脚本
```

### 核心模块

#### 1. GoJudgeService
集成 go-judge 判题引擎，提供代码编译和执行功能。

#### 2. SubmissionService
判题核心服务，实现异步判题流程：
1. 创建提交记录
2. 编译代码（如需要）
3. 运行测试用例
4. 比较输出结果
5. 更新提交状态和统计信息

#### 3. GoJudgeRequestBuilder
构建 go-judge 请求的工具类，支持：
- C 语言编译请求
- C++ 语言编译请求
- 程序运行请求

## 数据库设计

### 核心表

1. **language** - 编程语言表
2. **problem** - 题目表
3. **test_case** - 测试用例表
4. **submission** - 提交记录表
5. **submission_result** - 测试用例判题结果表
6. **tag** - 标签表
7. **problem_tag** - 题目标签关联表

详细的数据库结构请参考 `database/oj_schema_and_data.sql`。

## 快速开始

### 环境要求

- JDK 21+
- MySQL 8.0+
- Redis
- go-judge 服务

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/xiaohuanemiya/EmiyaOJ.git
cd EmiyaOJ
```

2. **初始化数据库**
```bash
mysql -u root -p < database/oj_schema_and_data.sql
```

3. **配置应用**

编辑 `oj-service/src/main/resources/application.yaml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emiya-oj
    username: root
    password: your_password

gojudge:
  url: http://localhost:10000
```

4. **编译项目**
```bash
mvn clean install
```

5. **运行应用**
```bash
cd oj-service
mvn spring-boot:run
```

6. **访问API文档**

打开浏览器访问: http://localhost:8080/doc.html

## API 接口

### 语言相关
- `GET /language/list` - 获取所有可用语言
- `GET /language/{id}` - 获取语言详情

### 题目相关
- `GET /problem/page` - 分页查询题目列表
- `GET /problem/{id}` - 获取题目详情

### 提交相关
- `POST /submission/submit` - 提交代码
- `GET /submission/page` - 分页查询提交记录
- `GET /submission/{id}` - 获取提交详情

## 测试

### 运行单元测试
```bash
mvn test
```

### 运行集成测试
```bash
# 确保数据库和 go-judge 服务正在运行
mvn test -Dtest=OJIntegrationTest
```

## 判题流程

1. 用户通过 API 提交代码
2. 系统创建提交记录，状态为 "Pending"
3. 异步判题任务启动：
   - 状态更新为 "Judging"
   - 如需编译，调用 go-judge 编译代码
   - 编译失败则返回 "Compile Error"
   - 编译成功后获取可执行文件
4. 依次运行所有测试用例：
   - 调用 go-judge 执行程序
   - 获取输出结果
   - 与预期输出比对
   - 记录每个测试用例的结果
5. 更新提交记录：
   - 计算最终状态
   - 记录时间和内存使用
   - 更新通过率
   - 更新题目统计
6. 清理临时文件

## 示例代码

### C++ A+B Problem
```cpp
#include <iostream>
int main() {
    int a, b;
    std::cin >> a >> b;
    std::cout << a + b << std::endl;
    return 0;
}
```

### C A+B Problem
```c
#include <stdio.h>
int main() {
    int a, b;
    scanf("%d %d", &a, &b);
    printf("%d\n", a + b);
    return 0;
}
```

## 扩展功能

### 添加新语言支持

1. 在 `language` 表中添加新语言记录
2. 在 `GoJudgeRequestBuilder` 中添加编译请求构建方法
3. 在 `SubmissionServiceImpl.compileCode()` 中添加语言判断逻辑

### 自定义判题逻辑

可以在 `SubmissionServiceImpl` 中修改：
- `compareOutput()` - 自定义输出比较逻辑
- `runTestCase()` - 自定义测试用例执行逻辑

## 注意事项

1. 确保 go-judge 服务正常运行
2. 数据库连接配置正确
3. 测试用例的输入输出格式要准确
4. 异步判题需要合理设置超时时间
5. 生产环境建议配置独立的判题队列

## 贡献

欢迎提交 Issue 和 Pull Request！

## 许可证

[MIT License](LICENSE)
