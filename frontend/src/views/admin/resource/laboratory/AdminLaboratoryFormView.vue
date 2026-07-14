<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminLaboratory, updateAdminLaboratory, getLaboratoryDetail } from '@/api/admin/resource'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminLaboratory, updateAdminLaboratory, () => {
  router.push('/admin/resource/laboratories')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getLaboratoryDetail(route.params.id)
    setFormData(res.data)
  }
})
</script>