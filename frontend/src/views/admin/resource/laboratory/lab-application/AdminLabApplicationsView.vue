<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminLabApplications, auditAdminLabApplication } from '@/api/admin/resource'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminLabApplications, {
  status: 0, // 默认待审批
  labId: undefined
})

const handleAudit = async (id, status, auditRemark) => {
  await auditAdminLabApplication(id, { status, auditRemark })
  ElMessage.success('审批完成')
  loadData()
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>实验室申请审批</h2>
    </div>

    <div class="search-bar">
      <el-select v-model="params.status" placeholder="状态" clearable style="width: 140px" @change="handleCurrentChange(1)">
        <el-option label="待审核" :value="0" />
        <el-option label="已通过" :value="1" />
        <el-option label="已拒绝" :value="2" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="labName" label="实验室名称" min-width="150" />
        <el-table-column prop="applicantName" label="申请人" width="120" />
        <el-table-column prop="name" label="实验名称" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 0 ? '待审核' : row.status === 1 ? '已通过' : '已拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 0">
              <el-button type="success" link size="small" @click="handleAudit(row.id, 1)">通过</el-button>
              <el-button type="danger" link size="small" @click="handleAudit(row.id, 2)">拒绝</el-button>
            </template>
            <span v-else style="color: #c0c4cc">已处理</span>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无申请" />
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
  margin-bottom: 20px;
}
.page-header h2 {
  font-size: 20px;
  color: #303133;
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