<template>
  <page-container>
    <el-card style="max-width:800px; margin:0 auto;">
      <template #header>
        <span>个人信息</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="角色">
          <el-input :value="roleMap[form.role]" disabled />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入11位手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存修改</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const formRef = ref(null)

const roleMap = { 1: '学生', 2: '老师', 3: '社会人士', 4: '管理员' }

const form = reactive({
  username: '',
  role: 1,
  phone: '',
  email: '',
  realName: ''
})

const originalForm = reactive({})

const validatePhone = (rule, value, callback) => {
  if (!value) { callback(); return }
  if (!/^1[3-9]\d{9}$/.test(value)) { callback(new Error('请输入正确的11位手机号')); return }
  callback()
}

const rules = {
  phone: [{ validator: validatePhone, trigger: 'blur' }]
}

const fetchData = () => {
  setTimeout(() => {
    Object.assign(form, {
      username: 'student01',
      role: 1,
      phone: '13800003333',
      email: 'student01@example.com',
      realName: '李同学'
    })
    Object.assign(originalForm, { ...form })
  }, 300)
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success('个人信息更新成功')
      Object.assign(originalForm, { ...form })
    }
  })
}

const handleReset = () => {
  Object.assign(form, { ...originalForm })
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>