# 前端 API 文档

本文档旨在为前端开发人员提供与 EmiyaOJ 后端服务集成的详细 API 指南。

## 基础 URL

所有 API 的基础 URL 都是相对于服务器的根路径。

## 通用响应格式

所有 API 请求成功后都会返回一个统一格式的 JSON 对象：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // ... specific data for each endpoint
  }
}
```

- `code`: HTTP 状态码, `200` 表示成功。
- `message`: 响应的消息。
- `data`: 请求返回的具体数据。

---

## 提交管理 (Submission)

基础路径: `/client/submission`

### 1. 提交代码

- **功能**: 用户提交代码进行判题。
- **URL**: `/client/submission/client/submit`
- **方法**: `POST`
- **Content-Type**: `application/json`

#### 请求体 (Request Body)

```json
{
  "problemId": 1,
  "languageId": 1,
  "code": "public class Main { public static void main(String[] args) { System.out.println(\"Hello World\"); } }"
}
```

- `problemId` (Long): 题目的唯一标识符。
- `languageId` (Long): 编程语言的唯一标识符。
- `code` (String): 用户提交的代码。

#### 成功响应 (Success Response)

响应 `data` 中包含本次提交记录的ID。

```json
{
  "code": 200,
  "message": "操作成功",
  "data": 1739961289898889218
}
```

### 2. 分页查询提交记录

- **功能**: 根据条件分页获取提交记录列表。
- **URL**: `/client/submission/page`
- **方法**: `GET`

#### 请求参数 (Query Parameters)

- `current` (Integer, **必选**): 当前页码，从 1 开始。
- `size` (Integer, **必选**): 每页记录数。
- `problemId` (Long, 可选): 题目ID，用于筛选特定题目的提交。
- `userId` (Long, 可选): 用户ID，用于筛选特定用户的提交。

**示例 URL**: `/client/submission/page?current=1&size=10&problemId=1`

#### 成功响应 (Success Response)

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1739961289898889218,
        "problemId": 1,
        "problemTitle": "A+B Problem",
        "userId": 1,
        "username": "admin",
        "language": "Java",
        "status": 2,
        "time": 100,
        "memory": 512,
        "createTime": "2025-12-02T10:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

- `records`: 提交记录对象数组。
  - `status`: 判题状态 (例如: 0-判题中, 1-编译错误, 2-答案正确, 3-答案错误, 4-超时, 5-内存超限)。
- `total`: 总记录数。
- `size`: 每页大小。
- `current`: 当前页码。
- `pages`: 总页数。

### 3. 获取提交详情

- **功能**: 获取单条提交记录的详细信息，包含代码。
- **URL**: `/client/submission/{id}`
- **方法**: `GET`

#### 路径参数 (Path Parameters)

- `id` (Long, **必选**): 提交记录的唯一标识符。

**示例 URL**: `/client/submission/1739961289898889218`

#### 成功响应 (Success Response)

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1739961289898889218,
    "problemId": 1,
    "problemTitle": "A+B Problem",
    "userId": 1,
    "username": "admin",
    "language": "Java",
    "code": "public class Main { ... }",
    "status": 2,
    "time": 100,
    "memory": 512,
    "judgeInfo": "{\"message\":\"Accepted\",\"time\":100,\"memory\":512}",
    "createTime": "2025-12-02T10:00:00"
  }
}
```
- `judgeInfo`: 判题信息，通常是一个JSON字符串。

---

## 题目管理 (Problem)

基础路径: `/client/problem`

### 1. 分页查询题目

- **功能**: 根据条件分页获取题目列表。
- **URL**: `/client/problem/page`
- **方法**: `GET`

#### 请求参数 (Query Parameters)

- `current` (Integer, **必选**): 当前页码，从 1 开始。
- `size` (Integer, **必选**): 每页记录数。
- `difficulty` (Integer, 可选): 题目难度 (例如: 0-简单, 1-中等, 2-困难)。
- `status` (Integer, 可选): 用户对于该题目的解答状态 (例如: 0-未尝试, 1-尝试过, 2-已通过)。
- `keyword` (String, 可选): 搜索关键词，用于匹配题目ID或标题。

**示例 URL**: `/client/problem/page?current=1&size=10&difficulty=0`

#### 成功响应 (Success Response)

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "A+B Problem",
        "difficulty": 0,
        "passRate": 0.5,
        "tags": ["入门", "简单"],
        "userStatus": 2
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```
- `userStatus`: 当前登录用户对该题的解答状态。

### 2. 获取题目详情

- **功能**: 获取单个题目的详细信息。
- **URL**: `/client/problem/{id}`
- **方法**: `GET`

#### 路径参数 (Path Parameters)

- `id` (Long, **必选**): 题目的唯一标识符。

**示例 URL**: `/client/problem/1`

#### 成功响应 (Success Response)

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "A+B Problem",
    "description": "输入两个整数 a 和 b，计算它们的和。",
    "inputFormat": "一行，两个整数 a 和 b。",
    "outputFormat": "一个整数，a+b 的和。",
    "examples": [
      { "input": "1 2", "output": "3" }
    ],
    "timeLimit": 1000,
    "memoryLimit": 256,
    "difficulty": 0,
    "passRate": 0.5,
    "tags": ["入门", "简单"],
    "userStatus": 2
  }
}
```

---

## 语言管理 (Language)

基础路径: `/client/language`

### 1. 获取所有可���语言

- **功能**: 获取系统中所有启用的编程语言列表。
- **URL**: `/client/language/list`
- **方法**: `GET`

#### 成功响应 (Success Response)

响应 `data` 是一个语言对象数组。

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "Java",
      "description": "Java 1.8",
      "status": 1,
      "createTime": "2025-12-01T00:00:00",
      "updateTime": "2025-12-01T00:00:00"
    },
    {
      "id": 2,
      "name": "C++",
      "description": "GCC 9.4.0",
      "status": 1,
      "createTime": "2025-12-01T00:00:00",
      "updateTime": "2025-12-01T00:00:00"
    }
  ]
}
```
- `status`: 1 表示启用，0 表示禁用。

### 2. 获取语言详情

- **功能**: 获取特定编程语言的详细信息。
- **URL**: `/client/language/{id}`
- **方法**: `GET`

#### 路径参数 (Path Parameters)

- `id` (Long, **必选**): 语言的唯一标识符。

**示例 URL**: `/client/language/1`

#### 成功响应 (Success Response)

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "Java",
    "description": "Java 1.8",
    "status": 1,
    "createTime": "2025-12-01T00:00:00",
    "updateTime": "2025-12-01T00:00:00"
  }
}
```

