<script setup>
import { getNewsDetail } from '@/api/bulletin'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getNewsDetail, '加载新闻详情失败')
</script>