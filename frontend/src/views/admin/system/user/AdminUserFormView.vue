<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名，4-16位字母/数字" maxlength="16" show-word-limit :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="密码" prop="password" v-if="!isEdit">
        <el-input v-model="form.password" type="password" placeholder="请输入密码，至少8位，含字母和数字" show-password />
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%;" :disabled="isEdit">
          <el-option label="学生" :value="1" />
          <el-option label="老师" :value="2" />
          <el-option label="社会人士" :value="3" />
          <el-option label="管理员" :value="4" />
        </el-select>
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
      <el-form-item label="学校编号" prop="schoolCode" v-if="form.role === 1 || form.role === 2">
        <el-input v-model="form.schoolCode" placeholder="学生/教师必填" />
      </el-form-item>
      <el-form-item label="班级" prop="classId" v-if="form.role === 1">
        <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%;">
          <el-option label="计算机科学与技术1班" :value="1" />
          <el-option label="计算机科学与技术2班" :value="2" />
          <el-option label="电子信息工程1班" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="职业类型" v-if="form.role === 3">
        <el-input v-model="form.occupationType" placeholder="请输入职业类型" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">{{ isEdit ? '保存修改' : '立即创建' }}</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)

const form = reactive({
  id: null,
  username: '',
  password: '',
  role: null,
  phone: '',
  email: '',
  realName: '',
  schoolCode: '',
  classId: null,
  occupationType: ''
})

const validatePassword = (rule, value, callback) => {
  if (!isEdit.value && !value) {
    callback(new Error('请输入密码'))
    return
  }
  if (value && !/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value)) {
    callback(new Error('密码至少8位，需包含字母和数字'))
    return
  }
  callback()
}

const validatePhone = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入手机号'))
    return
  }
  if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的11位手机号'))
    return
  }
  callback()
}

const validateSchoolCode = (rule, value, callback) => {
  if ((form.role === 1 || form.role === 2) && !value) {
    callback(new Error('学生/教师必须填写学校编号'))
    return
  }
  callback()
}

const validateClassId = (rule, value, callback) => {
  if (form.role === 1 && !value) {
    callback(new Error('学生必须选择班级'))
    return
  }
  callback()
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{4,16}$/, message: '用户名需为4-16位字母/数字', trigger: 'blur' }
  ],
  password: [{ validator: validatePassword, trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  schoolCode: [{ validator: validateSchoolCode, trigger: 'blur' }],
  classId: [{ validator: validateClassId, trigger: 'change' }]
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      // 过滤掉不需要的字段
      const submitData = { ...form }
      if (isEdit.value) {
        delete submitData.password
        delete submitData.username
        delete submitData.role
      }
      // 清理条件字段
      if (submitData.role !== 1 && submitData.role !== 2) {
        delete submitData.schoolCode
      }
      if (submitData.role !== 1) {
        delete submitData.classId
      }
      if (submitData.role !== 3) {
        delete submitData.occupationType
      }
      ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
      router.push('/admin/system/users')
    }
  })
}

const handleCancel = () => {
  router.push('/admin/system/users')
}

onMounted(() => {
  if (isEdit.value) {
    setTimeout(() => {
      Object.assign(form, {
        id: route.params.id,
        username: 'student01',
        role: 1,
        phone: '13800003333',
        email: 'student01@example.com',
        realName: '李同学',
        schoolCode: '2024001',
        classId: 1,
        occupationType: ''
      })
    }, 300)
  }
})
</script>

<style scoped lang="scss">
</style>