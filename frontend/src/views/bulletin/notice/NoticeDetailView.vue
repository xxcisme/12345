<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getNoticeDetail } from '@/api/bulletin'

const route = useRoute()
const detail = ref(null)

onMounted(async () => {
  const res = await getNoticeDetail(route.params.id)
  detail.value = res.data
})
</script>