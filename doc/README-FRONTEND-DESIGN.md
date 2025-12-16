# EmiyaOJ 前端设计文档导航

本目录包含 EmiyaOJ 在线判题系统用户端（Vue 3.5）的完整设计文档和代码示例。

## 📚 文档结构

### 核心设计文档

#### 1. [Vue 3.5 用户端设计文档](./vue3-user-frontend-design.md)
**主要内容：**
- ✅ 系统概述与技术栈
- ✅ 系统架构设计（分层架构图）
- ✅ 项目目录结构（完整的文件树）
- ✅ 核心模块设计
  - 路由配置与守卫
  - API 服务层（Axios 封装）
  - 状态管理（Pinia）
  - TypeScript 类型定义
- ✅ 核心组件设计（带完整代码）
  - 代码编辑器组件
  - 题目卡片组件
  - 状态标签组件
  - Markdown 查看器
- ✅ 页面视图设计
  - 题目列表页
  - 题目详情页
- ✅ 数据流与状态管理
- ✅ API 集成说明
- ✅ 实时判题状态更新（轮询机制）
- ✅ 安全性考虑
- ✅ 性能优化策略
- ✅ 开发指南
- ✅ 测试策略
- ✅ 部署方案
- ✅ 未来扩展规划

**文件信息：** 21KB, 744 行

#### 2. [用户交互流程文档](./user-interaction-flows.md)
**主要内容：**
- ✅ 用户认证流程（登录、权限控制）
- ✅ 题目浏览流程（列表、搜索、筛选）
- ✅ 题目详情与代码提交流程
- ✅ 判题结果查看流程（实时更新）
- ✅ 提交记录管理流程
- ✅ 错误处理流程（网络、表单、权限）
- ✅ 用户体验优化（加载、响应式、缓存）
- ✅ 性能优化（懒加载、防抖、虚拟滚动）
- ✅ 辅助功能（快捷键、主题）

**包含流程图：** ASCII 艺术格式的流程图

### 代码示例

#### 3. [Vue 3.5 代码示例目录](./vue3-examples/)

**包含文件：**

| 文件名 | 说明 | 行数 |
|--------|------|------|
| [package.json](./vue3-examples/package.json) | 项目依赖配置 | 46 |
| [vite.config.ts](./vue3-examples/vite.config.ts) | Vite 构建配置 | 78 |
| [SubmissionDetail.vue](./vue3-examples/SubmissionDetail.vue) | 提交详情页组件 | 189 |
| [useSubmission.ts](./vue3-examples/useSubmission.ts) | 提交逻辑组合式函数 | 185 |
| [README.md](./vue3-examples/README.md) | 示例使用说明 | 139 |

**代码示例特点：**
- ✅ 完整可运行的配置文件
- ✅ 真实的业务组件示例
- ✅ 最佳实践演示
- ✅ TypeScript 类型安全
- ✅ 详细的代码注释

## 🚀 快速开始

### 1. 阅读顺序建议

对于开发者：
1. 先阅读 [主设计文档](./vue3-user-frontend-design.md) 了解整体架构
2. 参考 [交互流程文档](./user-interaction-flows.md) 理解业务流程
3. 查看 [代码示例](./vue3-examples/) 学习具体实现

对于项目经理/产品经理：
1. 先阅读 [交互流程文档](./user-interaction-flows.md) 了解用户体验
2. 查看 [主设计文档](./vue3-user-frontend-design.md) 的功能模块部分
3. 参考未来扩展部分规划产品路线图

### 2. 技术栈总览

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5+ | 前端框架 |
| TypeScript | 5.x | 类型安全 |
| Vite | 6.x | 构建工具 |
| Pinia | 2.x | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Element Plus | 2.x | UI 组件库 |
| Axios | 1.x | HTTP 客户端 |
| Monaco Editor | 0.45.x | 代码编辑器 |
| markdown-it | 14.x | Markdown 渲染 |

### 3. 核心功能模块

```
EmiyaOJ 用户端
├── 用户认证
│   ├── 登录/登出
│   ├── Token 管理
│   └── 权限控制
├── 题目管理
│   ├── 题目列表
│   ├── 题目搜索
│   ├── 题目筛选
│   └── 题目详情
├── 代码编辑
│   ├── Monaco Editor
│   ├── 语法高亮
│   ├── 代码补全
│   └── 多语言支持
├── 代码提交
│   ├── 表单验证
│   ├── 异步提交
│   └── 结果跳转
├── 判题结果
│   ├── 实时轮询
│   ├── 状态展示
│   └── 详情查看
└── 提交记录
    ├── 记录列表
    ├── 筛选功能
    └── 历史查询
```

## 📊 文档统计

| 类型 | 数量 | 行数 | 大小 |
|------|------|------|------|
| 设计文档 | 2 | 1,200+ | 30KB |
| 代码示例 | 5 | 1,000+ | 15KB |
| 总计 | 7 | 2,207 | 45KB |

## 🎯 设计亮点

### 1. 完整的架构设计
- 清晰的分层架构
- 模块化设计
- 组件化开发
- 类型安全

### 2. 最佳实践
- Vue 3 Composition API
- TypeScript 严格模式
- Pinia 状态管理
- 组合式函数（Composables）

### 3. 用户体验
- 实时判题状态更新
- 友好的错误提示
- 响应式设计
- 性能优化

### 4. 可维护性
- 规范的代码结构
- 详细的文档注释
- 统一的错误处理
- 完整的类型定义

### 5. 可扩展性
- 插件化架构
- 灵活的配置
- 易于添加新功能
- 支持主题定制

## 📖 相关文档

### 后端文档
- [后端 API 文档](./oj-frontend-api-documentation.md)
- [Controller API 文档](./controller-api-documentation.md)
- [项目结构](./project.md)
- [实现总结](./IMPLEMENTATION_SUMMARY.md)

### 项目文档
- [OJ 系统 README](./OJ_README.md)
- [博客文档](./blog.md)

## 🔗 外部资源

- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Vite 官方文档](https://cn.vitejs.dev/)
- [Pinia 官方文档](https://pinia.vuejs.org/zh/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [TypeScript 手册](https://www.typescriptlang.org/zh/)
- [Monaco Editor](https://microsoft.github.io/monaco-editor/)

## 🤝 贡献指南

### 文档更新
- 保持文档与代码同步
- 添加新功能时更新相应文档
- 使用清晰的示例和图表
- 注意中英文排版规范

### 代码示例
- 确保示例代码可运行
- 添加详细的注释
- 遵循项目代码规范
- 包含完整的类型定义

## 📝 变更日志

### 2024-12-16
- ✅ 创建完整的 Vue 3.5 用户端设计文档
- ✅ 添加详细的用户交互流程文档
- ✅ 提供完整的代码示例
- ✅ 包含配置文件和构建脚本
- ✅ 添加最佳实践和开发指南

## 📧 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 GitHub Issue
- Pull Request
- 项目讨论区

---

**注意：** 本文档集基于 EmiyaOJ 项目的实际后端 API 设计，提供了完整的前端实现方案。开发时请参考最新的后端 API 文档确保接口对接正确。
