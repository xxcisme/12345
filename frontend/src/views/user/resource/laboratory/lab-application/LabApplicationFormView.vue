<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { submitLabApplication } from '@/api/user'

const router = useRouter()
const formRef = ref(null)
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

const rules = {
  labId: [{ required: true, message: '请输入实验室ID', trigger: 'blur' }],
  applicantName: [{ required: true, message: '请输入申请人姓名', trigger: 'blur' }],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  name: [{ required: true, message: '请输入实验名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 560px">
        <el-form-item label="实验室ID" prop="labId">
          <el-input v-model="form.labId" placeholder="请输入实验室ID" />
        </el-form-item>
        <el-form-item label="申请人" prop="applicantName">
          <el-input v-model="form.applicantName" placeholder="请输入申请人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="实验名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入实验名称" />
        </el-form-item>
        <el-form-item label="申请说明" prop="purpose">
          <el-input v-model="form.purpose" type="textarea" :rows="4" placeholder="请输入申请说明" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
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