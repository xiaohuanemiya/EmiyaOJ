# Vue 3.5 代码示例

本目录包含 EmiyaOJ 用户端的关键代码示例，供开发参考。

## 📁 文件说明

### 配置文件

- **package.json**: 项目依赖配置，包含所有必需的 npm 包
- **vite.config.ts**: Vite 构建配置，包含优化和插件设置

### 组件示例

- **SubmissionDetail.vue**: 提交详情页组件示例
  - 展示如何查看提交详情
  - 实现实时轮询判题结果
  - 代码高亮和复制功能

### 组合式函数

- **useSubmission.ts**: 提交相关的业务逻辑封装
  - 代码提交功能
  - 判题状态轮询
  - 状态管理和通知

## 🚀 使用方法

### 1. 安装依赖

```bash
# 使用 pnpm (推荐)
pnpm install

# 或使用 npm
npm install

# 或使用 yarn
yarn install
```

### 2. 环境配置

创建 `.env.development` 文件：

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=EmiyaOJ
```

创建 `.env.production` 文件：

```env
VITE_API_BASE_URL=https://api.emiyaoj.com
VITE_APP_TITLE=EmiyaOJ
```

### 3. 启动开发服务器

```bash
pnpm dev
```

应用将在 http://localhost:5173 启动

### 4. 构建生产版本

```bash
pnpm build
```

构建产物将输出到 `dist` 目录

## 📚 相关文档

- [完整设计文档](../vue3-user-frontend-design.md): 系统架构和详细设计
- [交互流程文档](../user-interaction-flows.md): 用户交互和业务流程

## 🔧 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.13 | 渐进式 JavaScript 框架 |
| TypeScript | 5.7.2 | JavaScript 的超集 |
| Vite | 6.0.5 | 下一代前端构建工具 |
| Pinia | 2.3.0 | Vue 状态管理库 |
| Vue Router | 4.5.0 | Vue 官方路由 |
| Element Plus | 2.9.1 | Vue 3 UI 组件库 |
| Axios | 1.7.9 | HTTP 客户端 |
| Monaco Editor | 0.45.0 | 代码编辑器 |

## 📖 代码示例说明

### SubmissionDetail.vue

这个组件展示了如何实现提交详情页的核心功能：

**主要特性：**
1. **实时判题状态更新**: 使用轮询机制每2秒检查一次判题结果
2. **详细信息展示**: 包括状态、运行时间、内存使用等
3. **代码查看**: 使用只读模式的 Monaco Editor 展示代码
4. **复制功能**: 一键复制提交的代码

**关键代码：**

```vue
// 轮询判题结果
const { start: startPolling, stop: stopPolling } = usePolling(
  async () => {
    const data = await getSubmissionDetail(submissionId)
    submission.value = data
    return data
  },
  () => isJudging.value,  // 判题中时继续轮询
  2000  // 每2秒轮询一次
)
```

**使用场景：**
- 用户提交代码后查看判题结果
- 实时更新判题进度
- 查看历史提交记录

### useSubmission.ts

这是一个组合式函数（Composable），封装了所有提交相关的业务逻辑：

**主要功能：**
1. **代码提交**: 验证和提交代码
2. **状态管理**: 管理提交状态和当前提交信息
3. **轮询控制**: 自动轮询判题结果
4. **结果通知**: 判题完成后显示相应通知

**使用示例：**

```typescript
import { useSubmission } from '@/composables/useSubmission'

const {
  submitting,        // 是否正在提交
  currentSubmission, // 当前提交信息
  isJudging,        // 是否正在判题
  submit,           // 提交代码方法
  fetchAndWatch     // 获取并监听提交状态
} = useSubmission()

// 提交代码
await submit({
  problemId: 1,
  languageId: 2,
  code: '...'
})
```

**优势：**
- 逻辑复用: 可在多个组件中使用
- 关注点分离: 将业务逻辑从组件中抽离
- 易于测试: 独立的函数便于单元测试

## 🎯 最佳实践

### 1. 组件设计

```vue
<script setup lang="ts">
// 使用 Composition API
// 使用 TypeScript 类型定义
// 使用组合式函数封装逻辑
</script>
```

### 2. 状态管理

```typescript
// 使用 Pinia 进行状态管理
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
```

### 3. API 调用

```typescript
// 统一的错误处理
try {
  const data = await apiCall()
  // 处理成功
} catch (error) {
  // 统一错误处理
  console.error(error)
  ElMessage.error('操作失败')
}
```

### 4. 类型安全

```typescript
// 定义明确的类型
interface SubmissionVO {
  id: number
  problemId: number
  status: number
  // ...
}
```

## 🐛 常见问题

### Q: Monaco Editor 加载慢怎么办？

A: 使用 CDN 加载或启用懒加载：

```typescript
const CodeEditor = defineAsyncComponent(() => 
  import('@/components/CodeEditor/index.vue')
)
```

### Q: 如何优化轮询性能？

A: 
1. 设置合理的轮询间隔（2-5秒）
2. 判题完成后立即停止轮询
3. 组件卸载时清理轮询定时器

### Q: 如何处理大量提交记录？

A:
1. 使用分页加载
2. 考虑虚拟滚动
3. 实现服务端搜索和筛选

## 📝 开发规范

### 命名规范

- **组件名**: PascalCase (如 `SubmissionDetail.vue`)
- **文件名**: kebab-case (如 `use-submission.ts`)
- **变量名**: camelCase (如 `isJudging`)
- **常量名**: UPPER_SNAKE_CASE (如 `MAX_RETRY_COUNT`)

### 代码风格

```typescript
// 使用 ESLint 和 Prettier
// 遵循 Vue 3 风格指南
// 添加必要的注释
// 保持代码简洁和可读性
```

### Git 提交

```bash
# 使用语义化提交消息
feat: 添加提交详情页
fix: 修复轮询内存泄漏
docs: 更新 README
style: 格式化代码
refactor: 重构提交逻辑
test: 添加单元测试
```

## 🔗 相关链接

- [Vue 3 文档](https://cn.vuejs.org/)
- [Vite 文档](https://cn.vitejs.dev/)
- [Pinia 文档](https://pinia.vuejs.org/zh/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [TypeScript 文档](https://www.typescriptlang.org/zh/)

## 📄 License

MIT License
