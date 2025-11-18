# OJ 系统 API 使用示例

## 1. 创建题目

```bash
POST /problem
Content-Type: application/json

{
  "title": "两数之和",
  "description": "给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。",
  "inputDescription": "第一行包含整数 n 和 target\n接下来 n 行，每行一个整数",
  "outputDescription": "输出两个整数的下标（从0开始）",
  "difficulty": 1,
  "timeLimit": 1000,
  "memoryLimit": 256,
  "stackLimit": 128,
  "examples": "[{\"input\":\"4 9\\n2\\n7\\n11\\n15\",\"output\":\"0 1\"}]",
  "hint": "可以使用哈希表来优化时间复杂度",
  "tags": "数组,哈希表"
}
```

响应：
```json
{
  "code": 200,
  "msg": "success",
  "data": 1  // 题目ID
}
```

## 2. 添加测试用例

在创建题目后，需要通过数据库直接插入测试用例（或扩展 API）：

```sql
INSERT INTO test_case (problem_id, input, output, is_sample, sort_order)
VALUES (1, '4 9\n2\n7\n11\n15', '0 1', 1, 1);

INSERT INTO test_case (problem_id, input, output, is_sample, sort_order)
VALUES (1, '3 6\n3\n2\n4', '1 2', 0, 2);
```

## 3. 查询题目列表

```bash
GET /problem/page?pageNo=1&pageSize=10&difficulty=1
```

响应：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "total": 100,
    "pages": 10,
    "list": [
      {
        "id": 1,
        "title": "两数之和",
        "difficulty": 1,
        "difficultyDesc": "简单",
        "acceptedCount": 50,
        "submitCount": 100,
        "acceptRate": 50.0,
        "tags": "数组,哈希表"
      }
    ]
  }
}
```

## 4. 获取题目详情

```bash
GET /problem/1
```

响应：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1,
    "title": "两数之和",
    "description": "给定一个整数数组...",
    "inputDescription": "第一行包含整数 n 和 target...",
    "outputDescription": "输出两个整数的下标...",
    "difficulty": 1,
    "difficultyDesc": "简单",
    "timeLimit": 1000,
    "memoryLimit": 256,
    "stackLimit": 128,
    "examples": "[{\"input\":\"4 9\\n2\\n7\\n11\\n15\",\"output\":\"0 1\"}]",
    "hint": "可以使用哈希表来优化时间复杂度",
    "tags": "数组,哈希表",
    "acceptedCount": 50,
    "submitCount": 100,
    "acceptRate": 50.0
  }
}
```

## 5. 提交代码（C++ 示例）

```bash
POST /submission/submit
Content-Type: application/json

{
  "problemId": 1,
  "language": "cpp",
  "code": "#include <iostream>\n#include <vector>\n#include <unordered_map>\nusing namespace std;\n\nint main() {\n    int n, target;\n    cin >> n >> target;\n    \n    vector<int> nums(n);\n    for (int i = 0; i < n; i++) {\n        cin >> nums[i];\n    }\n    \n    unordered_map<int, int> map;\n    for (int i = 0; i < n; i++) {\n        int complement = target - nums[i];\n        if (map.count(complement)) {\n            cout << map[complement] << \" \" << i << endl;\n            return 0;\n        }\n        map[nums[i]] = i;\n    }\n    \n    return 0;\n}\n"
}
```

响应：
```json
{
  "code": 200,
  "msg": "success",
  "data": 1001  // 提交ID
}
```

## 6. 查询提交结果

```bash
GET /submission/1001
```

响应（判题中）：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1001,
    "problemId": 1,
    "problemTitle": "两数之和",
    "userId": 1,
    "username": "admin",
    "language": "cpp",
    "status": "Judging",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

响应（判题完成）：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1001,
    "problemId": 1,
    "problemTitle": "两数之和",
    "userId": 1,
    "username": "admin",
    "language": "cpp",
    "status": "Accepted",
    "timeUsed": 15000000,
    "memoryUsed": 2048000,
    "passRate": "2/2",
    "createTime": "2024-01-01T12:00:00",
    "finishTime": "2024-01-01T12:00:02"
  }
}
```

## 7. 提交代码（Python 示例）

```bash
POST /submission/submit
Content-Type: application/json

{
  "problemId": 1,
  "language": "python",
  "code": "n, target = map(int, input().split())\nnums = []\nfor _ in range(n):\n    nums.append(int(input()))\n\nmap_dict = {}\nfor i in range(n):\n    complement = target - nums[i]\n    if complement in map_dict:\n        print(map_dict[complement], i)\n        break\n    map_dict[nums[i]] = i\n"
}
```

## 8. 提交代码（Java 示例）

```bash
POST /submission/submit
Content-Type: application/json

{
  "problemId": 1,
  "language": "java",
  "code": "import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int target = sc.nextInt();\n        \n        int[] nums = new int[n];\n        for (int i = 0; i < n; i++) {\n            nums[i] = sc.nextInt();\n        }\n        \n        HashMap<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < n; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                System.out.println(map.get(complement) + \" \" + i);\n                return;\n            }\n            map.put(nums[i], i);\n        }\n    }\n}\n"
}
```

## 9. 查询用户提交列表

```bash
GET /submission/user/1
```

## 10. 查询题目提交列表

```bash
GET /submission/problem/1
```

## 判题状态说明

| 状态 | 说明 |
|------|------|
| Pending | 等待判题 |
| Judging | 正在判题 |
| Accepted | 通过所有测试用例 |
| Wrong Answer | 答案错误 |
| Time Limit Exceeded | 运行超时 |
| Memory Limit Exceeded | 内存超限 |
| Runtime Error | 运行时错误（如段错误、除零等） |
| Compile Error | 编译失败 |
| System Error | 系统错误 |

## 注意事项

1. 提交代码前需要先登录获取 JWT token
2. 判题是异步的，需要轮询查询结果
3. 不同语言的主类名和文件名要求：
   - C: main.c
   - C++: main.cpp
   - Java: Main.java（主类名必须是 Main）
   - Python: main.py
   - Go: main.go
4. 所有代码都在沙箱中运行，有严格的资源限制
5. 输入输出通过标准输入输出进行
