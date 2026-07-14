<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminTeachers, deleteAdminTeacher, resetAdminTeacherPassword } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { ElMessageBox, ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminTeachers, {
  name: '',
  teacherId: '',
  company: '',
  onJob: undefined
})

const { handleDelete } = useConfirm(deleteAdminTeacher, () => {
  handleCurrentChange(pageNo.value)
})

const handleResetPwd = (id, name) => {
  ElMessageBox.confirm(
    `确定重置教师"${name}"的密码吗？`,
    '提示',
    { type: 'warning' }
  ).then(async () => {
    try {
      await resetAdminTeacherPassword(id)
      ElMessage.success('密码已重置为初始密码')
    } catch {
      // 错误已由拦截器处理
    }
  }).catch(() => {})
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>教师管理</h2>
      <router-link to="/admin/training/teachers/new">
        <el-button type="primary">新增教师</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索教师姓名" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-input v-model="params.teacherId" placeholder="搜索教师编号" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-input v-model="params.company" placeholder="搜索单位" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.onJob" placeholder="在职状态" clearable style="width: 120px" @change="handleCurrentChange(1)">
        <el-option label="在职" :value="1" />
        <el-option label="离职" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="teacherId" label="教师编号" width="120" />
        <el-table-column prop="typeName" label="师资类型" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="160" show-overflow-tooltip />
        <el-table-column prop="company" label="单位" width="150" show-overflow-tooltip />
        <el-table-column label="在职状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.onJob ? 'success' : 'info'" size="small">{{ row.onJob ? '在职' : '离职' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/admin/training/teachers/${row.id}`)">详情</el-button>
            <el-button type="primary" link size="small" @click="$router.push(`/admin/training/teachers/edit/${row.id}`)">编辑</el-button>
            <el-button type="warning" link size="small" @click="handleResetPwd(row.id, row.name)">重置密码</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.name)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无教师" />
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
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>