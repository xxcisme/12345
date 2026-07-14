<template>
  <page-container>
    <el-card style="max-width:800px; margin:0 auto;">
      <template #header>
        <span>设备详情</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备编号">{{ detail?.number }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ detail?.name }}</el-descriptions-item>
        <el-descriptions-item label="设备类别">{{ detail?.type || '-' }}</el-descriptions-item>
        <el-descriptions-item label="版本号">{{ detail?.versionNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在实验室">{{ detail?.laboratoryName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail?.status === 0" type="success">空闲</el-tag>
          <el-tag v-else-if="detail?.status === 1" type="warning">使用中</el-tag>
          <el-tag v-else-if="detail?.status === 2" type="info">保修</el-tag>
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
import { useRoute, useRouter } from 'vue-router'
import { getDeviceDetail } from '@/api/resource'
import { useDetail } from '@/utils/composables/useDetail'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const { detail, loading } = useDetail(getDeviceDetail, '加载设备详情失败')

const handleBack = () => {
  router.push('/resources/devices')
}
</script>

<style scoped lang="scss">
</style>