<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.resourceType" placeholder="资源类型" clearable style="width: 140px;">
          <el-option label="视频" :value="1" />
          <el-option label="音频" :value="2" />
          <el-option label="文档" :value="3" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="resourceName" label="资源名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="resourceType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.resourceType === 1" type="danger">视频</el-tag>
          <el-tag v-else-if="row.resourceType === 2" type="warning">音频</el-tag>
          <el-tag v-else type="info">文档</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="专业大类" width="120" />
      <el-table-column prop="score" label="评分" width="100" align="center">
        <template #default="{ row }">
          <el-rate v-model="row.score" disabled show-score text-color="#ff9900" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="收藏时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
          <el-button link type="danger" size="small" @click="handleCancel(row)">取消收藏</el-button>
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  resourceType: null
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, resourceName: 'Python基础编程视频教程', resourceType: 1, category: '计算机类', score: 4.5, createTime: '2026-07-01 10:00:00' },
      { id: 2, resourceName: '电路分析实验指导音频', resourceType: 2, category: '电子信息类', score: 3.8, createTime: '2026-07-05 14:30:00' },
      { id: 3, resourceName: '机械设计基础文档', resourceType: 3, category: '机械类', score: 4.2, createTime: '2026-07-08 09:15:00' }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => { searchForm.resourceType = null; handleSearch() }

const handleView = (row) => {
  router.push(`/resources/media/${row.id}`)
}

const handleCancel = (row) => {
  ElMessageBox.confirm(`确定取消收藏「${row.resourceName}」吗？`, '提示', { type: 'warning' }).then(() => {
    ElMessage.success('已取消收藏')
    fetchData()
  }).catch(() => {})
}

const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>