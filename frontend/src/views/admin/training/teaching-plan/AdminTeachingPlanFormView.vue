<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { addAdminTeachingPlan, updateAdminTeachingPlan, getAdminTeachingPlans } from '@/api/admin/training'
import { useForm } from '@/utils/composables/useForm'
import { useDetail } from '@/utils/composables/useDetail'

const router = useRouter()
const isEdit = ref(!!router.currentRoute.value.params.id)

const { form, formRef, submitting, setFormData, submit } = useForm(addAdminTeachingPlan, updateAdminTeachingPlan, () => {
  router.push('/admin/training/teaching-plans')
})

const { loadDetail } = useDetail(
  (id) => getAdminTeachingPlans({ pageNo: 1, pageSize: 1, id }).then(res => ({
    data: res.data.records?.[0] || null
  })),
  '加载教学计划详情失败',
  { autoLoad: false }
)

if (isEdit.value) {
  loadDetail().then(data => {
    if (data) setFormData(data)
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑教学计划' : '新增教学计划' }}</h2>
    </div>

    <div class="form-card">
      <el-form ref="formRef" :model="form" label-width="100px" style="max-width: 640px">
        <el-form-item label="计划名称" required>
          <el-input v-model="form.name" placeholder="请输入教学计划名称" />
        </el-form-item>
        <el-form-item label="学期" required>
          <el-input v-model="form.semester" placeholder="请输入学期" />
        </el-form-item>
        <el-form-item label="计划简介">
          <el-input v-model="form.profile" type="textarea" :rows="4" placeholder="请输入教学计划简介" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
          <el-button @click="router.push('/admin/training/teaching-plans')">取消</el-button>
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