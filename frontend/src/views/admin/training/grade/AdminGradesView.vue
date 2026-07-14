<template>
  <page-container>
    <base-search>
      <template #left>
        <el-select v-model="searchForm.courseId" placeholder="课程" clearable style="width: 160px;">
          <el-option label="Python程序设计" :value="1" />
          <el-option label="模拟电路分析" :value="2" />
        </el-select>
        <el-select v-model="searchForm.classId" placeholder="班级" clearable style="width: 160px;">
          <el-option label="计算机1班" :value="1" />
          <el-option label="计算机2班" :value="2" />
        </el-select>
        <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 120px;">
          <el-option label="待评定" :value="0" />
          <el-option label="已发布" :value="1" />
        </el-select>
        <el-input v-model="searchForm.studentName" placeholder="学生姓名" clearable style="width: 130px;" />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </template>
      <template #right>
        <el-button type="success" @click="handleExport">导出成绩</el-button>
        <el-button type="primary" @click="handleStatistics">成绩统计</el-button>
      </template>
    </base-search>

    <el-table :data="tableData" border style="width:100%; margin-top:10px;" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="studentName" label="学生姓名" width="100" />
      <el-table-column prop="courseName" label="课程名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="experimentName" label="实验名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="score" label="成绩" width="90" align="center">
        <template #default="{ row }">
          <span :style="{ color: row.score >= 60 ? '#67c23a' : '#f56c6c', fontWeight: 'bold' }">{{ row.score }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="comment" label="评语" min-width="150" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">待评定</el-tag>
          <el-tag v-else type="success">已发布</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleGrade(row)" v-if="row.status === 0">评定</el-button>
          <el-button link type="warning" size="small" @click="handleModify(row)" v-else>修改</el-button>
          <el-button link type="success" size="small" @click="handlePublish(row)" v-if="row.status === 0">发布</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 15px; justify-content: flex-end;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 评定/修改弹窗 -->
    <el-dialog v-model="gradeDialogVisible" :title="gradeDialogTitle" width="500px">
      <el-form :model="gradeForm" :rules="gradeRules" ref="gradeFormRef" label-width="100px">
        <el-form-item label="学生">
          <span>{{ gradeForm.studentName }}</span>
        </el-form-item>
        <el-form-item label="实验">
          <span>{{ gradeForm.experimentName }}</span>
        </el-form-item>
        <el-form-item label="成绩" prop="score">
          <el-input-number v-model="gradeForm.score" :min="0" :max="100" :precision="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="评语">
          <el-input v-model="gradeForm.comment" type="textarea" :rows="3" placeholder="请输入评语" />
        </el-form-item>
        <el-form-item label="修改原因" prop="modifyReason" v-if="isModify">
          <el-input v-model="gradeForm.modifyReason" type="textarea" :rows="2" placeholder="请输入修改原因（必填）" />
        </el-form-item>
        <el-form-item label="立即发布">
          <el-switch v-model="gradeForm.publish" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitGrade">确定</el-button>
      </template>
    </el-dialog>

    <!-- 成绩统计弹窗 -->
    <el-dialog v-model="statisticsVisible" title="成绩统计" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="总人数">{{ statistics.total }}</el-descriptions-item>
        <el-descriptions-item label="平均分">{{ statistics.average }}</el-descriptions-item>
        <el-descriptions-item label="最高分">{{ statistics.max }}</el-descriptions-item>
        <el-descriptions-item label="最低分">{{ statistics.min }}</el-descriptions-item>
        <el-descriptions-item label="及格率">{{ statistics.passRate }}</el-descriptions-item>
        <el-descriptions-item label="优秀率">{{ statistics.excellentRate }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="statisticsVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </page-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer/index.vue'
import BaseSearch from '@/components/BaseSearch/index.vue'

const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  courseId: null,
  classId: null,
  status: null,
  studentName: ''
})

const tableData = ref([])

const gradeDialogVisible = ref(false)
const gradeDialogTitle = ref('')
const isModify = ref(false)
const gradeFormRef = ref(null)
const gradeForm = reactive({
  id: null,
  studentName: '',
  experimentName: '',
  scheduleId: null,
  studentId: null,
  score: 0,
  comment: '',
  modifyReason: '',
  publish: false
})

const gradeRules = {
  score: [{ required: true, message: '请输入成绩', trigger: 'change' }],
  modifyReason: [{ required: true, message: '请输入修改原因', trigger: 'blur' }]
}

const statisticsVisible = ref(false)
const statistics = reactive({
  total: 45,
  average: 78.5,
  max: 98,
  min: 45,
  passRate: '82.2%',
  excellentRate: '24.4%'
})

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      { id: 1, studentName: '张三', courseName: 'Python程序设计', experimentName: 'Python基础语法实验', score: 85, comment: '代码规范，逻辑清晰', status: 1 },
      { id: 2, studentName: '李四', courseName: 'Python程序设计', experimentName: 'Python基础语法实验', score: 72, comment: '基本完成任务', status: 1 },
      { id: 3, studentName: '王五', courseName: '模拟电路分析', experimentName: '单管放大电路实验', score: null, comment: '', status: 0 },
      { id: 4, studentName: '赵六', courseName: '模拟电路分析', experimentName: '单管放大电路实验', score: null, comment: '', status: 0 },
      { id: 5, studentName: '孙七', courseName: 'Python程序设计', experimentName: '数据结构实验', score: 91, comment: '算法实现优秀', status: 0 }
    ]
    total.value = 5
    loading.value = false
  }, 500)
}

const handleSearch = () => { pageNo.value = 1; fetchData() }
const resetSearch = () => {
  Object.assign(searchForm, { courseId: null, classId: null, status: null, studentName: '' })
  handleSearch()
}

const handleGrade = (row) => {
  isModify.value = false
  gradeDialogTitle.value = '成绩评定'
  Object.assign(gradeForm, {
    id: null,
    studentName: row.studentName,
    experimentName: row.experimentName,
    scheduleId: row.id,
    studentId: row.id,
    score: 0,
    comment: '',
    modifyReason: '',
    publish: false
  })
  gradeDialogVisible.value = true
}

const handleModify = (row) => {
  isModify.value = true
  gradeDialogTitle.value = '修改成绩'
  Object.assign(gradeForm, {
    id: row.id,
    studentName: row.studentName,
    experimentName: row.experimentName,
    score: row.score,
    comment: row.comment,
    modifyReason: '',
    publish: false
  })
  gradeDialogVisible.value = true
}

const submitGrade = () => {
  gradeFormRef.value.validate((valid) => {
    if (valid) {
      if (isModify.value && !gradeForm.modifyReason) {
        ElMessage.warning('修改成绩必须填写修改原因')
        return
      }
      ElMessage.success(isModify.value ? '修改成功' : '评定成功')
      gradeDialogVisible.value = false
      fetchData()
    }
  })
}

const handlePublish = (row) => {
  ElMessageBox.confirm(`确定发布 ${row.studentName} 的成绩吗？`, '提示', { type: 'warning' }).then(() => {
    ElMessage.success('成绩发布成功')
    fetchData()
  }).catch(() => {})
}

const handleExport = () => {
  ElMessage.success('正在导出成绩报表...')
}

const handleStatistics = () => {
  statisticsVisible.value = true
}

const handleSizeChange = (val) => { pageSize.value = val; fetchData() }
const handleCurrentChange = (val) => { pageNo.value = val; fetchData() }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
</style>