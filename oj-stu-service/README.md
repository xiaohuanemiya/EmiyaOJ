# 学生端服务 (oj-stu-service)

## 简介

学生端服务是EmiyaOJ系统的学生答题模块，提供学生登录、题目浏览和代码提交功能。

## 功能特性

- ✅ 学生登录/退出
- ✅ 题目列表查询（仅公开题目）
- ✅ 题目详情查看
- ✅ 代码提交
- ✅ 提交记录查询
- ✅ 编程语言列表查询

## 技术栈

- Spring Boot 3.5.5
- Spring Security（JWT认证）
- MyBatis Plus
- MySQL
- Redis

## 端口配置

- 服务端口：8081
- 数据库：localhost:3306/emiya-oj
- Redis：localhost:6379

## API接口

### 认证相关
- `POST /stu/auth/login` - 学生登录
- `POST /stu/auth/logout` - 学生退出登录

### 题目相关
- `GET /stu/problem/page` - 分页查询题目列表
- `GET /stu/problem/{id}` - 查询题目详情
- `GET /stu/problem/languages` - 获取编程语言列表

### 提交相关
- `POST /stu/submission/submit` - 提交代码
- `GET /stu/submission/page` - 分页查询提交记录
- `GET /stu/submission/{id}` - 查询提交详情

## 启动说明

1. 确保MySQL数据库已启动并创建了`emiya-oj`数据库
2. 确保Redis已启动
3. 运行`StuApplication.java`启动服务
4. 访问 `http://localhost:8081/swagger-ui.html` 查看API文档

## 前端访问

学生端前端页面：
- 登录页：`http://localhost:3000/student/login`
- 题目列表：`http://localhost:3000/student/problems`
- 题目详情：`http://localhost:3000/student/problem/{id}`

