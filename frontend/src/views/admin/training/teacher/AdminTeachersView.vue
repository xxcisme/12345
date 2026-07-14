<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.name" placeholder="姓名" clearable style="width: 150px;" />
        <el-input v-model="searchForm.teacherId" placeholder="教师ID" clearable style="width: 150px;" />
        <el-input v-model="searchForm.company" placeholder="所在单位" clearable style="width: 180px;" />
        <el-select v-model="searchForm.onJob" placeholder="在职状态" clearable style="width: 120px;">
          <el-option label="在职" :value="true" />
          <el-option label="离职" :value="false" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleAdd">新增师资</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="teacherId" label="教师ID" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="type" label="师资类型" width="120">
        <template #default="{ row }">
          {{ row.type === '0' ? '实训老师' : '非实训老师' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="email" label="邮箱" min-width="160" show-overflow-tooltip />
      <el-table-column prop="company" label="所在单位" min-width="150" show-overflow-tooltip />
      <el-table-column prop="onJob" label="在职状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.onJob" type="success">在职</el-tag>
          <el-tag v-else type="info">离职</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="info" size="small" @click="handleDetail(row)">详情</el-button>
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
  name: '',
  teacherId: '',
  company: '',
  onJob: null
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, teacherId: 'T2026001', name: '张老师', type: '0', phone: '13800138001', email: 'zhang@example.com', company: 'XX大学计算机学院', onJob: true },
      { id: 2, teacherId: 'T2026002', name: '李老师', type: '0', phone: '13800138002', email: 'li@example.com', company: 'XX大学电子学院', onJob: true },
      { id: 3, teacherId: 'T2026003', name: '王老师', type: '1', phone: '13800138003', email: 'wang@example.com', company: 'XX工业大学', onJob: false }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => {
  Object.assign(searchForm, { name: '', teacherId: '', company: '', onJob: null })
  handleSearch()
}

const handleAdd = () => { router.push('/admin/training/teachers/new') }
const handleEdit = (row) => { router.push(`/admin/training/teachers/edit/${row.id}`) }
const handleDetail = (row) => { router.push(`/admin/training/teachers/${row.id}`) }

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除教师「${row.name}」吗？有关联课程时不可删除。`, '提示', { type: 'warning' }).then(() => {
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