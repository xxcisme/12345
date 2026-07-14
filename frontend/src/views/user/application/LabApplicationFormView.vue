<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { applyLab } from '@/api/user'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)
const form = ref({ laboratoryId: '', reason: '', date: '', timeSlot: '' })

const rules = {
  laboratoryId: [{ required: true, message: '请输入实验室ID', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入申请原因', trigger: 'blur' }],
  date: [{ required: true, message: '请选择使用日期', trigger: 'change' }],
  timeSlot: [{ required: true, message: '请选择时间段', trigger: 'change' }]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  loading.value = true
  try {
    await applyLab(form.value)
    ElMessage.success('申请已提交')
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 560px">
        <el-form-item label="实验室" prop="laboratoryId">
          <el-input v-model="form.laboratoryId" placeholder="请输入实验室ID" />
        </el-form-item>
        <el-form-item label="申请原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请输入申请原因" />
        </el-form-item>
        <el-form-item label="使用日期" prop="date">
          <el-date-picker v-model="form.date" type="date" placeholder="请选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="时间段" prop="timeSlot">
          <el-select v-model="form.timeSlot" placeholder="请选择时间段" style="width: 100%">
            <el-option label="上午 8:00-12:00" value="1" />
            <el-option label="下午 13:00-17:00" value="2" />
            <el-option label="晚上 18:00-21:00" value="3" />
          </el-select>
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
  max-width: 800px;
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