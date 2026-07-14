<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminLogs, exportAdminLogs, cleanAdminLogs } from '@/api/admin/system'
import { downloadExcel } from '@/utils/download'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLogs, {
  username: '',
  action: '',
  userRole: '',
  startTime: '',
  endTime: ''
})

const handleExport = async () => {
  await downloadExcel(exportAdminLogs, params.value, `logs_${Date.now()}.xls`)
}

const handleClean = async (daysToKeep) => {
  await cleanAdminLogs({ daysToKeep })
  ElMessage.success('清理成功')
  loadData()
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>操作日志</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出日志</el-button>
        <el-popconfirm title="确定清理30天前的日志？" @confirm="handleClean(30)">
          <template #reference>
            <el-button type="warning">清理日志</el-button>
          </template>
        </el-popconfirm>
      </div>
    </div>

    <div class="search-bar">
      <el-input v-model="params.username" placeholder="搜索用户名" clearable style="width: 160px" @keyup.enter="handleCurrentChange(1)" />
      <el-input v-model="params.action" placeholder="搜索操作" clearable style="width: 160px" @keyup.enter="handleCurrentChange(1)" />
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="username" label="操作用户" width="120" />
        <el-table-column prop="action" label="操作" min-width="150" show-overflow-tooltip />
        <el-table-column prop="userRole" label="用户角色" width="100" />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="createTime" label="操作时间" width="180" />
      </el-table>
      <el-empty v-else description="暂无日志" />
    </div>

    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="pageNo"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.page-header h2 {
  font-size: 20px;
  color: #303133;
}
.header-actions {
  display: flex;
  gap: 12px;
}
.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>