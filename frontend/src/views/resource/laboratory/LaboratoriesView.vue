<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.name" placeholder="实验室名称" clearable style="width: 180px;" />
        <el-input v-model="searchForm.number" placeholder="实验室编号" clearable style="width: 150px;" />
        <el-input v-model="searchForm.address" placeholder="地址" clearable style="width: 180px;" />
        <el-input-number v-model="searchForm.minStation" placeholder="最小工位数" :min="0" style="width: 140px;" />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd" v-if="isAdmin">新增实验室</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="number" label="编号" width="130" />
      <el-table-column prop="name" label="实验室名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="stationNum" label="工位数" width="90" align="center" />
      <el-table-column prop="area" label="面积(㎡)" width="100" align="center" />
      <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" width="180" fixed="right" align="center">
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
  address: '',
  minStation: null
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, number: 'LAB-2026-001', name: '计算机基础实验室', stationNum: 50, area: 120.50, address: '实验楼A座301室' },
      { id: 2, number: 'LAB-2026-002', name: '电子电路实验室', stationNum: 30, area: 90.00, address: '实验楼B座201室' },
      { id: 3, number: 'LAB-2026-003', name: '机械加工实验室', stationNum: 20, area: 200.00, address: '工程训练中心1楼' }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => {
  Object.assign(searchForm, { name: '', number: '', address: '', minStation: null })
  handleSearch()
}

const handleDetail = (row) => { router.push(`/resources/laboratories/${row.id}`) }
const handleEdit = (row) => { router.push(`/admin/resource/laboratory/edit/${row.id}`) }

const handleAdd = () => { router.push('/admin/resource/laboratory/new') }

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除实验室「${row.name}」吗？`, '提示', { type: 'warning' }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  }).catch(() => {})
}

const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>