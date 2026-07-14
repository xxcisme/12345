<script setup>
import { getNoticeDetail } from '@/api/bulletin'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getNoticeDetail, '加载公告详情失败')
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.title }}</h2>
        <div class="detail-meta">
          <span>发布时间：{{ detail.createTime }}</span>
        </div>
        <div class="detail-content" v-html="detail.content"></div>
      </template>
      <el-empty v-else description="公告不存在" />
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
</style>