<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getNewsList } from '@/api/bulletin'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getNewsList)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>新闻资讯</h2>
    </div>

    <div class="news-list">
      <div v-loading="loading">
        <div v-if="list.length" class="news-items">
          <div v-for="item in list" :key="item.id" class="news-item">
            <router-link :to="`/news/${item.id}`" class="news-link">
              <h3>{{ item.title }}</h3>
              <div class="news-meta">
                <span v-if="item.origin">{{ item.origin }}</span>
                <span>{{ item.publishTime }}</span>
              </div>
            </router-link>
          </div>
        </div>
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
.news-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.news-item {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  transition: box-shadow 0.3s;
}
.news-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.news-link {
  text-decoration: none;
  color: inherit;
}
.news-item h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #303133;
}
.news-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #c0c4cc;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}
</style>