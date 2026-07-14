<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminCourses, publishAdminCourse, unpublishAdminCourse, deleteAdminCourse } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { usePublish } from '@/utils/composables/usePublish'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminCourses, {
  courseName: '',
  courseCode: '',
  courseType: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminCourse, () => {
  handleCurrentChange(pageNo.value)
})

const { handlePublish, handleUnpublish } = usePublish(publishAdminCourse, unpublishAdminCourse, loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>课程管理</h2>
      <router-link to="/admin/training/courses/new">
        <el-button type="primary">新增课程</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.courseName" placeholder="搜索课程名称" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.status" placeholder="状态" clearable style="width: 120px" @change="handleCurrentChange(1)">
        <el-option label="草稿" :value="0" />
        <el-option label="已发布" :value="1" />
        <el-option label="已下架" :value="2" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="courseName" label="课程名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="courseCode" label="编号" width="120" />
        <el-table-column prop="courseType" label="类型" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'" size="small">{{ row.status === 1 ? '已发布' : row.status === 2 ? '已下架' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/admin/training/courses/edit/${row.id}`)">编辑</el-button>
            <template v-if="row.status === 1">
              <el-button type="warning" link size="small" @click="handleUnpublish(row.id, row.courseName)">下架</el-button>
            </template>
            <template v-else>
              <el-button type="success" link size="small" @click="handlePublish(row.id, row.courseName)">发布</el-button>
            </template>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.courseName)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无课程" />
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