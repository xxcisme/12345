<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminTeachingPlan, updateAdminTeachingPlan, getAdminTeachingPlans } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeachingPlan, updateAdminTeachingPlan, () => {
  router.push('/admin/training/teaching-plans')
})

const { loadDetail } = useDetail(
  (id) => getAdminTeachingPlans({ pageNo: 1, pageSize: 1, id }).then(res => ({
    data: res.data.records?.[0] || null
  })),
  '加载教学计划详情失败',
  { autoLoad: false }
)

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>