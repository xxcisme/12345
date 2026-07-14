<script setup>
import { getMyCourseDetail } from '@/api/user'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getMyCourseDetail, '加载课程详情失败')
</script>