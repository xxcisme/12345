<template>
  <page-container>
    <el-card style="max-width:600px; margin:0 auto;">
      <template #header>
        <span>修改密码</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="form.oldPassword" type="password" placeholder="请输入原密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" placeholder="至少8位，含字母和数字" show-password />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">确认修改</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </page-container>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const formRef = ref(null)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateNewPassword = (rule, value, callback) => {
  if (!value) { callback(new Error('请输入新密码')); return }
  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value)) {
    callback(new Error('密码至少8位，需包含字母和数字')); return
  }
  callback()
}

const validateConfirm = (rule, value, callback) => {
  if (!value) { callback(new Error('请确认新密码')); return }
  if (value !== form.newPassword) { callback(new Error('两次输入的密码不一致')); return }
  callback()
}

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ validator: validateNewPassword, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirm, trigger: 'blur' }]
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success('密码修改成功，请重新登录')
      form.oldPassword = ''
      form.newPassword = ''
      form.confirmPassword = ''
    }
  })
}

const handleReset = () => {
  formRef.value.resetFields()
}
</script>

<style scoped lang="scss">
</style>