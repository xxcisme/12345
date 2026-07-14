<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getResourceDetail, scoreResource } from '@/api/resource'
import { addFavorite } from '@/api/user'
import { getUser } from '@/utils/local_storage'

const route = useRoute()
const detail = ref(null)
const scoreVal = ref(0)
const favoriting = ref(false)
const scoring = ref(false)

onMounted(async () => {
  const res = await getResourceDetail(route.params.id)
  detail.value = res.data
})

const handleScore = async () => {
  if (!getUser()) { ElMessage.warning('请先登录'); return }
  scoring.value = true
  try {
    await scoreResource(route.params.id, { score: scoreVal.value })
    // 重新获取详情
    const res = await getResourceDetail(route.params.id)
    detail.value = res.data
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
    // 刷新详情（收藏状态由后端返回，前端重新拉取）
    const res = await getResourceDetail(route.params.id)
    detail.value = res.data
    ElMessage.success('已收藏')
  } finally {
    favoriting.value = false
  }
}
</script>