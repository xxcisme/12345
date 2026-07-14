<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addAdminResource, updateAdminResource } from '@/api/admin/resource'
import { getResourceDetail } from '@/api/resource'
import { buildFormData } from '@/utils/upload'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

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

const { form, formRef, submitting, setFormData } = useForm(
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

const { loadDetail } = useDetail(getResourceDetail, '加载资源详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) {
      const { id, name, type, category, school, leader, isShared, profile } = data
      setFormData({ id, name, type, category, school, leader, isShared, profile })
    }
  })
}
</script>