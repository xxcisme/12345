<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="课程编号" prop="courseCode">
        <el-input v-model="form.courseCode" placeholder="请输入课程编号，唯一" maxlength="50" show-word-limit :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="form.courseName" placeholder="请输入课程名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="课程类型" prop="courseType">
        <el-input v-model="form.courseType" placeholder="请输入课程类型，如：专业必修" />
      </el-form-item>
      <el-form-item label="授课教师" prop="teacherId">
        <el-select v-model="form.teacherId" placeholder="请选择授课教师" style="width: 100%;">
          <el-option label="张老师" :value="1" />
          <el-option label="李老师" :value="2" />
          <el-option label="王老师" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="课程介绍">
        <el-input v-model="form.introduction" type="textarea" :rows="6" placeholder="请输入课程介绍（可替换为富文本编辑器）" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit" :loading="submitting">{{ isEdit ? '保存修改' : '立即创建' }}</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminCourse, updateAdminCourse } from '@/api/admin/training'
import { getMyCourseDetail } from '@/api/user'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)
const submitting = ref(false)

const form = reactive({
  id: null,
  courseCode: '',
  courseName: '',
  courseType: '',
  introduction: '',
  teacherId: null
})

const rules = {
  courseCode: [{ required: true, message: '请输入课程编号', trigger: 'blur' }],
  courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  teacherId: [{ required: true, message: '请选择授课教师', trigger: 'change' }]
}

const submit = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      if (isEdit.value) {
        await updateAdminCourse(form)
        ElMessage.success('修改成功')
      } else {
        await addAdminCourse(form)
        ElMessage.success('创建成功')
      }
      router.push('/admin/training/courses')
    } catch (error) {
      ElMessage.error(isEdit.value ? '修改失败' : '创建失败')
    } finally {
      submitting.value = false
    }
  })
}

const handleCancel = () => {
  router.push('/admin/training/courses')
}

const fetchDetail = async () => {
  if (isEdit.value) {
    try {
      const data = await getMyCourseDetail(route.params.id)
      Object.assign(form, data)
    } catch (error) {
      ElMessage.error('加载课程详情失败')
    }
  }
}

fetchDetail()
</script>