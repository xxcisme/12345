<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminResources, deleteAdminResource } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminResources, {
  name: '',
  type: undefined,
  status: undefined,
  category: ''
})

const { handleDelete } = useConfirm(deleteAdminResource, () => {
  handleCurrentChange(pageNo.value)
})

const handleAudit = (id, status, auditRemark) => {
  // 打开审核弹窗，调用 auditAdminResource
  // 由页面内嵌弹窗处理，这里仅提供方法供模板调用
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>媒体资源管理</h2>
      <router-link to="/admin/resource/media/new">
        <el-button type="primary">新增资源</el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input v-model="params.name" placeholder="搜索资源名称" clearable style="width: 200px" @keyup.enter="handleCurrentChange(1)" />
      <el-select v-model="params.type" placeholder="资源类型" clearable style="width: 140px" @change="handleCurrentChange(1)">
        <el-option label="视频" :value="1" />
        <el-option label="音频" :value="2" />
        <el-option label="文档" :value="3" />
      </el-select>
      <el-button type="primary" @click="handleCurrentChange(1)">搜索</el-button>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="name" label="名称" min-width="180" show-overflow-tooltip />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.type === 1 ? '视频' : row.type === 2 ? '音频' : '文档' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="school" label="学校" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/admin/resource/media/edit/${row.id}`)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row.id, row.name)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无资源" />
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