<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminNews, updateAdminNews } from '@/api/admin/bulletin'
import { getNewsDetail } from '@/api/bulletin'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'
import { ElMessage } from 'element-plus'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(
  addAdminNews,
  updateAdminNews,
  () => router.push('/admin/bulletin/news')
)

Object.assign(form, {
  id: undefined,
  title: '',
  origin: '',
  content: '',
  enclosure: ''
})

const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    form.enclosure = response.data.url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.msg || '图片上传失败')
  }
}

const handleRemove = () => {
  form.enclosure = ''
}

const { loadDetail } = useDetail(getNewsDetail, '加载新闻详情失败', { autoLoad: false })

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>