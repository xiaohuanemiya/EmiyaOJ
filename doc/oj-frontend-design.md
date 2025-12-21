# EmiyaOJ 用户端前端设计文档

## 1. 项目概述

### 1.1 项目简介

EmiyaOJ 用户端是一个在线判题系统（Online Judge System）的前端应用，为用户提供刷题、代码提交、查看判题结果等功能。本系统采用现代化的前端技术栈，旨在提供流畅、高效的用户体验。

### 1.2 技术栈

- **框架**: Vue 3.5 (Composition API)
- **构建工具**: Vite 5.x
- **编程语言**: TypeScript 5.x
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router 4.x
- **HTTP客户端**: Axios
- **代码编辑器**: Monaco Editor / CodeMirror
- **Markdown渲染**: Markdown-it
- **代码高亮**: Highlight.js
- **样式**: SCSS

### 1.3 核心功能

1. **用户认证**: 用户登录、注册、登出
2. **题目浏览**: 题目列表、题目详情、题目筛选、题目搜索
3. **代码编辑**: 在线代码编辑器、语言选择、代码提交
4. **提交记录**: 提交历史、提交详情、判题状态查看
5. **个人中心**: 用户信息、提交统计

---

## 2. 项目架构设计

### 2.1 项目目录结构

```
emiya-oj-frontend/
├── public/                      # 静态资源
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── api/                     # API接口封装
│   │   ├── index.ts             # API统一导出
│   │   ├── request.ts           # Axios封装
│   │   ├── auth.ts              # 认证相关API
│   │   ├── problem.ts           # 题目相关API
│   │   ├── submission.ts        # 提交相关API
│   │   └── language.ts          # 语言相关API
│   ├── assets/                  # 资源文件
│   │   ├── styles/              # 样式文件
│   │   │   ├── index.scss       # 全局样式
│   │   │   ├── variables.scss   # 样式变量
│   │   │   └── mixins.scss      # 样式混入
│   │   └── images/              # 图片资源
│   ├── components/              # 通用组件
│   │   ├── CodeEditor/          # 代码编辑器组件
│   │   │   ├── index.vue
│   │   │   └── types.ts
│   │   ├── MarkdownViewer/      # Markdown渲染组件
│   │   │   └── index.vue
│   │   ├── StatusTag/           # 判题状态标签
│   │   │   └── index.vue
│   │   └── PageContainer/       # 页面容器组件
│   │       └── index.vue
│   ├── composables/             # 组合式函数
│   │   ├── useAuth.ts           # 认证相关逻辑
│   │   ├── useSubmission.ts     # 提交相关逻辑
│   │   └── usePagination.ts     # 分页逻辑
│   ├── layouts/                 # 布局组件
│   │   ├── DefaultLayout.vue    # 默认布局
│   │   └── AuthLayout.vue       # 认证页面布局
│   ├── router/                  # 路由配置
│   │   ├── index.ts             # 路由主文件
│   │   └── routes.ts            # 路由定义
│   ├── stores/                  # Pinia状态管理
│   │   ├── index.ts             # Store统一导出
│   │   ├── auth.ts              # 认证状态
│   │   ├── problem.ts           # 题目状态
│   │   ├── submission.ts        # 提交状态
│   │   └── language.ts          # 语言状态
│   ├── types/                   # TypeScript类型定义
│   │   ├── api.ts               # API响应类型
│   │   ├── problem.ts           # 题目类型
│   │   ├── submission.ts        # 提交类型
│   │   ├── language.ts          # 语言类型
│   │   └── user.ts              # 用户类型
│   ├── utils/                   # 工具函数
│   │   ├── constants.ts         # 常量定义
│   │   ├── format.ts            # 格式化函数
│   │   ├── storage.ts           # 本地存储封装
│   │   └── validation.ts        # 表单验证
│   ├── views/                   # 页面组件
│   │   ├── Home/                # 首页
│   │   │   └── index.vue
│   │   ├── Login/               # 登录页
│   │   │   └── index.vue
│   │   ├── ProblemList/         # 题目列表
│   │   │   └── index.vue
│   │   ├── ProblemDetail/       # 题目详情
│   │   │   └── index.vue
│   │   ├── SubmissionList/      # 提交列表
│   │   │   └── index.vue
│   │   ├── SubmissionDetail/    # 提交详情
│   │   │   └── index.vue
│   │   └── Profile/             # 个人中心
│   │       └── index.vue
│   ├── App.vue                  # 根组件
│   ├── main.ts                  # 应用入口
│   └── env.d.ts                 # 环境变量类型声明
├── .env.development             # 开发环境变量
├── .env.production              # 生产环境变量
├── .eslintrc.cjs                # ESLint配置
├── .prettierrc.json             # Prettier配置
├── index.html                   # HTML模板
├── package.json                 # 项目配置
├── tsconfig.json                # TypeScript配置
├── tsconfig.node.json           # Node TypeScript配置
└── vite.config.ts               # Vite配置
```

### 2.2 技术架构图

```
┌─────────────────────────────────────────────────────────────┐
│                         Browser                             │
├─────────────────────────────────────────────────────────────┤
│                      Vue 3 Application                      │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────────┐  │
│  │   Views     │  │  Components  │  │    Layouts        │  │
│  │  (Pages)    │  │  (Reusable)  │  │  (Page Structure) │  │
│  └─────────────┘  └──────────────┘  └───────────────────┘  │
│         │                 │                    │            │
│         └─────────────────┴────────────────────┘            │
│                           │                                 │
│         ┌─────────────────┴─────────────────┐               │
│         │                                   │               │
│    ┌────▼────┐                         ┌───▼────┐          │
│    │  Pinia  │                         │ Router │          │
│    │ (State) │                         │(Route) │          │
│    └────┬────┘                         └────────┘          │
│         │                                                   │
│    ┌────▼────────────────────────────┐                     │
│    │      API Layer (Axios)          │                     │
│    └────┬────────────────────────────┘                     │
│         │                                                   │
├─────────┼───────────────────────────────────────────────────┤
│         │           HTTP/HTTPS                              │
├─────────┼───────────────────────────────────────────────────┤
│    ┌────▼─────────────────────┐                             │
│    │   Backend API Server     │                             │
│    │   (Spring Boot)          │                             │
│    └──────────────────────────┘                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. 核心功能模块设计

### 3.1 用户认证模块

#### 3.1.1 功能说明

- 用户登录
- 用户注册
- 用户登出
- Token管理
- 路由守卫

#### 3.1.2 状态管理 (stores/auth.ts)

```typescript
// src/stores/auth.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginParams } from '@/types/user'
import { login as loginApi, logout as logoutApi } from '@/api/auth'
import { setToken, getToken, removeToken } from '@/utils/storage'

