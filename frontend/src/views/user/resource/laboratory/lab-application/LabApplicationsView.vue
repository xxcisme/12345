<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.status" placeholder="申请状态" clearable style="width: 140px;">
          <el-option label="待审批" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已拒绝" :value="2" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="primary" @click="handleApply">新建申请</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="labName" label="实验室名称" min-width="150" />
      <el-table-column prop="name" label="实验名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="startTime" label="开始时间" width="160" />
      <el-table-column prop="endTime" label="结束时间" width="160" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">待审批</el-tag>
          <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
          <el-tag v-else type="danger">已拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditRemark" label="审核备注" min-width="150" show-overflow-tooltip />
      <el-table-column prop="createTime" label="申请时间" width="160" />
      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="info" size="small" @click="handleView(row)">查看</el-button>
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

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="viewDialogVisible" title="申请详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="实验室">{{ viewForm.labName }}</el-descriptions-item>
        <el-descriptions-item label="实验名称">{{ viewForm.name }}</el-descriptions-item>
        <el-descriptions-item label="实验类型">{{ viewForm.experimentType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请说明">{{ viewForm.purpose || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ viewForm.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ viewForm.endTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewForm.status === 0" type="warning">待审批</el-tag>
          <el-tag v-else-if="viewForm.status === 1" type="success">已通过</el-tag>
          <el-tag v-else type="danger">已拒绝</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核备注">{{ viewForm.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
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
  status: null
})

const tableData = ref([])

const viewDialogVisible = ref(false)
const viewForm = reactive({
  labName: '',
  name: '',
  experimentType: '',
  purpose: '',
  startTime: '',
  endTime: '',
  status: 0,
  auditRemark: ''
})

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, labName: '计算机基础实验室', name: 'Python程序设计实验', startTime: '2026-07-15 08:00:00', endTime: '2026-07-15 12:00:00', status: 0, auditRemark: '', createTime: '2026-07-10 09:30:00' },
      { id: 2, labName: '电子电路实验室', name: '模拟电路实验', startTime: '2026-07-16 14:00:00', endTime: '2026-07-16 18:00:00', status: 1, auditRemark: '审批通过', createTime: '2026-07-11 10:00:00' },
      { id: 3, labName: '机械加工实验室', name: '数控加工实验', startTime: '2026-07-17 08:00:00', endTime: '2026-07-17 16:00:00', status: 2, auditRemark: '该时段实验室已安排其他课程', createTime: '2026-07-12 15:20:00' }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => { searchForm.status = null; handleSearch() }
const handleApply = () => { router.push('/user/lab-applications/new') }
const handleView = (row) => { Object.assign(viewForm, row); viewDialogVisible.value = true }
const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>