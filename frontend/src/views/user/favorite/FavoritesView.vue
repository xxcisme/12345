<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getFavorites } from '@/api/user'
import { cancelFavorite } from '@/api/user'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getFavorites, {
  resourceType: undefined
})

const { handleDelete } = useConfirm(
  (id) => cancelFavorite(id),
  () => handleCurrentChange(pageNo.value)
)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>我的收藏</h2>
    </div>

    <div v-loading="loading">
      <el-table v-if="list.length" :data="list" stripe style="width: 100%">
        <el-table-column prop="resourceName" label="资源名称" min-width="200" />
        <el-table-column label="资源类型" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ row.resourceType === 1 ? '视频' : row.resourceType === 2 ? '音频' : '文档' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="收藏时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link size="small" @click="handleDelete(row.resourceId, row.resourceName)">
              取消收藏
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无收藏" />
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 20px;
}
.page-header {
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>