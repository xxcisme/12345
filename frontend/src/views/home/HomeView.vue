<script setup>
import { onMounted, ref } from 'vue'
import { getNoticesList } from '@/api/bulletin'
import { getNewsList } from '@/api/bulletin'

const latestNotice = ref(null)
const news = ref([])

onMounted(async () => {
  try {
    const [noticeRes, newsRes] = await Promise.all([
      getNoticesList({ pageNo: 1, pageSize: 1 }),
      getNewsList({ pageNo: 1, pageSize: 8 })
    ])
    latestNotice.value = noticeRes.data?.records?.[0] || null
    news.value = newsRes.data?.records || []
  } catch (error) {
    // 数据加载失败时页面静默降级
  }
})
</script>

<template>
  <div class="home-container">
    <section class="hero">
      <h1>虚拟实训平台</h1>
      <p>提供高质量的虚拟仿真实训教学资源</p>
    </section>

    <section class="content">
      <div class="main-content">
        <div class="bulletin-row">
          <div class="announcement-col">
            <div class="col-header">
              <h3 class="section-title">公告</h3>
              <router-link to="/notices" class="col-more">更多</router-link>
            </div>
            <div v-if="latestNotice" class="announcement-card">
              <h4 class="announcement-title">{{ latestNotice.title }}</h4>
              <span class="announcement-time">{{ latestNotice.createTime }}</span>
              <div class="announcement-content" v-html="latestNotice.content"></div>
            </div>
            <el-empty v-else description="暂无公告" />
          </div>

          <div class="news-col">
            <div class="col-header">
              <h3 class="section-title">新闻</h3>
              <router-link to="/news" class="col-more">更多</router-link>
            </div>
            <div v-if="news.length" class="news-list">
              <div v-for="item in news" :key="item.id" class="news-item">
                <router-link :to="`/news/${item.id}`" class="news-link">
                  <span class="news-title">{{ item.title }}</span>
                  <span class="news-time">{{ item.createTime }}</span>
                </router-link>
              </div>
            </div>
            <el-empty v-else description="暂无新闻" />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home-container {
  background: #f5f7fa;
}
.hero {
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: #fff;
  text-align: center;
  padding: 60px 20px;
  min-height: 30vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.hero h1 {
  font-size: 32px;
  margin-bottom: 10px;
}
.hero p {
  font-size: 16px;
  opacity: 0.9;
}
.content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}
.section-title {
  font-size: 20px;
  color: #303133;
  margin: 0;
}
.bulletin-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  min-height: 50vh;
}
.announcement-col,
.news-col {
  display: flex;
  flex-direction: column;
}
.col-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.col-more {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}
.announcement-card {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.announcement-title {
  font-size: 18px;
  color: #e6a23c;
  margin-bottom: 8px;
}
.announcement-time {
  font-size: 13px;
  color: #c0c4cc;
  display: block;
  margin-bottom: 16px;
}
.announcement-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}
.announcement-content :deep(img) {
  max-width: 100%;
}
.news-list {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}
.news-item {
  border-bottom: 1px solid #f0f0f0;
}
.news-item:last-child {
  border-bottom: none;
}
.news-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  text-decoration: none;
  color: inherit;
  transition: background 0.2s;
}
.news-link:hover {
  background: #f5f7fa;
}
.news-title {
  font-size: 14px;
  color: #303133;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 16px;
}
.news-time {
  font-size: 12px;
  color: #c0c4cc;
  white-space: nowrap;
}
@media (max-width: 768px) {
  .bulletin-row {
    grid-template-columns: 1fr;
  }
}
</style>