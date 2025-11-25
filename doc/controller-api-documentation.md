# Controller API 文档

本文档包含语言管理、题目管理和测试用例管理的所有接口说明，供前端开发使用。

---

## 目录

1. [语言管理 API](#语言管理-api)
2. [题目管理 API](#题目管理-api)
3. [测试用例管理 API](#测试用例管理-api)
4. [通用响应格式](#通用响应格式)
5. [通用数据模型](#通用数据模型)

---

## 语言管理 API

### 1. 获取所有可用语言

**接口描述**: 获取系统中所有启用状态的编程语言列表

**请求方式**: `GET`

**请求路径**: `/language/list`

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": 1,
      "name": "C",
      "version": "gcc-11",
      "compileCommand": "gcc -O2 -std=c11 -o {executable} {source}",
      "executeCommand": "./{executable}",
      "sourceFileExt": ".c",
      "executableExt": "",
      "isCompiled": 1,
      "timeLimitMultiplier": 1.0,
      "memoryLimitMultiplier": 1.0,
      "status": 1,
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "name": "Java",
      "version": "jdk-21",
      "compileCommand": "javac {source}",
      "executeCommand": "java {className}",
      "sourceFileExt": ".java",
      "executableExt": ".class",
      "isCompiled": 1,
      "timeLimitMultiplier": 2.0,
      "memoryLimitMultiplier": 2.0,
      "status": 1,
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T10:00:00"
    }
  ]
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 语言ID |
| name | String | 语言名称（如 C, C++, Java, Python） |
| version | String | 语言版本（如 gcc-11, jdk-21, python-3.11） |
| compileCommand | String | 编译命令模板 |
| executeCommand | String | 执行命令模板 |
| sourceFileExt | String | 源文件扩展名（如 .c, .cpp, .java, .py） |
| executableExt | String | 可执行文件扩展名 |
| isCompiled | Integer | 是否需要编译（0-否，1-是） |
| timeLimitMultiplier | BigDecimal | 时间限制倍数 |
| memoryLimitMultiplier | BigDecimal | 内存限制倍数 |
| status | Integer | 状态（0-禁用，1-启用） |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |

---

### 2. 获取语言详情

**接口描述**: 根据语言ID获取语言详细信息

**请求方式**: `GET`

**请求路径**: `/language/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 语言ID |

**请求示例**: `/language/1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "name": "C",
    "version": "gcc-11",
    "compileCommand": "gcc -O2 -std=c11 -o {executable} {source}",
    "executeCommand": "./{executable}",
    "sourceFileExt": ".c",
    "executableExt": "",
    "isCompiled": 1,
    "timeLimitMultiplier": 1.0,
    "memoryLimitMultiplier": 1.0,
    "status": 1,
    "createTime": "2024-01-01T10:00:00",
    "updateTime": "2024-01-01T10:00:00"
  }
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "语言不存在",
  "data": null
}
```

---

## 题目管理 API

### 1. 分页查询题目

**接口描述**: 分页查询题目列表，支持按难度、状态和关键词筛选

**请求方式**: `GET`

**请求路径**: `/problem/page`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNo | Integer | 是 | 页码（从1开始） |
| pageSize | Integer | 是 | 每页数量 |
| difficulty | Integer | 否 | 难度筛选（1-简单，2-中等，3-困难） |
| status | Integer | 否 | 状态筛选（0-隐藏，1-公开） |
| keyword | String | 否 | 关键词搜索（匹配题目标题） |
| sortBy | String | 否 | 排序字段 |
| isAsc | Boolean | 否 | 是否升序（true-升序，false-降序） |

**请求示例**: `/problem/page?pageNo=1&pageSize=10&difficulty=1&keyword=排序`

**响应示例**:
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
        "description": "给定一个整数数组 nums 和一个整数目标值 target...",
        "inputDescription": "第一行包含两个整数 n 和 target...",
        "outputDescription": "输出两个整数的下标...",
        "sampleInput": "4 9\n2 7 11 15",
        "sampleOutput": "0 1",
        "hint": "可以使用哈希表优化时间复杂度",
        "difficulty": 1,
        "timeLimit": 1000,
        "memoryLimit": 256,
        "acceptCount": 1500,
        "submitCount": 3000,
        "createTime": "2024-01-01T10:00:00"
      }
    ]
  }
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| list | List | 题目列表 |

**题目对象字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 题目ID |
| title | String | 题目标题 |
| description | String | 题目描述 |
| inputDescription | String | 输入描述 |
| outputDescription | String | 输出描述 |
| sampleInput | String | 样例输入 |
| sampleOutput | String | 样例输出 |
| hint | String | 提示信息 |
| difficulty | Integer | 难度（1-简单，2-中等，3-困难） |
| timeLimit | Integer | CPU时间限制（毫秒） |
| memoryLimit | Integer | 内存限制（MB） |
| acceptCount | Integer | 通过次数 |
| submitCount | Integer | 提交次数 |
| createTime | LocalDateTime | 创建时间 |

---

### 2. 获取题目详情

**接口描述**: 根据题目ID获取题目详细信息

**请求方式**: `GET`

**请求路径**: `/problem/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 题目ID |

**请求示例**: `/problem/1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "title": "两数之和",
    "description": "给定一个整数数组 nums 和一个整数目标值 target...",
    "inputDescription": "第一行包含两个整数 n 和 target...",
    "outputDescription": "输出两个整数的下标...",
    "sampleInput": "4 9\n2 7 11 15",
    "sampleOutput": "0 1",
    "hint": "可以使用哈希表优化时间复杂度",
    "difficulty": 1,
    "timeLimit": 1000,
    "memoryLimit": 256,
    "acceptCount": 1500,
    "submitCount": 3000,
    "createTime": "2024-01-01T10:00:00"
  }
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "题目不存在",
  "data": null
}
```

---

### 3. 新增题目

**接口描述**: 创建新题目

**请求方式**: `POST`

**请求路径**: `/problem`

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
{
  "title": "两数之和",
  "description": "给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回它们的数组下标。",
  "inputDescription": "第一行包含两个整数 n 和 target，分别表示数组长度和目标值。\n第二行包含 n 个整数，表示数组元素。",
  "outputDescription": "输出两个整数的下标，用空格分隔。",
  "sampleInput": "4 9\n2 7 11 15",
  "sampleOutput": "0 1",
  "hint": "可以使用哈希表优化时间复杂度",
  "difficulty": 1,
  "timeLimit": 1000,
  "memoryLimit": 256,
  "stackLimit": 128,
  "source": "LeetCode",
  "status": 1
}
```

**请求字段说明**:

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | String | 是 | 题目标题（最多255个字符） |
| description | String | 是 | 题目描述 |
| inputDescription | String | 否 | 输入描述 |
| outputDescription | String | 否 | 输出描述 |
| sampleInput | String | 否 | 样例输入 |
| sampleOutput | String | 否 | 样例输出 |
| hint | String | 否 | 提示信息 |
| difficulty | Integer | 否 | 难度（1-简单，2-中等，3-困难） |
| timeLimit | Integer | 是 | CPU时间限制（毫秒） |
| memoryLimit | Integer | 是 | 内存限制（MB） |
| stackLimit | Integer | 否 | 栈内存限制（MB） |
| source | String | 否 | 题目来源（最多255个字符） |
| status | Integer | 否 | 状态（0-隐藏，1-公开） |

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "新增题目失败",
  "data": null
}
```

**参数校验失败示例**:
```json
{
  "code": 400,
  "msg": "题目标题不能为空",
  "data": null
}
```

---

### 4. 修改题目

**接口描述**: 更新题目信息

**请求方式**: `PUT`

**请求路径**: `/problem`

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
{
  "id": 1,
  "title": "两数之和（修改）",
  "description": "修改后的题目描述...",
  "inputDescription": "修改后的输入描述...",
  "outputDescription": "修改后的输出描述...",
  "sampleInput": "4 9\n2 7 11 15",
  "sampleOutput": "0 1",
  "hint": "修改后的提示信息",
  "difficulty": 2,
  "timeLimit": 2000,
  "memoryLimit": 512,
  "stackLimit": 256,
  "source": "LeetCode",
  "status": 1
}
```

**请求字段说明**: 与新增题目相同，但 `id` 字段必填

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "题目ID不能为空",
  "data": null
}
```

或

```json
{
  "code": 500,
  "msg": "修改题目失败",
  "data": null
}
```

---

### 5. 删除题目

**接口描述**: 根据题目ID删除题目

**请求方式**: `DELETE`

**请求路径**: `/problem/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 题目ID |

**请求示例**: `/problem/1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "删除题目失败",
  "data": null
}
```

---

### 6. 批量删除题目

**接口描述**: 批量删除多个题目

**请求方式**: `DELETE`

**请求路径**: `/problem/batch`

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
[1, 2, 3, 4, 5]
```

