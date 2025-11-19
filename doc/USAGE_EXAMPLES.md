# EmiyaOJ 使用示例

本文档提供完整的API调用示例，包括管理端和用户端的典型使用场景。

## 目录

1. [管理员操作流程](#管理员操作流程)
2. [用户操作流程](#用户操作流程)
3. [完整示例场景](#完整示例场景)

---

## 管理员操作流程

### 场景1: 创建一道新题目（完整流程）

#### 步骤1: 创建C语言支持

**请求:**
```bash
curl -X POST http://localhost:8080/admin/language \
  -H "Content-Type: application/json" \
  -d '{
    "name": "C",
    "displayName": "C (gcc-14)",
    "compileCommand": "/usr/bin/gcc-14 -DONLINE_JUDGE -O2 -Wall -std=c17 -fmax-errors=3 {src} -lm -o {exe}",
    "executeCommand": "{exe}",
    "sourceFileName": "main.c",
    "executableFileName": "main",
    "isCompiled": true,
    "enabled": true,
    "timeLimit": 1000000000,
    "memoryLimit": 268435456,
    "description": "GCC 14 with C17 standard"
  }'
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "C",
    "displayName": "C (gcc-14)",
    "enabled": true,
    "createTime": "2024-01-01T10:00:00"
  }
}
```

#### 步骤2: 创建C++语言支持

**请求:**
```bash
curl -X POST http://localhost:8080/admin/language \
  -H "Content-Type: application/json" \
  -d '{
    "name": "C++",
    "displayName": "C++ (g++-14)",
    "compileCommand": "/usr/bin/g++-14 -DONLINE_JUDGE -O2 -Wall -std=c++20 -fmax-errors=3 {src} -lm -o {exe}",
    "executeCommand": "{exe}",
    "sourceFileName": "main.cpp",
    "executableFileName": "main",
    "isCompiled": true,
    "enabled": true,
    "timeLimit": 1000000000,
    "memoryLimit": 268435456,
    "description": "G++ 14 with C++20 standard"
  }'
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 2,
    "name": "C++",
    "displayName": "C++ (g++-14)",
    "enabled": true,
    "createTime": "2024-01-01T10:00:01"
  }
}
```

#### 步骤3: 创建题目 "A+B Problem"

**请求:**
```bash
curl -X POST http://localhost:8080/admin/problem \
  -H "Content-Type: application/json" \
  -d '{
    "title": "A+B Problem",
    "description": "# 题目描述\n\n给定两个整数A和B，请你计算A+B的值。\n\n# 输入格式\n\n一行两个整数A和B，用空格分隔。\n\n# 输出格式\n\n输出一个整数，表示A+B的值。\n\n# 数据范围\n\n- $-10^9 \\leq A, B \\leq 10^9$",
    "inputDescription": "一行两个整数A和B，用空格分隔",
    "outputDescription": "输出一个整数，表示A+B的值",
    "sampleInput": "1 2",
    "sampleOutput": "3",
    "hint": "这是一道非常简单的入门题目",
    "difficulty": "Easy",
    "timeLimit": 1000,
    "memoryLimit": 256,
    "status": "Public",
    "source": "经典题目",
    "author": "Admin"
  }'
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "A+B Problem",
    "difficulty": "Easy",
    "status": "Public",
    "createTime": "2024-01-01T10:00:02",
    "submitCount": 0,
    "acceptCount": 0
  }
}
```

#### 步骤4: 为题目添加测试用例

**测试用例1:**
```bash
curl -X POST http://localhost:8080/admin/testcase \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "inputData": "1 2",
    "outputData": "3",
    "score": 10,
    "isSample": true
  }'
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "problemId": 1,
    "score": 10,
    "isSample": true,
    "createTime": "2024-01-01T10:00:03"
  }
}
```

**测试用例2-10 (批量创建):**
```bash
# 测试用例2: 负数
curl -X POST http://localhost:8080/admin/testcase \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "inputData": "-5 3",
    "outputData": "-2",
    "score": 10,
    "isSample": false
  }'

# 测试用例3: 大数
curl -X POST http://localhost:8080/admin/testcase \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "inputData": "1000000000 1000000000",
    "outputData": "2000000000",
    "score": 10,
    "isSample": false
  }'

# ... 继续添加其他测试用例
```

#### 步骤5: 验证题目设置

**查询题目详情:**
```bash
curl http://localhost:8080/admin/problem/1
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "A+B Problem",
    "description": "# 题目描述\n\n给定两个整数A和B...",
    "difficulty": "Easy",
    "timeLimit": 1000,
    "memoryLimit": 256,
    "status": "Public",
    "submitCount": 0,
    "acceptCount": 0,
    "testCaseCount": 10
  }
}
```

**查询测试用例列表:**
```bash
curl http://localhost:8080/admin/testcase/list?problemId=1
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "problemId": 1,
      "inputData": "1 2",
      "outputData": "3",
      "score": 10,
      "isSample": true
    },
    {
      "id": 2,
      "problemId": 1,
      "inputData": "-5 3",
      "outputData": "-2",
      "score": 10,
      "isSample": false
    }
    // ... 其他测试用例
  ]
}
```

---

## 用户操作流程

### 场景2: 用户提交代码并查询结果

#### 步骤1: 查询可用的编程语言

**请求:**
```bash
curl http://localhost:8080/language/list
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "C",
      "displayName": "C (gcc-14)",
      "description": "GCC 14 with C17 standard"
    },
    {
      "id": 2,
      "name": "C++",
      "displayName": "C++ (g++-14)",
      "description": "G++ 14 with C++20 standard"
    }
  ]
}
```

#### 步骤2: 浏览题目列表

**请求:**
```bash
curl "http://localhost:8080/problem/page?current=1&size=10"
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "A+B Problem",
        "difficulty": "Easy",
        "submitCount": 15,
        "acceptCount": 10,
        "acceptRate": "66.67%"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

#### 步骤3: 查看题目详情

**请求:**
```bash
curl http://localhost:8080/problem/1
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "A+B Problem",
    "description": "# 题目描述\n\n给定两个整数A和B，请你计算A+B的值。\n\n# 输入格式\n\n一行两个整数A和B，用空格分隔。\n\n# 输出格式\n\n输出一个整数，表示A+B的值。\n\n# 数据范围\n\n- $-10^9 \\leq A, B \\leq 10^9$",
    "inputDescription": "一行两个整数A和B，用空格分隔",
    "outputDescription": "输出一个整数，表示A+B的值",
    "sampleInput": "1 2",
    "sampleOutput": "3",
    "hint": "这是一道非常简单的入门题目",
    "difficulty": "Easy",
    "timeLimit": 1000,
    "memoryLimit": 256,
    "submitCount": 15,
    "acceptCount": 10
  }
}
```

#### 步骤4: 提交代码

**请求 (C语言正确代码):**
```bash
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() {\n    int a, b;\n    scanf(\"%d %d\", &a, &b);\n    printf(\"%d\", a + b);\n    return 0;\n}"
  }'
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "submissionId": 1,
    "message": "提交成功，请触发判题"
  }
}
```

#### 步骤5: 触发判题

**请求:**
```bash
curl -X POST http://localhost:8080/judge/1
```

**响应:**
```json
{
  "code": 200,
  "message": "判题任务已提交，请稍后查询结果",
  "data": null
}
```

#### 步骤6: 轮询查询判题结果

**第1次查询 (判题中):**
```bash
curl http://localhost:8080/submission/1
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "problemId": 1,
    "problemTitle": "A+B Problem",
    "languageId": 1,
    "languageName": "C",
    "status": "Judging",
    "score": 0,
    "timeUsed": 0,
    "memoryUsed": 0,
    "submitTime": "2024-01-01T14:30:00"
  }
}
```

**第2次查询 (2秒后，判题完成):**
```bash
curl http://localhost:8080/submission/1
```

**响应 (Accepted):**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "problemId": 1,
    "problemTitle": "A+B Problem",
    "languageId": 1,
    "languageName": "C",
    "status": "Accepted",
    "score": 100,
    "timeUsed": 15,
    "memoryUsed": 2048,
    "submitTime": "2024-01-01T14:30:00",
    "results": [
      {
        "testCaseId": 1,
        "status": "Accepted",
        "timeUsed": 2,
        "memoryUsed": 1024,
        "score": 10
      },
      {
        "testCaseId": 2,
        "status": "Accepted",
        "timeUsed": 1,
        "memoryUsed": 1024,
        "score": 10
      }
      // ... 其他10个测试点全部通过
    ]
  }
}
```

