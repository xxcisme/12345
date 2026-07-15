<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getResourceDetail, scoreResource } from '@/api/resource'
import { auditAdminResource } from '@/api/admin/resource'
import { addFavorite, cancelFavorite, getFavorites } from '@/api/user'
import { getUser } from '@/utils/local_storage'
import { ROLE_MAP } from '@/utils/constants'
import { useDetail } from '@/utils/composables/useDetail'

const route = useRoute()
const router = useRouter()
const scoreVal = ref(0)
const favoriting = ref(false)
const scoring = ref(false)
const auditing = ref(false)
const userFavorites = ref([])

const { detail, loading, loadDetail } = useDetail(getResourceDetail, '加载资源详情失败')

const user = computed(() => getUser())
const isAdmin = computed(() => user.value && ROLE_MAP[user.value.role] === 'admin')

const isFavorited = computed(() => {
  return userFavorites.value.some(f => f.resourceId === Number(route.params.id))
})

const loadUserFavorites = async () => {
  if (!getUser()) return
  try {
    const res = await getFavorites({ pageNo: 1, pageSize: 1000 })
    userFavorites.value = res.data?.records || res.data || []
  } catch {
    // 错误已由拦截器处理
  }
}

const handleScore = async () => {
  if (!getUser()) { ElMessage.warning('请先登录'); return }
  scoring.value = true
  try {
    await scoreResource(route.params.id, { score: scoreVal.value })
    await loadDetail()
    ElMessage.success('评分成功')
  } finally {
    scoring.value = false
  }
}

const toggleFavorite = async () => {
  if (!getUser()) { ElMessage.warning('请先登录'); return }
  favoriting.value = true
  try {
    if (isFavorited.value) {
      await cancelFavorite(route.params.id)
      ElMessage.success('取消收藏')
    } else {
      await addFavorite({ resourceId: route.params.id })
      ElMessage.success('加入收藏')
    }
    await loadDetail()
    await loadUserFavorites()
  } finally {
    favoriting.value = false
  }
}

const handleAudit = async (status) => {
  let auditRemark = ''
  if (status === 1) {
    try {
      const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请输入驳回原因'
      })
      auditRemark = value
    } catch {
      return
    }
  }
  auditing.value = true
  try {
    await auditAdminResource(route.params.id, { status, auditRemark })
    ElMessage.success('审核完成')
    router.push('/admin/resource/media')
  } finally {
    auditing.value = false
  }
}

loadUserFavorites()
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.name }}</h2>
        <div class="detail-meta">
          <span>类型：{{ detail.type === 1 ? '视频' : detail.type === 2 ? '音频' : '文档' }}</span>
          <span v-if="detail.category">分类：{{ detail.category }}</span>
          <span v-if="detail.school">学校：{{ detail.school }}</span>
          <span>负责人：{{ detail.leader }}</span>
          <span>发布时间：{{ detail.createTime }}</span>
        </div>

        <div v-if="detail.profile" class="detail-section">
          <h3>资源简介</h3>
          <p>{{ detail.profile }}</p>
        </div>

        <div v-if="!isAdmin" class="detail-section">
          <h3>评分</h3>
          <div class="score-area">
            <el-rate v-model="scoreVal" :disabled="!getUser()" show-score />
            <el-button type="primary" size="small" :loading="scoring" @click="handleScore" :disabled="!getUser()">
              提交评分
            </el-button>
            <el-button size="small" :loading="favoriting" @click="toggleFavorite" :disabled="!getUser()">
              <el-icon :color="isFavorited ? '#E6A23C' : undefined"><Star /></el-icon> {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>

        <div v-if="isAdmin" class="detail-section">
          <h3>审核操作</h3>
          <div class="audit-area">
            <template v-if="detail.status === 0">
              <el-button type="success" :loading="auditing" @click="handleAudit(2)">通过</el-button>
              <el-button type="danger" :loading="auditing" @click="handleAudit(1)">驳回</el-button>
            </template>
            <el-tag v-else :type="detail.status === 1 ? 'danger' : 'success'">
              {{ detail.status === 1 ? '已驳回' : '已发布' }}
            </el-tag>
          </div>
        </div>
      </template>
      <el-empty v-else description="资源不存在" />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}
.detail-card {
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.detail-title {
  font-size: 28px;
  color: #303133;
  margin-bottom: 16px;
}
.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 14px;
  color: #909399;
  padding-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 24px;
}
.detail-section {
  margin-bottom: 24px;
}
.detail-section h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 12px;
}
.detail-section p {
  font-size: 15px;
  color: #606266;
  line-height: 1.8;
}
.score-area {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}
.audit-area {
  display: flex;
  align-items: center;
  gap: 16px;
}
</style>