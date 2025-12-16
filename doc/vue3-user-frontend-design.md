# EmiyaOJ 用户端设计文档 (Vue 3.5)

## 1. 系统概述

### 1.1 项目简介

EmiyaOJ 用户端是一个基于 Vue 3.5 的在线编程判题系统前端应用，为用户提供题目浏览、代码编写、在线提交和判题结果查看等功能。

### 1.2 技术栈

- **框架**: Vue 3.5 (Composition API)
- **构建工具**: Vite 5.x
- **状态管理**: Pinia 2.x
- **路由**: Vue Router 4.x
- **HTTP 客户端**: Axios 1.x
- **UI 框架**: Element Plus / Ant Design Vue
- **代码编辑器**: Monaco Editor / CodeMirror
- **样式**: CSS3 / SCSS
- **TypeScript**: 5.x
- **Markdown 渲染**: markdown-it

### 1.3 核心功能

1. 用户认证与授权
2. 题目列表浏览与搜索
3. 题目详情查看
4. 在线代码编辑
5. 代码提交与判题
6. 提交记录查询
7. 判题结果实时展示
8. 个人提交历史

## 2. 系统架构

### 2.1 整体架构

```
┌─────────────────────────────────────────────────────────┐
│                     用户界面层                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │
│  │题目列表  │  │题目详情  │  │代码编辑  │  │提交记录│ │
│  └──────────┘  └──────────┘  └──────────┘  └────────┘ │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                    组件层                                │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │
│  │导航栏    │  │搜索栏    │  │代码编辑器│  │状态标签│ │
│  └──────────┘  └──────────┘  └──────────┘  └────────┘ │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                  业务逻辑层                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Pinia    │  │ Composables│ │ Utils   │             │
│  │ Stores   │  │            │  │         │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                   API 服务层                             │
│  ┌────────────────────────────────────────────────┐    │
│  │    Axios Instance + Request/Response          │    │
│  │    Interceptors                               │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                   后端 API                               │
│  EmiyaOJ Backend (Spring Boot)                          │
└─────────────────────────────────────────────────────────┘
```

### 2.2 项目目录结构

```
oj-user-frontend/
├── public/                      # 静态资源
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── api/                     # API 请求模块
│   │   ├── index.ts            # API 统一导出
│   │   ├── request.ts          # Axios 封装
│   │   ├── problem.ts          # 题目相关 API
│   │   ├── submission.ts       # 提交相关 API
│   │   ├── language.ts         # 语言相关 API
│   │   └── auth.ts             # 认证相关 API
│   ├── assets/                  # 静态资源
│   │   ├── styles/             # 样式文件
│   │   │   ├── main.scss       # 主样式
│   │   │   ├── variables.scss  # 变量
│   │   │   └── common.scss     # 通用样式
│   │   └── images/             # 图片资源
│   ├── components/              # 公共组件
│   │   ├── CodeEditor/         # 代码编辑器组件
│   │   │   ├── index.vue
│   │   │   └── types.ts
│   │   ├── ProblemCard/        # 题目卡片组件
│   │   │   └── index.vue
│   │   ├── StatusTag/          # 状态标签组件
│   │   │   └── index.vue
│   │   ├── LanguageSelector/   # 语言选择器
│   │   │   └── index.vue
│   │   ├── MarkdownViewer/     # Markdown 查看器
│   │   │   └── index.vue
│   │   ├── Pagination/         # 分页组件
│   │   │   └── index.vue
│   │   └── Layout/             # 布局组件
│   │       ├── Header.vue
│   │       ├── Footer.vue
│   │       └── Sidebar.vue
│   ├── composables/             # 组合式函数
│   │   ├── usePagination.ts    # 分页逻辑
│   │   ├── useCodeEditor.ts    # 代码编辑器逻辑
│   │   ├── useSubmission.ts    # 提交逻辑
│   │   └── usePolling.ts       # 轮询逻辑
│   ├── router/                  # 路由配置
│   │   ├── index.ts            # 路由主文件
│   │   └── guards.ts           # 路由守卫
│   ├── stores/                  # 状态管理
│   │   ├── index.ts            # Store 统一导出
│   │   ├── user.ts             # 用户状态
│   │   ├── problem.ts          # 题目状态
│   │   ├── submission.ts       # 提交状态
│   │   └── language.ts         # 语言状态
│   ├── types/                   # TypeScript 类型定义
│   │   ├── api.ts              # API 相关类型
│   │   ├── problem.ts          # 题目相关类型
│   │   ├── submission.ts       # 提交相关类型
│   │   └── common.ts           # 通用类型
│   ├── utils/                   # 工具函数
│   │   ├── request.ts          # 请求工具
│   │   ├── format.ts           # 格式化工具
│   │   ├── storage.ts          # 本地存储工具
│   │   ├── constants.ts        # 常量定义
│   │   └── validators.ts       # 验证器
│   ├── views/                   # 页面视图
│   │   ├── Home/               # 首页
│   │   │   └── index.vue
│   │   ├── ProblemList/        # 题目列表页
│   │   │   └── index.vue
│   │   ├── ProblemDetail/      # 题目详情页
│   │   │   └── index.vue
│   │   ├── SubmissionList/     # 提交记录页
│   │   │   └── index.vue
│   │   ├── SubmissionDetail/   # 提交详情页
│   │   │   └── index.vue
│   │   ├── Login/              # 登录页
│   │   │   └── index.vue
│   │   └── NotFound/           # 404 页面
│   │       └── index.vue
│   ├── App.vue                  # 根组件
│   ├── main.ts                  # 入口文件
│   └── env.d.ts                 # 环境变量类型声明
├── .env.development             # 开发环境变量
├── .env.production              # 生产环境变量
├── .gitignore                   # Git 忽略文件
├── index.html                   # HTML 模板
├── package.json                 # 项目依赖
├── tsconfig.json                # TypeScript 配置
├── vite.config.ts               # Vite 配置
└── README.md                    # 项目说明
```

