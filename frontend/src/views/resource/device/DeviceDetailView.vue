<template>
  <page-container>
    <el-card style="max-width:800px; margin:0 auto;">
      <template #header>
        <span>设备详情</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备编号">{{ device.number }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ device.name }}</el-descriptions-item>
        <el-descriptions-item label="设备类别">{{ device.type || '-' }}</el-descriptions-item>
        <el-descriptions-item label="版本号">{{ device.versionNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在实验室">{{ device.laboratoryName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="device.status === 0" type="success">空闲</el-tag>
          <el-tag v-else-if="device.status === 1" type="warning">使用中</el-tag>
          <el-tag v-else-if="device.status === 2" type="info">保修</el-tag>
          <el-tag v-else type="danger">损坏</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div style="max-width:800px; margin:20px auto 0; text-align:center;">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </page-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const device = ref({})

const fetchData = () => {
  setTimeout(() => {
    device.value = {
      id: route.params.id,
      number: 'DEV-2026-001',
      name: '高性能计算机工作站',
      type: '计算机设备',
      versionNumber: 'V2.5',
      laboratoryName: '计算机基础实验室',
      status: 0
    }
  }, 300)
}

const handleBack = () => {
  router.push('/resources/devices')
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>