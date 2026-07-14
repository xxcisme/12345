<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminUser, updateAdminUser, getAdminUserDetail } from '@/api/admin/system'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminUser, updateAdminUser, () => {
  router.push('/admin/system/users')
})

const { loadDetail } = useDetail(getAdminUserDetail, '加载用户详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>