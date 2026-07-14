<script setup>
import { useRoute } from 'vue-router'
import { getAdminTeacherDetail, resetAdminTeacherPassword } from '@/api/admin/training'
import { useDetail } from '@/utils/composables/useDetail'
import { ElMessage } from 'element-plus'

const route = useRoute()
const { detail, loading } = useDetail(getAdminTeacherDetail, '加载教师详情失败')

const handleResetPwd = async () => {
  try {
    await resetAdminTeacherPassword(route.params.id)
    ElMessage.success('密码已重置为初始密码')
  } catch (error) {
    ElMessage.error('重置密码失败')
  }
}
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.name }}</h2>
        <div class="detail-meta">
          <span>编号：{{ detail.teacherId }}</span>
          <span>单位：{{ detail.company }}</span>
          <span>
            状态：
            <el-tag :type="detail.onJob ? 'success' : 'info'" size="small">{{ detail.onJob ? '在职' : '离职' }}</el-tag>
          </span>
          <span>创建时间：{{ detail.createTime }}</span>
        </div>
        <div v-if="detail.profile" class="detail-section">
          <h3>教师简介</h3>
          <p>{{ detail.profile }}</p>
        </div>
        <div class="action-bar">
          <el-button type="warning" @click="handleResetPwd">重置密码</el-button>
          <router-link :to="`/admin/training/teachers/edit/${detail.id}`">
            <el-button type="primary">编辑</el-button>
          </router-link>
        </div>
      </template>
      <el-empty v-else description="教师不存在" />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
  max-width: 900px;
}
.detail-card {
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.detail-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 16px;
}
.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
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
.action-bar {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}
</style>