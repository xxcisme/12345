<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getAdminTeacherDetail, resetAdminTeacherPassword } from '@/api/admin/training'
import { ElMessage } from 'element-plus'

const route = useRoute()
const detail = ref(null)

onMounted(async () => {
  const res = await getAdminTeacherDetail(route.params.id)
  detail.value = res.data
})

const handleResetPwd = async () => {
  await resetAdminTeacherPassword(route.params.id)
  ElMessage.success('密码已重置为初始密码')
}
</script>