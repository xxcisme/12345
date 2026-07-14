<script setup>
import { getMyExperimentDetail } from '@/api/user'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getMyExperimentDetail, '加载实验详情失败')
</script>