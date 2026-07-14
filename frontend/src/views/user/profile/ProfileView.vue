<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile } from '@/api/user'

const profile = ref({})
const editMode = ref(false)
const form = ref({ phone: '', email: '', realName: '' })

onMounted(async () => {
  try {
    const res = await getProfile()
    profile.value = res.data
    Object.assign(form.value, res.data)
  } catch {
    // 错误已由拦截器处理
  }
})

const handleEdit = () => { editMode.value = true }

const handleSave = async () => {
  try {
    await updateProfile(form.value)
    ElMessage.success('更新成功')
    const res = await getProfile()
    profile.value = res.data
    editMode.value = false
  } catch {
    // 错误已由拦截器处理
  }
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>个人信息</h2>
    </div>

    <div class="profile-card">
      <div v-if="!editMode">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户名">{{ profile.username }}</el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ profile.realName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ profile.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ profile.email }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{ profile.roleName }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ profile.createTime }}</el-descriptions-item>
        </el-descriptions>
        <div class="action-bar">
          <el-button type="primary" @click="handleEdit">编辑信息</el-button>
          <router-link to="/user/password">
            <el-button>修改密码</el-button>
          </router-link>
        </div>
      </div>

      <div v-else>
        <el-form :model="form" label-width="100px">
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="form.realName" placeholder="请输入真实姓名" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSave">保存</el-button>
            <el-button @click="editMode = false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
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
.profile-card {
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.action-bar {
  margin-top: 24px;
  display: flex;
  gap: 12px;
}
</style>