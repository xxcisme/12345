<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="资源名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入资源名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="资源类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择资源类型" style="width: 100%;">
          <el-option label="视频" :value="1" />
          <el-option label="音频" :value="2" />
          <el-option label="文档" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="专业大类" prop="category">
        <el-input v-model="form.category" placeholder="请输入专业大类" />
      </el-form-item>
      <el-form-item label="所属学校">
        <el-input v-model="form.school" placeholder="请输入所属学校" />
      </el-form-item>
      <el-form-item label="负责人">
        <el-input v-model="form.leader" placeholder="请输入负责人姓名" />
      </el-form-item>
      <el-form-item label="开放共享" prop="isShared">
        <el-radio-group v-model="form.isShared">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="概要介绍">
        <el-input v-model="form.profile" type="textarea" :rows="4" placeholder="请输入资源概要介绍" />
      </el-form-item>
      <el-form-item label="资源文件" prop="file">
        <div v-if="isEdit && existingFileName" style="margin-bottom: 10px;">
          <span style="color: #606266;">当前文件：</span>
          <el-link type="primary" :href="existingFileUrl" target="_blank">{{ existingFileName }}</el-link>
        </div>
        <el-upload
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :limit="1"
          :file-list="fileList"
          drag
          style="width: 100%;"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            拖拽文件到此处或 <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持视频、音频、文档格式，单个文件不超过500MB
            </div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">{{ isEdit ? '保存修改' : '立即创建' }}</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { addAdminResource, updateAdminResource } from '@/api/admin/resource'
import { getResourceDetail } from '@/api/resource'
import { buildFormData } from '@/utils/upload'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)
const fileList = ref([])
const submitting = ref(false)
const file = ref(null)

const existingFileName = ref('')
const existingFileUrl = ref('')

const MAX_FILE_SIZE = 500 * 1024 * 1024

const form = reactive({
  id: null,
  name: '',
  type: null,
  category: '',
  school: '',
  leader: '',
  isShared: true,
  profile: ''
})

const rules = {
  name: [{ required: true, message: '请输入资源名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择资源类型', trigger: 'change' }],
  isShared: [{ required: true, message: '请选择是否开放共享', trigger: 'change' }]
}

const handleFileChange = (uploadFile) => {
  if (uploadFile.size && uploadFile.size > MAX_FILE_SIZE) {
    ElMessage.error('文件大小不能超过500MB')
    fileList.value = []
    file.value = null
    return
  }
  file.value = uploadFile.raw
  fileList.value = [uploadFile]
}

const handleFileRemove = () => {
  file.value = null
  fileList.value = []
}

const handleSubmit = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    
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
    } catch (error) {
      ElMessage.error(isEdit.value ? '修改失败' : '新增失败')
    } finally {
      submitting.value = false
    }
  })
}

const handleCancel = () => {
  router.push('/admin/resource/media')
}

const fetchDetail = async () => {
  if (isEdit.value) {
    try {
      const data = await getResourceDetail(route.params.id)
      const { id, name, type, category, school, leader, isShared, profile } = data
      Object.assign(form, { id, name, type, category, school, leader, isShared, profile })
      existingFileName.value = data.fileName || 'resource_file'
      existingFileUrl.value = data.fileUrl || '#'
    } catch (error) {
      ElMessage.error('加载资源详情失败')
    }
  }
}

onMounted(() => {
  fetchDetail()
})
</script>