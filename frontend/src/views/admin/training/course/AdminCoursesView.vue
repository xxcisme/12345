<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminCourses, publishAdminCourse, unpublishAdminCourse, deleteAdminCourse } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { ElMessage } from 'element-plus'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange, loadData } = useTable(getAdminCourses, {
  courseName: '',
  courseCode: '',
  courseType: '',
  status: undefined,
  teacherId: undefined
})

const { handleDelete } = useConfirm(deleteAdminCourse, () => {
  handleCurrentChange(pageNo.value)
})

const handlePublish = async (id) => {
  await publishAdminCourse(id)
  ElMessage.success('发布成功')
  loadData()
}

const handleUnpublish = async (id) => {
  await unpublishAdminCourse(id)
  ElMessage.success('下架成功')
  loadData()
}
</script>