## 3. 数据流与状态管理

### 3.1 数据流图

```
┌──────────────┐
│  用户操作    │
└──────┬───────┘
       ↓
┌──────────────┐      ┌──────────────┐
│  Vue 组件    │ ←──→ │  Pinia Store │
└──────┬───────┘      └──────────────┘
       ↓
┌──────────────┐
│   API 调用   │
└──────┬───────┘
       ↓
┌──────────────┐
│  后端 API    │
└──────┬───────┘
       ↓
┌──────────────┐
│  响应数据    │
└──────┬───────┘
       ↓
┌──────────────┐
│  更新 Store  │
└──────┬───────┘
       ↓
┌──────────────┐
│  UI 更新     │
└──────────────┘
```

### 3.2 状态管理策略

1. **用户状态**: 存储用户信息、登录态、权限等
2. **题目状态**: 缓存题目列表、当前查看的题目详情
3. **提交状态**: 存储提交记录、当前提交状态
4. **语言状态**: 缓存可用编程语言列表
5. **全局配置**: 主题、语言偏好等

## 4. 用户交互流程

### 4.1 用户登录流程

```
用户访问 → 检查登录状态 → 未登录 → 跳转登录页
                ↓
           已登录 → 进入首页
                ↓
           输入用户名密码
                ↓
           提交登录请求
                ↓
           验证成功 → 保存 Token → 跳转原页面
                ↓
           验证失败 → 显示错误 → 重新输入
```

### 4.2 题目浏览与搜索流程

```
进入题目列表页
     ↓
加载题目列表（分页）
     ↓
可选：筛选（难度、状态）
     ↓
可选：搜索（关键词）
     ↓
显示题目卡片
     ↓
点击题目 → 进入题目详情页
```

### 4.3 代码提交与判题流程

```
进入题目详情页
     ↓
查看题目描述
     ↓
编写代码
     ↓
选择编程语言
     ↓
点击提交
     ↓
前端验证（代码非空）
     ↓
发送提交请求
     ↓
后端创建提交记录
     ↓
跳转到提交详情页
     ↓
显示"判题中"状态
     ↓
轮询判题结果（每2秒）
     ↓
判题完成 → 显示结果
     ↓
显示状态、时间、内存
     ↓
可查看代码详情
```

### 4.4 提交记录查看流程

```
进入提交记录页
     ↓
加载个人提交列表
     ↓
可选：按题目筛选
     ↓
可选：按状态筛选
     ↓
显示提交记录表格
     ↓
点击查看 → 进入提交详情
     ↓
显示代码、状态、判题信息
```

