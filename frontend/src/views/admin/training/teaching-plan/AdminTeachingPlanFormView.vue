<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminTeachingPlan, updateAdminTeachingPlan } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeachingPlan, updateAdminTeachingPlan, () => {
  router.push('/admin/training/teaching-plans')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getAdminTeachingPlans({ pageNo: 1, pageSize: 1, id: route.params.id })
    const item = res.data.records?.[0]
    if (item) setFormData(item)
  }
})
</script>