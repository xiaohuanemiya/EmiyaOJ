# EmiyaOJ 项目文档

欢迎查阅 EmiyaOJ 在线判题系统的项目文档。

## 📚 文档目录

### 前端文档

#### 🎯 [OJ用户端前端设计文档](./oj-frontend-design.md)
**2873行完整设计文档** - 这是前端开发的核心参考文档，包含：

- ✅ 完整的技术架构设计 (Vue 3.5 + Vite + TypeScript + Element Plus)
- ✅ 详细的项目目录结构规划
- ✅ 核心功能模块实现 (认证、题目、代码编辑、提交)
- ✅ 完整的代码示例和组件实现
- ✅ Pinia状态管理方案
- ✅ Vue Router路由配置
- ✅ Axios API封装
- ✅ TypeScript类型定义
- ✅ 交互流程图和状态转换
- ✅ 部署配置 (Nginx、Docker)
- ✅ 最佳实践和性能优化建议

**适用场景**：前端开发团队的权威指南，详细的架构设计和实现参考

---

#### 🚀 [前端快速开始指南](./oj-frontend-quickstart.md)
**290行简明指南** - 快速上手EmiyaOJ前端开发：

- 项目初始化步骤
- 关键依赖安装
- 核心代码片段
- 常用命令参考
- 故障排查指南

**适用场景**：新团队成员快速上手，日常开发参考

---

#### 📖 [前端API文档](./oj-frontend-api-documentation.md)
**308行API参考** - 前后端接口对接文档：

- 统一响应格式说明
- 题目管理API
- 提交管理API
- 语言管理API
- 请求参数和响应示例

**适用场景**：前后端对接，API调用参考

---

### 后端文档

#### 📘 [后端API文档](./controller-api-documentation.md)
后端控制器接口详细说明，包含语言管理、题目管理、测试用例管理的所有接口

#### �� [项目实现总结](./IMPLEMENTATION_SUMMARY.md)
后端实现的完整总结，包含数据库实体、服务层、控制器和判题流程

#### 📙 [OJ系统说明](./OJ_README.md)
EmiyaOJ系统整体介绍，包含技术栈、核心功能、系统架构和快速开始指南

---

## 🏗️ 技术栈概览

### 前端技术栈
- **框架**: Vue 3.5 (Composition API)
- **构建工具**: Vite 5.x
- **语言**: TypeScript 5.x
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4.x
- **HTTP客户端**: Axios
- **代码编辑器**: Monaco Editor
- **Markdown渲染**: Markdown-it
- **代码高亮**: Highlight.js

### 后端技术栈
- **框架**: Spring Boot 3.5.5
- **Java版本**: 21
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.13
- **判题引擎**: go-judge
- **API文档**: Knife4j (Swagger)
- **缓存**: Redis

---

## 📖 使用建议

### 对于前端开发人员
1. **初次接触项目**: 先阅读 [快速开始指南](./oj-frontend-quickstart.md)
2. **深入开发**: 参考 [前端设计文档](./oj-frontend-design.md)
3. **API对接**: 查阅 [前端API文档](./oj-frontend-api-documentation.md)

### 对于后端开发人员
1. **了解系统**: 阅读 [OJ系统说明](./OJ_README.md)
2. **接口开发**: 参考 [后端API文档](./controller-api-documentation.md)
3. **实现细节**: 查看 [实现总结](./IMPLEMENTATION_SUMMARY.md)

### 对于项目管理人员
1. **整体架构**: 查阅前后端设计文档
2. **技术选型**: 了解技术栈说明
3. **开发规划**: 参考功能模块划分

---

## 🔄 文档更新记录

| 日期 | 文档 | 更新内容 |
|------|------|----------|
| 2024-12-16 | oj-frontend-design.md | 创建完整的前端设计文档 (2873行) |
| 2024-12-16 | oj-frontend-quickstart.md | 创建前端快速开始指南 (290行) |
| 2024-12-16 | README.md | 创建文档索引 |

---

## 📞 联系方式

如有疑问，请联系开发团队。

---

## 📄 许可证

本项目采用 MIT 许可证。

---

**最后更新**: 2024-12-16
