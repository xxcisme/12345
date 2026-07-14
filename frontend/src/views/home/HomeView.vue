<script setup>
import { onMounted, ref } from 'vue'
import { getNoticesList } from '@/api/bulletin'
import { getNewsList } from '@/api/bulletin'

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
    // 数据加载失败时页面静默降级
  }
})
</script>