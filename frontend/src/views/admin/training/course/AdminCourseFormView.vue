<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminCourse, updateAdminCourse } from '@/api/admin/training'
import { getMyCourseDetail } from '@/api/user'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminCourse, updateAdminCourse, () => {
  router.push('/admin/training/courses')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getMyCourseDetail(route.params.id)
    setFormData(res.data)
  }
})
</script>