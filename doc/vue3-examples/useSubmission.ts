/**
 * 提交相关的组合式函数
 * 封装提交逻辑和状态管理
 */
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { submitCode, getSubmissionDetail } from '@/api/submission'
import { SubmitCodeDTO, SubmissionVO, SubmissionStatus } from '@/types'
import { useRouter } from 'vue-router'
import { usePolling } from './usePolling'

export function useSubmission() {
  const router = useRouter()
  const submitting = ref(false)
  const currentSubmission = ref<SubmissionVO>()
  
  /**
   * 提交代码
   */
  async function submit(data: SubmitCodeDTO) {
    if (submitting.value) {
      return
    }
    
    // 验证
    if (!data.code || !data.code.trim()) {
      ElMessage.warning('请输入代码')
      return
    }
    
    if (!data.problemId) {
      ElMessage.warning('题目ID不能为空')
      return
    }
    
    if (!data.languageId) {
      ElMessage.warning('请选择编程语言')
      return
    }
    
    submitting.value = true
    
    try {
      const submissionId = await submitCode(data)
      ElMessage.success('提交成功！正在判题...')
      
      // 跳转到提交详情页
      router.push({
        name: 'SubmissionDetail',
        params: { id: submissionId }
      })
      
      return submissionId
    } catch (error) {
      console.error('Submit error:', error)
      ElMessage.error('提交失败，请稍后重试')
      return null
    } finally {
      submitting.value = false
    }
  }
  
  /**
   * 获取提交详情并轮询判题结果
   */
  async function fetchAndWatch(submissionId: number) {
    try {
      // 首次获取
      currentSubmission.value = await getSubmissionDetail(submissionId)
      
      // 如果正在判题，启动轮询
      if (isSubmissionPending(currentSubmission.value)) {
        startPolling(submissionId)
      }
    } catch (error) {
      console.error('Failed to fetch submission:', error)
      ElMessage.error('获取提交详情失败')
    }
  }
  
  /**
   * 判断提交是否还在判题中
   */
  function isSubmissionPending(submission?: SubmissionVO) {
    if (!submission) return false
    
    return submission.status === SubmissionStatus.PENDING ||
           submission.status === SubmissionStatus.JUDGING
  }
  
  /**
   * 轮询判题结果
   */
  const { start: startPolling, stop: stopPolling } = usePolling(
    async () => {
      if (!currentSubmission.value) return null
      
      const data = await getSubmissionDetail(currentSubmission.value.id)
      currentSubmission.value = data
      
      // 判题完成时的处理
      if (!isSubmissionPending(data)) {
        handleJudgeComplete(data)
      }
      
      return data
    },
    () => isSubmissionPending(currentSubmission.value),
    2000
  )
  
  /**
   * 判题完成后的处理
   */
  function handleJudgeComplete(submission: SubmissionVO) {
    stopPolling()
    
    // 根据判题结果显示不同的消息
    switch (submission.status) {
      case SubmissionStatus.ACCEPTED:
        ElMessage.success('恭喜！答案正确')
        break
      case SubmissionStatus.WRONG_ANSWER:
        ElMessage.error('答案错误')
        break
      case SubmissionStatus.TIME_LIMIT_EXCEEDED:
        ElMessage.error('运行超时')
        break
      case SubmissionStatus.MEMORY_LIMIT_EXCEEDED:
        ElMessage.error('内存超限')
        break
      case SubmissionStatus.RUNTIME_ERROR:
        ElMessage.error('运行时错误')
        break
      case SubmissionStatus.COMPILE_ERROR:
        ElMessage.error('编译错误')
        break
      case SubmissionStatus.SYSTEM_ERROR:
        ElMessage.error('系统错误，请联系管理员')
        break
    }
  }
  
  /**
   * 获取状态描述
   */
  function getStatusText(status: number): string {
    const statusMap: Record<number, string> = {
      [SubmissionStatus.PENDING]: '等待判题',
      [SubmissionStatus.JUDGING]: '判题中',
      [SubmissionStatus.ACCEPTED]: '通过',
      [SubmissionStatus.WRONG_ANSWER]: '答案错误',
      [SubmissionStatus.TIME_LIMIT_EXCEEDED]: '超时',
      [SubmissionStatus.MEMORY_LIMIT_EXCEEDED]: '内存超限',
      [SubmissionStatus.RUNTIME_ERROR]: '运行时错误',
      [SubmissionStatus.COMPILE_ERROR]: '编译错误',
      [SubmissionStatus.SYSTEM_ERROR]: '系统错误'
    }
    
    return statusMap[status] || '未知状态'
  }
  
  /**
   * 获取状态颜色类型
   */
  function getStatusType(status: number): string {
    const typeMap: Record<number, string> = {
      [SubmissionStatus.PENDING]: 'info',
      [SubmissionStatus.JUDGING]: 'primary',
      [SubmissionStatus.ACCEPTED]: 'success',
      [SubmissionStatus.WRONG_ANSWER]: 'danger',
      [SubmissionStatus.TIME_LIMIT_EXCEEDED]: 'warning',
      [SubmissionStatus.MEMORY_LIMIT_EXCEEDED]: 'warning',
      [SubmissionStatus.RUNTIME_ERROR]: 'danger',
      [SubmissionStatus.COMPILE_ERROR]: 'warning',
      [SubmissionStatus.SYSTEM_ERROR]: 'danger'
    }
    
    return typeMap[status] || 'info'
  }
  
  // 计算属性
  const isJudging = computed(() => isSubmissionPending(currentSubmission.value))
  const statusText = computed(() => 
    currentSubmission.value ? getStatusText(currentSubmission.value.status) : ''
  )
  const statusType = computed(() => 
    currentSubmission.value ? getStatusType(currentSubmission.value.status) : 'info'
  )
  
  return {
    // 状态
    submitting,
    currentSubmission,
    isJudging,
    statusText,
    statusType,
    
    // 方法
    submit,
    fetchAndWatch,
    isSubmissionPending,
    getStatusText,
    getStatusType,
    stopPolling
  }
}
