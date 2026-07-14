<script setup>
import { getNoticeDetail } from '@/api/bulletin'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getNoticeDetail, '加载公告详情失败')
</script>