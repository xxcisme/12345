<script setup>
import { getMyExperimentDetail } from '@/api/user'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getMyExperimentDetail, '加载实验详情失败')
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.name }}</h2>
        <div class="detail-meta">
          <span>编号：{{ detail.number }}</span>
          <span>课程：{{ detail.courseName }}</span>
          <span v-if="detail.objective">目标：{{ detail.objective }}</span>
          <span v-if="detail.grade !== undefined && detail.grade !== null">成绩：{{ detail.grade }}</span>
          <span v-if="detail.evaluationStatus !== undefined && detail.evaluationStatus !== null">
            评定状态：{{ detail.evaluationStatus === 0 ? '待评定' : detail.evaluationStatus === 1 ? '已通过' : '未通过' }}
          </span>
          <span>创建时间：{{ detail.createTime }}</span>
        </div>
        <div v-if="detail.steps" class="detail-section">
          <h3>实验步骤</h3>
          <p>{{ detail.steps }}</p>
        </div>
        <div v-if="detail.reportTemplate" class="detail-section">
          <h3>报告模板</h3>
          <p>{{ detail.reportTemplate }}</p>
        </div>
        <div v-if="detail.report" class="detail-section">
          <h3>我的报告</h3>
          <p>{{ detail.report }}</p>
        </div>
      </template>
      <el-empty v-else description="实验不存在" />
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
</style>