## 5. API 集成说明

### 5.1 API 基础配置

**基础 URL**: 通过环境变量配置

- 开发环境: `http://localhost:8080`
- 生产环境: `https://api.emiyaoj.com`

**请求格式**: JSON

**响应格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { }
}
```

### 5.2 核心 API 接口

#### 5.2.1 题目相关

1. **获取题目列表**
   - URL: `GET /client/problem/page`
   - 参数: `current`, `size`, `difficulty`, `status`, `keyword`
   - 返回: 分页题目列表

2. **获取题目详情**
   - URL: `GET /client/problem/{id}`
   - 返回: 题目完整信息（包含描述、示例等）

#### 5.2.2 提交相关

1. **提交代码**
   - URL: `POST /client/submission/client/submit`
   - 请求体: `{ problemId, languageId, code }`
   - 返回: 提交记录 ID

2. **获取提交列表**
   - URL: `GET /client/submission/page`
   - 参数: `current`, `size`, `problemId`, `userId`
   - 返回: 分页提交记录

3. **获取提交详情**
   - URL: `GET /client/submission/{id}`
   - 返回: 提交详细信息（包含代码、判题结果等）

#### 5.2.3 语言相关

1. **获取可用语言**
   - URL: `GET /client/language/list`
   - 返回: 编程语言列表

2. **获取语言详情**
   - URL: `GET /client/language/{id}`
   - 返回: 语言详细信息

### 5.3 错误处理

统一的错误处理机制：

1. **HTTP 状态码处理**
   - 401: 未授权 → 跳转登录
   - 403: 无权限 → 显示提示
   - 404: 资源不存在 → 显示错误
   - 500: 服务器错误 → 显示错误

2. **业务错误处理**
   - 根据 `code` 字段判断
   - 使用 `message` 显示错误信息
   - 记录错误日志

## 6. 实时判题状态更新

### 6.1 轮询机制

```typescript
// composables/usePolling.ts
import { ref, onUnmounted } from 'vue'

export function usePolling(
  fetchFn: () => Promise<any>,
  condition: () => boolean,
  interval: number = 2000
) {
  const timer = ref<number>()
  const data = ref<any>(null)
  const loading = ref(false)
  
  async function start() {
    loading.value = true
    
    const poll = async () => {
      try {
        data.value = await fetchFn()
        
        if (!condition()) {
          stop()
        } else {
          timer.value = window.setTimeout(poll, interval)
        }
      } catch (error) {
        console.error('Polling error:', error)
        stop()
      } finally {
        loading.value = false
      }
    }
    
    await poll()
  }
  
  function stop() {
    if (timer.value) {
      clearTimeout(timer.value)
      timer.value = undefined
    }
    loading.value = false
  }
  
  onUnmounted(() => {
    stop()
  })
  
  return {
    data,
    loading,
    start,
    stop
  }
}
```

### 6.2 使用示例

```typescript
// 在提交详情页使用
const { data: submission, start, stop } = usePolling(
  () => getSubmissionDetail(submissionId),
  () => submission.value?.status === 0 || submission.value?.status === 7,
  2000
)

onMounted(() => {
  start()
})
```

## 7. 安全性考虑

### 7.1 XSS 防护

1. 使用 Vue 的自动转义机制
2. 对用户输入进行过滤
3. Markdown 渲染使用安全的库配置

### 7.2 CSRF 防护

1. 使用 Token 认证
2. API 请求带上 CSRF Token
3. 设置合适的 CORS 策略

### 7.3 代码安全

1. 不在前端存储敏感信息
2. Token 使用 HTTP-only Cookie（可选）
3. 定期刷新 Token
4. 退出登录时清除所有本地数据

## 8. 性能优化策略

### 8.1 路由懒加载

```typescript
const routes = [
  {
    path: '/problem/:id',
    component: () => import('@/views/ProblemDetail/index.vue')
  }
]
```

### 8.2 组件懒加载

```typescript
defineAsyncComponent(() => import('@/components/CodeEditor/index.vue'))
```

### 8.3 API 请求优化

1. 使用防抖和节流
2. 合理使用缓存
3. 分页加载数据
4. 取消不需要的请求

### 8.4 代码编辑器优化

1. 延迟加载 Monaco Editor
2. 只在需要时初始化编辑器
3. 组件销毁时清理资源

### 8.5 打包优化

1. 代码分割
2. Tree Shaking
3. 压缩资源
4. CDN 加速

## 9. 开发指南

### 9.1 环境准备

```bash
# 安装 Node.js 18+
# 安装 pnpm
npm install -g pnpm