#### 步骤7: 查看提交历史

**请求:**
```bash
curl "http://localhost:8080/submission/list?current=1&size=10&problemId=1"
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "problemTitle": "A+B Problem",
        "languageName": "C",
        "status": "Accepted",
        "score": 100,
        "timeUsed": 15,
        "memoryUsed": 2048,
        "submitTime": "2024-01-01T14:30:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

---

## 完整示例场景

### 场景3: 错误提交示例

#### 编译错误 (Compile Error)

**提交代码 (缺少分号):**
```bash
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() {\n    int a, b;\n    scanf(\"%d %d\", &a, &b)\n    printf(\"%d\", a + b);\n    return 0;\n}"
  }'
```

**触发判题:**
```bash
curl -X POST http://localhost:8080/judge/2
```

**查询结果:**
```bash
curl http://localhost:8080/submission/2
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 2,
    "problemId": 1,
    "problemTitle": "A+B Problem",
    "languageId": 1,
    "languageName": "C",
    "status": "Compile Error",
    "score": 0,
    "errorMessage": "main.c:4:5: error: expected ';' before 'printf'",
    "submitTime": "2024-01-01T14:35:00"
  }
}
```

#### 答案错误 (Wrong Answer)

**提交代码 (输出 a-b):**
```bash
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() {\n    int a, b;\n    scanf(\"%d %d\", &a, &b);\n    printf(\"%d\", a - b);\n    return 0;\n}"
  }'
```

**触发判题并查询:**
```bash
curl -X POST http://localhost:8080/judge/3
sleep 2
curl http://localhost:8080/submission/3
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 3,
    "problemId": 1,
    "problemTitle": "A+B Problem",
    "languageId": 1,
    "languageName": "C",
    "status": "Wrong Answer",
    "score": 0,
    "timeUsed": 10,
    "memoryUsed": 1536,
    "submitTime": "2024-01-01T14:40:00",
    "results": [
      {
        "testCaseId": 1,
        "status": "Wrong Answer",
        "timeUsed": 1,
        "memoryUsed": 1024,
        "score": 0,
        "expectedOutput": "3",
        "actualOutput": "-1"
      }
      // ... 所有测试点都是Wrong Answer
    ]
  }
}
```

#### 超时 (Time Limit Exceeded)

**提交代码 (死循环):**
```bash
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() {\n    int a, b;\n    scanf(\"%d %d\", &a, &b);\n    while(1);\n    printf(\"%d\", a + b);\n    return 0;\n}"
  }'
```

**查询结果:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 4,
    "status": "Time Limit Exceeded",
    "score": 0,
    "timeUsed": 1000,
    "results": [
      {
        "testCaseId": 1,
        "status": "Time Limit Exceeded",
        "timeUsed": 1000,
        "score": 0
      }
    ]
  }
}
```

#### 内存超限 (Memory Limit Exceeded)

**提交代码 (申请大数组):**
```bash
curl -X POST http://localhost:8080/submission/submit \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "languageId": 1,
    "code": "#include <stdio.h>\nint main() {\n    int a, b;\n    int arr[100000000];\n    scanf(\"%d %d\", &a, &b);\n    printf(\"%d\", a + b);\n    return 0;\n}"
  }'
```

**查询结果:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 5,
    "status": "Memory Limit Exceeded",
    "score": 0,
    "memoryUsed": 268435456,
    "results": [
      {
        "testCaseId": 1,
        "status": "Memory Limit Exceeded",
        "memoryUsed": 268435456,
        "score": 0
      }
    ]
  }
}
```

---

## 前端集成示例

### JavaScript/TypeScript 示例

```javascript
class OJClient {
  constructor(baseURL = 'http://localhost:8080') {
    this.baseURL = baseURL;
  }

