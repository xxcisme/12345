<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="设备编号" prop="number">
        <el-input v-model="form.number" placeholder="请输入设备编号，全局唯一" maxlength="50" show-word-limit />
      </el-form-item>
      <el-form-item label="设备名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入设备名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="设备类别">
        <el-input v-model="form.type" placeholder="请输入设备类别" />
      </el-form-item>
      <el-form-item label="版本号">
        <el-input v-model="form.versionNumber" placeholder="请输入版本号" />
      </el-form-item>
      <el-form-item label="所在实验室" prop="laboratoryId">
        <el-select v-model="form.laboratoryId" placeholder="请选择所在实验室" style="width: 100%;">
          <el-option label="计算机基础实验室" :value="1" />
          <el-option label="电子电路实验室" :value="2" />
          <el-option label="机械加工实验室" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态" v-if="isEdit">
        <el-select v-model="form.status" placeholder="请选择设备状态" style="width: 100%;">
          <el-option label="空闲" :value="0" />
          <el-option label="使用中" :value="1" />
          <el-option label="保修" :value="2" />
          <el-option label="损坏" :value="3" />
        </el-select>
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
import { addAdminDevice, updateAdminDevice } from '@/api/admin/resource'
import { getDeviceDetail } from '@/api/resource'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)

const form = reactive({
  id: null,
  number: '',
  name: '',
  type: '',
  versionNumber: '',
  laboratoryId: null,
  status: 0
})

const rules = {
  number: [{ required: true, message: '请输入设备编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  laboratoryId: [{ required: true, message: '请选择所在实验室', trigger: 'change' }]
}

const handleSubmit = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (isEdit.value) {
        await updateAdminDevice(form)
        ElMessage.success('修改成功')
      } else {
        await addAdminDevice(form)
        ElMessage.success('创建成功')
      }
      router.push('/admin/resource/devices')
    } catch (error) {
      ElMessage.error(isEdit.value ? '修改失败' : '创建失败')
    }
  })
}

const handleCancel = () => {
  router.back()
}

const fetchDetail = async () => {
  if (isEdit.value) {
    try {
      const data = await getDeviceDetail(route.params.id)
      Object.assign(form, data)
    } catch (error) {
      ElMessage.error('加载设备详情失败')
    }
  }
}

onMounted(() => {
  fetchDetail()
})
</script>