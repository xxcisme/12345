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
      delete data.status
      setFormData(data)
    }
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑资源' : '新增资源' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="资源名称" required>
          <el-input v-model="form.name" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源类型" required>
          <el-select v-model="form.type" placeholder="请选择资源类型" style="width: 100%">
            <el-option label="视频" :value="1" />
            <el-option label="音频" :value="2" />
            <el-option label="文档" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="form.school" placeholder="请输入学校" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="共享">
          <el-switch v-model="form.isShared" />
        </el-form-item>
        <el-form-item label="资源简介">
          <el-input v-model="form.profile" type="textarea" :rows="4" placeholder="请输入资源简介" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="上传文件" required>
          <el-upload
            :auto-upload="false"
            :file-list="fileList"
            :accept="fileAccept"
            :limit="1"
            :on-change="handleFileChange"
            :on-remove="handleRemove"
          >
            <el-button type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
          <el-button @click="router.push('/admin/resource/media')">取消</el-button>
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