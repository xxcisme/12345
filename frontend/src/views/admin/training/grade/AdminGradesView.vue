<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminGrades } from '@/api/admin/training'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminGrades, {
  courseId: undefined,
  classId: undefined,
  status: 0,
  studentName: ''
})
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>成绩管理</h2>
    </div>

    <div class="search-bar">
      <el-input v-model="params.studentName" placeholder="搜索学生姓名" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column prop="experimentName" label="实验名称" min-width="150" />
        <el-table-column prop="overallScore" label="成绩" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.publishStatus === 1 ? 'success' : 'info'" size="small">{{ row.publishStatus === 1 ? '已发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评定时间" width="180" />
      </el-table>
      <el-empty v-else description="暂无成绩" />
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