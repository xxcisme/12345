<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminNotice, updateAdminNotice } from '@/api/admin/bulletin'
import { getNoticeDetail } from '@/api/bulletin'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(
  addAdminNotice,
  updateAdminNotice,
  () => router.push('/admin/bulletin/notice')
)

Object.assign(form, {
  id: undefined,
  title: '',
  content: ''
})

const { loadDetail } = useDetail(getNoticeDetail, '加载公告详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>