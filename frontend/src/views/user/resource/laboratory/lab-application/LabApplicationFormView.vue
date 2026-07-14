<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { submitLabApplication } from '@/api/user'

const router = useRouter()
const form = ref({
  labId: '',
  applicantName: '',
  contactPhone: '',
  purpose: '',
  name: '',
  startTime: '',
  endTime: ''
})
const loading = ref(false)

const handleSubmit = async () => {
  loading.value = true
  try {
    await submitLabApplication(form.value)
    ElMessage.success('申请提交成功')
    router.push('/user/lab-applications')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>申请实验室</h2>
    </div>

    <div class="form-card">
      <el-form :model="form" label-width="100px" style="max-width: 560px">
        <el-form-item label="申请人" required>
          <el-input v-model="form.applicantName" placeholder="请输入申请人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" required>
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="实验室名称" required>
          <el-input v-model="form.name" placeholder="请输入实验室名称" />
        </el-form-item>
        <el-form-item label="使用目的" required>
          <el-input v-model="form.purpose" type="textarea" :rows="4" placeholder="请描述使用目的" />
        </el-form-item>
        <el-form-item label="开始时间" required>
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束时间" required>
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">提交申请</el-button>
          <el-button @click="router.push('/user/lab-applications')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 700px;
  margin: 0 auto;
  padding: 40px 20px;
}
.page-header {
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.form-card {
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
</style>