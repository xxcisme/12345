<template>
  <div class="home-page">
    <el-row :gutter="20">
      <!-- 左侧公告栏 -->
      <el-col :span="16">
        <el-card class="announcement-card" shadow="never">
          <template #header>
            <span style="font-weight: 600; font-size: 16px;">📢 最新公告</span>
          </template>
          <div class="announcement-content" v-if="latestAnnouncement">
            <h3>{{ latestAnnouncement.title }}</h3>
            <div class="meta">
              <span>{{ latestAnnouncement.publishTime }}</span>
              <span>来源：{{ latestAnnouncement.origin || '平台' }}</span>
            </div>
            <div class="body" v-html="latestAnnouncement.content"></div>
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
            <li v-for="item in newsList" :key="item.id" class="news-item">
              <router-link :to="`/news/${item.id}`" class="news-title">
                {{ item.title }}
              </router-link>
              <span class="news-date">{{ item.publishTime }}</span>
            </li>
          </ul>
          <div v-if="newsList.length === 0" class="empty-text">暂无新闻</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
// 模拟数据，实际需请求接口
const latestAnnouncement = ref(null)
const newsList = ref([])

onMounted(() => {
  // 调用 /api/v1/news?type=2&pageSize=1 获取最新公告
  // 调用 /api/v1/news?type=1&pageSize=5 获取最新新闻
  // 此处暂用假数据演示
  latestAnnouncement.value = {
    id: 1,
    title: '关于2026年暑假实训安排的通知',
    origin: '教务处',
    publishTime: '2026-07-10',
    content: '各位同学，暑假实训将于7月20日开始...<br>详情请关注后续通知。'
  }
  newsList.value = [
    { id: 2, title: '我校荣获全国实训基地优秀单位', publishTime: '2026-07-08' },
    { id: 3, title: '实验室新增一批设备', publishTime: '2026-07-05' },
    { id: 4, title: '线上资源库更新公告', publishTime: '2026-07-03' },
    { id: 5, title: '师生座谈会顺利举行', publishTime: '2026-07-01' },
    { id: 6, title: '实训安全培训通知', publishTime: '2026-06-28' },
  ]
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