**请求字段说明**: 题目ID数组

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "批量删除题目失败",
  "data": null
}
```

---

### 7. 修改题目状态

**接口描述**: 修改题目的公开/隐藏状态

**请求方式**: `PUT`

**请求路径**: `/problem/{id}/status`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 题目ID |

**查询参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | Integer | 是 | 状态（0-隐藏，1-公开） |

**请求示例**: `/problem/1/status?status=1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "修改题目状态失败",
  "data": null
}
```

---

## 测试用例管理 API

### 1. 根据题目ID获取测试用例列表

**接口描述**: 获取指定题目的所有测试用例

**请求方式**: `GET`

**请求路径**: `/testcase/problem/{problemId}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| problemId | Long | 是 | 题目ID |

**请求示例**: `/testcase/problem/1`

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": 1,
      "problemId": 1,
      "input": "4 9\n2 7 11 15",
      "output": "0 1",
      "isSample": 1,
      "score": 10,
      "sortOrder": 1,
      "createTime": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "problemId": 1,
      "input": "5 10\n1 2 3 4 5",
      "output": "-1",
      "isSample": 0,
      "score": 10,
      "sortOrder": 2,
      "createTime": "2024-01-01T10:00:00"
    }
  ]
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 测试用例ID |
| problemId | Long | 题目ID |
| input | String | 输入数据 |
| output | String | 预期输出 |
| isSample | Integer | 是否为样例（0-否，1-是） |
| score | Integer | 分值（若支持部分分） |
| sortOrder | Integer | 排序 |
| createTime | LocalDateTime | 创建时间 |

