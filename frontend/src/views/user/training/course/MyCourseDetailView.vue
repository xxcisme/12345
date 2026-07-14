<template>
  <page-container>
    <el-card style="max-width:900px; margin:0 auto;">
      <template #header>
        <span>课程详情</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="课程编号">{{ detail.courseCode }}</el-descriptions-item>
        <el-descriptions-item label="课程名称">{{ detail.courseName }}</el-descriptions-item>
        <el-descriptions-item label="课程类型">{{ detail.courseType }}</el-descriptions-item>
        <el-descriptions-item label="授课教师">{{ detail.teacherName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail.status === 1" type="success">已发布</el-tag>
          <el-tag v-else type="info">已下架</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="选课时间">{{ detail.enrollTime }}</el-descriptions-item>
        <el-descriptions-item label="课程介绍" :span="2">{{ detail.introduction || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="max-width:900px; margin:20px auto 0;">
      <template #header>
        <span>实验列表</span>
      </template>
      <el-table :data="experiments" border style="width:100%;">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="number" label="实验编号" width="130" />
        <el-table-column prop="name" label="实验名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="experimentType" label="实验类型" width="120" />
        <el-table-column prop="status" label="完成状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.completed" type="success">已完成</el-tag>
            <el-tag v-else type="warning">未完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleExperiment(row)">进入实验</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div style="max-width:900px; margin:20px auto 0; text-align:center;">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </page-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getMyCourseDetail } from '@/api/user'
import { useDetail } from '@/utils/composables/useDetail'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const { detail, loading } = useDetail(getMyCourseDetail, '加载课程详情失败')
const experiments = ref([])

const fetchExperiments = () => {
  setTimeout(() => {
    experiments.value = [
      { id: 1, number: 'EXP-001', name: 'Python基础语法实验', experimentType: '编程实验', completed: true },
      { id: 2, number: 'EXP-002', name: '数据结构实验', experimentType: '编程实验', completed: false },
      { id: 3, number: 'EXP-003', name: '面向对象编程实验', experimentType: '编程实验', completed: false }
    ]
  }, 300)
}

const handleExperiment = (row) => {
  router.push(`/user/experiments/${row.id}`)
}

const handleBack = () => {
  router.push('/user/courses')
}

onMounted(() => {
  fetchExperiments()
})
</script>

<style scoped lang="scss">
</style>