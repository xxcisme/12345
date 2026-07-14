<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminUser, updateAdminUser, getAdminUserDetail } from '@/api/admin/system'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminUser, updateAdminUser, () => {
  router.push('/admin/system/users')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getAdminUserDetail(route.params.id)
    setFormData(res.data)
  }
})
</script>