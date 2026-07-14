<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.title" placeholder="新闻标题" clearable style="width:200px" />
        <el-select v-model="searchForm.type" placeholder="类型" clearable style="width:120px">
          <el-option label="新闻" :value="1" />
          <el-option label="公告" :value="2" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增新闻</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'primary' : 'success'">
            {{ row.type === 1 ? '新闻' : '公告' }}
          </el-tag>
        </template>
      </el-table-column>
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

const searchForm = reactive({
  title: '',
  type: null
})
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const loading = ref(false)

// 模拟数据
const mockFetch = () => {
  loading.value = true
  // 模拟异步请求
  setTimeout(() => {
    tableData.value = [
      { id: 1, type: 1, title: '我校与XX公司签署合作协议', origin: '宣传部', publishTime: '2026-07-12 10:00' },
      { id: 2, type: 1, title: '2026年秋季学期实训安排通知', origin: '教务处', publishTime: '2026-07-10 09:30' },
      { id: 3, type: 2, title: '关于实验室开放时间调整的公告', origin: '实验室管理中心', publishTime: '2026-07-08 14:00' },
    ]
    total.value = 3
    loading.value = false
  }, 300)
}

const fetchData = () => {
  // 实际调用 GET /api/v1/admin/news?pageNo=&pageSize=&title=&type=
  // 这里用模拟
  mockFetch()
}

const handleSearch = () => { fetchData() }
const resetSearch = () => {
  searchForm.title = ''
  searchForm.type = null
  fetchData()
}

const handleAdd = () => {
  router.push('/admin/bulletin/news/new')
}

const handleEdit = (row) => {
  router.push(`/admin/bulletin/news/edit/${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除新闻「${row.title}」吗？`, '提示', { type: 'warning' })
    .then(() => {
      // 调用 DELETE /api/v1/admin/news/{id}
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
// 无需额外样式，继承自 PageContainer
</style>