<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.name" placeholder="实验名称" clearable style="width: 180px;" />
        <el-input v-model="searchForm.number" placeholder="实验编号" clearable style="width: 150px;" />
        <el-select v-model="searchForm.courseId" placeholder="所属课程" clearable style="width: 160px;">
          <el-option label="Python程序设计" :value="1" />
          <el-option label="模拟电路分析" :value="2" />
          <el-option label="机械制图基础" :value="3" />
        </el-select>
        <el-input v-model="searchForm.category" placeholder="专业" clearable style="width: 130px;" />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd" v-if="isAdmin || isTeacher">新增实验</el-button>
      </template>
    </base-search>

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="number" label="实验编号" width="130" />
      <el-table-column prop="name" label="实验名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="courseName" label="所属课程" min-width="150" show-overflow-tooltip />
      <el-table-column prop="category" label="专业" width="120" />
      <el-table-column prop="experimentType" label="实验类型" width="120" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0">草稿</el-tag>
          <el-tag v-else-if="row.status === 1" type="success">已发布</el-tag>
          <el-tag v-else type="info">已下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="220" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="success" size="small" @click="handlePublish(row)" v-if="row.status === 0">发布</el-button>
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
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTable } from '@/utils/composables/useTable'
import { getAdminExperiments, publishAdminExperiment, deleteAdminExperiment } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { useUserStore } from '@/stores/user'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'admin')
const isTeacher = computed(() => userStore.role === 'teacher')

const searchForm = reactive({
  name: '',
  number: '',
  courseId: null,
  category: ''
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminExperiments, {
  name: '',
  number: '',
  courseId: undefined,
  category: ''
})

const { handleDelete } = useConfirm(deleteAdminExperiment, () => {
  handleCurrentChange(pageNo.value)
})

const handleSearch = () => {
  params.name = searchForm.name
  params.number = searchForm.number
  params.courseId = searchForm.courseId
  params.category = searchForm.category
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.number = ''
  searchForm.courseId = null
  searchForm.category = ''
  params.name = ''
  params.number = ''
  params.courseId = undefined
  params.category = ''
  handleCurrentChange(1)
}

const handleAdd = () => {
  router.push('/admin/training/experiments/new')
}

const handleEdit = (row) => {
  router.push(`/admin/training/experiments/edit/${row.id}`)
}

const handlePublish = async (row) => {
  ElMessageBox.confirm(`确定发布实验「${row.name}」吗？`, '提示', { type: 'warning' }).then(async () => {
    await publishAdminExperiment(row.id)
    ElMessage.success('发布成功')
    loadData()
  }).catch(() => {})
}
</script>