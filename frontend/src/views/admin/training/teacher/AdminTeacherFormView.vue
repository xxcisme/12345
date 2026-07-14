<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminTeacher, updateAdminTeacher, getAdminTeachers } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeacher, updateAdminTeacher, () => {
  router.push('/admin/training/teachers')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getAdminTeachers({ pageNo: 1, pageSize: 1000 })
    const item = res.data.records.find(item => item.id === Number(route.params.id))
    if (item) setFormData(item)
  }
})
</script>