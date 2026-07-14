<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getMyCourseDetail } from '@/api/user'

const route = useRoute()
const detail = ref(null)

onMounted(async () => {
  const res = await getMyCourseDetail(route.params.id)
  detail.value = res.data
})
</script>