<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminNewsList, deleteAdminNews } from '@/api/admin/bulletin'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminNewsList, {
  title: ''
})

const { handleDelete } = useConfirm(deleteAdminNews, () => {
  handleCurrentChange(pageNo.value)
})
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>新闻管理</h2>
      <router-link to="/admin/bulletin/news/new">
        <el-button type="primary">新增新闻</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.title" placeholder="搜索新闻标题" clearable style="width: 240px" @keyup.enter="handleCurrentChange(1)" />
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="origin" label="来源" width="120" />
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/admin/bulletin/news/edit/${row.id}`)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.title)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无新闻" />
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