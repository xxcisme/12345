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
        <!-- 这里可以使用富文本编辑器，为简化先用textarea -->
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
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
        <el-button @click="router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const submitting = ref(false)

// 判断是新增还是编辑
const isEdit = route.params.id ? true : false

const form = reactive({
  type: 1,          // 默认新闻
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

// 模拟获取详情（编辑时）
const fetchDetail = () => {
  // 实际调用 GET /api/v1/news/{id}（或admin接口）
  // 模拟数据
  if (isEdit) {
    const mockDetail = {
      id: Number(route.params.id),
      type: 1,
      title: '示例新闻标题',
      origin: '宣传部',
      content: '这里是新闻内容，可以包含<strong>HTML</strong>标签',
      enclosure: 'https://example.com/file.pdf',
      publishTime: '2026-07-12 10:00:00'
    }
    Object.assign(form, mockDetail)
  }
}

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (!valid) return
    submitting.value = true
    // 构造请求数据
    const payload = { ...form }
    // 如果是编辑，添加id
    if (isEdit) payload.id = Number(route.params.id)
    // 实际调用 POST /api/v1/admin/news 或 PUT /api/v1/admin/news
    console.log('提交数据:', payload)
    setTimeout(() => {
      ElMessage.success(isEdit ? '修改成功' : '新增成功')
      submitting.value = false
      router.push('/admin/bulletin/news')
    }, 500)
  })
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped lang="scss">
// 表单样式由全局提供，这里无需额外样式
</style>