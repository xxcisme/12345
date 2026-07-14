<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="实验编号" prop="number">
        <el-input v-model="form.number" placeholder="请输入实验编号，唯一" maxlength="50" show-word-limit :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="实验名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入实验名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="所属课程" prop="courseId">
        <el-select v-model="form.courseId" placeholder="请选择所属课程" style="width: 100%;">
          <el-option label="Python程序设计" :value="1" />
          <el-option label="模拟电路分析" :value="2" />
          <el-option label="机械制图基础" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="form.category" placeholder="请输入专业" />
      </el-form-item>
      <el-form-item label="实验类型">
        <el-input v-model="form.experimentType" placeholder="请输入实验类型" />
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.profile" type="textarea" :rows="3" placeholder="请输入实验简介" />
      </el-form-item>
      <el-form-item label="实验介绍">
        <el-input v-model="form.description" type="textarea" :rows="6" placeholder="请输入实验详细介绍（可替换为富文本编辑器）" />
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
import { addAdminExperiment, updateAdminExperiment } from '@/api/admin/training'
import { getMyExperimentDetail } from '@/api/user'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)
const submitting = ref(false)

const form = reactive({
  id: null,
  number: '',
  name: '',
  courseId: null,
  category: '',
  experimentType: '',
  profile: '',
  description: ''
})

const rules = {
  number: [{ required: true, message: '请输入实验编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入实验名称', trigger: 'blur' }],
  courseId: [{ required: true, message: '请选择所属课程', trigger: 'change' }]
}

const submit = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      if (isEdit.value) {
        await updateAdminExperiment(form)
        ElMessage.success('修改成功')
      } else {
        await addAdminExperiment(form)
        ElMessage.success('创建成功')
      }
      router.push('/admin/training/experiments')
    } catch (error) {
      ElMessage.error(isEdit.value ? '修改失败' : '创建失败')
    } finally {
      submitting.value = false
    }
  })
}

const handleCancel = () => {
  router.push('/admin/training/experiments')
}

const fetchDetail = async () => {
  if (isEdit.value) {
    try {
      const data = await getMyExperimentDetail(route.params.id)
      Object.assign(form, data)
    } catch (error) {
      ElMessage.error('加载实验详情失败')
    }
  }
}

fetchDetail()
</script>