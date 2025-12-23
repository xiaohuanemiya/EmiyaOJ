# EmiyaOJ 前端快速开始指南

本文档提供EmiyaOJ用户端前端项目的快速开始指南。详细的设计文档请参考 [oj-frontend-design.md](./oj-frontend-design.md)。

## 一、项目初始化

### 1. 创建项目

```bash
# 使用Vite创建Vue 3 + TypeScript项目
npm create vite@latest emiya-oj-frontend -- --template vue-ts
cd emiya-oj-frontend
```

### 2. 安装依赖

```bash
# 安装所有必需依赖
npm install vue@^3.5.0 vue-router@^4.4.0 pinia@^2.2.0 \
  element-plus@^2.8.0 @element-plus/icons-vue@^2.3.0 \
  axios@^1.7.0 monaco-editor@^0.50.0 \
  markdown-it@^14.1.0 highlight.js@^11.10.0

# 安装开发依赖
npm install -D sass@^1.77.0 typescript@^5.5.0 vue-tsc@^2.1.0 \
  @typescript-eslint/eslint-plugin@^8.0.0 @typescript-eslint/parser@^8.0.0 \
  eslint@^9.9.0 eslint-plugin-vue@^9.27.0 prettier@^3.3.0
```

### 3. 创建目录结构

```bash
# 创建核心目录
mkdir -p src/{api,assets/{styles,images},components/{CodeEditor,MarkdownViewer,StatusTag},composables,layouts,router,stores,types,utils,views/{Home,Login,ProblemList,ProblemDetail,SubmissionList,SubmissionDetail,Profile}}
```

## 二、核心配置文件

### 1. Vite配置 (vite.config.ts)

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
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

### 2. 环境变量 (.env.development)

```bash
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=EmiyaOJ
```

## 三、关键代码片段

### 1. API请求封装 (src/api/request.ts)

```typescript
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/storage'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000
})

service.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

service.interceptors.response.use(
  response => response.data,
  error => {
    ElMessage.error(error.message || '请求失败')
    return Promise.reject(error)
  }
)

export default service
```

### 2. 认证Store (src/stores/auth.ts)

```typescript
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref('')
  const user = ref(null)
  
  const isAuthenticated = computed(() => !!token.value)
  
  const login = async (params) => {
    // 调用登录API
    // 保存token和用户信息
  }
  
  const logout = () => {
    token.value = ''
    user.value = null
  }
  
  return { token, user, isAuthenticated, login, logout }
})
```

### 3. 路由配置 (src/router/index.ts)

```typescript
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: () => import('@/views/Login/index.vue') },
    { path: '/problems', component: () => import('@/views/ProblemList/index.vue') },
    { path: '/problem/:id', component: () => import('@/views/ProblemDetail/index.vue') }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router
```

## 四、主要功能模块

### 1. 用户认证
- **登录页面**: `/src/views/Login/index.vue`
- **认证Store**: `/src/stores/auth.ts`
- **路由守卫**: `/src/router/index.ts`

### 2. 题目浏览
- **题目列表**: `/src/views/ProblemList/index.vue`
- **题目详情**: `/src/views/ProblemDetail/index.vue`
- **题目Store**: `/src/stores/problem.ts`

### 3. 代码提交
- **代码编辑器**: `/src/components/CodeEditor/index.vue`
- **提交Store**: `/src/stores/submission.ts`
- **提交API**: `/src/api/submission.ts`

### 4. 提交记录
- **提交列表**: `/src/views/SubmissionList/index.vue`
- **提交详情**: `/src/views/SubmissionDetail/index.vue`
- **状态标签**: `/src/components/StatusTag/index.vue`

## 五、运行和部署

### 开发环境

```bash
# 启动开发服务器
npm run dev

# 访问 http://localhost:3000
```

### 生产构建

```bash
# 构建生产版本
npm run build

# 预览构建结果
npm run preview
```

### Nginx部署

```nginx
server {
    listen 80;
    server_name emiyaoj.com;
    root /var/www/emiya-oj-frontend/dist;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
    }
}
```

## 六、核心API接口

### 用户认证
- `POST /auth/login` - 用户登录
- `POST /auth/logout` - 用户登出

### 题目管理
- `GET /client/problem/page` - 分页查询题目
- `GET /client/problem/{id}` - 获取题目详情

### 代码提交
- `POST /client/submission/client/submit` - 提交代码
- `GET /client/submission/page` - 查询提交记录
- `GET /client/submission/{id}` - 获取提交详情

### 编程语言
- `GET /client/language/list` - 获取所有可用语言

## 七、常用命令

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 代码检查
npm run lint

# 代码格式化
npm run format

# 类型检查
npm run type-check
```

## 八、开发建议

1. **使用TypeScript**: 利用类型系统提高代码质量
2. **组件化开发**: 将UI拆分为可复用的小组件
3. **状态管理**: 使用Pinia管理全局状态
4. **API封装**: 统一管理所有API请求
5. **错误处理**: 优雅地处理API错误和异常情况
6. **代码规范**: 遵循ESLint和Prettier配置

## 九、故障排查

### 问题1: API请求跨域
**解决**: 在vite.config.ts中配置proxy代理

### 问题2: Monaco Editor加载慢
**解决**: 使用CDN或配置代码分割

### 问题3: 路由404
**解决**: 配置Nginx try_files规则

## 十、参考资源

- **完整设计文档**: [oj-frontend-design.md](./oj-frontend-design.md)
- **后端API文档**: [oj-frontend-api-documentation.md](./oj-frontend-api-documentation.md)
- **Vue 3文档**: https://vuejs.org/
- **Element Plus文档**: https://element-plus.org/
- **Vite文档**: https://vitejs.dev/

---

更新日期: 2024-12-16
