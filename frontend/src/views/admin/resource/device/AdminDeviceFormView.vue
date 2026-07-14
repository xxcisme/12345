<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminDevice, updateAdminDevice } from '@/api/admin/resource'
import { getDeviceDetail } from '@/api/resource'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminDevice, updateAdminDevice, () => {
  router.push('/admin/resource/devices')
})

const { loadDetail } = useDetail(getDeviceDetail, '加载设备详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>