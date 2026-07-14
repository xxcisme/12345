import { ElMessageBox, ElMessage } from 'element-plus'

export function useConfirm(deleteApi, onSuccess) {
    const handleDelete = (id, name = '') => {
        ElMessageBox.confirm(
            `确定删除${name ? `“${name}”` : ''}吗？此操作不可恢复！`,
            '警告',
            { type: 'warning' }
        ).then(async () => {
            await deleteApi(id)
            ElMessage.success('删除成功')
            onSuccess && onSuccess()
        }).catch(() => { })
    }
    return { handleDelete }
}