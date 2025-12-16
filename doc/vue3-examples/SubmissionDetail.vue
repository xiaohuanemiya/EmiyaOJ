<template>
  <div class="submission-detail-page" v-loading="loading">
    <el-page-header @back="goBack" title="返回">
      <template #content>
        <span class="page-title">提交详情 #{{ submissionId }}</span>
      </template>
    </el-page-header>

    <el-card class="submission-info" v-if="submission">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="题目">
          <el-link :href="`/problem/${submission.problemId}`">
            {{ submission.problemTitle }}
          </el-link>
        </el-descriptions-item>
        
        <el-descriptions-item label="状态">
          <StatusTag :status="submission.status" />
        </el-descriptions-item>
        
        <el-descriptions-item label="语言">
          {{ submission.language }}
        </el-descriptions-item>
        
        <el-descriptions-item label="提交时间">
          {{ formatTime(submission.createTime) }}
        </el-descriptions-item>
        
        <el-descriptions-item label="运行时间" v-if="submission.time">
          {{ submission.time }} ms
        </el-descriptions-item>
        
        <el-descriptions-item label="内存使用" v-if="submission.memory">
          {{ submission.memory }} KB
        </el-descriptions-item>
      </el-descriptions>

      <!-- 判题信息 -->
      <div class="judge-info" v-if="submission.judgeInfo">
        <h3>判题信息</h3>
        <pre>{{ formatJudgeInfo(submission.judgeInfo) }}</pre>
      </div>

      <!-- 代码展示 -->
      <div class="code-section">
        <div class="section-header">
          <h3>提交代码</h3>
          <el-button size="small" @click="copyCode">
            <el-icon><CopyDocument /></el-icon>
            复制代码
          </el-button>
        </div>
        <CodeEditor
          v-model="submission.code"
          :language="getLanguageId(submission.language)"
          :readonly="true"
        />
      </div>
    </el-card>

    <!-- 判题中提示 -->
    <el-alert
      v-if="isJudging"
      type="info"
      :closable="false"
      show-icon
    >
      <template #title>
        <span>正在判题中，请稍候...</span>
      </template>
    </el-alert>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { CopyDocument } from '@element-plus/icons-vue'
import { getSubmissionDetail } from '@/api/submission'
import { SubmissionVO, SubmissionStatus } from '@/types'
import StatusTag from '@/components/StatusTag/index.vue'
import CodeEditor from '@/components/CodeEditor/index.vue'
import { usePolling } from '@/composables/usePolling'
import { formatDateTime } from '@/utils/format'

const route = useRoute()
const router = useRouter()
const submissionId = Number(route.params.id)
const loading = ref(false)
const submission = ref<SubmissionVO>()

// 判断是否还在判题中
const isJudging = computed(() => {
  return submission.value?.status === SubmissionStatus.PENDING ||
         submission.value?.status === SubmissionStatus.JUDGING
})

// 轮询获取判题结果
const { start: startPolling, stop: stopPolling } = usePolling(
  async () => {
    const data = await getSubmissionDetail(submissionId)
    submission.value = data
    return data
  },
  () => isJudging.value,
  2000
)

async function fetchSubmissionDetail() {
  loading.value = true
  try {
    submission.value = await getSubmissionDetail(submissionId)
    
    // 如果正在判题，开始轮询
    if (isJudging.value) {
      startPolling()
    }
  } catch (error) {
    console.error('Failed to fetch submission detail:', error)
    ElMessage.error('获取提交详情失败')
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.back()
}

function formatTime(time: string) {
  return formatDateTime(time)
}

function formatJudgeInfo(info: string) {
  try {
    const parsed = JSON.parse(info)
    return JSON.stringify(parsed, null, 2)
  } catch {
    return info
  }
}

function getLanguageId(language: string): number {
  const languageMap: Record<string, number> = {
    'C': 1,
    'C++': 2,
    'Java': 3,
    'Python': 4
  }
  return languageMap[language] || 1
}

async function copyCode() {
  if (!submission.value?.code) return
  
  try {
    await navigator.clipboard.writeText(submission.value.code)
    ElMessage.success('代码已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

onMounted(() => {
  fetchSubmissionDetail()
})

onUnmounted(() => {
  stopPolling()
})
</script>

<style scoped lang="scss">
.submission-detail-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;

  .page-title {
    font-size: 20px;
    font-weight: 600;
  }

  .submission-info {
    margin-top: 24px;

    .judge-info {
      margin-top: 24px;
      padding: 16px;
      background-color: #f5f7fa;
      border-radius: 4px;

      h3 {
        margin-top: 0;
        margin-bottom: 12px;
      }

      pre {
        margin: 0;
        padding: 12px;
        background-color: #fff;
        border-radius: 4px;
        overflow-x: auto;
        font-family: 'Courier New', monospace;
        font-size: 14px;
      }
    }

    .code-section {
      margin-top: 24px;

      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        h3 {
          margin: 0;
        }
      }
    }
  }

  .el-alert {
    margin-top: 16px;
  }
}
</style>
