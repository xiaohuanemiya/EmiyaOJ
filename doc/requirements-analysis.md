# EmiyaOJ 在线判题系统需求分析文档

**文档版本**: v1.0  
**编写日期**: 2026-01-06  
**项目名称**: EmiyaOJ (Emiya Online Judge)  
**数据库**: emiya-oj (MySQL 8.0)

---

## 目录

1. [项目概述](#1-项目概述)
2. [系统架构](#2-系统架构)
3. [功能需求分析](#3-功能需求分析)
4. [数据库设计分析](#4-数据库设计分析)
5. [技术栈与依赖](#5-技术栈与依赖)
6. [非功能性需求](#6-非功能性需求)
7. [用户角色与权限](#7-用户角色与权限)
8. [业务流程](#8-业务流程)
9. [接口设计](#9-接口设计)
10. [部署与运维](#10-部署与运维)

---

## 1. 项目概述

### 1.1 项目背景

EmiyaOJ 是一个功能完整的在线判题系统（Online Judge），旨在为编程学习者和竞赛参与者提供一个高效、稳定的代码提交和自动评测平台。系统支持多种编程语言，具备完善的权限管理、博客社区、题目管理等功能。

### 1.2 项目目标

- **核心功能**: 提供在线编程题目的提交、自动判题和结果反馈
- **用户体验**: 打造友好的代码编辑界面和直观的题目展示
- **社区建设**: 集成博客系统，促进用户交流和知识分享
- **权限控制**: 实现基于 RBAC 的细粒度权限管理系统
- **可扩展性**: 支持多种编程语言，易于扩展新语言和功能

### 1.3 核心价值

1. **教育价值**: 为编程初学者提供实践平台
2. **竞赛价值**: 支持算法竞赛和编程比赛
3. **社区价值**: 通过博客功能促进知识共享
4. **技术价值**: 采用现代化技术栈，具有良好的架构设计

---

## 2. 系统架构

### 2.1 整体架构

EmiyaOJ 采用**前后端分离**的架构设计：

```
┌─────────────────────────────────────────────────────────┐
│                     前端层 (Frontend)                    │
│  Vue 3.5 + Vite + TypeScript + Element Plus            │
│  - 用户界面                                              │
│  - 代码编辑器 (Monaco Editor)                           │
│  - Markdown 渲染                                        │
└─────────────────────────────────────────────────────────┘
                            ↕ REST API
┌─────────────────────────────────────────────────────────┐
│                     后端层 (Backend)                     │
│  Spring Boot 3.5.5 + Java 21                           │
│  - 业务逻辑层 (Service)                                 │
│  - 数据访问层 (MyBatis-Plus)                           │
│  - 权限控制 (Spring Security)                          │
│  - API 文档 (Knife4j/Swagger)                          │
└─────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────┐
│                   判题引擎 (Judge Engine)                │
│  go-judge                                               │
│  - 代码编译                                              │
│  - 沙箱执行                                              │
│  - 资源限制                                              │
└─────────────────────────────────────────────────────────┘
            ↕                           ↕
┌──────────────────────┐    ┌──────────────────────┐
│   数据库 (Database)   │    │   缓存 (Cache)       │
│   MySQL 8.0          │    │   Redis              │
└──────────────────────┘    └──────────────────────┘
```

### 2.2 模块划分

**核心模块**:
1. **OJ 判题模块** (`oj-service`): 题目管理、代码提交、判题逻辑
2. **公共模块** (`oj-common`): 通用工具类、实体类、常量定义
3. **博客模块**: 博客发布、评论、点赞、标签管理
4. **权限模块**: RBAC 权限管理、用户认证和授权
5. **操作日志模块**: 系统操作审计和追踪

---

## 3. 功能需求分析

### 3.1 在线判题系统 (OJ Core)

#### 3.1.1 题目管理

**功能描述**: 管理编程题目的完整生命周期

**需求细节**:
- **题目基本信息**:
  - 题目标题、描述、输入输出说明
  - 样例输入输出
  - 提示信息、题目来源
  - 难度等级（简单、中等、困难）
  - 时间限制、内存限制、栈限制

- **题目状态管理**:
  - 公开/隐藏状态控制
  - 软删除支持（deleted 字段）
  - 题目统计（通过次数、提交次数）

- **题目分类**:
  - 标签系统（数学、字符串、动态规划等）
  - 多标签关联
  - 按标签筛选

- **题目作者**:
  - 出题人信息记录
  - 创建者和更新者追踪

#### 3.1.2 测试用例管理

**功能描述**: 管理题目的测试数据

**需求细节**:
- 输入数据和预期输出
- 样例标识（is_sample: 是否为样例）
- 分值设置（支持部分分）
- 排序管理
- 软删除支持

#### 3.1.3 编程语言支持

**功能描述**: 多语言支持配置

**当前支持语言**:
- C (gcc-11)
- C++ (g++-11)

**语言配置项**:
- 编译命令模板
- 执行命令模板
- 源文件扩展名
- 可执行文件扩展名
- 是否需要编译
- 时间/内存限制倍数
- 启用/禁用状态

#### 3.1.4 代码提交与判题

**功能描述**: 用户提交代码并自动评测

**提交信息**:
- 题目 ID、用户 ID、语言 ID
- 源代码
- 判题状态（Pending, Judging, Accepted, Wrong Answer, TLE, MLE, RE, CE, System Error）
- 得分、实际使用时间/内存
- 错误信息、编译信息
- 通过率（如 10/10）
- 提交 IP 地址

**判题流程**:
1. 接收代码提交
2. 创建提交记录（状态: Pending）
3. 调用判题引擎
4. 编译代码（如需要）
5. 运行测试用例
6. 记录每个测试用例结果
7. 计算总分和通过率
8. 更新提交状态

#### 3.1.5 测试用例结果

**功能描述**: 记录每个测试用例的详细判题结果

**结果信息**:
- 提交 ID、测试用例 ID
- 判题状态
- 使用时间、使用内存
- 错误信息

### 3.2 博客系统 (Blog System)

#### 3.2.1 博客发布

**功能描述**: 用户发布技术博客和题解

**博客信息**:
- 用户 ID、标题、内容
- 创建时间、更新时间
- 软删除支持

**博客功能**:
- Markdown 支持
- 图片上传和管理
- 标签分类
- 博客索引（按更新时间）

#### 3.2.2 博客互动

**评论功能**:
- 博客 ID、用户 ID、评论内容
- 创建和更新时间
- 软删除支持

**点赞功能**:
- 用户-博客点赞关联
- 唯一索引防止重复点赞
- 点赞时间记录

#### 3.2.3 博客标签

**功能描述**: 博客分类和标签管理

**标签信息**:
- 标签名称、描述
- 博客-标签多对多关联
- 唯一约束防止重复关联

#### 3.2.4 用户博客统计

**功能描述**: 统计用户博客数据

**统计信息**:
- 用户 ID、用户名、昵称
- 博客数量
- 获赞数量

**触发器机制**:
- 博客插入时自动增加博客计数
- 博客删除时自动减少博客计数
- 点赞时自动增加点赞计数
- 取消点赞时自动减少点赞计数
- 用户名/昵称更新时同步更新

### 3.3 权限管理系统 (RBAC)

#### 3.3.1 用户管理

**功能描述**: 系统用户信息管理

**用户信息**:
- 用户名（唯一）、密码
- 昵称、邮箱（唯一）、手机号
- 头像 URL
- 状态（启用/禁用）
- 软删除支持
- 创建者和更新者追踪

#### 3.3.2 角色管理

**功能描述**: 系统角色定义

**角色信息**:
- 角色编码（唯一）
- 角色名称
- 角色描述
- 状态（启用/禁用）
- 软删除支持

**典型角色**:
- 管理员（Admin）
- 普通用户（User）
- 出题人（Author）

#### 3.3.3 权限管理

**功能描述**: 系统资源权限定义

**权限类型**:
1. **菜单权限**: 页面访问控制
2. **按钮权限**: 操作权限控制
3. **接口权限**: API 访问控制

**权限信息**:
- 父权限 ID（树形结构）
- 权限编码（唯一）
- 权限名称
- 权限类型
- 路由路径、组件路径
- 图标、排序
- 状态（启用/禁用）

#### 3.3.4 用户-角色-权限关联

**功能描述**: RBAC 模型实现

**关联关系**:
- 用户-角色关联（多对多）
- 角色-权限关联（多对多）
- 用户-权限关联（特殊权限分配，支持授予/拒绝）

**外键约束**:
- 级联删除支持
- 唯一约束防止重复关联

### 3.4 操作日志系统

**功能描述**: 记录系统操作审计日志

**日志信息**:
- 模块标题
- 业务类型（新增、修改、删除、授权等）
- 方法名称、请求方式
- 操作人员、操作 URL
- 主机地址、操作地点
- 请求参数、返回结果
- 操作状态、错误消息
- 操作时间

**索引优化**:
- 标题、业务类型、状态、操作时间索引

---

## 4. 数据库设计分析

### 4.1 数据库表结构

EmiyaOJ 系统共包含 **22 张数据表**，分为以下几个功能域：

#### 4.1.1 OJ 核心表（8 张）

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `problem` | 题目表 | id, title, description, difficulty, time_limit, memory_limit |
| `test_case` | 测试用例表 | id, problem_id, input, output, is_sample, score |
| `language` | 编程语言表 | id, name, version, compile_command, execute_command |
| `submission` | 提交记录表 | id, problem_id, user_id, language_id, code, status, score |
| `submission_result` | 测试用例结果表 | id, submission_id, test_case_id, status, time_used, memory_used |
| `tag` | 标签表 | id, name, description, color |
| `problem_tag` | 题目标签关联表 | id, problem_id, tag_id |
| `operation_log` | 操作日志表 | id, title, business_type, oper_name, oper_time |

#### 4.1.2 博客系统表（7 张）

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `blog` | 博客表 | id, user_id, title, content, create_time, update_time |
| `blog_comment` | 博客评论表 | id, blog_id, user_id, content |
| `blog_star` | 博客点赞表 | id, user_id, blog_id |
| `blog_picture` | 博客图片表 | url, deleted |
| `blog_tag` | 博客标签表 | id, tag, desc |
| `blog_tag_association` | 博客标签关联表 | id, blog_id, tag_id |
| `user_blog` | 用户博客统计表 | user_id, username, nickname, blog_count, star_count |

#### 4.1.3 权限管理表（7 张）

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `user` | 用户表 | id, username, password, nickname, email, phone, avatar, status |
| `role` | 角色表 | id, role_code, role_name, description, status |
| `permission` | 权限表 | id, parent_id, permission_code, permission_name, permission_type |
| `user_role` | 用户角色关联表 | id, user_id, role_id |
| `role_permission` | 角色权限关联表 | id, role_id, permission_id |
| `user_permission` | 用户权限关联表 | id, user_id, permission_id, permission_type |
| `operation_log` | 操作日志表 | （已在 OJ 核心表中列出） |

### 4.2 数据库设计特点

#### 4.2.1 软删除设计

多个表采用软删除机制（`deleted` 字段），避免数据丢失：
- `user`, `role`, `permission`
- `problem`, `test_case`
- `blog`, `blog_comment`, `blog_star`, `blog_picture`

#### 4.2.2 审计字段

关键表包含审计字段：
- `create_time`, `update_time`: 时间戳
- `create_by`, `update_by`: 创建者和更新者 ID

#### 4.2.3 索引优化

**主要索引**:
- 外键字段索引（如 `user_id`, `problem_id`, `blog_id`）
- 状态字段索引（`status`, `deleted`）
- 时间字段索引（`create_time`, `update_time`, `oper_time`）
- 唯一索引（`username`, `email`, `uk_problem_tag`, `uk_user_role`）

#### 4.2.4 触发器机制

**博客统计触发器**:
1. `after_blog_insert`: 博客插入后增加博客计数
2. `after_blog_delete`: 博客删除后减少博客计数
3. `after_blog_star_insert`: 点赞后增加点赞计数
4. `after_blog_star_delete`: 取消点赞后减少点赞计数
5. `after_user_username_update`: 用户名更新后同步
6. `after_user_nickname_update`: 昵称更新后同步

#### 4.2.5 外键约束

**级联删除**:
- `user_role`: 用户或角色删除时级联删除关联
- `role_permission`: 角色或权限删除时级联删除关联
- `user_permission`: 用户或权限删除时级联删除关联

### 4.3 数据库字符集

- **字符集**: UTF8MB4
- **排序规则**: utf8mb4_unicode_ci (OJ 表) / utf8mb4_general_ci (博客表)
- **存储引擎**: InnoDB

---

## 5. 技术栈与依赖

### 5.1 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21 | 编程语言 |
| Spring Boot | 3.5.5 | 应用框架 |
| Spring Security | 6.x | 安全框架 |
| MyBatis-Plus | 3.5.x | ORM 框架 |
| MySQL | 8.0 | 关系数据库 |
| Redis | - | 缓存中间件 |
| go-judge | - | 判题引擎 |
| Knife4j (Swagger) | - | API 文档 |
| Maven | - | 项目管理 |
| Lombok | - | 代码简化 |

### 5.2 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue.js | 3.5 | 前端框架 |
| Vite | 5.x | 构建工具 |
| TypeScript | 5.x | 类型系统 |
| Element Plus | - | UI 组件库 |
| Pinia | - | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Axios | - | HTTP 客户端 |
| Monaco Editor | - | 代码编辑器 |
| Markdown-it | - | Markdown 渲染 |
| Highlight.js | - | 代码高亮 |

### 5.3 开发工具

- **IDE**: IntelliJ IDEA / VSCode
- **数据库工具**: Navicat / DBeaver
- **API 测试**: Postman / Knife4j UI
- **版本控制**: Git

---

## 6. 非功能性需求

### 6.1 性能需求

**响应时间**:
- 页面加载: < 2 秒
- API 响应: < 500 毫秒
- 判题时间: 视题目限制而定

**并发支持**:
- 同时在线用户: 1000+
- 判题队列: 支持异步处理

**数据库性能**:
- 索引优化
- 查询优化
- 连接池配置

### 6.2 可靠性需求

**数据可靠性**:
- 数据库定期备份
- 事务完整性保证
- 软删除机制防止误删

**系统稳定性**:
- 异常处理机制
- 日志记录完整
- 错误追踪

### 6.3 安全性需求

**认证与授权**:
- JWT Token 认证
- Spring Security 集成
- RBAC 权限控制
- 密码加密存储

**数据安全**:
- SQL 注入防护
- XSS 攻击防护
- CSRF 保护
- 敏感信息脱敏

**判题安全**:
- 沙箱隔离执行
- 资源限制（CPU、内存、时间）
- 恶意代码防护

### 6.4 可扩展性需求

**水平扩展**:
- 无状态服务设计
- 负载均衡支持
- 分布式部署

**功能扩展**:
- 插件化语言支持
- 模块化设计
- 接口标准化

### 6.5 可维护性需求

**代码质量**:
- 代码规范遵守
- 注释完整
- 单元测试覆盖

**文档完善**:
- API 文档（Knife4j）
- 数据库文档
- 部署文档
- 开发指南

### 6.6 兼容性需求

**浏览器支持**:
- Chrome (推荐)
- Firefox
- Safari
- Edge

**数据库版本**:
- MySQL 8.0+

---

## 7. 用户角色与权限

### 7.1 用户角色定义

#### 7.1.1 管理员 (Admin)

**权限范围**: 系统所有功能

**主要职责**:
- 用户管理（创建、编辑、删除、禁用）
- 角色和权限管理
- 题目管理（创建、编辑、删除、隐藏/公开）
- 测试用例管理
- 语言配置管理
- 博客管理（删除不当内容）
- 系统配置
- 操作日志查看

#### 7.1.2 出题人 (Author)

**权限范围**: 题目相关功能

**主要职责**:
- 创建题目
- 编辑自己的题目
- 管理测试用例
- 查看题目统计

#### 7.1.3 普通用户 (User)

**权限范围**: 基础功能

**主要职责**:
- 浏览公开题目
- 提交代码
- 查看提交记录和结果
- 发布博客
- 评论和点赞
- 个人信息管理

#### 7.1.4 游客 (Guest)

**权限范围**: 只读访问

**主要职责**:
- 浏览公开题目
- 查看博客文章

### 7.2 权限矩阵

| 功能模块 | 游客 | 普通用户 | 出题人 | 管理员 |
|---------|------|----------|--------|--------|
| 浏览题目 | ✓ | ✓ | ✓ | ✓ |
| 提交代码 | ✗ | ✓ | ✓ | ✓ |
| 创建题目 | ✗ | ✗ | ✓ | ✓ |
| 管理题目 | ✗ | ✗ | 部分 | ✓ |
| 发布博客 | ✗ | ✓ | ✓ | ✓ |
| 管理博客 | ✗ | 自己的 | 自己的 | ✓ |
| 用户管理 | ✗ | ✗ | ✗ | ✓ |
| 权限管理 | ✗ | ✗ | ✗ | ✓ |
| 系统配置 | ✗ | ✗ | ✗ | ✓ |

### 7.3 权限控制实现

**后端权限控制**:
- Spring Security 拦截器
- 注解式权限控制（`@PreAuthorize`）
- 方法级权限验证

**前端权限控制**:
- 路由守卫
- 按钮级权限控制
- 菜单动态渲染

---

## 8. 业务流程

### 8.1 用户注册与登录流程

```
用户访问 → 注册表单 → 输入信息 → 后端验证 → 创建用户
                                    ↓
                             分配默认角色(User)
                                    ↓
                             返回注册成功

用户登录 → 输入凭据 → 后端验证 → 生成 JWT Token
                                    ↓
                            返回Token+用户信息
                                    ↓
                            前端存储Token
                                    ↓
                          后续请求携带Token
```

### 8.2 题目浏览与提交流程

```
用户浏览题目列表 → 筛选/搜索 → 查看题目详情
                                    ↓
                           查看题目描述、样例
                                    ↓
                           选择编程语言
                                    ↓
                           编写代码 (Monaco Editor)
                                    ↓
                           提交代码
                                    ↓
                    创建提交记录 (status: Pending)
                                    ↓
                           进入判题队列
                                    ↓
                    判题引擎处理 (status: Judging)
                                    ↓
                           编译代码
                                    ↓
                    运行测试用例 (逐个测试)
                                    ↓
                    记录每个测试用例结果
                                    ↓
                    计算总分和通过率
                                    ↓
            更新提交状态 (Accepted/WA/TLE/MLE/RE/CE)
                                    ↓
            返回判题结果给用户
```

### 8.3 判题详细流程

```
1. 接收提交
   ├─ 验证用户权限
   ├─ 验证题目存在
   ├─ 验证语言支持
   └─ 创建提交记录

2. 代码编译 (如需要)
   ├─ 获取编译命令模板
   ├─ 替换模板变量
   ├─ 执行编译
   ├─ 检查编译结果
   └─ 返回编译错误 (如有)

3. 测试用例执行
   FOR EACH 测试用例:
   ├─ 获取测试输入
   ├─ 设置资源限制 (时间/内存)
   ├─ 沙箱中执行程序
   ├─ 捕获程序输出
   ├─ 比对输出与期望
   ├─ 记录使用资源
   └─ 记录测试结果

4. 结果汇总
   ├─ 统计通过测试数
   ├─ 计算总分
   ├─ 确定最终状态
   ├─ 更新提交记录
   └─ 更新题目统计

5. 返回结果
   └─ 通知前端显示结果
```

### 8.4 博客发布流程

```
用户编辑博客 → 输入标题和内容 (Markdown)
                     ↓
              选择标签
                     ↓
              预览博客
                     ↓
              提交发布
                     ↓
       创建博客记录 (blog 表)
                     ↓
       创建标签关联 (blog_tag_association 表)
                     ↓
       触发器: 更新 user_blog 计数
                     ↓
       返回发布成功
                     ↓
       博客列表展示
```

### 8.5 权限验证流程

```
前端发起请求 → 携带 JWT Token
                     ↓
       后端拦截器验证 Token
                     ↓
            Token 有效?
       ┌───────┴───────┐
      NO              YES
       ↓               ↓
  返回 401      解析用户信息
 (未授权)             ↓
              查询用户角色
                     ↓
              查询角色权限
                     ↓
              检查资源权限
                     ↓
            有权限访问?
       ┌───────┴───────┐
      NO              YES
       ↓               ↓
  返回 403        执行业务逻辑
  (禁止访问)           ↓
               返回业务数据
```

---

## 9. 接口设计

### 9.1 接口规范

**基础路径**: `/api/v1`

**响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

**错误码**:
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未认证
- `403`: 无权限
- `404`: 资源不存在
- `500`: 服务器错误

### 9.2 核心接口列表

#### 9.2.1 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/auth/register` | 用户注册 |
| POST | `/auth/login` | 用户登录 |
| POST | `/auth/logout` | 用户登出 |
| GET | `/auth/info` | 获取当前用户信息 |

#### 9.2.2 题目接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/problems` | 获取题目列表 |
| GET | `/problems/{id}` | 获取题目详情 |
| POST | `/problems` | 创建题目 (Author+) |
| PUT | `/problems/{id}` | 更新题目 (Author+) |
| DELETE | `/problems/{id}` | 删除题目 (Admin) |
| GET | `/problems/{id}/testcases` | 获取测试用例 (Admin) |

#### 9.2.3 提交接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/submissions` | 提交代码 |
| GET | `/submissions` | 获取提交列表 |
| GET | `/submissions/{id}` | 获取提交详情 |
| GET | `/submissions/{id}/result` | 获取判题结果 |

#### 9.2.4 语言接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/languages` | 获取支持的语言列表 |
| GET | `/languages/{id}` | 获取语言详情 |
| POST | `/languages` | 添加语言 (Admin) |
| PUT | `/languages/{id}` | 更新语言 (Admin) |

#### 9.2.5 博客接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/blogs` | 获取博客列表 |
| GET | `/blogs/{id}` | 获取博客详情 |
| POST | `/blogs` | 发布博客 |
| PUT | `/blogs/{id}` | 更新博客 |
| DELETE | `/blogs/{id}` | 删除博客 |
| POST | `/blogs/{id}/star` | 点赞博客 |
| DELETE | `/blogs/{id}/star` | 取消点赞 |
| GET | `/blogs/{id}/comments` | 获取评论 |
| POST | `/blogs/{id}/comments` | 发表评论 |

#### 9.2.6 用户接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/users` | 获取用户列表 (Admin) |
| GET | `/users/{id}` | 获取用户信息 |
| PUT | `/users/{id}` | 更新用户信息 |
| PUT | `/users/{id}/status` | 更新用户状态 (Admin) |

#### 9.2.7 角色权限接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/roles` | 获取角色列表 (Admin) |
| POST | `/roles` | 创建角色 (Admin) |
| PUT | `/roles/{id}` | 更新角色 (Admin) |
| GET | `/permissions` | 获取权限树 (Admin) |
| PUT | `/roles/{id}/permissions` | 分配权限 (Admin) |

### 9.3 分页参数

```json
{
  "page": 1,
  "size": 20,
  "sort": "createTime",
  "order": "desc"
}
```

### 9.4 鉴权方式

**Header**:
```
Authorization: Bearer <JWT_TOKEN>
```

---

## 10. 部署与运维

### 10.1 部署架构

**推荐部署架构**:
```
Internet
    ↓
Nginx (反向代理 + 静态资源)
    ↓
├─ Frontend (Vue 应用)
└─ Backend (Spring Boot 应用)
    ↓
├─ MySQL 8.0
├─ Redis
└─ go-judge (判题引擎)
```

### 10.2 环境要求

**服务器配置** (最低):
- CPU: 2 核
- 内存: 4GB
- 存储: 50GB
- 操作系统: Linux (Ubuntu 20.04+ / CentOS 7+)

**软件依赖**:
- Java 21
- MySQL 8.0
- Redis 6.0+
- Nginx 1.18+
- go-judge

### 10.3 部署步骤

#### 10.3.1 数据库初始化

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE `emiya-oj` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构和数据
mysql -u root -p emiya-oj < database/emiya-oj_final.sql
mysql -u root -p emiya-oj < database/rbac_init_data.sql
```

#### 10.3.2 后端部署

```bash
# 编译打包
mvn clean package -DskipTests

# 运行应用
java -jar oj-service/target/oj-service-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

#### 10.3.3 前端部署

```bash
# 构建生产版本
npm run build

# 配置 Nginx
# 将 dist 目录部署到 Nginx 静态目录
```

#### 10.3.4 Nginx 配置示例

```nginx
server {
    listen 80;
    server_name emiyaoj.example.com;

    # 前端静态资源
    location / {
        root /var/www/emiyaoj/frontend/dist;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # API 文档
    location /doc.html {
        proxy_pass http://localhost:8080;
    }
}
```

### 10.4 监控与日志

**应用监控**:
- Spring Boot Actuator
- 日志聚合 (ELK)
- 性能监控 (Prometheus + Grafana)

**日志管理**:
- 应用日志: `/logs/emiyaoj.log`
- 操作日志: `operation_log` 表
- Nginx 日志: `/var/log/nginx/`

### 10.5 备份策略

**数据库备份**:
```bash
# 每日自动备份
mysqldump -u root -p emiya-oj > backup/emiya-oj_$(date +%Y%m%d).sql
```

**代码备份**:
- Git 版本控制
- 定期镜像备份

---

## 11. 总结

### 11.1 项目特色

1. **完整的 OJ 功能**: 题目管理、多语言支持、自动判题
2. **丰富的社区功能**: 博客系统、评论、点赞、标签
3. **完善的权限体系**: RBAC 模型、细粒度权限控制
4. **现代化技术栈**: Spring Boot 3.5 + Vue 3.5 + Java 21
5. **良好的数据库设计**: 合理的索引、触发器、外键约束
6. **安全的判题环境**: 沙箱隔离、资源限制

### 11.2 系统亮点

- **软删除机制**: 防止误删数据
- **审计追踪**: 完整的操作日志和创建者追踪
- **性能优化**: 索引优化、缓存支持
- **扩展性强**: 模块化设计、插件化语言支持
- **文档完善**: API 文档、设计文档齐全

### 11.3 未来规划

1. **功能扩展**:
   - 竞赛模式支持
   - 题目讨论区
   - 用户排行榜
   - 题解系统

2. **性能优化**:
   - 分布式判题
   - 缓存优化
   - 数据库分表

3. **用户体验**:
   - 移动端适配
   - 主题切换
   - 国际化支持

4. **语言支持**:
   - Python
   - Java
   - JavaScript/Node.js
   - Go
   - Rust

---

**文档结束**

如有疑问或需要补充，请联系开发团队。
