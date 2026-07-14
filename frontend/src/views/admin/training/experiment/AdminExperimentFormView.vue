<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminExperiment, updateAdminExperiment, getAdminExperiments } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminExperiment, updateAdminExperiment, () => {
  router.push('/admin/training/experiments')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getAdminExperiments({ pageNo: 1, pageSize: 1000 })
    const item = res.data.records.find(item => item.id === Number(route.params.id))
    if (item) setFormData(item)
  }
})
</script>