---

### 2. 获取测试用例详情

**接口描述**: 根据测试用例ID获取详细信息

**请求方式**: `GET`

**请求路径**: `/testcase/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 测试用例ID |

**请求示例**: `/testcase/1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "problemId": 1,
    "input": "4 9\n2 7 11 15",
    "output": "0 1",
    "isSample": 1,
    "score": 10,
    "sortOrder": 1,
    "createTime": "2024-01-01T10:00:00"
  }
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "测试用例不存在",
  "data": null
}
```

---

### 3. 新增测试用例

**接口描述**: 为题目添加测试用例

**请求方式**: `POST`

**请求路径**: `/testcase`

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
{
  "problemId": 1,
  "input": "4 9\n2 7 11 15",
  "output": "0 1",
  "isSample": 1,
  "score": 10,
  "sortOrder": 1
}
```

**请求字段说明**:

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| problemId | Long | 是 | 题目ID |
| input | String | 是 | 输入数据 |
| output | String | 是 | 预期输出 |
| isSample | Integer | 否 | 是否为样例（0-否，1-是） |
| score | Integer | 否 | 分值（若支持部分分） |
| sortOrder | Integer | 否 | 排序 |

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "新增测试用例失败",
  "data": null
}
```

**参数校验失败示例**:
```json
{
  "code": 400,
  "msg": "题目ID不能为空",
  "data": null
}
```

---

### 4. 修改测试用例

**接口描述**: 更新测试用例信息

**请求方式**: `PUT`

**请求路径**: `/testcase`

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
{
  "id": 1,
  "problemId": 1,
  "input": "4 9\n2 7 11 15",
  "output": "0 1",
  "isSample": 1,
  "score": 20,
  "sortOrder": 1
}
```

