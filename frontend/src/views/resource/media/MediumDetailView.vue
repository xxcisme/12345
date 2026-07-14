<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getResourceDetail, scoreResource } from '@/api/resource'
import { addFavorite } from '@/api/user'
import { getUser } from '@/utils/local_storage'
import { useDetail } from '@/utils/composables/useDetail'

const route = useRoute()
const scoreVal = ref(0)
const favoriting = ref(false)
const scoring = ref(false)

const { detail, loadDetail } = useDetail(getResourceDetail, '加载资源详情失败')

const handleScore = async () => {
  if (!getUser()) { ElMessage.warning('请先登录'); return }
  scoring.value = true
  try {
    await scoreResource(route.params.id, { score: scoreVal.value })
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
    ElMessage.success('已收藏')
  } finally {
    favoriting.value = false
  }
}
</script>