<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminGrades, addAdminGrade, updateAdminGrade, publishAdminGrade, getAdminGradeStatistics } from '@/api/admin/training'
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminGrades, {
  courseId: undefined,
  classId: undefined,
  status: 0, // 默认待评定
  studentName: ''
})

const stats = ref(null)

const loadStats = async () => {
  const res = await getAdminGradeStatistics({ courseId: params.courseId, classId: params.classId })
  stats.value = res.data
}

const handleAddGrade = async (data) => {
  await addAdminGrade(data)
  ElMessage.success('评定成功')
  loadData()
}

const handleUpdateGrade = async (data) => {
  await updateAdminGrade(data)
  ElMessage.success('修改成功')
  loadData()
}

const handlePublish = async (id) => {
  await publishAdminGrade(id)
  ElMessage.success('发布成功')
  loadData()
}
</script>