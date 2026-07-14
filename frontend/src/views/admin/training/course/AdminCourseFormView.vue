<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminCourse, updateAdminCourse } from '@/api/admin/training'
import { getMyCourseDetail } from '@/api/user'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminCourse, updateAdminCourse, () => {
  router.push('/admin/training/courses')
})

const { loadDetail } = useDetail(getMyCourseDetail, '加载课程详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>