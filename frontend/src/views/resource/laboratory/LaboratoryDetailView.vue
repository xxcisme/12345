<template>
  <page-container>
    <el-card style="max-width:900px; margin:0 auto;">
      <template #header>
        <span>实验室详情</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="实验室编号">{{ detail?.number }}</el-descriptions-item>
        <el-descriptions-item label="实验室名称">{{ detail?.name }}</el-descriptions-item>
        <el-descriptions-item label="工位数量">{{ detail?.stationNum }}</el-descriptions-item>
        <el-descriptions-item label="面积">{{ detail?.area }} ㎡</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail?.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag type="success">正常开放</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="简介" :span="2">{{ detail?.profile || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细介绍" :span="2">{{ detail?.description || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="max-width:900px; margin:20px auto 0;">
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span>设备列表</span>
          <el-button type="primary" size="small" @click="handleApplyLab">申请使用</el-button>
        </div>
      </template>
      <el-table :data="deviceList" border style="width:100%;">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="number" label="设备编号" width="130" />
        <el-table-column prop="name" label="设备名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="type" label="设备类别" width="120" />
        <el-table-column prop="versionNumber" label="版本号" width="100" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="success">空闲</el-tag>
            <el-tag v-else-if="row.status === 1" type="warning">使用中</el-tag>
            <el-tag v-else-if="row.status === 2" type="info">保修</el-tag>
            <el-tag v-else type="danger">损坏</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleDeviceDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div style="max-width:900px; margin:20px auto 0; text-align:center;">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </page-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getLaboratoryDetail } from '@/api/resource'
import { useDetail } from '@/utils/composables/useDetail'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const { detail, loading } = useDetail(getLaboratoryDetail, '加载实验室详情失败')

const deviceList = ref([])

const handleApplyLab = () => {
  router.push('/user/lab-applications/new')
}

const handleDeviceDetail = (row) => {
  router.push(`/resources/devices/${row.id}`)
}

const handleBack = () => {
  router.push('/resources/laboratories')
}
</script>

<style scoped lang="scss">
</style>