  // 提交代码并等待判题结果
  async submitAndJudge(problemId, languageId, code) {
    try {
      // 1. 提交代码
      const submitRes = await fetch(`${this.baseURL}/submission/submit`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ problemId, languageId, code })
      });
      const submitData = await submitRes.json();
      
      if (submitData.code !== 200) {
        throw new Error(submitData.message);
      }
      
      const submissionId = submitData.data.submissionId;
      console.log(`提交成功，ID: ${submissionId}`);
      
      // 2. 触发判题
      await fetch(`${this.baseURL}/judge/${submissionId}`, {
        method: 'POST'
      });
      console.log('判题任务已提交');
      
      // 3. 轮询判题结果
      return await this.pollResult(submissionId);
      
    } catch (error) {
      console.error('提交失败:', error);
      throw error;
    }
  }

  // 轮询判题结果
  async pollResult(submissionId, maxAttempts = 30) {
    for (let i = 0; i < maxAttempts; i++) {
      await new Promise(resolve => setTimeout(resolve, 1000)); // 等待1秒
      
      const res = await fetch(`${this.baseURL}/submission/${submissionId}`);
      const data = await res.json();
      
      if (data.code !== 200) {
        throw new Error(data.message);
      }
      
      const status = data.data.status;
      console.log(`第${i+1}次查询，状态: ${status}`);
      
      // 判题完成
      if (status !== 'Pending' && status !== 'Judging') {
        return data.data;
      }
    }
    
    throw new Error('判题超时');
  }

  // 获取题目列表
  async getProblems(page = 1, size = 10) {
    const res = await fetch(
      `${this.baseURL}/problem/page?current=${page}&size=${size}`
    );
    return await res.json();
  }

  // 获取题目详情
  async getProblem(problemId) {
    const res = await fetch(`${this.baseURL}/problem/${problemId}`);
    return await res.json();
  }
}

