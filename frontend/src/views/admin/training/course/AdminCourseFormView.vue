<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminCourse, updateAdminCourse } from '@/api/admin/training'
import { getMyCourseDetail } from '@/api/user'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminCourse, updateAdminCourse, () => {
  router.push('/admin/training/courses')
})

const { loadDetail } = useDetail(getMyCourseDetail, '加载课程详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑课程' : '新增课程' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="课程名称" required>
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程编号" required>
          <el-input v-model="form.courseCode" placeholder="请输入课程编号" />
        </el-form-item>
        <el-form-item label="课程类型">
          <el-input v-model="form.courseType" placeholder="请输入课程类型" />
        </el-form-item>
        <el-form-item label="教师ID" required>
          <el-input-number v-model="form.teacherId" :min="1" placeholder="请输入教师ID" style="width: 100%" />
        </el-form-item>
        <el-form-item label="课程简介">
          <el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="请输入课程简介" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/training/courses')">取消</el-button>
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