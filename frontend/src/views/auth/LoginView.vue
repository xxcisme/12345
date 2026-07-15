<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'
import { setToken, setUser } from '@/utils/local_storage'

const router = useRouter()
const route = useRoute()
const form = ref({ username: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(form.value)
    console.log('登录响应:', res)
    console.log('res.data:', res.data)
    const { token, user } = res.data
    console.log('token:', token)
    console.log('user:', user)
    setToken(token)
    setUser(user)
    // 验证是否存储成功
    console.log('localStorage token:', localStorage.getItem('token'))
    console.log('localStorage user:', localStorage.getItem('user'))
    ElMessage.success('登录成功')
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败:', error)
    throw error
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2 class="auth-title">虚拟实训平台</h2>
      <el-form ref="formRef" :model="form" label-position="top" @submit.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        <router-link to="/register">没有账号？立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f0f2f5;
}
.auth-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
.auth-title {
  text-align: center;
  margin-bottom: 32px;
  color: #303133;
  font-size: 24px;
}
.auth-footer {
  text-align: center;
  margin-top: 16px;
}
.auth-footer a {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}
</style>