<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="实验室编号" prop="number">
        <el-input v-model="form.number" placeholder="请输入实验室编号，全局唯一" maxlength="50" show-word-limit />
      </el-form-item>
      <el-form-item label="实验室名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入实验室名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="工位数量" prop="stationNum">
        <el-input-number v-model="form.stationNum" :min="1" :max="999" style="width: 100%;" />
      </el-form-item>
      <el-form-item label="面积(㎡)">
        <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%;" placeholder="请输入实验室面积" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" placeholder="请输入实验室地址" />
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.profile" type="textarea" :rows="3" placeholder="请输入实验室简介" />
      </el-form-item>
      <el-form-item label="详细介绍">
        <el-input v-model="form.description" type="textarea" :rows="6" placeholder="请输入实验室详细介绍（可替换为富文本编辑器）" />
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
import { addAdminLaboratory, updateAdminLaboratory } from '@/api/admin/resource'
import { getLaboratoryDetail } from '@/api/resource'
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
  stationNum: 1,
  area: null,
  address: '',
  profile: '',
  description: ''
})

const rules = {
  number: [{ required: true, message: '请输入实验室编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入实验室名称', trigger: 'blur' }],
  stationNum: [{ required: true, message: '请输入工位数量', trigger: 'change' }]
}

const handleSubmit = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (isEdit.value) {
        await updateAdminLaboratory(form)
        ElMessage.success('修改成功')
      } else {
        await addAdminLaboratory(form)
        ElMessage.success('创建成功')
      }
      router.push('/admin/resource/laboratories')
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
      const data = await getLaboratoryDetail(route.params.id)
      Object.assign(form, data)
    } catch (error) {
      ElMessage.error('加载实验室详情失败')
    }
  }
}

onMounted(() => {
  fetchDetail()
})
</script>