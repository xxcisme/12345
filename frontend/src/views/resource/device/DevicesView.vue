<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="params.name" placeholder="设备名称" clearable style="width: 180px;" />
        <el-input v-model="params.number" placeholder="设备编号" clearable style="width: 150px;" />
        <el-select v-model="params.laboratoryId" placeholder="所在实验室" clearable style="width: 160px;">
          <el-option label="计算机基础实验室" :value="1" />
          <el-option label="电子电路实验室" :value="2" />
          <el-option label="机械加工实验室" :value="3" />
        </el-select>
        <el-input v-model="params.type" placeholder="设备类别" clearable style="width: 130px;" />
        <el-select v-model="params.status" placeholder="状态" clearable style="width: 120px;">
          <el-option label="空闲" :value="0" />
          <el-option label="使用中" :value="1" />
          <el-option label="保修" :value="2" />
          <el-option label="损坏" :value="3" />
        </el-select>
        <el-button type="primary" @click="loadData()">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增设备</el-button>
        <el-button type="success" @click="handleExport">导出</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="number" label="设备编号" width="130" />
      <el-table-column prop="name" label="设备名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="type" label="设备类别" width="120" />
      <el-table-column prop="versionNumber" label="版本号" width="100" />
      <el-table-column prop="laboratoryName" label="所在实验室" min-width="150" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="success">空闲</el-tag>
          <el-tag v-else-if="row.status === 1" type="warning">使用中</el-tag>
          <el-tag v-else-if="row.status === 2" type="info">保修</el-tag>
          <el-tag v-else type="danger">损坏</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleDetail(row)">详情</el-button>
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 15px; justify-content: flex-end;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </page-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useTable } from '@/utils/composables/useTable'
import { useConfirm } from '@/utils/composables/useConfirm'
import { getDevices } from '@/api/resource'
import { deleteAdminDevice } from '@/api/admin/resource'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getDevices, {
  name: '',
  number: '',
  laboratoryId: undefined,
  type: '',
  status: undefined
})

const { handleDelete } = useConfirm(deleteAdminDevice, () => handleCurrentChange(pageNo.value))

const resetSearch = () => {
  params.value = { name: '', number: '', laboratoryId: undefined, type: '', status: undefined }
  loadData()
}

const handleDetail = (row) => { router.push(`/resources/devices/${row.id}`) }
const handleEdit = (row) => { router.push(`/admin/resource/device/edit/${row.id}`) }
const handleAdd = () => { router.push('/admin/resource/device/new') }
const handleExport = () => {
  ElMessage.success('正在导出设备数据...')
}
</script>

<style scoped lang="scss">
</style>