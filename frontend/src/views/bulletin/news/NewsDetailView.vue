<script setup>
import { getNewsDetail } from '@/api/bulletin'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getNewsDetail, '加载新闻详情失败')
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.title }}</h2>
        <div class="detail-meta">
          <span v-if="detail.origin">来源：{{ detail.origin }}</span>
          <span>发布时间：{{ detail.createTime }}</span>
        </div>
        <div class="detail-content" v-html="detail.content"></div>
        <div v-if="detail.enclosure" class="detail-enclosure">
          <el-image :src="detail.enclosure" fit="contain" style="max-width: 100%" />
        </div>
      </template>
      <el-empty v-else description="新闻不存在" />
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
  line-height: 1.4;
}
.detail-meta {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: #909399;
  padding-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 24px;
}
.detail-content {
  font-size: 16px;
  color: #606266;
  line-height: 1.8;
}
.detail-enclosure {
  margin-top: 24px;
}
</style>