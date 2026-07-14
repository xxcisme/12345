<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminExperiment, updateAdminExperiment } from '@/api/admin/training'
import { getMyExperimentDetail } from '@/api/user'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminExperiment, updateAdminExperiment, () => {
  router.push('/admin/training/experiments')
})

const { loadDetail } = useDetail(getMyExperimentDetail, '加载实验详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>