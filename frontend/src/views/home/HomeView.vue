<template>
  <div class="home-page">
    <el-row :gutter="20">
      <!-- 左侧公告栏 -->
      <el-col :span="16">
        <el-card class="announcement-card" shadow="never">
          <template #header>
            <span style="font-weight: 600; font-size: 16px;">📢 最新公告</span>
          </template>
          <div class="announcement-content" v-if="notices.length > 0">
            <h3>{{ notices[0]?.title }}</h3>
            <div class="meta">
              <span>{{ notices[0]?.publishTime }}</span>
              <span>来源：{{ notices[0]?.origin || '平台' }}</span>
            </div>
            <div class="body" v-html="notices[0]?.content"></div>
          </div>
          <el-empty v-else description="暂无公告" />
        </el-card>
      </el-col>

      <!-- 右侧新闻列表 -->
      <el-col :span="8">
        <el-card class="news-card" shadow="never">
          <template #header>
            <span style="font-weight: 600; font-size: 16px;">📰 最新新闻</span>
          </template>
          <ul class="news-list">
            <li v-for="item in news" :key="item.id" class="news-item">
              <router-link :to="`/news/${item.id}`" class="news-title">
                {{ item.title }}
              </router-link>
              <span class="news-date">{{ item.publishTime }}</span>
            </li>
          </ul>
          <div v-if="news.length === 0" class="empty-text">暂无新闻</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNoticesList, getNewsList } from '@/api/bulletin'

const notices = ref([])
const news = ref([])

onMounted(async () => {
  try {
    const [noticeRes, newsRes] = await Promise.all([
      getNoticesList({ pageNo: 1, pageSize: 1 }),
      getNewsList({ pageNo: 1, pageSize: 8 })
    ])
    notices.value = noticeRes.data?.records || []
    news.value = newsRes.data?.records || []
  } catch (error) {
  }
})
</script>

<style scoped lang="scss">
.home-page {
  padding: 10px;
}
.announcement-card {
  height: 500px;
  .announcement-content {
    height: 400px;
    overflow-y: auto;
    h3 {
      margin: 0 0 8px 0;
      font-size: 18px;
    }
    .meta {
      font-size: 13px;
      color: #909399;
      margin-bottom: 12px;
      span {
        margin-right: 16px;
      }
    }
    .body {
      line-height: 1.8;
      color: #303133;
    }
  }
}
.news-card {
  height: 500px;
  .news-list {
    list-style: none;
    padding: 0;
    margin: 0;
    .news-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;
      border-bottom: 1px solid #f0f2f5;
      .news-title {
        text-decoration: none;
        color: #303133;
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 10px;
        &:hover {
          color: $primary-color;
        }
      }
      .news-date {
        font-size: 12px;
        color: #909399;
        white-space: nowrap;
      }
    }
  }
  .empty-text {
    text-align: center;
    color: #909399;
    padding: 40px 0;
  }
}
</style>