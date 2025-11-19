# EmiyaOJ API 文档

## 基础信息

- **Base URL**: `http://localhost:8080`
- **API 文档**: `http://localhost:8080/doc.html`
- **OpenAPI 规范**: `http://localhost:8080/v3/api-docs`

## 认证

目前系统集成了 Spring Security，需要根据实际配置进行认证。
默认情况下，某些接口可能需要 JWT Token。

```
Authorization: Bearer <your_jwt_token>
```

## API 端点

### 1. 语言管理

#### 1.1 获取支持的编程语言列表

**接口**: `GET /language/list`

**描述**: 获取所有启用的编程语言

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": [
    {
      "id": 1,
      "name": "C",
      "version": "gcc-14",
      "sourceFileExt": ".c",
      "isCompiled": 1
    },
    {
      "id": 2,
      "name": "C++",
      "version": "g++-14",
      "sourceFileExt": ".cpp",
      "isCompiled": 1
    }
  ]
}
```

### 2. 题目管理

#### 2.1 分页查询题目列表

**接口**: `GET /problem/page`

**参数**:
| 参数 | 类型 | 必填 | 描述 | 默认值 |
|------|------|------|------|--------|
| page | int | 否 | 页码 | 1 |
| size | int | 否 | 每页大小 | 20 |
| difficulty | int | 否 | 难度 (1-简单, 2-中等, 3-困难) | - |

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 2,
    "pages": 1,
    "list": [
      {
        "id": 1,
        "title": "A+B Problem",
        "difficulty": 1,
        "acceptCount": 100,
        "submitCount": 150,
        "createTime": "2024-01-01T10:00:00"
      },
      {
        "id": 2,
        "title": "Hello World",
        "difficulty": 1,
        "acceptCount": 200,
        "submitCount": 210,
        "createTime": "2024-01-01T11:00:00"
      }
    ]
  }
}
```

#### 2.2 获取题目详情

**接口**: `GET /problem/{id}`

**路径参数**:
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| id | long | 是 | 题目ID |

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 1,
    "title": "A+B Problem",
    "description": "给定两个整数A和B，计算A+B的值。",
    "inputDescription": "一行包含两个整数A和B，用空格分隔。",
    "outputDescription": "输出A+B的值。",
    "sampleInput": "1 2",
    "sampleOutput": "3",
    "hint": "这是最简单的题目。",
    "difficulty": 1,
    "timeLimit": 1000,
    "memoryLimit": 128,
    "acceptCount": 100,
    "submitCount": 150,
    "createTime": "2024-01-01T10:00:00"
  }
}
```

### 3. 代码提交

#### 3.1 提交代码

**接口**: `POST /submission/submit`

**请求体**:
```json
{
  "problemId": 1,
  "languageId": 1,
  "code": "#include <stdio.h>\nint main() {\n    int a, b;\n    scanf(\"%d %d\", &a, &b);\n    printf(\"%d\", a + b);\n    return 0;\n}"
}
```

**参数说明**:
| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| problemId | long | 是 | 题目ID |
| languageId | long | 是 | 语言ID |
| code | string | 是 | 源代码 |

**响应示例**:
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

#### 3.2 查询提交列表

**接口**: `GET /submission/list`

**参数**:
| 参数 | 类型 | 必填 | 描述 | 默认值 |
|------|------|------|------|--------|
| page | int | 否 | 页码 | 1 |
| size | int | 否 | 每页大小 | 20 |
| userId | long | 否 | 用户ID | - |
| problemId | long | 否 | 题目ID | - |

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "total": 1,
    "pages": 1,
    "list": [
      {
        "id": 123,
        "problemId": 1,
        "problemTitle": "A+B Problem",
        "userId": 1,
        "languageId": 1,
        "languageName": "C gcc-14",
        "status": "Accepted",
        "score": 100,
        "timeUsed": 10,
        "memoryUsed": 512,
        "passRate": "10/10",
        "createTime": "2024-01-01T12:00:00"
      }
    ]
  }
}
```

#### 3.3 获取提交详情

