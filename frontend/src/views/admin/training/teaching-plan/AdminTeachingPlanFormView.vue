<template>
  <page-container>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" style="max-width:900px; margin:0 auto;">
      <el-form-item label="计划名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入教学计划名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="学期">
        <el-input v-model="form.semester" placeholder="如：2026-2027-1" />
      </el-form-item>
      <el-form-item label="课程" prop="courseId">
        <el-select v-model="form.courseId" placeholder="请选择课程" style="width: 100%;">
          <el-option label="Python程序设计" :value="1" />
          <el-option label="模拟电路分析" :value="2" />
          <el-option label="机械制图基础" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="教师" prop="teacherId">
        <el-select v-model="form.teacherId" placeholder="请选择教师" style="width: 100%;">
          <el-option label="张老师" :value="1" />
          <el-option label="李老师" :value="2" />
          <el-option label="王老师" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="实验安排" prop="experimentIds">
        <div v-for="(item, index) in scheduleList" :key="index" style="display:flex; gap:10px; margin-bottom:10px; align-items:center;">
          <el-select v-model="item.experimentId" placeholder="选择实验" style="flex:1;">
            <el-option label="Python基础语法实验" :value="1" />
            <el-option label="数据结构实验" :value="2" />
            <el-option label="面向对象编程实验" :value="3" />
          </el-select>
          <el-date-picker v-model="item.scheduleDate" type="date" placeholder="上课日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="flex:1;" />
          <el-button type="danger" circle @click="removeSchedule(index)" v-if="scheduleList.length > 1">
            <el-icon><delete /></el-icon>
          </el-button>
        </div>
        <el-button type="primary" plain @click="addSchedule" style="margin-top:5px;">+ 添加实验</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">{{ isEdit ? '保存修改' : '立即创建' }}</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer/index.vue'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  semester: '',
  courseId: null,
  teacherId: null,
  experimentIds: [],
  scheduleDates: []
})

const scheduleList = ref([
  { experimentId: null, scheduleDate: '' }
])

const rules = {
  name: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
  teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
  experimentIds: [{
    validator: (rule, value, callback) => {
      const valid = scheduleList.value.every(item => item.experimentId && item.scheduleDate)
      if (!valid || scheduleList.value.length === 0) {
        callback(new Error('请完整填写所有实验安排'))
      } else {
        callback()
      }
    },
    trigger: 'change'
  }]
}

const addSchedule = () => {
  scheduleList.value.push({ experimentId: null, scheduleDate: '' })
}

const removeSchedule = (index) => {
  scheduleList.value.splice(index, 1)
}

const handleSubmit = () => {
  form.experimentIds = scheduleList.value.map(item => item.experimentId)
  form.scheduleDates = scheduleList.value.map(item => item.scheduleDate)
  formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
      router.push('/admin/training/teaching-plans')
    }
  })
}

const handleCancel = () => {
  router.push('/admin/training/teaching-plans')
}

onMounted(() => {
  if (isEdit.value) {
    setTimeout(() => {
      Object.assign(form, {
        id: route.params.id,
        name: '2026秋季Python教学计划',
        semester: '2026-2027-1',
        courseId: 1,
        teacherId: 1
      })
      scheduleList.value = [
        { experimentId: 1, scheduleDate: '2026-09-15' },
        { experimentId: 2, scheduleDate: '2026-09-22' },
        { experimentId: 3, scheduleDate: '2026-10-06' }
      ]
    }, 300)
  }
})
</script>

<style scoped lang="scss">
</style>