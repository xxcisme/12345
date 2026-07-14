<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.status" placeholder="完成状态" clearable style="width: 140px;">
          <el-option label="待评定" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="未通过" :value="2" />
        </el-select>
        <el-select v-model="searchForm.courseId" placeholder="所属课程" clearable style="width: 160px;">
          <el-option label="Python程序设计" :value="1" />
          <el-option label="模拟电路分析" :value="2" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="number" label="实验编号" width="130" />
      <el-table-column prop="name" label="实验名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="courseName" label="所属课程" min-width="150" />
      <el-table-column prop="experimentType" label="实验类型" width="120" />
      <el-table-column prop="score" label="成绩" width="90" align="center">
        <template #default="{ row }">
          <span v-if="row.score !== null" :style="{ color: row.score >= 60 ? '#67c23a' : '#f56c6c', fontWeight: 'bold' }">{{ row.score }}</span>
          <span v-else style="color:#999;">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">待评定</el-tag>
          <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
          <el-tag v-else type="danger">未通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="160" />
      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleDetail(row)">详情</el-button>
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
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const router = useRouter()

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  status: null,
  courseId: null
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, number: 'EXP-001', name: 'Python基础语法实验', courseName: 'Python程序设计', experimentType: '编程实验', score: 85, status: 1, submitTime: '2026-07-10 10:00:00' },
      { id: 2, number: 'EXP-002', name: '数据结构实验', courseName: 'Python程序设计', experimentType: '编程实验', score: null, status: 0, submitTime: '2026-07-12 14:30:00' },
      { id: 3, number: 'EXP-101', name: '单管放大电路实验', courseName: '模拟电路分析', experimentType: '电路实验', score: 72, status: 2, submitTime: '2026-07-08 09:00:00' }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => { Object.assign(searchForm, { status: null, courseId: null }); handleSearch() }
const handleDetail = (row) => { router.push(`/user/experiments/${row.id}`) }
const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>