**接口**: `GET /submission/{id}`

**路径参数**:
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| id | long | 是 | 提交ID |

**响应示例**:
```json
{
  "code": 200,
  "msg": "Success",
  "data": {
    "id": 123,
    "problemId": 1,
    "problemTitle": "A+B Problem",
    "userId": 1,
    "languageId": 1,
    "languageName": "C gcc-14",
    "code": "#include <stdio.h>\nint main() {...}",
    "status": "Accepted",
    "score": 100,
    "timeUsed": 10,
    "memoryUsed": 512,
    "errorMessage": null,
    "compileMessage": null,
    "passRate": "10/10",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

## 判题状态说明

| 状态 | 描述 |
|------|------|
| Pending | 等待判题 |
| Judging | 判题中 |
| Accepted | 通过 |
| Wrong Answer | 答案错误 |
| Time Limit Exceeded | 时间超限 |
| Memory Limit Exceeded | 内存超限 |
| Output Limit Exceeded | 输出超限 |
| Runtime Error | 运行时错误 |
| Compile Error | 编译错误 |
| System Error | 系统错误 |

## 错误码说明

| 错误码 | 描述 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 使用示例

### Python 提交示例

```python
import requests

# 1. 获取题目详情
problem_response = requests.get('http://localhost:8080/problem/1')
problem = problem_response.json()['data']
print(f"题目: {problem['title']}")

# 2. 提交代码
submit_data = {
    "problemId": 1,
    "languageId": 1,  # C
    "code": """
#include <stdio.h>
int main() {
    int a, b;
    scanf("%d %d", &a, &b);
    printf("%d", a + b);
    return 0;
}
"""
}

submit_response = requests.post(
    'http://localhost:8080/submission/submit',
    json=submit_data,
    headers={'Content-Type': 'application/json'}
)

result = submit_response.json()
submission_id = result['data']['submissionId']
print(f"提交ID: {submission_id}")

# 3. 轮询获取判题结果
import time
while True:
    detail_response = requests.get(f'http://localhost:8080/submission/{submission_id}')
    detail = detail_response.json()['data']
    
    if detail['status'] not in ['Pending', 'Judging']:
        print(f"判题结果: {detail['status']}")
        print(f"通过率: {detail['passRate']}")
        print(f"时间: {detail['timeUsed']}ms")
        print(f"内存: {detail['memoryUsed']}KB")
        break
    
    time.sleep(1)
```

### cURL 示例

```bash
# 获取语言列表
curl -X GET http://localhost:8080/language/list

# 获取题目列表
curl -X GET "http://localhost:8080/problem/page?page=1&size=10&difficulty=1"

# 提交代码
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() { int a, b; scanf(\"%d %d\", &a, &b); printf(\"%d\", a+b); return 0; }"
  }'

# 查看提交详情
curl -X GET http://localhost:8080/submission/123
```

## 注意事项

1. **代码长度限制**: 建议单个代码文件不超过 64KB
2. **提交频率限制**: 建议实施提交频率限制，防止恶意提交
3. **并发限制**: 沙箱同时处理的判题任务数量有限
4. **超时设置**: 长时间运行的代码可能被判定为超时
5. **内存限制**: 不同语言的内存限制倍数不同，注意查看语言配置

## 开发调试

### 启用详细日志

在 `application.yaml` 中设置:
```yaml
logging:
  level:
    com.emiyaoj: DEBUG
    com.emiyaoj.service.sandbox: TRACE
```

### 监控沙箱请求

```bash
# 查看沙箱版本
curl http://10.30.22.1:10000/version

# 查看沙箱配置
curl http://10.30.22.1:10000/config

# 查看沙箱文件列表
curl http://10.30.22.1:10000/file
```

## 后续扩展

### 计划功能
- [ ] 竞赛模式
- [ ] 题目标签和分类
- [ ] 题解功能
- [ ] 代码高亮显示
- [ ] 实时排名
- [ ] 提交统计分析
- [ ] SPJ（Special Judge）支持
- [ ] 交互式题目支持
