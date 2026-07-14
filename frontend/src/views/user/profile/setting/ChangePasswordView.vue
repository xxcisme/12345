<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { changePassword } from '@/api/user'

const form = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const loading = ref(false)

const handleSubmit = async () => {
  if (form.value.newPassword !== form.value.confirmPassword) {
    ElMessage.error('两次新密码不一致')
    return
  }
  loading.value = true
  try {
    await changePassword(form.value)
    ElMessage.success('密码修改成功')
    form.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } finally {
    loading.value = false
  }
}
</script>