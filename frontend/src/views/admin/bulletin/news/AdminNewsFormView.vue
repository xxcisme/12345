<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px; margin:0 auto;">
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio :label="1">新闻</el-radio>
          <el-radio :label="2">公告</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入标题" style="width:100%;" />
      </el-form-item>

      <el-form-item label="来源" prop="origin">
        <el-input v-model="form.origin" placeholder="请输入来源（选填）" style="width:100%;" />
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入内容" style="width:100%;" />
        <div style="font-size:12px; color:#909399; margin-top:4px;">支持富文本（若需富文本编辑器可替换此组件）</div>
      </el-form-item>

      <el-form-item label="附件地址" prop="enclosure">
        <el-input v-model="form.enclosure" placeholder="请输入附件URL（选填）" style="width:100%;" />
      </el-form-item>

      <el-form-item label="发布时间" prop="publishTime">
        <el-date-picker
          v-model="form.publishTime"
          type="datetime"
          placeholder="选择发布时间（不选则立即发布）"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width:100%;"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submit" :loading="submitting">保存</el-button>
        <el-button @click="router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { addAdminNews, updateAdminNews } from '@/api/admin/bulletin'
import { getNewsDetail } from '@/api/bulletin'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const submitting = ref(false)

const isEdit = route.params.id ? true : false

const form = reactive({
  type: 1,
  title: '',
  origin: '',
  content: '',
  enclosure: '',
  publishTime: ''
})

const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const fetchDetail = async () => {
  if (isEdit) {
    try {
      const data = await getNewsDetail(route.params.id)
      Object.assign(form, data)
    } catch (error) {
      ElMessage.error('加载新闻详情失败')
    }
  }
}

const submit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      const payload = { ...form }
      if (isEdit) {
        payload.id = Number(route.params.id)
        await updateAdminNews(payload)
      } else {
        await addAdminNews(payload)
      }
      ElMessage.success(isEdit ? '修改成功' : '新增成功')
      router.push('/admin/bulletin/news')
    } catch (error) {
      ElMessage.error(isEdit ? '修改失败' : '新增失败')
    } finally {
      submitting.value = false
    }
  })
}

onMounted(() => {
  fetchDetail()
})
</script>