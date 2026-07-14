<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addAdminDevice, updateAdminDevice } from '@/api/admin/resource'
import { getDeviceDetail } from '@/api/resource'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminDevice, updateAdminDevice, () => {
  router.push('/admin/resource/devices')
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getDeviceDetail(route.params.id)
    setFormData(res.data)
  }
})
</script>