<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminTeacher, updateAdminTeacher, getAdminTeacherDetail } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeacher, updateAdminTeacher, () => {
  router.push('/admin/training/teachers')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getAdminTeacherDetail(route.params.id)
    setFormData(res.data)
  }
})
</script>