export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref<string>(getToken() || '')
  const user = ref<User | null>(null)
  const isLoading = ref(false)

  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const username = computed(() => user.value?.username || '')

  // Actions
  const login = async (params: LoginParams) => {
    isLoading.value = true
    try {
      const response = await loginApi(params)
      if (response.code === 200 && response.data) {
        token.value = response.data.token
        user.value = response.data.user
        setToken(response.data.token)
        return true
      }
      return false
    } catch (error) {
      console.error('Login failed:', error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  const logout = async () => {
    try {
      await logoutApi()
    } catch (error) {
      console.error('Logout failed:', error)
    } finally {
      token.value = ''
      user.value = null
      removeToken()
    }
  }

  const setUserInfo = (userInfo: User) => {
    user.value = userInfo
  }

  return {
    token,
    user,
    isLoading,
    isAuthenticated,
    username,
    login,
    logout,
    setUserInfo
  }
})
```

#### 3.1.3 API封装 (api/auth.ts)

```typescript
// src/api/auth.ts
import request from './request'
import type { ApiResponse } from '@/types/api'
import type { User, LoginParams, LoginResponse } from '@/types/user'

/**
 * 用户登录
 */
export const login = (data: LoginParams): Promise<ApiResponse<LoginResponse>> => {
  return request({
    url: '/auth/login',
    method: 'POST',
    data
  })
}

/**
 * 用户登出
 */
export const logout = (): Promise<ApiResponse<void>> => {
  return request({
    url: '/auth/logout',
    method: 'POST'
  })
}

/**
 * 获取当前用户信息
 */
export const getCurrentUser = (): Promise<ApiResponse<User>> => {
  return request({
    url: '/user/current',
    method: 'GET'
  })
}
```

#### 3.1.4 登录页面 (views/Login/index.vue)

```vue
<!-- src/views/Login/index.vue -->
<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>EmiyaOJ 在线判题系统</h2>
        </div>
      </template>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-position="top"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="authStore.isLoading"
            style="width: 100%"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import type { LoginParams } from '@/types/user'

const router = useRouter()
const authStore = useAuthStore()

const loginFormRef = ref<FormInstance>()
const loginForm = reactive<LoginParams>({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 32, message: '密码长度在6到32个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      const success = await authStore.login(loginForm)
      if (success) {
        ElMessage.success('登录成功')
        router.push('/')
      } else {
        ElMessage.error('登录失败，请检查用户名和密码')
      }
    }
  })
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  
  .card-header {
    text-align: center;
    
    h2 {
      margin: 0;
      color: #303133;
    }
  }
}
</style>
```

### 3.2 题目管理模块

#### 3.2.1 功能说明

- 题目列表展示
- 题目搜索和筛选
- 题目详情查看
- 题目难度标识
- 题目通过率展示

#### 3.2.2 状态管理 (stores/problem.ts)

```typescript
// src/stores/problem.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Problem, ProblemQueryParams } from '@/types/problem'
import { getProblemList, getProblemDetail } from '@/api/problem'

export const useProblemStore = defineStore('problem', () => {
  // State
  const problems = ref<Problem[]>([])
  const currentProblem = ref<Problem | null>(null)
  const total = ref(0)
  const loading = ref(false)

  // Actions
  const fetchProblems = async (params: ProblemQueryParams) => {
    loading.value = true
    try {
      const response = await getProblemList(params)
      if (response.code === 200 && response.data) {
        problems.value = response.data.records
        total.value = response.data.total
      }
    } catch (error) {
      console.error('Failed to fetch problems:', error)
    } finally {
      loading.value = false
    }
  }

  const fetchProblemDetail = async (id: number) => {
    loading.value = true
    try {
      const response = await getProblemDetail(id)
      if (response.code === 200 && response.data) {
        currentProblem.value = response.data
      }
    } catch (error) {
      console.error('Failed to fetch problem detail:', error)
    } finally {
      loading.value = false
    }
  }

  return {
    problems,
    currentProblem,
    total,
    loading,
    fetchProblems,
    fetchProblemDetail
  }
})
```

#### 3.2.3 API封装 (api/problem.ts)

```typescript
// src/api/problem.ts
import request from './request'
import type { ApiResponse, PageResult } from '@/types/api'
import type { Problem, ProblemQueryParams } from '@/types/problem'

/**
 * 分页查询题目列表
 */
export const getProblemList = (
  params: ProblemQueryParams
): Promise<ApiResponse<PageResult<Problem>>> => {
  return request({
    url: '/client/problem/page',
    method: 'GET',
    params
  })
}

/**
 * 获取题目详情
 */
