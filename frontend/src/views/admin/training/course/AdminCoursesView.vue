<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminCourses, publishAdminCourse, unpublishAdminCourse, deleteAdminCourse } from '@/api/admin/training'
import { useConfirm } from '@/utils/composables/useConfirm'
import { usePublish } from '@/utils/composables/usePublish'

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

const { handlePublish, handleUnpublish } = usePublish(publishAdminCourse, unpublishAdminCourse, loadData)
</script>