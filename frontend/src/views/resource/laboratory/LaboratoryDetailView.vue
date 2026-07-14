<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getLaboratoryDetail } from '@/api/resource'

const route = useRoute()
const detail = ref(null)

onMounted(async () => {
  const res = await getLaboratoryDetail(route.params.id)
  detail.value = res.data
})
</script>