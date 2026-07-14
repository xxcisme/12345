<script setup>
import { useRoute } from 'vue-router'
import { getAdminTeacherDetail, resetAdminTeacherPassword } from '@/api/admin/training'
import { useDetail } from '@/utils/composables/useDetail'
import { ElMessage } from 'element-plus'

const route = useRoute()
const { detail, loading } = useDetail(getAdminTeacherDetail, '加载教师详情失败')

const handleResetPwd = async () => {
  try {
    await resetAdminTeacherPassword(route.params.id)
    ElMessage.success('密码已重置为初始密码')
  } catch (error) {
    ElMessage.error('重置密码失败')
  }
}
</script>