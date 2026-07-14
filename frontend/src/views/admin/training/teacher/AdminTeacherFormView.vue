<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminTeacher, updateAdminTeacher, getAdminTeacherDetail } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeacher, updateAdminTeacher, () => {
  router.push('/admin/training/teachers')
})

const { loadDetail } = useDetail(getAdminTeacherDetail, '加载教师详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑教师' : '新增教师' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" placeholder="请输入教师姓名" />
        </el-form-item>
        <el-form-item label="教师编号" required>
          <el-input v-model="form.teacherId" placeholder="请输入教师编号" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.company" placeholder="请输入所属单位" />
        </el-form-item>
        <el-form-item label="在职状态">
          <el-switch v-model="form.onJob" active-text="在职" inactive-text="离职" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.profile" type="textarea" :rows="4" placeholder="请输入教师简介" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/training/teachers')">取消</el-button>
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