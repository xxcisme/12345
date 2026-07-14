<script setup>
import { useTable } from '@/utils/composables/useTable'
import { getAdminResources, deleteAdminResource } from '@/api/admin/resource'
import { useConfirm } from '@/utils/composables/useConfirm'

const { list, total, loading, pageNo, pageSize, params, handleSizeChange, handleCurrentChange } = useTable(getAdminResources, {
  name: '',
  type: undefined,
  status: undefined,
  category: ''
})

const { handleDelete } = useConfirm(deleteAdminResource, () => {
  handleCurrentChange(pageNo.value)
})

const handleAudit = (id, status, auditRemark) => {
  // 打开审核弹窗，调用 auditAdminResource
  // 由页面内嵌弹窗处理，这里仅提供方法供模板调用
}
</script>