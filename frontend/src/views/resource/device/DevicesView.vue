<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.name" placeholder="设备名称" clearable style="width: 180px;" />
        <el-input v-model="searchForm.number" placeholder="设备编号" clearable style="width: 150px;" />
        <el-select v-model="searchForm.laboratoryId" placeholder="所在实验室" clearable style="width: 160px;">
          <el-option label="计算机基础实验室" :value="1" />
          <el-option label="电子电路实验室" :value="2" />
          <el-option label="机械加工实验室" :value="3" />
        </el-select>
        <el-input v-model="searchForm.type" placeholder="设备类别" clearable style="width: 130px;" />
        <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 120px;" v-if="isAdmin">
          <el-option label="空闲" :value="0" />
          <el-option label="使用中" :value="1" />
          <el-option label="保修" :value="2" />
          <el-option label="损坏" :value="3" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd" v-if="isAdmin">新增设备</el-button>
        <el-button type="success" @click="handleExport" v-if="isAdmin">导出</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="number" label="设备编号" width="130" />
      <el-table-column prop="name" label="设备名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="type" label="设备类别" width="120" />
      <el-table-column prop="versionNumber" label="版本号" width="100" />
      <el-table-column prop="laboratoryName" label="所在实验室" min-width="150" />
      <el-table-column prop="status" label="状态" width="100" align="center" v-if="isAdmin">
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
          <el-button link type="primary" size="small" @click="handleEdit(row)" v-if="isAdmin">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)" v-if="isAdmin">删除</el-button>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'admin')

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  name: '',
  number: '',
  laboratoryId: null,
  type: '',
  status: null
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, number: 'DEV-2026-001', name: '高性能计算机工作站', type: '计算机设备', versionNumber: 'V2.5', laboratoryName: '计算机基础实验室', status: 0 },
      { id: 2, number: 'DEV-2026-002', name: '数字示波器', type: '测试仪器', versionNumber: 'V1.0', laboratoryName: '电子电路实验室', status: 1 },
      { id: 3, number: 'DEV-2026-003', name: '数控车床', type: '加工设备', versionNumber: 'V3.2', laboratoryName: '机械加工实验室', status: 0 },
      { id: 4, number: 'DEV-2026-004', name: '3D打印机', type: '成型设备', versionNumber: 'V2.0', laboratoryName: '机械加工实验室', status: 2 }
    ]
    total.value = 4
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => {
  Object.assign(searchForm, { name: '', number: '', laboratoryId: null, type: '', status: null })
  handleSearch()
}

const handleDetail = (row) => { router.push(`/resources/devices/${row.id}`) }
const handleEdit = (row) => { router.push(`/admin/resource/device/edit/${row.id}`) }

const handleAdd = () => { router.push('/admin/resource/device/new') }

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除设备「${row.name}」吗？`, '提示', { type: 'warning' }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  }).catch(() => {})
}

const handleExport = () => {
  ElMessage.success('正在导出设备数据...')
}

const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>