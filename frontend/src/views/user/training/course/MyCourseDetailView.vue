<script setup>
import { getMyCourseDetail } from '@/api/user'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getMyCourseDetail, '加载课程详情失败')
</script>

<template>
  <div class="page-container">
    <div class="detail-card" v-loading="loading">
      <template v-if="detail">
        <h2 class="detail-title">{{ detail.courseName }}</h2>
        <div class="detail-meta">
          <span>编号：{{ detail.courseCode }}</span>
          <span>类型：{{ detail.courseType }}</span>
          <span>教师：{{ detail.teacherName }}</span>
          <span>创建时间：{{ detail.createTime }}</span>
        </div>
        <div v-if="detail.profile" class="detail-section">
          <h3>课程简介</h3>
          <p>{{ detail.profile }}</p>
        </div>
      </template>
      <el-empty v-else description="课程不存在" />
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