**请求字段说明**: 与新增测试用例相同，但 `id` 字段必填

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "测试用例ID不能为空",
  "data": null
}
```

或

```json
{
  "code": 500,
  "msg": "修改测试用例失败",
  "data": null
}
```

---

### 5. 删除测试用例

**接口描述**: 根据测试用例ID删除测试用例

**请求方式**: `DELETE`

**请求路径**: `/testcase/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 测试用例ID |

**请求示例**: `/testcase/1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "删除测试用例失败",
  "data": null
}
```

---

### 6. 批量删除测试用例

**接口描述**: 批量删除多个测试用例

**请求方式**: `DELETE`

**请求路径**: `/testcase/batch`

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
[1, 2, 3, 4, 5]
```

**请求字段说明**: 测试用例ID数组

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "批量删除测试用例失败",
  "data": null
}
```

---

### 7. 根据题目ID删除所有测试用例

**接口描述**: 删除指定题目的所有测试用例

**请求方式**: `DELETE`

**请求路径**: `/testcase/problem/{problemId}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| problemId | Long | 是 | 题目ID |

**请求示例**: `/testcase/problem/1`

**成功响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": null
}
```

**失败响应示例**:
```json
{
  "code": 500,
  "msg": "删除测试用例失败",
  "data": null
}
```

---

## 通用响应格式

所有接口统一使用 `ResponseResult` 包装响应数据：

```json
{
  "code": 200,
  "msg": "Success",
  "data": {}
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 响应状态码（200-成功，500-失败，400-参数错误） |
| msg | String | 响应消息 |
| data | Object | 响应数据（可以为null、对象、数组等） |

---

## 通用数据模型

### 分页请求参数 (PageDTO)

用于分页查询的请求参数：

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNo | Integer | 是 | 页码（从1开始） |
| pageSize | Integer | 是 | 每页数量 |
| sortBy | String | 否 | 排序字段 |
| isAsc | Boolean | 否 | 是否升序（true-升序，false-降序） |

### 分页响应数据 (PageVO)

分页查询的响应数据格式：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| list | List | 数据列表 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误（参数校验失败） |
| 401 | 未授权（需要登录） |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 注意事项

1. **时间格式**: 所有时间字段使用 ISO 8601 格式，如 `2024-01-01T10:00:00`
2. **字符编码**: 所有接口使用 UTF-8 编码
3. **Content-Type**: POST、PUT 请求需要设置 `Content-Type: application/json`
4. **ID字段**: 新增操作不需要传 ID，修改操作必须传 ID
5. **必填字段**: 请求参数中标记为"必填"的字段不能为空或null
6. **字符长度限制**: 
   - 题目标题: 最多255个字符
   - 题目来源: 最多255个字符
7. **难度枚举值**: 1-简单，2-中等，3-困难
8. **状态枚举值**: 
   - 题目状态: 0-隐藏，1-公开
   - 语言状态: 0-禁用，1-启用
   - 测试用例样例标识: 0-否，1-是
9. **时间限制单位**: 毫秒（ms）
10. **内存限制单位**: MB

---

## 更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-11-25 | 初始版本，包含语言管理、题目管理、测试用例管理所有接口 |

---

## 联系方式

如有疑问，请联系后端开发团队。

