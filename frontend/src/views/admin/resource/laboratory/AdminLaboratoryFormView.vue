<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminLaboratory, updateAdminLaboratory } from '@/api/admin/resource'
import { getLaboratoryDetail } from '@/api/resource'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminLaboratory, updateAdminLaboratory, () => {
  router.push('/admin/resource/laboratories')
})

const { loadDetail } = useDetail(getLaboratoryDetail, '加载实验室详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑实验室' : '新增实验室' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="实验室名称" required>
          <el-input v-model="form.name" placeholder="请输入实验室名称" />
        </el-form-item>
        <el-form-item label="编号" required>
          <el-input v-model="form.number" placeholder="请输入实验室编号" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="最低工位">
          <el-input-number v-model="form.minStation" :min="1" placeholder="最低工位" style="width: 100%" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.profile" type="textarea" :rows="4" placeholder="请输入实验室简介" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/resource/laboratories')">取消</el-button>
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