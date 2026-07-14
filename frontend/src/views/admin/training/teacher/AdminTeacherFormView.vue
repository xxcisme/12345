<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="教师ID" prop="teacherId">
        <el-input v-model="form.teacherId" placeholder="请输入教师ID，唯一" maxlength="50" show-word-limit :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名" maxlength="50" />
      </el-form-item>
      <el-form-item label="师资类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择师资类型" style="width: 100%;">
          <el-option label="实训老师" value="0" />
          <el-option label="非实训老师" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入11位手机号" maxlength="11" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="所在单位">
        <el-input v-model="form.company" placeholder="请输入所在单位" />
      </el-form-item>
      <el-form-item label="在职状态" prop="onJob">
        <el-radio-group v-model="form.onJob">
          <el-radio :label="true">在职</el-radio>
          <el-radio :label="false">离职</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">{{ isEdit ? '保存修改' : '立即创建' }}</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addAdminTeacher, updateAdminTeacher, getAdminTeacherDetail } from '@/api/admin/training'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)

const form = reactive({
  id: null,
  teacherId: '',
  name: '',
  type: '0',
  phone: '',
  email: '',
  company: '',
  onJob: true
})

const validatePhone = (rule, value, callback) => {
  if (!value) { callback(new Error('请输入手机号')); return }
  if (!/^1[3-9]\d{9}$/.test(value)) { callback(new Error('请输入正确的11位手机号')); return }
  callback()
}

const rules = {
  teacherId: [{ required: true, message: '请输入教师ID', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  type: [{ required: true, message: '请选择师资类型', trigger: 'change' }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  onJob: [{ required: true, message: '请选择在职状态', trigger: 'change' }]
}

const handleSubmit = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateAdminTeacher(form)
        } else {
          await addAdminTeacher(form)
        }
        ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
        router.push('/admin/training/teachers')
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/admin/training/teachers')
}

onMounted(async () => {
  if (isEdit.value) {
    try {
      const res = await getAdminTeacherDetail(route.params.id)
      Object.assign(form, res.data)
    } catch (error) {
      ElMessage.error('加载教师详情失败')
    }
  }
})
</script>

<style scoped lang="scss">
</style>