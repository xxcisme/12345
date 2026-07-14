<script setup>
import { getDeviceDetail } from '@/api/resource'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getDeviceDetail, '加载设备详情失败')
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.name }}</h2>
        <div class="detail-meta">
          <span>编号：{{ detail.number }}</span>
          <span>类型：{{ detail.type }}</span>
          <span v-if="detail.versionNumber">版本号：{{ detail.versionNumber }}</span>
          <span v-if="detail.laboratoryName">所属实验室：{{ detail.laboratoryName }}</span>
          <span>
            状态：
            <el-tag :type="detail.status === 0 ? 'success' : detail.status === 1 ? 'warning' : detail.status === 2 ? 'info' : 'danger'" size="small">
              {{ detail.status === 0 ? '空闲' : detail.status === 1 ? '使用中' : detail.status === 2 ? '保修' : '损坏' }}
            </el-tag>
          </span>
          <span>创建时间：{{ detail.createTime }}</span>
        </div>
      </template>
      <el-empty v-else description="设备不存在" />
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
</style>