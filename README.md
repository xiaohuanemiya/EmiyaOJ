# EmiyaOJ - Online Judge System

基于 Spring Boot 和 go-judge 沙箱的在线评测系统。

## 系统功能

### 已实现功能

1. **题目管理**
   - 创建、修改、删除题目
   - 分页查询题目列表
   - 按难度和标签筛选
   - 题目详情展示

2. **测试用例管理**
   - 为题目添加测试用例
   - 支持样例和隐藏测试用例
   - 自动评分

3. **代码提交与判题**
   - 支持多种编程语言：C, C++, Java, Python, Go
   - 异步判题机制
   - 编译错误检测
   - 运行时错误检测
   - 时间和内存限制检查
   - 输出结果对比

4. **提交记录查询**
   - 查看个人提交记录
   - 查看题目提交记录
   - 详细的判题结果

## 技术栈

- **后端框架**: Spring Boot 3.5.5
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.13
- **缓存**: Redis
- **判题沙箱**: go-judge (运行于 Docker 容器)
- **API 文档**: Knife4j
- **认证授权**: Spring Security + JWT

## 系统架构

```
EmiyaOJ/
├── oj-common/          # 公共模块
│   ├── config/         # 通用配置
│   ├── domain/         # 通用领域对象
│   ├── exception/      # 异常定义
│   └── utils/          # 工具类
└── oj-service/         # 业务服务模块
    ├── controller/     # 控制器
    ├── service/        # 业务逻辑
    ├── mapper/         # 数据访问层
    ├── domain/         # 领域对象
    │   ├── pojo/       # 实体类
    │   ├── dto/        # 数据传输对象
    │   ├── vo/         # 视图对象
    │   └── sandbox/    # 沙箱 API 模型
    └── properties/     # 配置属性
```

## 沙箱配置

沙箱服务运行在 Docker 容器中，配置如下：

```yaml
sandbox:
  url: http://10.30.22.1:10000
  timeout: 30000  # 超时时间（毫秒）
```

## API 接口

### 题目管理

| 接口 | 方法 | 描述 |
|------|------|------|
| `/problem/page` | GET | 分页查询题目 |
| `/problem/{id}` | GET | 获取题目详情 |
| `/problem` | POST | 创建题目 |
| `/problem/{id}` | PUT | 更新题目 |
| `/problem/{id}` | DELETE | 删除题目 |
| `/problem/batch` | DELETE | 批量删除题目 |

### 提交管理

| 接口 | 方法 | 描述 |
|------|------|------|
| `/submission/submit` | POST | 提交代码 |
| `/submission/{id}` | GET | 获取提交详情 |
| `/submission/user/{userId}` | GET | 获取用户提交列表 |
| `/submission/problem/{problemId}` | GET | 获取题目提交列表 |

## 数据库表结构

### problem 表（题目表）
- 题目基本信息
- 时间、内存、栈限制
- 通过次数、提交次数统计

### test_case 表（测试用例表）
- 关联题目ID
- 输入输出数据
- 是否为样例
- 分数权重

### submission 表（提交记录表）
- 关联题目和用户
- 代码内容
- 判题状态
- 运行时间和内存
- 详细判题结果

## 支持的编程语言

| 语言 | 编译器/解释器 | 文件名 |
|------|---------------|--------|
| C | GCC (C11) | main.c |
| C++ | G++ (C++17) | main.cpp |
| Java | OpenJDK | Main.java |
| Python | Python 3 | main.py |
| Go | Go | main.go |

## 判题状态

- `Pending` - 等待判题
- `Judging` - 判题中
- `Accepted` - 通过
- `Wrong Answer` - 答案错误
- `Time Limit Exceeded` - 超时
- `Memory Limit Exceeded` - 内存超限
- `Runtime Error` - 运行时错误
- `Compile Error` - 编译错误
- `System Error` - 系统错误

## 部署说明

1. **环境要求**
   - JDK 17+
   - MySQL 8.0+
   - Redis
   - go-judge 沙箱容器

2. **数据库初始化**
   ```bash
   # 执行 RBAC 权限表结构
   mysql -u root -p < database/rbac_schema.sql
   
   # 执行 RBAC 初始数据
   mysql -u root -p < database/rbac_init_data.sql
   
   # 执行 OJ 系统表结构
   mysql -u root -p < database/oj_schema.sql
   ```

3. **配置文件**
   编辑 `oj-service/src/main/resources/application.yaml`：
   - 数据库连接信息
   - Redis 连接信息
   - 沙箱 URL
   - JWT 密钥

4. **编译运行**
   ```bash
   # 编译
   ./mvnw clean package -DskipTests
   
   # 运行
   java -jar oj-service/target/oj-service-0.0.1-SNAPSHOT.jar
   ```

5. **访问系统**
   - API 文档: http://localhost:8080/doc.html
   - 接口地址: http://localhost:8080

## 开发计划

- [x] 题目管理
- [x] 提交判题
- [x] 多语言支持
- [ ] 竞赛管理
- [ ] 排行榜
- [ ] 题解系统
- [ ] 讨论区
- [ ] 用户个人中心
- [ ] 统计分析

## 许可证

本项目采用 MIT 许可证。
