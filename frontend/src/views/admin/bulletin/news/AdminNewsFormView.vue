<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElUpload } from 'element-plus'
import { addAdminNews, updateAdminNews } from '@/api/admin/bulletin'
import { getNewsDetail } from '@/api/bulletin'
import { useForm } from '@/utils/composables/useForm'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)

// 表单数据，增加 enclosure 字段用于存储图片地址
const { form, formRef, submitting, setFormData, submit } = useForm(
  addAdminNews,
  updateAdminNews,
  () => router.push('/admin/bulletin/news')
)

// 初始化表单结构
form.value = {
  id: undefined,
  title: '',
  origin: '',
  content: '',
  enclosure: '' // 图片地址
}

// 图片上传回调
const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    form.value.enclosure = response.data.url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.msg || '图片上传失败')
  }
}

// 图片移除回调
const handleRemove = () => {
  form.value.enclosure = ''
}

onMounted(async () => {
  if (isEdit.value) {
    try {
      // 使用前台详情接口获取数据
      const res = await getNewsDetail(route.params.id)
      // 直接填充表单（字段名与详情返回一致）
      setFormData(res.data)
    } catch (error) {
      ElMessage.error('加载新闻详情失败')
    }
  }
})
</script>