<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminGrades, addAdminGrade, updateAdminGrade, publishAdminGrade, getAdminGradeStatistics } from '@/api/admin/training'
import { usePublish } from '@/utils/composables/usePublish'
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminGrades, {
  courseId: undefined,
  classId: undefined,
  status: 0,
  studentName: ''
})

const stats = ref(null)

const loadStats = async () => {
  try {
    const res = await getAdminGradeStatistics({ courseId: params.value.courseId, classId: params.value.classId })
    stats.value = res.data
  } catch (error) {
  }
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

const { handlePublish } = usePublish(publishAdminGrade, null, loadData)
</script>