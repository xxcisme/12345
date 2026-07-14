<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addAdminNotice, updateAdminNotice } from '@/api/admin/bulletin'
import { getNoticeDetail } from '@/api/bulletin'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(
  addAdminNotice,
  updateAdminNotice,
  () => router.push('/admin/bulletin/notice')
)

form.value = {
  id: undefined,
  title: '',
  content: ''
}

onMounted(async () => {
  if (isEdit.value) {
    try {
      const res = await getNoticeDetail(route.params.id)
      setFormData(res.data)
    } catch (error) {
      ElMessage.error('加载公告详情失败')
    }
  }
})
</script>