<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminExperiment, updateAdminExperiment } from '@/api/admin/training'
import { getMyExperimentDetail } from '@/api/user'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminExperiment, updateAdminExperiment, () => {
  router.push('/admin/training/experiments')
})

const { loadDetail } = useDetail(getMyExperimentDetail, '加载实验详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑实验' : '新增实验' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="实验名称" required>
          <el-input v-model="form.name" placeholder="请输入实验名称" />
        </el-form-item>
        <el-form-item label="实验编号" required>
          <el-input v-model="form.number" placeholder="请输入实验编号" />
        </el-form-item>
        <el-form-item label="所属课程">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="实验目标">
          <el-input v-model="form.objective" type="textarea" :rows="3" placeholder="请输入实验目标" />
        </el-form-item>
        <el-form-item label="实验步骤">
          <el-input v-model="form.steps" type="textarea" :rows="6" placeholder="请输入实验步骤" />
        </el-form-item>
        <el-form-item label="报告模板">
          <el-input v-model="form.reportTemplate" type="textarea" :rows="4" placeholder="请输入报告模板" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/training/experiments')">取消</el-button>
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