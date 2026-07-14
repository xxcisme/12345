<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminDevice, updateAdminDevice } from '@/api/admin/resource'
import { getDeviceDetail } from '@/api/resource'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminDevice, updateAdminDevice, () => {
  router.push('/admin/resource/devices')
})

const rules = {
  name: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  number: [{ required: true, message: '请输入设备编号', trigger: 'blur' }],
  laboratoryId: [{ required: true, message: '请输入所属实验室ID', trigger: 'blur' }]
}

const { loadDetail } = useDetail(getDeviceDetail, '加载设备详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑设备' : '新增设备' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 640px">
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备编号" prop="number">
          <el-input v-model="form.number" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-input v-model="form.type" placeholder="请输入设备类型" />
        </el-form-item>
        <el-form-item label="版本号">
          <el-input v-model="form.versionNumber" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="所属实验室" prop="laboratoryId">
          <el-input v-model="form.laboratoryId" placeholder="请输入实验室ID" />
        </el-form-item>
        <el-form-item v-if="isEdit" label="设备状态">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="空闲" :value="0" />
            <el-option label="使用中" :value="1" />
            <el-option label="保修" :value="2" />
            <el-option label="损坏" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/resource/devices')">取消</el-button>
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