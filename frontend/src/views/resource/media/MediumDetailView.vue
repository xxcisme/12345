<template>
  <page-container>
    <el-card style="max-width:900px; margin:0 auto;">
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-size:18px; font-weight:bold;">{{ detail?.name }}</span>
          <div>
            <el-button :type="isFavorited ? 'danger' : 'default'" @click="toggleFavorite">
              <el-icon><star-filled v-if="isFavorited" /><star v-else /></el-icon>
              {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="资源类型">
          <el-tag v-if="detail?.type === 1" type="danger">视频</el-tag>
          <el-tag v-else-if="detail?.type === 2" type="warning">音频</el-tag>
          <el-tag v-else type="info">文档</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="专业大类">{{ detail?.category }}</el-descriptions-item>
        <el-descriptions-item label="所属学校">{{ detail?.school || '-' }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detail?.leader || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开放共享">
          <el-tag v-if="detail?.isShared" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ detail?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="概要介绍" :span="2">{{ detail?.profile || '-' }}</el-descriptions-item>
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
          <el-rate v-model="scoreVal" allow-half @change="handleScore" />
          <span style="color:#ff9900; font-size:18px; font-weight:bold;">{{ detail?.avgScore }}</span>
          <span style="color:#999;">（{{ detail?.scoreCount }}人评分）</span>
        </div>
      </div>
    </el-card>

    <div style="max-width:900px; margin:20px auto 0; text-align:center;">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </page-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { getResourceDetail, scoreResource } from '@/api/resource'
import { addFavorite } from '@/api/user'
import { getUser } from '@/utils/local_storage'
import { useDetail } from '@/utils/composables/useDetail'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()

const { detail, loadDetail } = useDetail(getResourceDetail, '加载资源详情失败')

const isFavorited = ref(false)
const scoreVal = ref(0)
const favoriting = ref(false)
const scoring = ref(false)

const handleScore = async (val) => {
  if (!getUser()) { ElMessage.warning('请先登录'); return }
  scoring.value = true
  try {
    await scoreResource(route.params.id, { score: val })
    await loadDetail()
    ElMessage.success('评分成功')
  } finally {
    scoring.value = false
  }
}

const toggleFavorite = async () => {
  if (!getUser()) { ElMessage.warning('请先登录'); return }
  favoriting.value = true
  try {
    await addFavorite({ resourceId: route.params.id })
    await loadDetail()
    isFavorited.value = !isFavorited.value
    ElMessage.success('已收藏')
  } finally {
    favoriting.value = false
  }
}

const handleBack = () => {
  router.push('/resources/media')
}
</script>

<style scoped lang="scss">
</style>