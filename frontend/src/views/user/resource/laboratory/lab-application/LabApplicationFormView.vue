<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="实验室" prop="labId">
        <el-select v-model="form.labId" placeholder="请选择实验室" style="width: 100%;">
          <el-option label="计算机基础实验室" :value="1" />
          <el-option label="电子电路实验室" :value="2" />
          <el-option label="机械加工实验室" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="申请人姓名" prop="applicantName">
        <el-input v-model="form.applicantName" placeholder="请输入申请人姓名" />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="form.contactPhone" placeholder="请输入11位联系电话" maxlength="11" />
      </el-form-item>
      <el-form-item label="实验名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入实验名称" />
      </el-form-item>
      <el-form-item label="实验类型">
        <el-input v-model="form.experimentType" placeholder="请输入实验类型" />
      </el-form-item>
      <el-form-item label="申请说明">
        <el-input v-model="form.purpose" type="textarea" :rows="3" placeholder="请输入申请说明" />
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.profile" type="textarea" :rows="3" placeholder="请输入实验简介" />
      </el-form-item>
      <el-form-item label="实验介绍">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入实验详细介绍（可替换为富文本编辑器）" />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%;" />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%;" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">提交申请</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const router = useRouter()
const formRef = ref(null)

const form = reactive({
  labId: null,
  applicantName: '',
  contactPhone: '',
  name: '',
  experimentType: '',
  purpose: '',
  profile: '',
  description: '',
  startTime: '',
  endTime: ''
})

const validatePhone = (rule, value, callback) => {
  if (!value) { callback(new Error('请输入联系电话')); return }
  if (!/^1[3-9]\d{9}$/.test(value)) { callback(new Error('请输入正确的11位手机号')); return }
  callback()
}

const validateEndTime = (rule, value, callback) => {
  if (!value) { callback(new Error('请选择结束时间')); return }
  if (form.startTime && value <= form.startTime) { callback(new Error('结束时间必须晚于开始时间')); return }
  callback()
}

const rules = {
  labId: [{ required: true, message: '请选择实验室', trigger: 'change' }],
  applicantName: [{ required: true, message: '请输入申请人姓名', trigger: 'blur' }],
  contactPhone: [{ validator: validatePhone, trigger: 'blur' }],
  name: [{ required: true, message: '请输入实验名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ validator: validateEndTime, trigger: 'change' }]
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success('申请提交成功')
      router.push('/user/lab-applications')
    }
  })
}

const handleCancel = () => {
  router.push('/user/lab-applications')
}
</script>

<style scoped lang="scss">
</style>