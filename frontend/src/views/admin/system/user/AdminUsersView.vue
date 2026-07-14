<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminUsers, toggleAdminUserStatus, deleteAdminUser } from '@/api/admin/system'
import { useConfirm } from '@/utils/composables/useConfirm'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminUsers, {
  username: '',
  realName: '',
  phone: '',
  role: undefined,
  status: undefined
})

const { handleDelete } = useConfirm(deleteAdminUser, () => {
  handleCurrentChange(pageNo.value)
})

const handleToggleStatus = async (id, currentStatus) => {
  const newStatus = currentStatus === 1 ? 0 : 1
  await toggleAdminUserStatus(id, { status: newStatus })
  ElMessage.success('状态切换成功')
  loadData()
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <router-link to="/admin/system/users/new">
        <el-button type="primary">新增用户</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.username" placeholder="搜索用户名" clearable style="width: 180px" @keyup.enter="handleCurrentChange(1)" />
      <el-input v-model="params.realName" placeholder="搜索真实姓名" clearable style="width: 180px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.status" placeholder="状态" clearable style="width: 120px" @change="handleCurrentChange(1)">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="roleName" label="角色" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/admin/system/users/edit/${row.id}`)">编辑</el-button>
            <el-button :type="row.status === 1 ? 'warning' : 'success'" link size="small" @click="handleToggleStatus(row.id, row.status)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.username)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无用户" />
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
.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>