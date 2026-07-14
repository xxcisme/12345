<template>
  <page-container>
    <el-card style="max-width:900px; margin:0 auto;">
      <template #header>
        <span>实验详情</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="实验编号">{{ experiment.number }}</el-descriptions-item>
        <el-descriptions-item label="实验名称">{{ experiment.name }}</el-descriptions-item>
        <el-descriptions-item label="所属课程">{{ experiment.courseName }}</el-descriptions-item>
        <el-descriptions-item label="实验类型">{{ experiment.experimentType }}</el-descriptions-item>
        <el-descriptions-item label="成绩">
          <span v-if="experiment.score !== null" :style="{ color: experiment.score >= 60 ? '#67c23a' : '#f56c6c', fontWeight: 'bold', fontSize: '18px' }">{{ experiment.score }}</span>
          <span v-else style="color:#999;">待评定</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="experiment.status === 0" type="warning">待评定</el-tag>
          <el-tag v-else-if="experiment.status === 1" type="success">已通过</el-tag>
          <el-tag v-else type="danger">未通过</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="实验简介" :span="2">{{ experiment.profile || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实验介绍" :span="2">{{ experiment.description || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="max-width:900px; margin:20px auto 0;">
      <template #header>
        <span>实验报告</span>
      </template>
      <el-form :model="reportForm" label-width="100px">
        <el-form-item label="报告内容">
          <el-input v-model="reportForm.content" type="textarea" :rows="10" placeholder="请输入实验报告内容（可替换为富文本编辑器）" />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload action="#" :auto-upload="false" :limit="3" :file-list="fileList" drag style="width:100%;">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">拖拽文件到此处或 <em>点击上传</em></div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交报告</el-button>
          <el-button @click="handleBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const experiment = ref({})
const reportForm = reactive({
  content: ''
})
const fileList = ref([])

const fetchData = () => {
  setTimeout(() => {
    experiment.value = {
      id: route.params.id,
      number: 'EXP-001',
      name: 'Python基础语法实验',
      courseName: 'Python程序设计',
      experimentType: '编程实验',
      score: 85,
      status: 1,
      profile: '通过编写Python代码掌握基础语法。',
      description: '实验内容包括变量定义、数据类型、条件语句、循环语句、函数定义等基础语法的编程练习。'
    }
  }, 300)
}

const handleSubmit = () => {
  ElMessage.success('实验报告提交成功')
}

const handleBack = () => {
  router.push('/user/experiments')
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>