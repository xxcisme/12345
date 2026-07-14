<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { submitLabApplication } from '@/api/user'

const router = useRouter()
const form = ref({
  labId: '',
  applicantName: '',
  contactPhone: '',
  purpose: '',
  name: '',
  startTime: '',
  endTime: ''
})
const loading = ref(false)

const handleSubmit = async () => {
  loading.value = true
  try {
    await submitLabApplication(form.value)
    ElMessage.success('申请提交成功')
    router.push('/user/lab-applications')
  } finally {
    loading.value = false
  }
}
</script>