# 克隆项目
git clone <repository-url>
cd oj-user-frontend

# 安装依赖
pnpm install
```

### 9.2 启动开发服务器

```bash
# 启动开发服务器
pnpm dev

# 访问 http://localhost:5173
```

### 9.3 构建生产版本

```bash
# 构建
pnpm build

# 预览
pnpm preview
```

### 9.4 代码规范

1. 使用 ESLint 进行代码检查
2. 使用 Prettier 格式化代码
3. 使用 TypeScript 严格模式
4. 遵循 Vue 3 风格指南

## 10. 测试策略

### 10.1 单元测试

使用 Vitest 进行组件和工具函数测试

```typescript
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import StatusTag from '@/components/StatusTag/index.vue'

describe('StatusTag', () => {
  it('renders correctly', () => {
    const wrapper = mount(StatusTag, {
      props: { status: 2 }
    })
    expect(wrapper.text()).toContain('通过')
  })
})
```

### 10.2 集成测试

测试关键业务流程：

1. 用户登录流程
2. 题目提交流程
3. 判题结果查看

### 10.3 E2E 测试

使用 Playwright 进行端到端测试

## 11. 部署方案

### 11.1 静态部署

```bash
# 构建
pnpm build

# dist 目录部署到 Nginx/Apache
```

### 11.2 Nginx 配置

```nginx
server {
    listen 80;
    server_name emiyaoj.com;
    
    root /var/www/oj-frontend/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 11.3 Docker 部署

```dockerfile
FROM node:18-alpine as builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## 12. 未来扩展

### 12.1 功能扩展

1. 社区讨论功能
2. 题解分享
3. 比赛模式
4. 排行榜
5. 每日一题推荐
6. 代码分享功能
7. 在线 Debug 工具

### 12.2 技术优化

1. WebSocket 实时判题推送
2. Service Worker 离线支持
3. PWA 支持
4. 国际化 (i18n)
5. 主题切换（深色模式）
6. 代码对比工具
7. 性能监控

## 13. 附录

### 13.1 项目依赖清单

```json
{
  "dependencies": {
    "vue": "^3.5.0",
    "vue-router": "^4.0.0",
    "pinia": "^2.0.0",
    "axios": "^1.0.0",
    "element-plus": "^2.0.0",
    "monaco-editor": "^0.45.0",
    "markdown-it": "^14.0.0",
    "highlight.js": "^11.0.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "vite": "^5.0.0",
    "typescript": "^5.0.0",
    "sass": "^1.0.0",
    "eslint": "^8.0.0",
    "prettier": "^3.0.0",
    "vitest": "^1.0.0",
    "@vue/test-utils": "^2.0.0"
  }
}
```

### 13.2 环境变量配置

**.env.development**
```
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=EmiyaOJ
```

**.env.production**
```
VITE_API_BASE_URL=https://api.emiyaoj.com
VITE_APP_TITLE=EmiyaOJ
```

### 13.3 Vite 配置示例

```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          'ui-vendor': ['element-plus'],
          'editor-vendor': ['monaco-editor']
        }
      }
    }
  }
})
```

## 14. 总结

本设计文档为 EmiyaOJ 在线判题系统的 Vue 3.5 用户端提供了完整的技术架构和实现方案。通过采用现代化的前端技术栈和最佳实践，系统具备以下特点：

1. **模块化设计**: 清晰的目录结构和职责划分
2. **类型安全**: 完整的 TypeScript 类型定义
3. **状态管理**: 使用 Pinia 进行集中式状态管理
4. **组件复用**: 丰富的公共组件库
5. **性能优化**: 懒加载、代码分割、缓存策略
6. **用户体验**: 实时判题状态、友好的错误提示
7. **可维护性**: 规范的代码结构和文档
8. **可扩展性**: 灵活的架构支持功能扩展

该设计方案为开发团队提供了清晰的开发指南，确保项目能够高质量、高效率地完成。
