<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getMessages, markMessagesRead } from '@/api/user'
import { useMessageRead } from '@/utils/composables/useMessageRead'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getMessages, {
  type: undefined,
  isRead: undefined
})

const { markRead, markLoading } = useMessageRead(markMessagesRead, loadData)

const markAll = () => {
  const ids = list.value.filter(item => !item.isRead).map(item => item.id)
  if (ids.length) markRead(ids)
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>消息通知</h2>
      <el-button type="primary" @click="markAll">全部标为已读</el-button>
    </div>

    <div v-loading="loading">
      <div v-if="list.length" class="message-list">
        <div v-for="item in list" :key="item.id" class="message-item" :class="{ unread: !item.isRead }" @click="markRead([item.id])">
          <div class="message-header">
            <span class="message-title">{{ item.title }}</span>
            <el-tag v-if="!item.isRead" type="danger" size="small">未读</el-tag>
          </div>
          <p class="message-content">{{ item.content }}</p>
          <span class="message-time">{{ item.createTime }}</span>
        </div>
      </div>
      <el-empty v-else description="暂无消息" />
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
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.message-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.message-item {
  background: #fff;
  border-radius: 8px;
  padding: 20px 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.message-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.message-item.unread {
  border-left: 3px solid #409eff;
}
.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.message-title {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
}
.message-content {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
  line-height: 1.5;
}
.message-time {
  font-size: 12px;
  color: #c0c4cc;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>