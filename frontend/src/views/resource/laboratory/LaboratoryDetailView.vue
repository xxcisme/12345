<script setup>
import { getLaboratoryDetail } from '@/api/resource'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getLaboratoryDetail, '加载实验室详情失败')
</script>