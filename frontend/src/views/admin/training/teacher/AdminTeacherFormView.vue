<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminTeacher, updateAdminTeacher, getAdminTeacherDetail } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeacher, updateAdminTeacher, () => {
  router.push('/admin/training/teachers')
})

const { loadDetail } = useDetail(getAdminTeacherDetail, '加载教师详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>