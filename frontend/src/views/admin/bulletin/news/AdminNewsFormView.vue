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

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑新闻' : '新增新闻' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入新闻标题" />
        </el-form-item>
        <el-form-item label="来源">
          <el-input v-model="form.origin" placeholder="请输入新闻来源" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入新闻内容" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            class="upload-cover"
            action="/api/upload"
            :show-file-list="!!form.enclosure"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :file-list="form.enclosure ? [{ name: 'cover', url: form.enclosure }] : []"
            list-type="picture"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/bulletin/news')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
}
.page-header h2 {
  font-size: 20px;
  color: #303133;
}
.form-card {
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
</style>