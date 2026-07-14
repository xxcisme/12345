<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminExperiment, updateAdminExperiment } from '@/api/admin/training'
import { getMyExperimentDetail } from '@/api/user'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminExperiment, updateAdminExperiment, () => {
  router.push('/admin/training/experiments')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getMyExperimentDetail(route.params.id)
    setFormData(res.data)
  }
})
</script>