// 使用示例
const client = new OJClient();

// 提交代码
const result = await client.submitAndJudge(
  1, // problemId
  1, // languageId (C)
  `#include <stdio.h>
int main() {
    int a, b;
    scanf("%d %d", &a, &b);
    printf("%d", a + b);
    return 0;
}`
);

console.log('判题结果:', result);
// 输出: { status: 'Accepted', score: 100, ... }
```

### Python 示例

```python
import requests
import time

class OJClient:
    def __init__(self, base_url='http://localhost:8080'):
        self.base_url = base_url
    
    def submit_and_judge(self, problem_id, language_id, code):
        """提交代码并等待判题结果"""
        # 1. 提交代码
        submit_res = requests.post(
            f'{self.base_url}/submission/submit',
            json={
                'problemId': problem_id,
                'languageId': language_id,
                'code': code
            }
        )
        submit_data = submit_res.json()
        
        if submit_data['code'] != 200:
            raise Exception(submit_data['message'])
        
        submission_id = submit_data['data']['submissionId']
        print(f'提交成功，ID: {submission_id}')
        
        # 2. 触发判题
        requests.post(f'{self.base_url}/judge/{submission_id}')
        print('判题任务已提交')
        
        # 3. 轮询判题结果
        return self.poll_result(submission_id)
    
    def poll_result(self, submission_id, max_attempts=30):
        """轮询判题结果"""
        for i in range(max_attempts):
            time.sleep(1)  # 等待1秒
            
            res = requests.get(f'{self.base_url}/submission/{submission_id}')
            data = res.json()
            
            if data['code'] != 200:
                raise Exception(data['message'])
            
            status = data['data']['status']
            print(f'第{i+1}次查询，状态: {status}')
            
            # 判题完成
            if status not in ['Pending', 'Judging']:
                return data['data']
        
        raise Exception('判题超时')

# 使用示例
client = OJClient()

code = """#include <stdio.h>
int main() {
    int a, b;
    scanf("%d %d", &a, &b);
    printf("%d", a + b);
    return 0;
}"""

result = client.submit_and_judge(1, 1, code)
print(f'判题结果: {result["status"]}, 分数: {result["score"]}')
```

---

## 管理操作示例

### 更新题目

```bash
curl -X PUT http://localhost:8080/admin/problem/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "A+B Problem (Updated)",
    "description": "更新后的题目描述...",
    "difficulty": "Easy",
    "timeLimit": 2000,
    "memoryLimit": 512,
    "status": "Public"
  }'
```

### 删除测试用例

```bash
# 删除单个测试用例
curl -X DELETE http://localhost:8080/admin/testcase/1

# 批量删除题目的所有测试用例
curl -X DELETE http://localhost:8080/admin/testcase/batch?problemId=1
```

### 禁用语言

```bash
curl -X PUT http://localhost:8080/admin/language/1 \
  -H "Content-Type: application/json" \
  -d '{
    "enabled": false
  }'
```

---

## 常见问题

### Q1: 提交后一直是 Pending 状态怎么办？

**A:** 手动触发判题：
```bash
curl -X POST http://localhost:8080/judge/{submissionId}
```

### Q2: 如何查看详细的编译错误信息？

**A:** 查询提交详情，`errorMessage` 字段包含编译器输出：
```bash
curl http://localhost:8080/submission/{submissionId}
```

### Q3: 如何设置不同语言的资源限制？

**A:** 在创建语言时设置 `timeLimit` 和 `memoryLimit`，系统会根据语言类型自动调整倍率（编译型语言通常是解释型的2-3倍）。

---

## 总结

本文档提供了EmiyaOJ系统的完整使用示例，涵盖：

1. **管理端**: 创建语言、题目、测试用例
2. **用户端**: 浏览题目、提交代码、查询结果
3. **错误场景**: CE、WA、TLE、MLE等各种情况
4. **前端集成**: JavaScript/Python客户端示例

更多API详情请参考 Swagger 文档: `http://localhost:8080/doc.html`