export const getProblemDetail = (id: number): Promise<ApiResponse<Problem>> => {
  return request({
    url: `/client/problem/${id}`,
    method: 'GET'
  })
}
```

#### 3.2.4 题目列表页面 (views/ProblemList/index.vue)

```vue
<!-- src/views/ProblemList/index.vue -->
<template>
  <div class="problem-list-container">
    <el-card>
      <template #header>
        <div class="header-content">
          <h2>题目列表</h2>
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索题目..."
            style="width: 300px"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button icon="Search" @click="handleSearch" />
            </template>
          </el-input>
        </div>
      </template>

      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-space wrap>
          <span>难度：</span>
          <el-radio-group v-model="queryParams.difficulty" @change="handleSearch">
            <el-radio-button :label="undefined">全部</el-radio-button>
            <el-radio-button :label="0">简单</el-radio-button>
            <el-radio-button :label="1">中等</el-radio-button>
            <el-radio-button :label="2">困难</el-radio-button>
          </el-radio-group>
        </el-space>
      </div>

      <!-- 题目表格 -->
      <el-table
        v-loading="problemStore.loading"
        :data="problemStore.problems"
        style="width: 100%; margin-top: 20px"
        @row-click="handleRowClick"
      >
        <el-table-column prop="id" label="题号" width="100" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column label="难度" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getDifficultyType(row.difficulty)"
              size="small"
            >
              {{ getDifficultyText(row.difficulty) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="通过率" width="120">
          <template #default="{ row }">
            {{ formatPassRate(row.passRate) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.userStatus === 2" type="success" size="small">
              已通过
            </el-tag>
            <el-tag v-else-if="row.userStatus === 1" type="warning" size="small">
              尝试过
            </el-tag>
            <el-tag v-else type="info" size="small">
              未尝试
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标签" min-width="200">
          <template #default="{ row }">
            <el-tag
              v-for="tag in row.tags"
              :key="tag"
              size="small"
              style="margin-right: 5px"
            >
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :total="problemStore.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useProblemStore } from '@/stores/problem'
import type { ProblemQueryParams } from '@/types/problem'

const router = useRouter()
const problemStore = useProblemStore()

const queryParams = reactive<ProblemQueryParams>({
  current: 1,
  size: 20,
  difficulty: undefined,
  keyword: ''
})

const handleSearch = () => {
  queryParams.current = 1
  problemStore.fetchProblems(queryParams)
}

const handlePageChange = () => {
  problemStore.fetchProblems(queryParams)
}

const handleSizeChange = () => {
  queryParams.current = 1
  problemStore.fetchProblems(queryParams)
}

const handleRowClick = (row: any) => {
  router.push(`/problem/${row.id}`)
}

const getDifficultyType = (difficulty: number) => {
  const types = ['success', 'warning', 'danger']
  return types[difficulty] || 'info'
}

const getDifficultyText = (difficulty: number) => {
  const texts = ['简单', '中等', '困难']
  return texts[difficulty] || '未知'
}

const formatPassRate = (rate: number) => {
  return `${(rate * 100).toFixed(1)}%`
}

onMounted(() => {
  problemStore.fetchProblems(queryParams)
})
</script>

<style scoped lang="scss">
.problem-list-container {
  padding: 20px;
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h2 {
      margin: 0;
    }
  }
  
  .filter-bar {
    padding: 15px 0;
    border-bottom: 1px solid #ebeef5;
  }
  
  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  
  :deep(.el-table__row) {
    cursor: pointer;
    
    &:hover {
      background-color: #f5f7fa;
    }
  }
}
</style>
```


#### 3.2.5 题目详情页面 (views/ProblemDetail/index.vue)

```vue
<!-- src/views/ProblemDetail/index.vue -->
<template>
  <div class="problem-detail-container">
    <el-row :gutter="20">
      <!-- 左侧：题目描述 -->
      <el-col :span="12">
        <el-card v-loading="problemStore.loading">
          <template #header>
            <div class="problem-header">
              <h2>{{ currentProblem?.id }}. {{ currentProblem?.title }}</h2>
              <el-tag
                :type="getDifficultyType(currentProblem?.difficulty)"
                size="large"
              >
                {{ getDifficultyText(currentProblem?.difficulty) }}
              </el-tag>
            </div>
          </template>

          <!-- 题目描述 -->
          <div class="problem-section">
            <h3>题目描述</h3>
            <markdown-viewer :content="currentProblem?.description || ''" />
          </div>

          <!-- 输入格式 -->
          <div class="problem-section">
            <h3>输入格式</h3>
            <markdown-viewer :content="currentProblem?.inputFormat || ''" />
          </div>

          <!-- 输出格式 -->
          <div class="problem-section">
            <h3>输出格式</h3>
            <markdown-viewer :content="currentProblem?.outputFormat || ''" />
          </div>

          <!-- 示例 -->
          <div
            v-for="(example, index) in currentProblem?.examples"
            :key="index"
            class="problem-section"
          >
            <h3>示例 {{ index + 1 }}</h3>
            <div class="example-block">
              <div class="example-item">
                <strong>输入：</strong>
                <pre>{{ example.input }}</pre>
              </div>
              <div class="example-item">
                <strong>输出：</strong>
                <pre>{{ example.output }}</pre>
              </div>
            </div>
          </div>

          <!-- 限制 -->
          <div class="problem-section">
            <h3>限制</h3>
            <ul>
              <li>时间限制：{{ currentProblem?.timeLimit }}ms</li>
              <li>内存限制：{{ currentProblem?.memoryLimit }}MB</li>
            </ul>
          </div>

          <!-- 标签 -->
          <div class="problem-section">
            <h3>标签</h3>
            <el-tag
              v-for="tag in currentProblem?.tags"
              :key="tag"
              style="margin-right: 10px"
            >
              {{ tag }}
            </el-tag>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：代码编辑器 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="editor-header">
              <h3>代码编辑器</h3>
              <el-select
                v-model="selectedLanguageId"
                placeholder="选择语言"
                style="width: 200px"
              >
                <el-option
                  v-for="lang in languageStore.languages"
                  :key="lang.id"
                  :label="lang.name"
                  :value="lang.id"
                />
              </el-select>
            </div>
          </template>

          <!-- 代码编辑器 -->
          <code-editor
            v-model="code"
            :language="getLanguageMode(selectedLanguageId)"
            height="600px"
          />

          <!-- 提交按钮 -->
          <div class="submit-actions">
            <el-button
              type="primary"
              size="large"
              :loading="submitting"
              @click="handleSubmit"
            >
              提交代码
            </el-button>
            <el-button size="large" @click="handleReset">
              重置代码
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useProblemStore } from '@/stores/problem'
import { useLanguageStore } from '@/stores/language'
import { useSubmissionStore } from '@/stores/submission'
import CodeEditor from '@/components/CodeEditor/index.vue'
import MarkdownViewer from '@/components/MarkdownViewer/index.vue'

const route = useRoute()
const router = useRouter()
const problemStore = useProblemStore()
const languageStore = useLanguageStore()
const submissionStore = useSubmissionStore()

const problemId = Number(route.params.id)
const selectedLanguageId = ref<number>(1)
const code = ref('')
const submitting = ref(false)

const currentProblem = computed(() => problemStore.currentProblem)

const getDifficultyType = (difficulty?: number) => {
  if (difficulty === undefined) return 'info'
  const types = ['success', 'warning', 'danger']
  return types[difficulty] || 'info'
}

const getDifficultyText = (difficulty?: number) => {
  if (difficulty === undefined) return '未知'
  const texts = ['简单', '中等', '困难']
  return texts[difficulty] || '未知'
}

const getLanguageMode = (languageId: number) => {
  const language = languageStore.languages.find(l => l.id === languageId)
  const modeMap: Record<string, string> = {
    'Java': 'java',
    'C': 'c',
    'C++': 'cpp',
    'Python': 'python',
    'JavaScript': 'javascript'
  }
  return modeMap[language?.name || ''] || 'plaintext'
}

const handleSubmit = async () => {
  if (!code.value.trim()) {
    ElMessage.warning('请输入代码')
    return
  }

  submitting.value = true
  try {
    const submissionId = await submissionStore.submitCode({
      problemId: problemId,
      languageId: selectedLanguageId.value,
      code: code.value
    })

    if (submissionId) {
      ElMessage.success('提交成功')
      router.push(`/submission/${submissionId}`)
    } else {
      ElMessage.error('提交失败')
    }
  } catch (error) {
    console.error('Submit error:', error)
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

const handleReset = () => {
  code.value = ''
  ElMessage.info('代码已重置')
}

onMounted(async () => {
  await problemStore.fetchProblemDetail(problemId)
  await languageStore.fetchLanguages()
  
  // 设置默认语言
  if (languageStore.languages.length > 0) {
    selectedLanguageId.value = languageStore.languages[0].id
  }
})
</script>

<style scoped lang="scss">
.problem-detail-container {
  padding: 20px;

  .problem-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h2 {
      margin: 0;
    }
  }

  .editor-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      margin: 0;
    }
  }

  .problem-section {
    margin-bottom: 30px;

    h3 {
      color: #303133;
      margin-bottom: 15px;
      font-size: 16px;
      font-weight: 600;
    }

    ul {
      list-style: none;
      padding: 0;

      li {
        padding: 5px 0;
      }
    }
  }

  .example-block {
    .example-item {
      margin-bottom: 15px;

      pre {
        background-color: #f5f7fa;
        padding: 15px;
        border-radius: 4px;
        margin-top: 5px;
        overflow-x: auto;
      }
    }
  }

  .submit-actions {
    margin-top: 20px;
    text-align: center;

    .el-button {
      margin: 0 10px;
    }
  }
}
</style>
```

### 3.3 代码提交模块

#### 3.3.1 功能说明

- 代码编辑
- 语言选择
- 代码提交
- 提交反馈

#### 3.3.2 状态管理 (stores/submission.ts)

```typescript
// src/stores/submission.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Submission, SubmitCodeParams, SubmissionQueryParams } from '@/types/submission'
import { submitCode as submitCodeApi, getSubmissionList, getSubmissionDetail } from '@/api/submission'

export const useSubmissionStore = defineStore('submission', () => {
  // State
  const submissions = ref<Submission[]>([])
  const currentSubmission = ref<Submission | null>(null)
  const total = ref(0)
  const loading = ref(false)

  // Actions
  const submitCode = async (params: SubmitCodeParams): Promise<number | null> => {
    try {
      const response = await submitCodeApi(params)
      if (response.code === 200 && response.data) {
        return response.data
      }
      return null
    } catch (error) {
      console.error('Failed to submit code:', error)
      return null
    }
  }

  const fetchSubmissions = async (params: SubmissionQueryParams) => {
    loading.value = true
    try {
      const response = await getSubmissionList(params)
      if (response.code === 200 && response.data) {
        submissions.value = response.data.records
        total.value = response.data.total
      }
    } catch (error) {
      console.error('Failed to fetch submissions:', error)
    } finally {
      loading.value = false
    }
  }

  const fetchSubmissionDetail = async (id: number) => {
    loading.value = true
    try {
      const response = await getSubmissionDetail(id)
      if (response.code === 200 && response.data) {
        currentSubmission.value = response.data
      }
    } catch (error) {
      console.error('Failed to fetch submission detail:', error)
    } finally {
      loading.value = false
    }
  }

  return {
    submissions,
    currentSubmission,
    total,
    loading,
    submitCode,
    fetchSubmissions,
    fetchSubmissionDetail
  }
})
```

#### 3.3.3 API封装 (api/submission.ts)

```typescript
// src/api/submission.ts
import request from './request'
import type { ApiResponse, PageResult } from '@/types/api'
import type { Submission, SubmitCodeParams, SubmissionQueryParams } from '@/types/submission'

/**
 * 提交代码
 */
export const submitCode = (data: SubmitCodeParams): Promise<ApiResponse<number>> => {
  return request({
    url: '/client/submission/client/submit',
    method: 'POST',
    data
  })
}

/**
 * 分页查询提交记录
 */
export const getSubmissionList = (
  params: SubmissionQueryParams
): Promise<ApiResponse<PageResult<Submission>>> => {
  return request({
    url: '/client/submission/page',
    method: 'GET',
    params
  })
}

/**
 * 获取提交详情
 */
export const getSubmissionDetail = (id: number): Promise<ApiResponse<Submission>> => {
  return request({
    url: `/client/submission/${id}`,
    method: 'GET'
  })
}
```

#### 3.3.4 代码编辑器组件 (components/CodeEditor/index.vue)

```vue
<!-- src/components/CodeEditor/index.vue -->
<template>
  <div ref="editorContainer" class="code-editor-container"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as monaco from 'monaco-editor'

interface Props {
  modelValue: string
  language?: string
  height?: string
  readonly?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  language: 'java',
  height: '500px',
  readonly: false
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const editorContainer = ref<HTMLElement>()
let editor: monaco.editor.IStandaloneCodeEditor | null = null

onMounted(() => {
  if (!editorContainer.value) return

  // 创建编辑器实例
  editor = monaco.editor.create(editorContainer.value, {
    value: props.modelValue,
    language: props.language,
    theme: 'vs-dark',
    automaticLayout: true,
    fontSize: 14,
    minimap: { enabled: true },
    scrollBeyondLastLine: false,
    readOnly: props.readonly,
    tabSize: 4,
    wordWrap: 'on'
  })

  // 监听内容变化
  editor.onDidChangeModelContent(() => {
    if (editor) {
      emit('update:modelValue', editor.getValue())
    }
  })
})

// 监听属性变化
watch(() => props.language, (newLang) => {
  if (editor) {
    const model = editor.getModel()
    if (model) {
      monaco.editor.setModelLanguage(model, newLang)
    }
  }
})

watch(() => props.modelValue, (newValue) => {
  if (editor && newValue !== editor.getValue()) {
    editor.setValue(newValue)
  }
})

onUnmounted(() => {
  if (editor) {
    editor.dispose()
  }
})
</script>

<style scoped lang="scss">
.code-editor-container {
  width: 100%;
  height: v-bind(height);
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
</style>
```

### 3.4 提交记录模块

#### 3.4.1 提交列表页面 (views/SubmissionList/index.vue)

```vue
<!-- src/views/SubmissionList/index.vue -->
<template>
  <div class="submission-list-container">
    <el-card>
      <template #header>
        <h2>提交记录</h2>
      </template>

      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="题目ID">
            <el-input
              v-model="queryParams.problemId"
              placeholder="输入题目ID"
              clearable
              @clear="handleSearch"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 提交记录表格 -->
      <el-table
        v-loading="submissionStore.loading"
        :data="submissionStore.submissions"
        style="width: 100%"
        @row-click="handleRowClick"
      >
        <el-table-column prop="id" label="提交ID" width="150" />
        <el-table-column prop="problemTitle" label="题目" min-width="200" />
        <el-table-column prop="language" label="语言" width="100" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <status-tag :status="row.status" />
          </template>
        </el-table-column>
        <el-table-column label="时间" width="100">
          <template #default="{ row }">
            {{ row.time ? `${row.time}ms` : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="内存" width="100">
          <template #default="{ row }">
            {{ row.memory ? `${row.memory}KB` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :total="submissionStore.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSubmissionStore } from '@/stores/submission'
import StatusTag from '@/components/StatusTag/index.vue'
import type { SubmissionQueryParams } from '@/types/submission'

const router = useRouter()
const submissionStore = useSubmissionStore()

const queryParams = reactive<SubmissionQueryParams>({
  current: 1,
  size: 20,
  problemId: undefined
})

const handleSearch = () => {
  queryParams.current = 1
  submissionStore.fetchSubmissions(queryParams)
}

const handleReset = () => {
  queryParams.problemId = undefined
  handleSearch()
}

const handlePageChange = () => {
  submissionStore.fetchSubmissions(queryParams)
}

const handleSizeChange = () => {
  queryParams.current = 1
  submissionStore.fetchSubmissions(queryParams)
}

const handleRowClick = (row: any) => {
  router.push(`/submission/${row.id}`)
}

onMounted(() => {
  submissionStore.fetchSubmissions(queryParams)
})
</script>

<style scoped lang="scss">
.submission-list-container {
  padding: 20px;

  .filter-bar {
    margin-bottom: 20px;
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  :deep(.el-table__row) {
    cursor: pointer;

    &:hover {
      background-color: #f5f7fa;
    }
  }
}
</style>
```

#### 3.4.2 提交详情页面 (views/SubmissionDetail/index.vue)

```vue
<!-- src/views/SubmissionDetail/index.vue -->
<template>
  <div class="submission-detail-container">
    <el-card v-loading="submissionStore.loading">
      <template #header>
        <div class="header-content">
          <h2>提交详情 #{{ submissionId }}</h2>
          <status-tag :status="currentSubmission?.status" size="large" />
        </div>
      </template>

      <!-- 提交信息 -->
      <el-descriptions :column="2" border>
        <el-descriptions-item label="题目">
          {{ currentSubmission?.problemTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="语言">
          {{ currentSubmission?.language }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <status-tag :status="currentSubmission?.status" />
        </el-descriptions-item>
        <el-descriptions-item label="用时">
          {{ currentSubmission?.time ? `${currentSubmission.time}ms` : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="内存">
          {{ currentSubmission?.memory ? `${currentSubmission.memory}KB` : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ currentSubmission?.createTime }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 判题信息 -->
      <div v-if="currentSubmission?.judgeInfo" class="judge-info-section">
        <h3>判题信息</h3>
        <pre>{{ formatJudgeInfo(currentSubmission.judgeInfo) }}</pre>
      </div>

      <!-- 提交代码 -->
      <div class="code-section">
        <h3>提交代码</h3>
        <code-editor
          :model-value="currentSubmission?.code || ''"
          :language="getLanguageMode(currentSubmission?.language)"
          :readonly="true"
          height="500px"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useSubmissionStore } from '@/stores/submission'
import CodeEditor from '@/components/CodeEditor/index.vue'
import StatusTag from '@/components/StatusTag/index.vue'

const route = useRoute()
const submissionStore = useSubmissionStore()

const submissionId = Number(route.params.id)
const currentSubmission = computed(() => submissionStore.currentSubmission)

const getLanguageMode = (language?: string) => {
  const modeMap: Record<string, string> = {
    'Java': 'java',
    'C': 'c',
    'C++': 'cpp',
    'Python': 'python',
    'JavaScript': 'javascript'
  }
  return modeMap[language || ''] || 'plaintext'
}

const formatJudgeInfo = (judgeInfo: string) => {
  try {
    return JSON.stringify(JSON.parse(judgeInfo), null, 2)
  } catch {
    return judgeInfo
  }
}

onMounted(() => {
  submissionStore.fetchSubmissionDetail(submissionId)
})
</script>

<style scoped lang="scss">
.submission-detail-container {
  padding: 20px;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h2 {
      margin: 0;
    }
  }

  .judge-info-section,
  .code-section {
    margin-top: 30px;

    h3 {
      margin-bottom: 15px;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }

    pre {
      background-color: #f5f7fa;
      padding: 15px;
      border-radius: 4px;
      overflow-x: auto;
    }
  }
}
</style>
```

---

## 4. 通用组件设计

### 4.1 状态标签组件 (components/StatusTag/index.vue)

```vue
<!-- src/components/StatusTag/index.vue -->
<template>
  <el-tag :type="statusType" :size="size">
    {{ statusText }}
  </el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  status?: number
  size?: 'small' | 'default' | 'large'
}

const props = withDefaults(defineProps<Props>(), {
  status: 0,
  size: 'default'
})

const statusConfig = [
  { text: '判题中', type: 'info' },
  { text: '编译错误', type: 'warning' },
  { text: '答案正确', type: 'success' },
  { text: '答案错误', type: 'danger' },
  { text: '超时', type: 'danger' },
  { text: '内存超限', type: 'danger' },
  { text: '运行错误', type: 'danger' },
  { text: '系统错误', type: 'danger' }
]

const statusText = computed(() => {
  return statusConfig[props.status]?.text || '未知'
})

const statusType = computed(() => {
  return statusConfig[props.status]?.type as any || 'info'
})
</script>
```

### 4.2 Markdown渲染组件 (components/MarkdownViewer/index.vue)

```vue
<!-- src/components/MarkdownViewer/index.vue -->
<template>
  <div class="markdown-viewer" v-html="renderedContent"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

interface Props {
  content: string
}

const props = defineProps<Props>()

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: (str, lang) => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(str, { language: lang }).value
      } catch {}
    }
    return ''
  }
})

const renderedContent = computed(() => {
  return md.render(props.content)
})
</script>

<style scoped lang="scss">
.markdown-viewer {
  line-height: 1.6;
  color: #333;

  :deep(h1),
  :deep(h2),
  :deep(h3),
  :deep(h4),
  :deep(h5),
  :deep(h6) {
    margin-top: 24px;
    margin-bottom: 16px;
    font-weight: 600;
    line-height: 1.25;
  }

  :deep(p) {
    margin-bottom: 16px;
  }

  :deep(code) {
    padding: 2px 6px;
    background-color: #f5f7fa;
    border-radius: 3px;
    font-family: 'Courier New', monospace;
  }

  :deep(pre) {
    padding: 16px;
    background-color: #f5f7fa;
    border-radius: 4px;
    overflow-x: auto;

    code {
      padding: 0;
      background-color: transparent;
    }
  }

  :deep(ul),
  :deep(ol) {
    padding-left: 2em;
    margin-bottom: 16px;
  }

  :deep(li) {
    margin-bottom: 8px;
  }
}
</style>
```

---

## 5. TypeScript类型定义

### 5.1 API响应类型 (types/api.ts)

```typescript
// src/types/api.ts

/**
 * 统一API响应格式
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 分页结果
 */
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 分页查询参数
 */
export interface PageQuery {
  current: number
  size: number
}
```

### 5.2 题目类型 (types/problem.ts)

```typescript
// src/types/problem.ts
import type { PageQuery } from './api'

/**
 * 题目信息
 */
export interface Problem {
  id: number
  title: string
  description: string
  inputFormat: string
  outputFormat: string
  examples: Example[]
  timeLimit: number
  memoryLimit: number
  difficulty: number
  passRate: number
  tags: string[]
  userStatus: number
  acceptCount?: number
  submitCount?: number
  createTime: string
}

/**
 * 题目示例
 */
export interface Example {
  input: string
  output: string
}

/**
 * 题目查询参数
 */
export interface ProblemQueryParams extends PageQuery {
  difficulty?: number
  status?: number
  keyword?: string
}
```

### 5.3 提交类型 (types/submission.ts)

```typescript
// src/types/submission.ts
import type { PageQuery } from './api'

/**
 * 提交记录
 */
export interface Submission {
  id: number
  problemId: number
  problemTitle: string
  userId: number
  username: string
  language: string
  code?: string
  status: number
  time?: number
  memory?: number
  judgeInfo?: string
  createTime: string
}

/**
 * 代码提交参数
 */
export interface SubmitCodeParams {
  problemId: number
  languageId: number
  code: string
}

/**
 * 提交记录查询参数
 */
export interface SubmissionQueryParams extends PageQuery {
  problemId?: number
  userId?: number
}
```

### 5.4 语言类型 (types/language.ts)

```typescript
// src/types/language.ts

/**
 * 编程语言信息
 */
export interface Language {
  id: number
  name: string
  description: string
  status: number
  createTime: string
  updateTime: string
}
```

### 5.5 用户类型 (types/user.ts)

```typescript
// src/types/user.ts

/**
 * 用户信息
 */
export interface User {
  id: number
  username: string
  email?: string
  avatar?: string
  createTime: string
}

/**
 * 登录参数
 */
export interface LoginParams {
  username: string
  password: string
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string
  user: User
}
```


---

## 6. 路由配置

### 6.1 路由定义 (router/routes.ts)

```typescript
// src/router/routes.ts
import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login/index.vue'),
    meta: {
      requiresAuth: false,
      title: '登录'
    }
  },
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home/index.vue'),
        meta: {
          requiresAuth: false,
          title: '首页'
        }
      },
      {
        path: 'problems',
        name: 'ProblemList',
        component: () => import('@/views/ProblemList/index.vue'),
        meta: {
          requiresAuth: false,
          title: '题目列表'
        }
      },
      {
        path: 'problem/:id',
        name: 'ProblemDetail',
        component: () => import('@/views/ProblemDetail/index.vue'),
        meta: {
          requiresAuth: true,
          title: '题目详情'
        }
      },
      {
        path: 'submissions',
        name: 'SubmissionList',
        component: () => import('@/views/SubmissionList/index.vue'),
        meta: {
          requiresAuth: true,
          title: '提交记录'
        }
      },
      {
        path: 'submission/:id',
        name: 'SubmissionDetail',
        component: () => import('@/views/SubmissionDetail/index.vue'),
        meta: {
          requiresAuth: true,
          title: '提交详情'
        }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile/index.vue'),
        meta: {
          requiresAuth: true,
          title: '个人中心'
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound/index.vue'),
    meta: {
      requiresAuth: false,
      title: '404'
    }
  }
]
```

### 6.2 路由主文件 (router/index.ts)

```typescript
// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import { routes } from './routes'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 设置页面标题
  document.title = `${to.meta.title || 'EmiyaOJ'} - EmiyaOJ`

  // 检查路由是否需要认证
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    next({
      name: 'Login',
      query: { redirect: to.fullPath }
    })
  } else if (to.name === 'Login' && authStore.isAuthenticated) {
    // 已登录用户访问登录页，重定向到首页
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router
```

---

## 7. API请求封装

### 7.1 Axios封装 (api/request.ts)

```typescript
// src/api/request.ts
import axios, type { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { getToken } from '@/utils/storage'
import type { ApiResponse } from '@/types/api'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 添加token到请求头
    const token = getToken()
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data

    // 如果code不是200，视为错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')

      // 401: 未授权，跳转到登录页
      if (res.code === 401) {
        const authStore = useAuthStore()
        authStore.logout()
        window.location.href = '/login'
      }

      return Promise.reject(new Error(res.message || '请求失败'))
    }

    return res
  },
  (error: AxiosError) => {
    console.error('Response error:', error)

    let message = '网络错误'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          const authStore = useAuthStore()
          authStore.logout()
          window.location.href = '/login'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求资源不存在'
          break
        case 500:
          message = '服务器错误'
          break
        case 503:
          message = '服务不可用'
          break
        default:
          message = `连接错误 ${error.response.status}`
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时'
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service
```

### 7.2 语言API (api/language.ts)

```typescript
// src/api/language.ts
import request from './request'
import type { ApiResponse } from '@/types/api'
import type { Language } from '@/types/language'

/**
 * 获取所有可用语言
 */
export const getLanguageList = (): Promise<ApiResponse<Language[]>> => {
  return request({
    url: '/client/language/list',
    method: 'GET'
  })
}

/**
 * 获取语言详情
 */
export const getLanguageDetail = (id: number): Promise<ApiResponse<Language>> => {
  return request({
    url: `/client/language/${id}`,
    method: 'GET'
  })
}
```

---

## 8. 工具函数

### 8.1 本地存储封装 (utils/storage.ts)

```typescript
// src/utils/storage.ts

const TOKEN_KEY = 'emiya_oj_token'
const USER_KEY = 'emiya_oj_user'

/**
 * 保存Token
 */
export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 获取Token
 */
export const getToken = (): string | null => {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * 移除Token
 */
export const removeToken = (): void => {
  localStorage.removeItem(TOKEN_KEY)
}

/**
 * 保存用户信息
 */
export const setUser = (user: any): void => {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

/**
 * 获取用户信息
 */
export const getUser = (): any | null => {
  const userStr = localStorage.getItem(USER_KEY)
  return userStr ? JSON.parse(userStr) : null
}

/**
 * 移除用户信息
 */
export const removeUser = (): void => {
  localStorage.removeItem(USER_KEY)
}

/**
 * 清空所有本地存储
 */
export const clearStorage = (): void => {
  localStorage.clear()
}
```

### 8.2 常量定义 (utils/constants.ts)

```typescript
// src/utils/constants.ts

/**
 * 判题状态
 */
export const JUDGE_STATUS = {
  PENDING: 0,
  COMPILE_ERROR: 1,
  ACCEPTED: 2,
  WRONG_ANSWER: 3,
  TIME_LIMIT_EXCEEDED: 4,
  MEMORY_LIMIT_EXCEEDED: 5,
  RUNTIME_ERROR: 6,
  SYSTEM_ERROR: 7
} as const

/**
 * 判题状态文本
 */
export const JUDGE_STATUS_TEXT: Record<number, string> = {
  [JUDGE_STATUS.PENDING]: '判题中',
  [JUDGE_STATUS.COMPILE_ERROR]: '编译错误',
  [JUDGE_STATUS.ACCEPTED]: '答案正确',
  [JUDGE_STATUS.WRONG_ANSWER]: '答案错误',
  [JUDGE_STATUS.TIME_LIMIT_EXCEEDED]: '超时',
  [JUDGE_STATUS.MEMORY_LIMIT_EXCEEDED]: '内存超限',
  [JUDGE_STATUS.RUNTIME_ERROR]: '运行错误',
  [JUDGE_STATUS.SYSTEM_ERROR]: '系统错误'
}

/**
 * 题目难度
 */
export const PROBLEM_DIFFICULTY = {
  EASY: 0,
  MEDIUM: 1,
  HARD: 2
} as const

/**
 * 题目难度文本
 */
export const PROBLEM_DIFFICULTY_TEXT: Record<number, string> = {
  [PROBLEM_DIFFICULTY.EASY]: '简单',
  [PROBLEM_DIFFICULTY.MEDIUM]: '中等',
  [PROBLEM_DIFFICULTY.HARD]: '困难'
}

/**
 * 用户题目状态
 */
export const USER_PROBLEM_STATUS = {
  NOT_ATTEMPTED: 0,
  ATTEMPTED: 1,
  SOLVED: 2
} as const
```

### 8.3 格式化函数 (utils/format.ts)

```typescript
// src/utils/format.ts

/**
 * 格式化时间
 */
export const formatTime = (ms: number): string => {
  if (ms < 1000) {
    return `${ms}ms`
  }
  return `${(ms / 1000).toFixed(2)}s`
}

/**
 * 格式化内存
 */
export const formatMemory = (kb: number): string => {
  if (kb < 1024) {
    return `${kb}KB`
  }
  return `${(kb / 1024).toFixed(2)}MB`
}

/**
 * 格式化百分比
 */
export const formatPercentage = (rate: number): string => {
  return `${(rate * 100).toFixed(1)}%`
}

/**
 * 格式化日期时间
 */
export const formatDateTime = (dateStr: string): string => {
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
```

---

## 9. 交互流程设计

### 9.1 用户登录流程

```
┌─────────┐
│ 用户访问 │
│ 登录页面 │
└────┬────┘
     │
     ▼
┌────────────┐
│ 输入用户名  │
│ 和密码      │
└────┬────┘
     │
     ▼
┌────────────┐
│ 点击登录    │
│ 按钮        │
└────┬────┘
     │
     ▼
┌────────────┐       ┌──────────┐
│ 调用登录API ├──────▶│ 后端验证 │
└────┬────┘       └────┬─────┘
     │                 │
     │                 ▼
     │            ┌────────┐
     │            │ 验证结果│
     │            └────┬───┘
     │                 │
     ▼                 ▼
┌────────────┐   ┌──────────┐
│  成功？     │◀──┤          │
└─┬────┬─────┘   └──────────┘
  │    │
  Yes  No
  │    │
  │    └──▶ ┌──────────┐
  │         │ 显示错误  │
  │         │ 消息      │
  │         └──────────┘
  │
  ▼
┌──────────┐
│ 保存Token│
│ 和用户信息│
└────┬─────┘
     │
     ▼
┌──────────┐
│ 跳转到首页│
└──────────┘
```

### 9.2 代码提交流程

```
┌─────────┐
│ 用户进入 │
│ 题目详情 │
└────┬────┘
     │
     ▼
┌────────────┐
│ 阅读题目    │
│ 描述        │
└────┬────┘
     │
     ▼
┌────────────┐
│ 选择编程    │
│ 语言        │
└────┬────┘
     │
     ▼
┌────────────┐
│ 在代码编辑器│
│ 中编写代码  │
└────┬────┘
     │
     ▼
┌────────────┐
│ 点击提交    │
│ 按钮        │
└────┬────┘
     │
     ▼
┌────────────┐       ┌──────────┐
│ 调用提交API ├──────▶│ 后端接收 │
└────┬────┘       └────┬─────┘
     │                 │
     │                 ▼
     │            ┌────────┐
     │            │ 创建提交│
     │            │ 记录    │
     │            └────┬───┘
     │                 │
     │                 ▼
     │            ┌────────┐
     │            │ 异步判题│
     │            └────┬───┘
     │                 │
     ▼                 ▼
┌────────────┐   ┌──────────┐
│  提交成功   │◀──┤ 返回提交ID│
└────┬────┘   └──────────┘
     │
     ▼
┌──────────┐
│ 跳转到提交│
│ 详情页    │
└────┬─────┘
     │
     ▼
┌──────────┐
│ 显示判题  │      ┌──────────┐
│ 状态      │◀─────┤ 轮询或   │
│           │      │ WebSocket│
└──────────┘      └──────────┘
```

### 9.3 题目浏览流程

```
┌─────────┐
│ 用户访问 │
│ 题目列表 │
└────┬────┘
     │
     ▼
┌────────────┐
│ 加载题目列表│◀────┐
└────┬────┘       │
     │            │
     ▼            │
┌────────────┐    │
│ 显示题目信息│    │
│ - 题号      │    │
│ - 标题      │    │
│ - 难度      │    │ 筛选/搜索
│ - 通过率    │    │
│ - 状态      │    │
└────┬────┘    │
     │            │
     ├────────────┘
     │
     ▼
┌────────────┐
│ 用户可以：  │
│ - 搜索题目  │
│ - 按难度筛选│
│ - 翻页浏览  │
└────┬────┘
     │
     ▼
┌────────────┐
│ 点击题目行  │
└────┬────┘
     │
     ▼
┌────────────┐
│ 跳转到题目  │
│ 详情页      │
└──────────┘
```

### 9.4 提交结果查看流程

```
┌─────────┐
│ 用户访问 │
│ 提交详情 │
└────┬────┘
     │
     ▼
┌────────────┐       ┌──────────┐
│ 加载提交详情├──────▶│ API请求  │
└────┬────┘       └────┬─────┘
     │                 │
     │                 ▼
     │            ┌────────┐
     │            │ 返回数据│
     │            └────┬───┘
     ▼                 ▼
┌────────────┐   ┌──────────┐
│ 显示基本信息│◀──┤          │
│ - 题目标题  │   └──────────┘
│ - 编程语言  │
│ - 提交时间  │
└────┬────┘
     │
     ▼
┌────────────┐
│ 显示判题结果│
│ - 状态      │
│ - 用时      │
│ - 内存      │
│ - 错误信息  │
└────┬────┘
     │
     ▼
┌────────────┐
│ 显示提交代码│
│ (只读模式)  │
└──────────┘
```

---

## 10. 项目配置

### 10.1 Vite配置 (vite.config.ts)

```typescript
// vite.config.ts
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
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          'element-plus': ['element-plus'],
          'monaco-editor': ['monaco-editor']
        }
      }
    }
  }
})
```

### 10.2 TypeScript配置 (tsconfig.json)

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "module": "ESNext",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "skipLibCheck": true,
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "jsx": "preserve",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true,
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"]
    }
  },
  "include": ["src/**/*.ts", "src/**/*.d.ts", "src/**/*.tsx", "src/**/*.vue"],
  "references": [{ "path": "./tsconfig.node.json" }]
}
```

### 10.3 Package.json

```json
{
  "name": "emiya-oj-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc && vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext .vue,.js,.jsx,.cjs,.mjs,.ts,.tsx,.cts,.mts --fix --ignore-path .gitignore",
    "format": "prettier --write src/"
  },
  "dependencies": {
    "vue": "^3.5.0",
    "vue-router": "^4.4.0",
    "pinia": "^2.2.0",
    "axios": "^1.7.0",
    "element-plus": "^2.8.0",
    "@element-plus/icons-vue": "^2.3.0",
    "monaco-editor": "^0.50.0",
    "markdown-it": "^14.1.0",
    "highlight.js": "^11.10.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.1.0",
    "vite": "^5.4.0",
    "typescript": "^5.5.0",
    "vue-tsc": "^2.1.0",
    "@typescript-eslint/eslint-plugin": "^8.0.0",
    "@typescript-eslint/parser": "^8.0.0",
    "eslint": "^9.9.0",
    "eslint-plugin-vue": "^9.27.0",
    "prettier": "^3.3.0",
    "sass": "^1.77.0"
  }
}
```

### 10.4 环境变量配置

#### 开发环境 (.env.development)

```bash
# API基础URL
VITE_API_BASE_URL=http://localhost:8080

# 应用标题
VITE_APP_TITLE=EmiyaOJ

# 是否开启Mock
VITE_USE_MOCK=false
```

#### 生产环境 (.env.production)

```bash
# API基础URL
VITE_API_BASE_URL=https://api.emiyaoj.com

# 应用标题
VITE_APP_TITLE=EmiyaOJ

# 是否开启Mock
VITE_USE_MOCK=false
```

---

## 11. 项目初始化步骤

### 11.1 创建项目

```bash
# 使用Vite创建Vue 3 + TypeScript项目
npm create vite@latest emiya-oj-frontend -- --template vue-ts

# 进入项目目录
cd emiya-oj-frontend
```

### 11.2 安装依赖

```bash
# 安装核心依赖
npm install vue@^3.5.0 vue-router@^4.4.0 pinia@^2.2.0

# 安装UI组件库
npm install element-plus @element-plus/icons-vue

# 安装HTTP客户端
npm install axios

# 安装代码编辑器
npm install monaco-editor

# 安装Markdown和代码高亮
npm install markdown-it highlight.js

# 安装开发依赖
npm install -D sass typescript vue-tsc
npm install -D @typescript-eslint/eslint-plugin @typescript-eslint/parser
npm install -D eslint eslint-plugin-vue prettier
```

### 11.3 配置ESLint和Prettier

#### .eslintrc.cjs

```javascript
module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    node: true
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:@typescript-eslint/recommended'
  ],
  parser: 'vue-eslint-parser',
  parserOptions: {
    ecmaVersion: 'latest',
    parser: '@typescript-eslint/parser',
    sourceType: 'module'
  },
  plugins: ['vue', '@typescript-eslint'],
  rules: {
    'vue/multi-word-component-names': 'off',
    '@typescript-eslint/no-explicit-any': 'off'
  }
}
```

#### .prettierrc.json

```json
{
  "semi": false,
  "singleQuote": true,
  "tabWidth": 2,
  "trailingComma": "none",
  "printWidth": 100,
  "arrowParens": "avoid"
}
```

### 11.4 创建目录结构

```bash
# 创建所有必要的目录
mkdir -p src/{api,assets/{styles,images},components,composables,layouts,router,stores,types,utils,views}

# 创建子目录
mkdir -p src/components/{CodeEditor,MarkdownViewer,StatusTag,PageContainer}
mkdir -p src/views/{Home,Login,ProblemList,ProblemDetail,SubmissionList,SubmissionDetail,Profile}
```

### 11.5 启动开发服务器

```bash
# 运行开发服务器
npm run dev

# 在浏览器中访问 http://localhost:3000
```

### 11.6 构建生产版本

```bash
# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

---

## 12. 部署指南

### 12.1 构建配置

确保在生产环境中正确配置了 `.env.production` 文件，特别是 `VITE_API_BASE_URL` 指向正确的后端API地址。

### 12.2 Nginx配置示例

```nginx
server {
    listen 80;
    server_name emiyaoj.com;

    root /var/www/emiya-oj-frontend/dist;
    index index.html;

    # Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

    # 处理Vue Router的History模式
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 12.3 Docker部署

#### Dockerfile

```dockerfile
# 构建阶段
FROM node:18-alpine as build-stage

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# 生产阶段
FROM nginx:alpine as production-stage

COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

#### docker-compose.yml

```yaml
version: '3.8'

services:
  frontend:
    build: .
    ports:
      - "80:80"
    environment:
      - VITE_API_BASE_URL=http://backend:8080
    depends_on:
      - backend
    networks:
      - emiyaoj-network

  backend:
    image: emiyaoj-backend:latest
    ports:
      - "8080:8080"
    networks:
      - emiyaoj-network

networks:
  emiyaoj-network:
    driver: bridge
```

---

## 13. 最佳实践

### 13.1 代码规范

1. **组件命名**: 使用PascalCase命名组件文件和组件名
2. **Props验证**: 始终为Props定义类型和默认值
3. **Emit事件**: 使用TypeScript定义Emit事件类型
4. **样式作用域**: 使用scoped样式避免样式污染
5. **代码注释**: 为复杂逻辑添加清晰的注释

### 13.2 性能优化

1. **路由懒加载**: 使用动态import()实现路由组件懒加载
2. **组件懒加载**: 对大型组件使用defineAsyncComponent
3. **列表虚拟化**: 对长列表使用虚拟滚动
4. **图片优化**: 使用适当的图片格式和大小
5. **代码分割**: 合理配置Vite的代码分割策略

### 13.3 安全性

1. **XSS防护**: 避免使用v-html，使用专门的Markdown渲染组件
2. **CSRF防护**: 在请求中包含CSRF Token
3. **敏感信息**: 不在前端存储敏感信息
4. **Token管理**: 及时刷新和清除过期Token
5. **输入验证**: 对用户输入进行严格验证

### 13.4 用户体验

1. **加载状态**: 为所有异步操作显示加载状态
2. **错误处理**: 友好的错误提示和错误页面
3. **响应式设计**: 支持多种设备和屏幕尺寸
4. **无障碍**: 遵循WCAG无障碍标准
5. **离线支持**: 考虑使用Service Worker实现离线功能

---

## 14. 总结

本设计文档详细描述了EmiyaOJ用户端前端应用的完整架构和实现方案，包括：

1. **完整的技术栈选型**: Vue 3.5 + Vite + TypeScript + Element Plus
2. **清晰的项目结构**: 模块化、可维护的目录组织
3. **核心功能实现**: 用户认证、题目浏览、代码编辑、提交记录
4. **详细的代码示例**: 涵盖所有核心模块的实际代码
5. **类型安全**: 完整的TypeScript类型定义
6. **最佳实践**: 代码规范、性能优化、安全性建议

使用本文档作为指南，开发团队可以快速搭建和开发EmiyaOJ前端应用，确保代码质量和项目可维护性。

---

## 附录

### A. 常见问题

**Q: 如何处理代码编辑器的性能问题？**
A: 使用Monaco Editor的懒加载，只在需要时才加载编辑器；对于简单场景可以使用轻量级的CodeMirror。

**Q: 如何实现实时判题状态更新？**
A: 可以使用WebSocket实现服务器推送，或者使用轮询方式定期查询判题状态。

**Q: 如何优化大量题目列表的加载？**
A: 使用分页加载，配合虚拟滚动技术；对题目列表进行缓存。

### B. 参考资源

- [Vue 3 官方文档](https://vuejs.org/)
- [Vite 官方文档](https://vitejs.dev/)
- [Element Plus 官方文档](https://element-plus.org/)
- [Pinia 官方文档](https://pinia.vuejs.org/)
- [Monaco Editor 官方文档](https://microsoft.github.io/monaco-editor/)

---

**文档版本**: v1.0.0  
**创建日期**: 2024-12-16  
**最后更新**: 2024-12-16
