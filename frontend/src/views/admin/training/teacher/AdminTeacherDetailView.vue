<template>
  <page-container>
    <el-card style="max-width:900px; margin:0 auto;">
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span>师资详情</span>
          <el-button type="warning" @click="handleResetPassword">重置密码</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="教师ID">{{ teacher.teacherId }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ teacher.name }}</el-descriptions-item>
        <el-descriptions-item label="师资类型">{{ teacher.type === '0' ? '实训老师' : '非实训老师' }}</el-descriptions-item>
        <el-descriptions-item label="在职状态">
          <el-tag v-if="teacher.onJob" type="success">在职</el-tag>
          <el-tag v-else type="info">离职</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="手机号">{{ teacher.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ teacher.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在单位" :span="2">{{ teacher.company || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="max-width:900px; margin:20px auto 0;">
      <template #header>
        <span>授课记录</span>
      </template>
      <el-table :data="courseRecords" border style="width:100%;">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="courseCode" label="课程编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="courseType" label="课程类型" width="120" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">已发布</el-tag>
            <el-tag v-else-if="row.status === 0">草稿</el-tag>
            <el-tag v-else type="info">已下架</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="studentCount" label="学生数" width="90" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const teacher = ref({})
const courseRecords = ref([])

const fetchData = () => {
  setTimeout(() => {
    teacher.value = {
      id: route.params.id,
      teacherId: 'T2026001',
      name: '张老师',
      type: '0',
      phone: '13800138001',
      email: 'zhang@example.com',
      company: 'XX大学计算机学院',
      onJob: true
    }
    courseRecords.value = [
      { id: 1, courseCode: 'CS-101', courseName: 'Python程序设计', courseType: '专业必修', status: 1, studentCount: 45, createTime: '2026-06-01 09:00:00' },
      { id: 2, courseCode: 'CS-102', courseName: '数据结构与算法', courseType: '专业必修', status: 1, studentCount: 38, createTime: '2026-07-01 08:00:00' },
      { id: 3, courseCode: 'CS-201', courseName: 'Web开发技术', courseType: '专业选修', status: 0, studentCount: 0, createTime: '2026-07-10 14:00:00' }
    ]
  }, 300)
}

const handleResetPassword = () => {
  ElMessageBox.confirm(`确定重置 ${teacher.value.name} 的登录密码为初始密码吗？`, '提示', { type: 'warning' }).then(() => {
    ElMessage.success('密码重置成功')
  }).catch(() => {})
}

const handleBack = () => {
  router.push('/admin/training/teachers')
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>