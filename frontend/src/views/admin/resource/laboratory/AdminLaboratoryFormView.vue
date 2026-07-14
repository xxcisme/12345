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

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
      router.push('/resources/laboratories')
    }
  })
}

const handleCancel = () => {
  router.back()
}

onMounted(() => {
  if (isEdit.value) {
    setTimeout(() => {
      Object.assign(form, {
        id: route.params.id,
        number: 'LAB-2026-001',
        name: '计算机基础实验室',
        stationNum: 50,
        area: 120.50,
        address: '实验楼A座301室',
        profile: '配备高性能计算机，用于程序设计、数据结构等课程实验。',
        description: '本实验室配备50台高性能计算机，安装有Python、Java、C++等开发环境，支持程序设计、数据结构、算法分析等课程的实验教学。'
      })
    }, 300)
  }
})
</script>

<style scoped lang="scss">
</style>