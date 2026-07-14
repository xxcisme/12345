<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminLaboratory, updateAdminLaboratory } from '@/api/admin/resource'
import { getLaboratoryDetail } from '@/api/resource'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminLaboratory, updateAdminLaboratory, () => {
  router.push('/admin/resource/laboratories')
})

const { loadDetail } = useDetail(getLaboratoryDetail, '加载实验室详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>