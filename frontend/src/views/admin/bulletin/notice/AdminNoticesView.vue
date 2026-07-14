<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.title" placeholder="公告标题" clearable style="width:200px" />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增公告</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="origin" label="来源" min-width="120" />
      <el-table-column prop="publishTime" label="发布时间" min-width="170" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" danger @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      :total="total"
      @size-change="handleSearch"
      @current-change="handleSearch"
      style="margin-top:16px; justify-content: flex-end;"
    />
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const searchForm = reactive({ title: '' })
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const loading = ref(false)

const mockFetch = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 3, title: '关于实验室开放时间调整的公告', origin: '实验室管理中心', publishTime: '2026-07-08 14:00' },
      { id: 5, title: '2026年暑假放假安排公告', origin: '校办', publishTime: '2026-07-05 16:30' },
    ]
    total.value = 2
    loading.value = false
  }, 300)
}

const fetchData = () => {
  // 实际调用 GET /api/v1/admin/news?type=2&title=...
  mockFetch()
}

const handleSearch = () => { fetchData() }
const resetSearch = () => {
  searchForm.title = ''
  fetchData()
}

const handleAdd = () => {
  router.push('/admin/bulletin/notice/new')
}

const handleEdit = (row) => {
  router.push(`/admin/bulletin/notice/edit/${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除公告「${row.title}」吗？`, '提示', { type: 'warning' })
    .then(() => {
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

onMounted(() => { fetchData() })
</script>