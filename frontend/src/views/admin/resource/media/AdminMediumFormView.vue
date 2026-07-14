<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addAdminResource, updateAdminResource } from '@/api/admin/resource'
import { getResourceDetail } from '@/api/resource'
import { buildFormData } from '@/utils/upload'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

const file = ref(null)
const fileList = ref([])

const defaultForm = {
  id: undefined,
  name: '',
  type: undefined,
  category: '',
  school: '',
  leader: '',
  isShared: true,
  profile: ''
}

const { form, formRef, submitting, setFormData, submit } = useForm(
  addAdminResource,
  updateAdminResource,
  () => router.push('/admin/resource/media')
)

Object.assign(form, defaultForm)

const fileAccept = computed(() => {
  const map = {
    1: 'video/*',
    2: 'audio/*',
    3: '.pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.txt'
  }
  return map[form.type] || '*'
})

const handleFileChange = (uploadFile) => {
  file.value = uploadFile.raw
}

const handleRemove = () => {
  file.value = null
  fileList.value = []
}

const handleSubmit = async () => {
  if (formRef.value) {
    await formRef.value.validate()
  }

  if (!isEdit.value && !file.value) {
    ElMessage.warning('请选择要上传的资源文件')
    return
  }

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateAdminResource(form)
      ElMessage.success('修改成功')
    } else {
      const payload = { ...form, file: file.value }
      await addAdminResource(buildFormData(payload))
      ElMessage.success('新增成功')
    }
    router.push('/admin/resource/media')
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  if (isEdit.value) {
    const res = await getResourceDetail(route.params.id)
    const { id, name, type, category, school, leader, isShared, profile } = res.data
    setFormData({ id, name, type, category, school, leader, isShared, profile })
  }
})
</script>