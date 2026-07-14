<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getNoticesList } from '@/api/bulletin'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getNoticesList)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>公告通知</h2>
    </div>

    <div v-loading="loading">
      <div v-if="list.length" class="notice-items">
        <div v-for="item in list" :key="item.id" class="notice-item">
          <router-link :to="`/notices/${item.id}`" class="notice-link">
            <div class="notice-title">
              <el-tag type="warning" size="small" style="margin-right: 12px">公告</el-tag>
              <span>{{ item.title }}</span>
            </div>
            <span class="notice-time">{{ item.createTime }}</span>
          </router-link>
        </div>
      </div>
      <el-empty v-else description="暂无公告" />
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
  margin-bottom: 32px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.notice-items {
  display: flex;
  flex-direction: column;
  gap: 1px;
  background: #ebeef5;
  border-radius: 8px;
  overflow: hidden;
}
.notice-item {
  background: #fff;
  padding: 20px 24px;
  transition: background 0.2s;
}
.notice-item:hover {
  background: #f5f7fa;
}
.notice-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-decoration: none;
  color: inherit;
}
.notice-title {
  display: flex;
  align-items: center;
  font-size: 15px;
  color: #303133;
}
.notice-time {
  font-size: 13px;
  color: #c0c4cc;
  white-space: nowrap;
  margin-left: 20px;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}
</style>