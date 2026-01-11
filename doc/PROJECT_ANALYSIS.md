# EmiyaOJ 项目完整分析

## 目录

1. [项目概述](#1-项目概述)
2. [技术栈分析](#2-技术栈分析)
3. [项目架构](#3-项目架构)
4. [模块分析](#4-模块分析)
5. [数据库设计](#5-数据库设计)
6. [API接口分析](#6-api接口分析)
7. [核心功能分析](#7-核心功能分析)
8. [代码结构分析](#8-代码结构分析)
9. [安全机制](#9-安全机制)
10. [开发与部署](#10-开发与部署)
11. [扩展建议](#11-扩展建议)

---

## 1. 项目概述

### 1.1 项目简介

EmiyaOJ (Emiya Online Judge) 是一个功能完整的在线判题系统，采用前后端分离架构。系统支持用户提交代码，自动编译、运行并与预期输出进行比对，实现自动化判题功能。

### 1.2 核心功能

| 功能模块 | 描述 |
|---------|------|
| **用户管理** | 用户注册、登录、权限管理、角色分配 |
| **权限管理** | RBAC权限控制，支持菜单、按钮级别权限 |
| **题目管理** | 题目的CRUD操作、标签管理、难度分级 |
| **测试用例管理** | 测试用例增删改查、样例与隐藏用例 |
| **代码提交** | 支持C/C++语言代码提交 |
| **自动判题** | 异步判题、编译检测、多测试用例验证 |
| **判题结果** | 详细的判题反馈，包括时间、内存使用 |
| **博客系统** | 用户博客发布、评论、点赞功能 |
| **操作日志** | 系统操作日志记录 |

### 1.3 系统特点

- **异步判题**: 采用Spring @Async实现异步判题，提高系统响应速度
- **安全沙箱**: 集成go-judge判题引擎，提供安全的代码执行环境
- **RBAC权限**: 完善的角色权限管理系统
- **RESTful API**: 标准的RESTful接口设计
- **API文档**: 集成Knife4j提供交互式API文档

---

## 2. 技术栈分析

### 2.1 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| **Java** | 21 | 开发语言 |
| **Spring Boot** | 3.5.5 | 应用框架 |
| **Spring Security** | 6.x | 安全框架 |
| **MyBatis-Plus** | 3.5.13 | ORM框架 |
| **MySQL** | 8.0 | 关系型数据库 |
| **Redis** | - | 缓存与会话管理 |
| **Lombok** | - | 代码简化工具 |
| **Knife4j** | - | API文档生成 |
| **JWT** | - | 身份认证 |

### 2.2 判题引擎

| 技术 | 用途 |
|------|------|
| **go-judge** | 代码编译与执行沙箱 |

### 2.3 前端技术栈（设计规划）

| 技术 | 版本 | 用途 |
|------|------|------|
| **Vue** | 3.5 | 前端框架 |
| **Vite** | 5.x | 构建工具 |
| **TypeScript** | 5.x | 开发语言 |
| **Element Plus** | - | UI组件库 |
| **Pinia** | - | 状态管理 |
| **Vue Router** | 4.x | 路由管理 |
| **Axios** | - | HTTP客户端 |
| **Monaco Editor** | - | 代码编辑器 |

---

## 3. 项目架构

### 3.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        前端应用                              │
│              (Vue 3 + Element Plus + Monaco Editor)          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Spring Boot 应用                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │  Controller │  │   Service   │  │   Mapper    │          │
│  │    层       │→│     层      │→│     层      │          │
│  └─────────────┘  └─────────────┘  └─────────────┘          │
│         │                │                │                  │
│         ▼                ▼                ▼                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │  Security   │  │   go-judge  │  │   MySQL     │          │
│  │   Filter    │  │   Service   │  │   Redis     │          │
│  └─────────────┘  └─────────────┘  └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      go-judge 判题引擎                       │
│              (代码编译 & 安全沙箱执行)                       │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 项目模块结构

```
EmiyaOJ/
├── pom.xml                    # 父项目POM（多模块管理）
├── oj-common/                 # 公共模块
│   ├── pom.xml
│   └── src/main/java/com/emiyaoj/common/
│       ├── config/            # 通用配置
│       ├── constant/          # 常量定义
│       ├── domain/            # 通用领域对象
│       ├── exception/         # 异常定义
│       ├── handler/           # 全局处理器
│       ├── properties/        # 配置属性
│       └── utils/             # 工具类
├── oj-service/                # 服务模块
│   ├── pom.xml
│   └── src/main/java/com/emiyaoj/service/
│       ├── config/            # 服务配置
│       ├── controller/        # 控制器层
│       ├── service/           # 服务层
│       ├── mapper/            # 数据访问层
│       ├── domain/            # 领域对象
│       │   ├── pojo/          # 实体类
│       │   ├── dto/           # 数据传输对象
│       │   └── vo/            # 视图对象
│       └── util/              # 判题工具类
├── database/                  # 数据库脚本
└── doc/                       # 项目文档
```

### 3.3 模块依赖关系

```
oj-service
    └── 依赖 → oj-common
```

---

## 4. 模块分析

### 4.1 oj-common 公共模块

#### 4.1.1 配置类 (config/)

| 类名 | 功能 |
|------|------|
| `CommonConfig.java` | 通用Bean配置（如RestTemplate） |
| `RedisConfig.java` | Redis序列化配置 |

#### 4.1.2 常量类 (constant/)

| 类名 | 功能 |
|------|------|
| `JwtClaimsConstant.java` | JWT声明常量 |
| `ResultEnum.java` | 响应结果枚举 |

#### 4.1.3 领域对象 (domain/)

| 类名 | 功能 |
|------|------|
| `ResponseResult.java` | 统一响应封装 |
| `PageDTO.java` | 分页请求参数 |
| `PageVO.java` | 分页响应对象 |

#### 4.1.4 异常类 (exception/)

| 类名 | 功能 |
|------|------|
| `BaseException.java` | 基础异常类 |
| `BadRequestException.java` | 请求错误异常 |

#### 4.1.5 处理器 (handler/)

| 类名 | 功能 |
|------|------|
| `GlobalExceptionHandler.java` | 全局异常处理 |
| `AnonymousAuthenticationHandler.java` | 匿名认证处理 |
| `CustomerAccessDeniedHandler.java` | 访问拒绝处理 |
| `LoginFailureHandler.java` | 登录失败处理 |

#### 4.1.6 工具类 (utils/)

| 类名 | 功能 |
|------|------|
| `JwtUtil.java` | JWT生成与解析 |
| `RedisUtil.java` | Redis操作封装 |
| `BaseContext.java` | 线程上下文（存储用户ID） |
| `ObjectUtil.java` | 对象工具类 |

### 4.2 oj-service 服务模块

#### 4.2.1 控制器层 (controller/)

| 控制器 | 路由前缀 | 功能 |
|--------|----------|------|
| `AuthController` | `/auth` | 用户认证（登录/登出） |
| `UserController` | `/user` | 用户管理 |
| `RoleController` | `/role` | 角色管理 |
| `PermissionController` | `/permission` | 权限管理 |
| `ProblemController` | `/problem` | 题目管理 |
| `TestCaseController` | `/testcase` | 测试用例管理 |
| `LanguageController` | `/language` | 编程语言管理 |
| `BlogController` | `/blog` | 博客管理 |
| `OperationLogController` | `/operation-log` | 操作日志 |

#### 4.2.2 服务层接口 (service/)

| 接口 | 实现类 | 功能 |
|------|--------|------|
| `IUserService` | `UserServiceImpl` | 用户业务逻辑 |
| `IRoleService` | `RoleServiceImpl` | 角色业务逻辑 |
| `IPermissionService` | `PermissionServiceImpl` | 权限业务逻辑 |
| `IProblemService` | `ProblemServiceImpl` | 题目业务逻辑 |
| `ITestCaseService` | `TestCaseServiceImpl` | 测试用例业务逻辑 |
| `ILanguageService` | `LanguageServiceImpl` | 语言业务逻辑 |
| `ISubmissionService` | `SubmissionServiceImpl` | **核心判题服务** |
| `ISubmissionResultService` | `SubmissionResultServiceImpl` | 判题结果服务 |
| `IBlogService` | `BlogServiceImpl` | 博客服务 |
| `IOperationLogService` | `OperationLogServiceImpl` | 操作日志服务 |

#### 4.2.3 数据访问层 (mapper/)

继承MyBatis-Plus的BaseMapper，提供基础CRUD操作：

- `UserMapper`, `RoleMapper`, `PermissionMapper`
- `ProblemMapper`, `TestCaseMapper`, `TagMapper`, `ProblemTagMapper`
- `LanguageMapper`, `SubmissionMapper`, `SubmissionResultMapper`
- `BlogMapper`, `BlogCommentMapper`, `BlogTagMapper`
- `OperationLogMapper`

#### 4.2.4 实体类 (domain/pojo/)

**用户权限相关：**
- `User` - 用户
- `Role` - 角色
- `Permission` - 权限
- `UserRole` - 用户角色关联
- `RolePermission` - 角色权限关联
- `UserPermission` - 用户权限关联

**判题系统相关：**
- `Language` - 编程语言
- `Problem` - 题目
- `TestCase` - 测试用例
- `Tag` - 标签
- `ProblemTag` - 题目标签关联
- `Submission` - 提交记录
- `SubmissionResult` - 判题结果

**博客相关：**
- `Blog` - 博客
- `BlogComment` - 博客评论
- `BlogTag` - 博客标签
- `BlogStar` - 博客点赞

#### 4.2.5 判题工具类 (util/)

| 类 | 功能 |
|----|------|
| `GoJudgeService` | 与go-judge通信的服务类 |
| `GoJudgeRequestBuilder` | 构建go-judge请求 |
| `AuthUtils` | 认证工具类 |

**go-judge模型类 (util/oj/Model/)：**
- `Request` - 判题请求
- `Result` - 判题结果
- `Cmd` - 执行命令
- `BaseFile`, `LocalFile`, `MemoryFile`, `PreparedFile` - 文件模型
- `StreamIn`, `StreamOut` - 输入输出流

---

## 5. 数据库设计

### 5.1 ER图概述

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│     user     │────<│  user_role   │>────│     role     │
└──────────────┘     └──────────────┘     └──────────────┘
                                                  │
                                                  │
                                          ┌──────────────┐
                                          │role_permission│
                                          └──────────────┘
                                                  │
                                                  ▼
                                          ┌──────────────┐
                                          │  permission  │
                                          └──────────────┘

┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   problem    │────<│ problem_tag  │>────│     tag      │
└──────────────┘     └──────────────┘     └──────────────┘
       │
       │
       ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  test_case   │     │  submission  │────>│  language    │
└──────────────┘     └──────────────┘     └──────────────┘
       │                    │
       │                    │
       ▼                    ▼
┌──────────────────────────────────┐
│      submission_result           │
└──────────────────────────────────┘
```

### 5.2 核心表结构

#### 5.2.1 language（编程语言表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 语言名称（C, C++） |
| version | VARCHAR(50) | 版本（gcc-11） |
| compile_command | TEXT | 编译命令模板 |
| execute_command | TEXT | 执行命令模板 |
| source_file_ext | VARCHAR(20) | 源文件扩展名 |
| executable_ext | VARCHAR(20) | 可执行文件扩展名 |
| is_compiled | TINYINT | 是否需要编译 |
| time_limit_multiplier | DECIMAL(3,2) | 时间限制倍数 |
| memory_limit_multiplier | DECIMAL(3,2) | 内存限制倍数 |
| status | TINYINT | 状态（0禁用/1启用） |

#### 5.2.2 problem（题目表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(255) | 题目标题 |
| description | TEXT | 题目描述 |
| input_description | TEXT | 输入描述 |
| output_description | TEXT | 输出描述 |
| sample_input | TEXT | 样例输入 |
| sample_output | TEXT | 样例输出 |
| hint | TEXT | 提示信息 |
| difficulty | TINYINT | 难度（1简单/2中等/3困难） |
| time_limit | INT | CPU时间限制（毫秒） |
| memory_limit | INT | 内存限制（MB） |
| stack_limit | INT | 栈内存限制（MB） |
| accept_count | INT | 通过次数 |
| submit_count | INT | 提交次数 |
| status | TINYINT | 状态（0隐藏/1公开） |
| deleted | TINYINT | 软删除标记 |

#### 5.2.3 test_case（测试用例表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| problem_id | BIGINT | 题目ID |
| input | LONGTEXT | 输入数据 |
| output | LONGTEXT | 预期输出 |
| is_sample | TINYINT | 是否为样例 |
| score | INT | 分值 |
| sort_order | INT | 排序 |
| deleted | TINYINT | 软删除标记 |

#### 5.2.4 submission（提交记录表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| problem_id | BIGINT | 题目ID |
| user_id | BIGINT | 用户ID |
| language_id | BIGINT | 语言ID |
| code | LONGTEXT | 源代码 |
| status | VARCHAR(50) | 判题状态 |
| score | INT | 得分 |
| time_used | INT | 使用时间（毫秒） |
| memory_used | INT | 使用内存（KB） |
| error_message | TEXT | 错误信息 |
| compile_message | TEXT | 编译信息 |
| pass_rate | VARCHAR(50) | 通过率（如"10/10"） |

#### 5.2.5 submission_result（判题结果表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| submission_id | BIGINT | 提交ID |
| test_case_id | BIGINT | 测试用例ID |
| status | VARCHAR(50) | 判题状态 |
| time_used | INT | 使用时间（毫秒） |
| memory_used | INT | 使用内存（KB） |
| error_message | TEXT | 错误信息 |

### 5.3 判题状态枚举

| 状态 | 说明 |
|------|------|
| Pending | 等待判题 |
| Judging | 判题中 |
| Accepted | 通过 |
| Wrong Answer | 答案错误 |
| Time Limit Exceeded | 超时 |
| Memory Limit Exceeded | 内存超限 |
| Runtime Error | 运行时错误 |
| Compile Error | 编译错误 |
| System Error | 系统错误 |

---

## 6. API接口分析

### 6.1 认证接口

| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/auth/login` | 用户登录 |
| POST | `/auth/logout` | 用户登出 |

### 6.2 用户管理接口

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/user/page` | 分页查询用户 |
| GET | `/user/{id}` | 获取用户详情 |
| POST | `/user` | 新增用户 |
| PUT | `/user` | 修改用户 |
| DELETE | `/user/{id}` | 删除用户 |
| DELETE | `/user/batch` | 批量删除用户 |
| PUT | `/user/{id}/status` | 修改用户状态 |
| PUT | `/user/{id}/reset-password` | 重置密码 |
| PUT | `/user/{id}/roles` | 分配角色 |
| GET | `/user/{id}/permissions` | 获取用户权限 |

### 6.3 角色管理接口

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/role/page` | 分页查询角色 |
| GET | `/role/list` | 获取角色列表 |
| GET | `/role/{id}` | 获取角色详情 |
| POST | `/role` | 新增角色 |
| PUT | `/role` | 修改角色 |
| DELETE | `/role/{id}` | 删除角色 |
| PUT | `/role/{id}/permissions` | 分配权限 |
| GET | `/role/{id}/permissions` | 获取角色权限 |

### 6.4 权限管理接口

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/permission/tree` | 获取权限树 |
| GET | `/permission/list` | 获取权限列表 |
| GET | `/permission/{id}` | 获取权限详情 |
| POST | `/permission` | 新增权限 |
| PUT | `/permission` | 修改权限 |
| DELETE | `/permission/{id}` | 删除权限 |
| GET | `/permission/menu/{userId}` | 获取用户菜单 |
| GET | `/permission/button/{userId}` | 获取用户按钮权限 |

### 6.5 题目管理接口

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/problem/page` | 分页查询题目 |
| GET | `/problem/{id}` | 获取题目详情 |
| POST | `/problem` | 新增题目 |
| PUT | `/problem` | 修改题目 |
| DELETE | `/problem/{id}` | 删除题目 |
| DELETE | `/problem/batch` | 批量删除题目 |
| PUT | `/problem/{id}/status` | 修改题目状态 |

### 6.6 测试用例接口

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/testcase/problem/{problemId}` | 获取题目测试用例 |
| GET | `/testcase/{id}` | 获取测试用例详情 |
| POST | `/testcase` | 新增测试用例 |
| PUT | `/testcase` | 修改测试用例 |
| DELETE | `/testcase/{id}` | 删除测试用例 |
| DELETE | `/testcase/batch` | 批量删除测试用例 |
| DELETE | `/testcase/problem/{problemId}` | 删除题目所有测试用例 |

### 6.7 语言管理接口

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/language/list` | 获取可用语言列表 |
| GET | `/language/{id}` | 获取语言详情 |

### 6.8 提交管理接口

| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/submission/submit` | 提交代码 |
| GET | `/submission/page` | 分页查询提交记录 |
| GET | `/submission/{id}` | 获取提交详情 |

---

## 7. 核心功能分析

### 7.1 判题流程

```
用户提交代码
      │
      ▼
┌─────────────────┐
│ 创建提交记录    │
│ status=Pending  │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│   异步判题      │
│ @Async执行      │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ 更新状态为      │
│ Judging         │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ 编译代码        │
│ (如需要)        │
└─────────────────┘
      │
      ├──── 编译失败 ────> 返回 Compile Error
      │
      ▼
┌─────────────────┐
│ 获取测试用例    │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ 循环执行        │
│ 每个测试用例    │
└─────────────────┘
      │
      ├─── 超时 ──────> Time Limit Exceeded
      ├─── 内存超限 ──> Memory Limit Exceeded
      ├─── 运行错误 ──> Runtime Error
      ├─── 输出错误 ──> Wrong Answer
      │
      ▼
┌─────────────────┐
│ 全部通过        │
│ status=Accepted │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ 更新题目统计    │
│ 清理临时文件    │
└─────────────────┘
```

### 7.2 判题服务核心代码分析

**SubmissionServiceImpl.java** 核心方法：

1. **submitCode()** - 提交入口
   - 验证题目和语言
   - 创建提交记录
   - 调用异步判题

2. **judgeAsync()** - 异步判题主流程
   - 编译代码
   - 执行测试用例
   - 更新判题结果
   - 更新题目统计

3. **compileCode()** - 代码编译
   - 根据语言选择编译命令
   - 调用go-judge编译

4. **runTestCase()** - 运行测试用例
   - 设置资源限制
   - 执行程序
   - 比较输出

5. **compareOutput()** - 输出比较
   - 忽略行尾空格
   - 忽略空行

### 7.3 go-judge集成

**GoJudgeService** 通过HTTP与go-judge通信：

```java
// 执行判题请求
POST {GoJudge.url}/run
Content-Type: application/json
Body: Request对象

// 删除缓存文件
DELETE {GoJudge.url}/file/{fileId}
```

**GoJudgeRequestBuilder** 构建请求：

- `buildCppCompileRequest()` - C++编译请求
- `buildCCompileRequest()` - C编译请求
- `buildRunRequest()` - 程序运行请求

---

## 8. 代码结构分析

### 8.1 设计模式应用

| 模式 | 应用场景 |
|------|----------|
| **MVC** | Controller-Service-Mapper分层 |
| **DTO/VO模式** | 数据传输对象和视图对象分离 |
| **Builder模式** | GoJudgeRequestBuilder构建请求 |
| **Template模式** | ServiceImpl继承ServiceImpl |
| **Facade模式** | GoJudgeService封装判题引擎调用 |

### 8.2 代码规范

1. **包命名规范**
   - `com.emiyaoj.common` - 公共模块
   - `com.emiyaoj.service` - 服务模块

2. **类命名规范**
   - Controller后缀：`XxxController`
   - Service接口：`IXxxService`
   - Service实现：`XxxServiceImpl`
   - Mapper接口：`XxxMapper`
   - 实体类：无后缀
   - DTO类：`XxxDTO`或`XxxSaveDTO`
   - VO类：`XxxVO`

3. **注解使用**
   - `@RestController` - REST控制器
   - `@Service` - 服务类
   - `@Mapper` - 数据访问接口
   - `@RequiredArgsConstructor` - Lombok构造注入
   - `@Slf4j` - 日志注解
   - `@Transactional` - 事务控制
   - `@Async` - 异步执行

### 8.3 异常处理

```java
// 全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseResult<?> handleBadRequest(BadRequestException e) {
        return ResponseResult.fail(e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e) {
        return ResponseResult.fail("系统错误");
    }
}
```

---

## 9. 安全机制

### 9.1 认证机制

- **JWT Token认证**
  - 登录成功后签发Token
  - Token有效期：7200000毫秒（2小时）
  - Token通过Authorization请求头传递

### 9.2 授权机制

- **基于Spring Security的RBAC**
  - 用户 → 角色 → 权限
  - 方法级权限控制：`@PreAuthorize("hasAuthority('xxx')")`

### 9.3 JWT过滤器

```java
@Component
public class JwtTokenOncePerRequestFilter extends OncePerRequestFilter {
    // 每个请求验证Token
    // 解析Token获取用户信息
    // 设置SecurityContext
}
```

### 9.4 判题沙箱安全

- go-judge提供安全的代码执行环境
- 资源限制（CPU时间、内存、栈空间）
- 隔离执行环境

---

## 10. 开发与部署

### 10.1 开发环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Redis
- go-judge服务

### 10.2 配置文件

**application.yaml** 主要配置：

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emiya-oj
    username: root
    password: 123456
  data:
    redis:
      host: 127.0.0.1
      port: 6379

jwt:
  secret-key: xxx
  ttl: 7200000
  token-name: Authorization

GoJudge:
  url: http://127.0.0.1:12812

mybatis-plus:
  mapper-locations: classpath:/mapper/**/*.xml
```

### 10.3 构建命令

```bash
# 编译项目
mvn clean install

# 运行应用
cd oj-service
mvn spring-boot:run

# 打包
mvn clean package -DskipTests
```

### 10.4 数据库初始化

```bash
# 执行SQL脚本
mysql -u root -p < database/oj_schema_and_data.sql
mysql -u root -p < database/rbac_schema.sql
mysql -u root -p < database/rbac_init_data.sql
```

### 10.5 API文档访问

启动应用后访问：
- Swagger UI: http://localhost:8080/swagger-ui.html
- Knife4j: http://localhost:8080/doc.html

---

## 11. 扩展建议

### 11.1 功能扩展

1. **支持更多编程语言**
   - Java, Python, Go, Rust等
   - 在language表添加配置
   - 在GoJudgeRequestBuilder添加构建方法

2. **Special Judge支持**
   - 自定义判题器
   - 支持浮点数精度比较
   - 支持多解答案

3. **竞赛模式**
   - 竞赛创建和管理
   - 排行榜
   - 封榜机制

4. **代码相似度检测**
   - 防抄袭检测
   - 代码指纹比对

5. **题目推荐系统**
   - 基于用户做题记录推荐
   - 难度递进推荐

### 11.2 性能优化

1. **判题队列**
   - 使用消息队列（RabbitMQ/Kafka）
   - 多判题节点负载均衡

2. **缓存优化**
   - 题目信息缓存
   - 测试用例缓存

3. **数据库优化**
   - 读写分离
   - 分库分表（提交记录表）

### 11.3 运维优化

1. **Docker化部署**
   - 应用容器化
   - 判题沙箱容器化

2. **监控告警**
   - 应用监控（Prometheus + Grafana）
   - 判题队列监控
   - 错误告警

3. **日志管理**
   - ELK日志收集
   - 判题日志追踪

---

## 附录

### A. 相关文档

| 文档 | 说明 |
|------|------|
| [README.md](./README.md) | 文档索引 |
| [OJ_README.md](./OJ_README.md) | OJ系统说明 |
| [IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md) | 实现总结 |
| [controller-api-documentation.md](./controller-api-documentation.md) | API文档 |
| [oj-frontend-design.md](./oj-frontend-design.md) | 前端设计文档 |
| [oj-frontend-quickstart.md](./oj-frontend-quickstart.md) | 前端快速开始 |
| [oj-frontend-api-documentation.md](./oj-frontend-api-documentation.md) | 前端API文档 |

### B. 技术参考

- [Spring Boot官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MyBatis-Plus文档](https://baomidou.com/)
- [go-judge文档](https://github.com/criyle/go-judge)
- [Vue 3文档](https://vuejs.org/)
- [Element Plus文档](https://element-plus.org/)

---

**文档版本**: 1.0.0  
**最后更新**: 2024-01-11  
**作者**: EmiyaOJ Team
