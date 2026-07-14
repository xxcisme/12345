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

    <el-table :data="list" border style="width:100%; margin-top:10px;" v-loading="loading">
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
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTable } from '@/utils/composables/useTable'
import { getAdminLogs, exportAdminLogs, cleanAdminLogs } from '@/api/admin/system'
import { downloadExcel } from '@/utils/download'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const searchForm = reactive({
  username: '',
  action: '',
  userRole: '',
  dateRange: []
})

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLogs, {
  username: '',
  action: '',
  userRole: '',
  startTime: '',
  endTime: ''
})

const handleSearch = () => {
  params.username = searchForm.username
  params.action = searchForm.action
  params.userRole = searchForm.userRole
  params.startTime = searchForm.dateRange[0] || ''
  params.endTime = searchForm.dateRange[1] || ''
  handleCurrentChange(1)
}

const resetSearch = () => {
  searchForm.username = ''
  searchForm.action = ''
  searchForm.userRole = ''
  searchForm.dateRange = []
  params.username = ''
  params.action = ''
  params.userRole = ''
  params.startTime = ''
  params.endTime = ''
  handleCurrentChange(1)
}

const handleExport = async () => {
  await downloadExcel(exportAdminLogs, params.value, `logs_${Date.now()}.xls`)
}

const handleClear = () => {
  ElMessageBox.confirm('确定清理所有操作日志吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await cleanAdminLogs({ daysToKeep: 0 })
    ElMessage.success('日志清理成功')
    loadData()
  }).catch(() => {})
}
</script>