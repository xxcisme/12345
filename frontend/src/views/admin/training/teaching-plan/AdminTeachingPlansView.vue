<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminTeachingPlans, publishAdminTeachingPlan, deleteAdminTeachingPlan } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { usePublish } from '@/utils/composables/usePublish'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminTeachingPlans, {
  name: '',
  semester: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminTeachingPlan, () => {
  handleCurrentChange(pageNo.value)
})

const { handlePublish } = usePublish(publishAdminTeachingPlan, null, loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>教学计划管理</h2>
      <router-link to="/admin/training/teaching-plans/new">
        <el-button type="primary">新增教学计划</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索计划名称" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.status" placeholder="状态" clearable style="width: 120px" @change="handleCurrentChange(1)">
        <el-option label="草稿" :value="0" />
        <el-option label="已发布" :value="1" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="name" label="计划名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="semester" label="学期" width="120" />
        <el-table-column prop="className" label="班级" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '已发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/admin/training/teaching-plans/edit/${row.id}`)">编辑</el-button>
            <el-button v-if="row.status !== 1" type="success" link size="small" @click="handlePublish(row.id, row.name)">发布</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.name)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无教学计划" />
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