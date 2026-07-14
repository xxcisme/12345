<script setup>
import { getDeviceDetail } from '@/api/resource'
import { useDetail } from '@/utils/composables/useDetail'

const { detail, loading } = useDetail(getDeviceDetail, '加载设备详情失败')
</script>