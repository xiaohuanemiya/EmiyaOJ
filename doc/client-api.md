# EmiyaOJ 前端客户端API接口文档

本文档基于 `LanguageController`、`ProblemController`、`SubmissionController`、`AuthController` 为 Vue3 + TypeScript 前端提供接口说明。

## 目录

- [1. 通用说明](#1-通用说明)
  - [1.1 基础响应结构](#11-基础响应结构)
  - [1.2 分页请求参数](#12-分页请求参数)
  - [1.3 分页响应结构](#13-分页响应结构)
  - [1.4 认证方式](#14-认证方式)
- [2. Token 解析说明](#2-token-解析说明)
  - [2.1 Token 结构](#21-token-结构)
  - [2.2 前端 Token 解析示例](#22-前端-token-解析示例)
  - [2.3 Token 解析后数据结构](#23-token-解析后数据结构)
- [3. 登录管理接口（AuthController）](#3-登录管理接口authcontroller)
  - [3.1 用户登录](#31-用户登录)
  - [3.2 退出登录](#32-退出登录)
- [4. 语言管理接口（LanguageController）](#4-语言管理接口languagecontroller)
  - [4.1 获取所有可用语言](#41-获取所有可用语言)
  - [4.2 获取语言详情](#42-获取语言详情)
- [5. 题目管理接口（ProblemController）](#5-题目管理接口problemcontroller)
  - [5.1 分页查询题目](#51-分页查询题目)
  - [5.2 获取题目详情](#52-获取题目详情)
- [6. 提交管理接口（SubmissionController）](#6-提交管理接口submissioncontroller)
  - [6.1 提交代码](#61-提交代码)
  - [6.2 分页查询提交记录](#62-分页查询提交记录)
  - [6.3 获取提交详情](#63-获取提交详情)
- [7. TypeScript 类型定义](#7-typescript-类型定义)

---

## 1. 通用说明

### 1.1 基础响应结构

所有接口返回统一的响应格式：

```typescript
interface ResponseResult<T> {
  code: number;    // 状态码，200表示成功
  msg: string;     // 响应消息
  data: T;         // 响应数据
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {}
}
```

### 1.2 分页请求参数

分页接口统一使用以下查询参数：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNo | number | 是 | 页码，从1开始 |
| pageSize | number | 是 | 每页记录数 |
| sortBy | string | 否 | 排序字段 |
| isAsc | boolean | 否 | 是否升序，默认false |

### 1.3 分页响应结构

```typescript
interface PageVO<T> {
  total: number;   // 总记录数
  pages: number;   // 总页数
  list: T[];       // 数据列表
}
```

### 1.4 认证方式

需要认证的接口需在请求头中携带 JWT Token：

```
Authorization: Bearer <token>
```

---

## 2. Token 解析说明

### 2.1 Token 结构

EmiyaOJ 使用 JWT（JSON Web Token）进行身份认证。Token 由三部分组成：
- Header（头部）
- Payload（载荷）
- Signature（签名）

### 2.2 前端 Token 解析示例

在 Vue3 + TypeScript 项目中，可以使用 `jwt-decode` 库解析 Token：

**安装依赖：**
```bash
npm install jwt-decode
```

**解析 Token：**
```typescript
import { jwtDecode } from 'jwt-decode';

// Token 载荷类型定义
interface JwtPayload {
  userLogin: UserLoginInfo;
  exp: number;  // 过期时间戳（秒）
}

// 解析 Token
function parseToken(token: string): JwtPayload | null {
  try {
    const decoded = jwtDecode<JwtPayload>(token);
    return decoded;
  } catch (error) {
    console.error('Token 解析失败:', error);
    return null;
  }
}

// 检查 Token 是否过期
function isTokenExpired(token: string): boolean {
  const decoded = parseToken(token);
  if (!decoded) return true;
  
  // exp 是秒级时间戳，需要转换为毫秒
  const currentTime = Date.now() / 1000;
  return decoded.exp < currentTime;
}

// 使用示例
const token = localStorage.getItem('token');
if (token) {
  const payload = parseToken(token);
  if (payload && !isTokenExpired(token)) {
    console.log('用户信息:', payload.userLogin);
    console.log('用户名:', payload.userLogin.user.username);
    console.log('角色:', payload.userLogin.roles);
    console.log('权限:', payload.userLogin.permissions);
  }
}
```

### 2.3 Token 解析后数据结构

Token 解析后的 `userLogin` 字段包含以下信息：

```typescript
interface UserLoginInfo {
  accountNonExpired: boolean;     // 账户是否未过期
  accountNonLocked: boolean;      // 账户是否未锁定
  credentialsNonExpired: boolean; // 凭证是否未过期
  enabled: boolean;               // 账户是否启用
  password: string;               // 加密后的密码
  permissions: string[];          // 权限列表
  roles: string[];                // 角色列表
  user: User;                     // 用户详细信息
  username: string;               // 用户名
}

interface User {
  createTime: string;    // 创建时间
  deleted: number;       // 删除标记：0-未删除
  id: number;            // 用户ID（注意：是bigint类型）
  nickname: string;      // 昵称
  password: string;      // 加密后的密码
  status: number;        // 状态：1-正常
  updateTime: string;    // 更新时间
  username: string;      // 用户名
}
```

**Token 解析示例数据：**
```json
{
  "userLogin": {
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true,
    "enabled": true,
    "password": "$2a$10$ui0jZ5pY///bAiJIS1MkLeWD4t/nKPc3bxBOLsgglhqQsQ6aX9iFa",
    "permissions": [
      "USER", "USER.LIST", "USER.ADD", "USER.DELETE", "USER.EDIT",
      "ROLE", "ROLE.LIST", "ROLE.ADD", "ROLE.DELETE", "ROLE.EDIT", "ROLE.ASSIGN",
      "PERMISSION", "PERMISSION.LIST", "PERMISSION.ADD", "PERMISSION.DELETE", "PERMISSION.EDIT", "PERMISSION.ASSIGN",
      "BLOG", "BLOG.LIST", "BLOG.ADD", "BLOG.DELETE", "BLOG.EDIT", "BLOG.STAR", "BLOG.COMMENT",
      "COMMENT.LIST", "COMMENT.ADD", "COMMENT.DELETE",
      "PROBLEM", "PROBLEM.LIST", "PROBLEM.ADD", "PROBLEM.DELETE", "PROBLEM.EDIT",
      "LANGUAGE", "LANGUAGE.LIST",
      "TESTCASE", "TESTCASE.LIST", "TESTCASE.DELETE", "TESTCASE.EDIT", "TESTCASE.ADD"
    ],
    "roles": ["ROLE_ADMIN"],
    "user": {
      "createTime": "2025-09-24 15:01:55",
      "deleted": 0,
      "id": 1970745494857310210,
      "nickname": "emiya",
      "password": "$2a$10$ui0jZ5pY///bAiJIS1MkLeWD4t/nKPc3bxBOLsgglhqQsQ6aX9iFa",
      "status": 1,
      "updateTime": "2025-09-24 15:01:55",
      "username": "emiya"
    },
    "username": "emiya"
  },
  "exp": 1764687485
}
```

---

## 3. 登录管理接口（AuthController）

### 3.1 用户登录

**接口说明：** 用户登录，获取认证Token

**请求方式：** `POST`

**请求路径：** `/auth/login`

**是否需要认证：** 否

**请求头：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Content-Type | string | 是 | application/json |

**请求体：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名，长度5-20位 |
| password | string | 是 | 密码，长度5-16位 |

**请求示例：**
```json
{
  "username": "emiya",
  "password": "123456"
}
```

**响应数据：**
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | string | 用户ID（字符串格式，避免精度丢失） |
| username | string | 用户名 |
| name | string | 姓名 |
| token | string | JWT令牌 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": "1970745494857310210",
    "username": "emiya",
    "name": "emiya",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**错误响应：**
| code | msg | 说明 |
|------|-----|------|
| 409 | 用户已登录 | 当前用户已有有效token |
| 500 | 用户名或密码错误 | 认证失败 |

### 3.2 退出登录

**接口说明：** 用户退出登录，清除认证信息

**请求方式：** `POST`

**请求路径：** `/auth/logout`

**是否需要认证：** 是

**请求头：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | string | 是 | Bearer {token} |

**请求体：** 无

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

---

## 4. 语言管理接口（LanguageController）

### 4.1 获取所有可用语言

**接口说明：** 获取系统支持的所有编程语言列表

**请求方式：** `GET`

**请求路径：** `/language/list`

**是否需要认证：** 否

**请求参数：** 无

**响应数据：** `Language[]`

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | number | 语言ID |
| name | string | 语言名称，如 C, C++, Java, Python |
| version | string | 版本，如 gcc-11, jdk-21, python-3.11 |
| compileCommand | string | 编译命令模板 |
| executeCommand | string | 执行命令模板 |
| sourceFileExt | string | 源文件扩展名，如 .c, .cpp, .java, .py |
| executableExt | string | 可执行文件扩展名 |
| isCompiled | number | 是否需要编译：0-否，1-是 |
| timeLimitMultiplier | number | 时间限制倍数 |
| memoryLimitMultiplier | number | 内存限制倍数 |
| status | number | 状态：0-禁用，1-启用 |
| createTime | string | 创建时间 |
| updateTime | string | 更新时间 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": 1,
      "name": "C",
      "version": "gcc-11",
      "compileCommand": "gcc -o {output} {source}",
      "executeCommand": "./{output}",
      "sourceFileExt": ".c",
      "executableExt": "",
      "isCompiled": 1,
      "timeLimitMultiplier": 1.0,
      "memoryLimitMultiplier": 1.0,
      "status": 1,
      "createTime": "2025-01-01 00:00:00",
      "updateTime": "2025-01-01 00:00:00"
    },
    {
      "id": 2,
      "name": "Python",
      "version": "python-3.11",
      "compileCommand": null,
      "executeCommand": "python3 {source}",
      "sourceFileExt": ".py",
      "executableExt": null,
      "isCompiled": 0,
      "timeLimitMultiplier": 2.0,
      "memoryLimitMultiplier": 2.0,
      "status": 1,
      "createTime": "2025-01-01 00:00:00",
      "updateTime": "2025-01-01 00:00:00"
    }
  ]
}
```

### 4.2 获取语言详情

**接口说明：** 根据ID获取单个语言的详细信息

**请求方式：** `GET`

**请求路径：** `/language/{id}`

**是否需要认证：** 否

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | number | 是 | 语言ID |

**响应数据：** `Language`（同上）

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "name": "C",
    "version": "gcc-11",
    "compileCommand": "gcc -o {output} {source}",
    "executeCommand": "./{output}",
    "sourceFileExt": ".c",
    "executableExt": "",
    "isCompiled": 1,
    "timeLimitMultiplier": 1.0,
    "memoryLimitMultiplier": 1.0,
    "status": 1,
    "createTime": "2025-01-01 00:00:00",
    "updateTime": "2025-01-01 00:00:00"
  }
}
```

**错误响应：**
| code | msg | 说明 |
|------|-----|------|
| 500 | 语言不存在 | 指定ID的语言不存在 |

---

## 5. 题目管理接口（ProblemController）

### 5.1 分页查询题目

**接口说明：** 分页获取题目列表，支持按难度、状态、关键词筛选

**请求方式：** `GET`

**请求路径：** `/problem/page`

**是否需要认证：** 否

**查询参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNo | number | 是 | 页码 |
| pageSize | number | 是 | 每页大小 |
| sortBy | string | 否 | 排序字段 |
| isAsc | boolean | 否 | 是否升序 |
| difficulty | number | 否 | 难度筛选：1-简单，2-中等，3-困难 |
| status | number | 否 | 状态筛选 |
| keyword | string | 否 | 关键词搜索（搜索标题） |

**响应数据：** `PageVO<ProblemVO>`

**ProblemVO 字段说明：**
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | number | 题目ID |
| title | string | 题目标题 |
| description | string | 题目描述 |
| inputDescription | string | 输入描述 |
| outputDescription | string | 输出描述 |
| sampleInput | string | 样例输入 |
| sampleOutput | string | 样例输出 |
| hint | string | 提示信息 |
| difficulty | number | 难度：1-简单，2-中等，3-困难 |
| timeLimit | number | CPU时间限制（毫秒） |
| memoryLimit | number | 内存限制（MB） |
| acceptCount | number | 通过次数 |
| submitCount | number | 提交次数 |
| createTime | string | 创建时间 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 100,
    "pages": 10,
    "list": [
      {
        "id": 1,
        "title": "两数之和",
        "description": "给定一个整数数组 nums 和一个目标值 target...",
        "inputDescription": "第一行输入数组长度n...",
        "outputDescription": "输出两个整数的下标...",
        "sampleInput": "4\n2 7 11 15\n9",
        "sampleOutput": "0 1",
        "hint": "注意数组下标从0开始",
        "difficulty": 1,
        "timeLimit": 1000,
        "memoryLimit": 256,
        "acceptCount": 1234,
        "submitCount": 5678,
        "createTime": "2025-01-01 00:00:00"
      }
    ]
  }
}
```

### 5.2 获取题目详情

**接口说明：** 根据ID获取题目的详细信息

**请求方式：** `GET`

**请求路径：** `/problem/{id}`

**是否需要认证：** 否

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | number | 是 | 题目ID |

**响应数据：** `ProblemVO`（同上）

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "title": "两数之和",
    "description": "给定一个整数数组 nums 和一个目标值 target...",
    "inputDescription": "第一行输入数组长度n...",
    "outputDescription": "输出两个整数的下标...",
    "sampleInput": "4\n2 7 11 15\n9",
    "sampleOutput": "0 1",
    "hint": "注意数组下标从0开始",
    "difficulty": 1,
    "timeLimit": 1000,
    "memoryLimit": 256,
    "acceptCount": 1234,
    "submitCount": 5678,
    "createTime": "2025-01-01 00:00:00"
  }
}
```

**错误响应：**
| code | msg | 说明 |
|------|-----|------|
| 500 | 题目不存在 | 指定ID的题目不存在 |

---

## 6. 提交管理接口（SubmissionController）

### 6.1 提交代码

**接口说明：** 提交代码进行判题

**请求方式：** `POST`

**请求路径：** `/submission/submit`

**是否需要认证：** 是

**请求头：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Content-Type | string | 是 | application/json |
| Authorization | string | 是 | Bearer {token} |

**请求体：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| problemId | number | 是 | 题目ID |
| languageId | number | 是 | 语言ID |
| code | string | 是 | 源代码 |

**请求示例：**
```json
{
  "problemId": 1,
  "languageId": 1,
  "code": "#include <stdio.h>\nint main() { printf(\"Hello World\"); return 0; }"
}
```

**响应数据：** `number`（提交记录ID）

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": 123456789
}
```

### 6.2 分页查询提交记录

**接口说明：** 分页获取提交记录列表，支持按题目、用户筛选

**请求方式：** `GET`

**请求路径：** `/submission/page`

**是否需要认证：** 否

**查询参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNo | number | 是 | 页码 |
| pageSize | number | 是 | 每页大小 |
| sortBy | string | 否 | 排序字段 |
| isAsc | boolean | 否 | 是否升序 |
| problemId | number | 否 | 题目ID筛选 |
| userId | number | 否 | 用户ID筛选 |

**响应数据：** `PageVO<SubmissionVO>`

**SubmissionVO 字段说明：**
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | number | 提交ID |
| problemId | number | 题目ID |
| problemTitle | string | 题目标题 |
| userId | number | 用户ID |
| username | string | 用户名 |
| languageId | number | 语言ID |
| languageName | string | 语言名称 |
| status | string | 判题状态 |
| score | number | 得分 |
| timeUsed | number | 实际使用时间（毫秒） |
| memoryUsed | number | 实际使用内存（KB） |
| passRate | string | 通过率 |
| createTime | string | 提交时间 |

**判题状态说明：**
| 状态值 | 说明 |
|--------|------|
| PENDING | 等待判题 |
| JUDGING | 判题中 |
| ACCEPTED | 通过 |
| WRONG_ANSWER | 答案错误 |
| TIME_LIMIT_EXCEEDED | 超时 |
| MEMORY_LIMIT_EXCEEDED | 内存超限 |
| RUNTIME_ERROR | 运行时错误 |
| COMPILE_ERROR | 编译错误 |
| SYSTEM_ERROR | 系统错误 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 50,
    "pages": 5,
    "list": [
      {
        "id": 123456789,
        "problemId": 1,
        "problemTitle": "两数之和",
        "userId": 1970745494857310210,
        "username": "emiya",
        "languageId": 1,
        "languageName": "C",
        "status": "ACCEPTED",
        "score": 100,
        "timeUsed": 15,
        "memoryUsed": 1024,
        "passRate": "10/10",
        "createTime": "2025-01-01 12:00:00"
      }
    ]
  }
}
```

### 6.3 获取提交详情

**接口说明：** 根据ID获取提交记录的详细信息

**请求方式：** `GET`

**请求路径：** `/submission/{id}`

**是否需要认证：** 否

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | number | 是 | 提交记录ID |

**响应数据：** `SubmissionVO`（同上）

**响应示例：**
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 123456789,
    "problemId": 1,
    "problemTitle": "两数之和",
    "userId": 1970745494857310210,
    "username": "emiya",
    "languageId": 1,
    "languageName": "C",
    "status": "ACCEPTED",
    "score": 100,
    "timeUsed": 15,
    "memoryUsed": 1024,
    "passRate": "10/10",
    "createTime": "2025-01-01 12:00:00"
  }
}
```

---

## 7. TypeScript 类型定义

以下是完整的 TypeScript 类型定义，可直接用于 Vue3 项目：

```typescript
// ==================== 通用类型 ====================

/**
 * 通用响应结构
 */
export interface ResponseResult<T> {
  code: number;
  msg: string;
  data: T;
}

/**
 * 分页请求参数
 */
export interface PageDTO {
  pageNo: number;
  pageSize: number;
  sortBy?: string;
  isAsc?: boolean;
}

/**
 * 分页响应结构
 */
export interface PageVO<T> {
  total: number;
  pages: number;
  list: T[];
}

// ==================== 用户认证相关 ====================

/**
 * 登录请求参数
 */
export interface UserLoginDTO {
  username: string;
  password: string;
}

/**
 * 登录响应数据
 */
export interface UserLoginVO {
  id: string;
  username: string;
  name: string;
  token: string;
}

/**
 * Token 载荷
 */
export interface JwtPayload {
  userLogin: UserLoginInfo;
  exp: number;
}

/**
 * 用户登录信息（Token解析后）
 */
export interface UserLoginInfo {
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
  password: string;
  permissions: string[];
  roles: string[];
  user: User;
  username: string;
}

/**
 * 用户详细信息
 */
export interface User {
  createTime: string;
  deleted: number;
  id: number;
  nickname: string;
  password: string;
  status: number;
  updateTime: string;
  username: string;
}

// ==================== 语言相关 ====================

/**
 * 编程语言
 */
export interface Language {
  id: number;
  name: string;
  version: string;
  compileCommand: string | null;
  executeCommand: string;
  sourceFileExt: string;
  executableExt: string | null;
  isCompiled: number;
  timeLimitMultiplier: number;
  memoryLimitMultiplier: number;
  status: number;
  createTime: string;
  updateTime: string;
}

// ==================== 题目相关 ====================

/**
 * 题目难度枚举
 */
export enum ProblemDifficulty {
  EASY = 1,
  MEDIUM = 2,
  HARD = 3
}

/**
 * 题目VO
 */
export interface ProblemVO {
  id: number;
  title: string;
  description: string;
  inputDescription: string;
  outputDescription: string;
  sampleInput: string;
  sampleOutput: string;
  hint: string;
  difficulty: ProblemDifficulty;
  timeLimit: number;
  memoryLimit: number;
  acceptCount: number;
  submitCount: number;
  createTime: string;
}

// ==================== 提交相关 ====================

/**
 * 判题状态枚举
 */
export enum JudgeStatus {
  PENDING = 'PENDING',
  JUDGING = 'JUDGING',
  ACCEPTED = 'ACCEPTED',
  WRONG_ANSWER = 'WRONG_ANSWER',
  TIME_LIMIT_EXCEEDED = 'TIME_LIMIT_EXCEEDED',
  MEMORY_LIMIT_EXCEEDED = 'MEMORY_LIMIT_EXCEEDED',
  RUNTIME_ERROR = 'RUNTIME_ERROR',
  COMPILE_ERROR = 'COMPILE_ERROR',
  SYSTEM_ERROR = 'SYSTEM_ERROR'
}

/**
 * 提交代码请求参数
 */
export interface SubmitCodeDTO {
  problemId: number;
  languageId: number;
  code: string;
}

/**
 * 提交记录VO
 */
export interface SubmissionVO {
  id: number;
  problemId: number;
  problemTitle: string;
  userId: number;
  username: string;
  languageId: number;
  languageName: string;
  status: JudgeStatus | string;
  score: number;
  timeUsed: number;
  memoryUsed: number;
  passRate: string;
  createTime: string;
}

// ==================== API 接口定义 ====================

/**
 * API 接口路径常量
 */
export const API_PATHS = {
  // 认证
  AUTH_LOGIN: '/auth/login',
  AUTH_LOGOUT: '/auth/logout',
  
  // 语言
  LANGUAGE_LIST: '/language/list',
  LANGUAGE_DETAIL: (id: number) => `/language/${id}`,
  
  // 题目
  PROBLEM_PAGE: '/problem/page',
  PROBLEM_DETAIL: (id: number) => `/problem/${id}`,
  
  // 提交
  SUBMISSION_SUBMIT: '/submission/submit',
  SUBMISSION_PAGE: '/submission/page',
  SUBMISSION_DETAIL: (id: number) => `/submission/${id}`
} as const;
```

---

## 附录：Vue3 封装示例

以下是在 Vue3 项目中封装 API 请求的示例：

```typescript
// api/request.ts
import axios, { type AxiosInstance, type AxiosResponse } from 'axios';
import type { ResponseResult } from './types';

const instance: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
});

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
  (response: AxiosResponse<ResponseResult<unknown>>) => {
    const { data } = response;
    if (data.code !== 200) {
      // 处理业务错误
      return Promise.reject(new Error(data.msg));
    }
    return response;
  },
  (error) => Promise.reject(error)
);

export default instance;
```

```typescript
// api/auth.ts
import request from './request';
import type { ResponseResult, UserLoginDTO, UserLoginVO } from './types';

export const authApi = {
  login(data: UserLoginDTO): Promise<ResponseResult<UserLoginVO>> {
    return request.post('/auth/login', data);
  },
  
  logout(): Promise<ResponseResult<null>> {
    return request.post('/auth/logout');
  }
};
```

```typescript
// api/problem.ts
import request from './request';
import type { ResponseResult, PageDTO, PageVO, ProblemVO } from './types';

export const problemApi = {
  getPage(params: PageDTO & { difficulty?: number; status?: number; keyword?: string }): Promise<ResponseResult<PageVO<ProblemVO>>> {
    return request.get('/problem/page', { params });
  },
  
  getDetail(id: number): Promise<ResponseResult<ProblemVO>> {
    return request.get(`/problem/${id}`);
  }
};
```

```typescript
// api/submission.ts
import request from './request';
import type { ResponseResult, PageDTO, PageVO, SubmitCodeDTO, SubmissionVO } from './types';

export const submissionApi = {
  submit(data: SubmitCodeDTO): Promise<ResponseResult<number>> {
    return request.post('/submission/submit', data);
  },
  
  getPage(params: PageDTO & { problemId?: number; userId?: number }): Promise<ResponseResult<PageVO<SubmissionVO>>> {
    return request.get('/submission/page', { params });
  },
  
  getDetail(id: number): Promise<ResponseResult<SubmissionVO>> {
    return request.get(`/submission/${id}`);
  }
};
```

```typescript
// api/language.ts
import request from './request';
import type { ResponseResult, Language } from './types';

export const languageApi = {
  getList(): Promise<ResponseResult<Language[]>> {
    return request.get('/language/list');
  },
  
  getDetail(id: number): Promise<ResponseResult<Language>> {
    return request.get(`/language/${id}`);
  }
};
```
