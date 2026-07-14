<template>
  <page-container>
    <base-search>
      <template #left>
        <el-input v-model="searchForm.username" placeholder="操作用户名" clearable style="width: 150px;" />
        <el-input v-model="searchForm.action" placeholder="操作内容" clearable style="width: 150px;" />
        <el-select v-model="searchForm.userRole" placeholder="账号类型" clearable style="width: 130px;">
          <el-option label="学生" value="学生" />
          <el-option label="老师" value="老师" />
          <el-option label="管理员" value="管理员" />
          <el-option label="社会人士" value="社会人士" />
        </el-select>
        <el-date-picker
          v-model="searchForm.dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 360px;"
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="success" @click="handleExport">导出</el-button>
        <el-button type="danger" @click="handleClear">清理</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="操作用户" min-width="120" />
      <el-table-column prop="action" label="操作内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="userRole" label="账号类型" width="100" />
      <el-table-column prop="ip" label="IP地址" width="130" />
      <el-table-column prop="createTime" label="操作时间" width="170" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  username: '',
  action: '',
  userRole: '',
  dateRange: []
})

const tableData = ref([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        username: 'admin',
        action: '新增用户：student01',
        userRole: '管理员',
        ip: '192.168.1.100',
        createTime: '2026-07-14 09:30:00'
      },
      {
        id: 2,
        username: 'teacher01',
        action: '发布课程：Python程序设计',
        userRole: '老师',
        ip: '192.168.1.105',
        createTime: '2026-07-14 10:15:00'
      },
      {
        id: 3,
        username: 'admin',
        action: '审核通过资源：数据结构算法视频',
        userRole: '管理员',
        ip: '192.168.1.100',
        createTime: '2026-07-14 11:00:00'
      },
      {
        id: 4,
        username: 'admin',
        action: '删除公告：暑期实验室开放通知',
        userRole: '管理员',
        ip: '192.168.1.100',
        createTime: '2026-07-14 14:20:00'
      },
      {
        id: 5,
        username: 'teacher02',
        action: '评定成绩：张三 - 模拟电路实验 - 85分',
        userRole: '老师',
        ip: '192.168.1.110',
        createTime: '2026-07-14 16:45:00'
      }
    ]
    total.value = 5
    loading.value = false
  }, 500)
}

const handleSearch = () => {
  pageNo.value = 1
  fetchData()
}

const resetSearch = () => {
  searchForm.username = ''
  searchForm.action = ''
  searchForm.userRole = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleExport = () => {
  ElMessage.success('正在导出日志，请稍候...')
  // 调用 /api/v1/admin/logs/export
}

const handleClear = () => {
  ElMessageBox.confirm('确定清理所有操作日志吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('日志清理成功')
    fetchData()
  }).catch(() => {})
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  pageNo.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
</style>