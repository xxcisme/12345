<template>
  <page-container>
    <el-card style="max-width:900px; margin:0 auto;">
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-size:18px; font-weight:bold;">{{ resource.name }}</span>
          <div>
            <el-button :type="isFavorited ? 'danger' : 'default'" @click="handleFavorite">
              <el-icon><star-filled v-if="isFavorited" /><star v-else /></el-icon>
              {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="资源类型">
          <el-tag v-if="resource.type === 1" type="danger">视频</el-tag>
          <el-tag v-else-if="resource.type === 2" type="warning">音频</el-tag>
          <el-tag v-else type="info">文档</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="专业大类">{{ resource.category }}</el-descriptions-item>
        <el-descriptions-item label="所属学校">{{ resource.school || '-' }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ resource.leader || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开放共享">
          <el-tag v-if="resource.isShared" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ resource.createTime }}</el-descriptions-item>
        <el-descriptions-item label="概要介绍" :span="2">{{ resource.profile || '-' }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top:20px; padding:20px; background:#f5f7fa; border-radius:4px;">
        <div style="font-weight:bold; margin-bottom:10px;">资源预览</div>
        <div style="height:300px; background:#000; display:flex; align-items:center; justify-content:center; color:#fff; border-radius:4px;">
          <span>资源播放器占位（视频/音频/文档预览组件）</span>
        </div>
      </div>

      <div style="margin-top:20px;">
        <div style="font-weight:bold; margin-bottom:10px;">评分</div>
        <div style="display:flex; align-items:center; gap:15px;">
          <el-rate v-model="userScore" allow-half @change="handleScore" />
          <span style="color:#ff9900; font-size:18px; font-weight:bold;">{{ resource.avgScore }}</span>
          <span style="color:#999;">（{{ resource.scoreCount }}人评分）</span>
        </div>
      </div>
    </el-card>

    <div style="max-width:900px; margin:20px auto 0; text-align:center;">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </page-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const resource = ref({})
const isFavorited = ref(false)
const userScore = ref(0)

const fetchData = () => {
  setTimeout(() => {
    resource.value = {
      id: route.params.id,
      name: 'Python基础编程视频教程',
      type: 1,
      category: '计算机类',
      school: 'XX大学',
      leader: '张老师',
      isShared: true,
      profile: '本视频教程涵盖Python编程基础语法、数据类型、控制结构等内容。',
      createTime: '2026-07-01 10:00:00',
      avgScore: 4.5,
      scoreCount: 128
    }
    isFavorited.value = false
    userScore.value = 0
  }, 300)
}

const handleFavorite = () => {
  isFavorited.value = !isFavorited.value
  ElMessage.success(isFavorited.value ? '收藏成功' : '已取消收藏')
}

const handleScore = (val) => {
  ElMessage.success(`评分成功：${val} 星`)
}

const handleBack = () => {
  router.push('/resources/media')
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>