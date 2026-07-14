<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminUser, updateAdminUser, getAdminUserDetail } from '@/api/admin/system'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminUser, updateAdminUser, () => {
  router.push('/admin/system/users')
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const { loadDetail } = useDetail(getAdminUserDetail, '加载用户详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑用户' : '新增用户' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 560px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="学生" :value="1" />
            <el-option label="老师" :value="2" />
            <el-option label="社会人士" :value="3" />
            <el-option label="管理员" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="学校编号">
          <el-input v-model="form.schoolCode" placeholder="学生/教师必填" />
        </el-form-item>
        <el-form-item label="班级ID">
          <el-input-number v-model="form.classId" :min="1" placeholder="学生必填" style="width: 100%" />
        </el-form-item>
        <el-form-item label="职业类型">
          <el-input v-model="form.occupationType" placeholder="社会人士选填" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/system/users')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
}
.page-header h2 {
  font-size: 20px;
  color: #303133;
}
.form-card {
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
</style>