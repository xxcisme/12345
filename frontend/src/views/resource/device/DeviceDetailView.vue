<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDeviceDetail } from '@/api/resource'

const route = useRoute()
const detail = ref(null)

onMounted(async () => {
  const res = await getDeviceDetail(route.params.id)
  detail.value = res.data
})
</script>