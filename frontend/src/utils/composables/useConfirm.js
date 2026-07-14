import { ElMessageBox, ElMessage } from 'element-plus'

export function useConfirm(deleteApi, onSuccess) {
    const handleDelete = (id, name = '') => {
        ElMessageBox.confirm(
            `确定删除${name ? `"${name}"` : ''}吗？此操作不可恢复！`,
            '警告',
            { type: 'warning' }
        ).then(async () => {
            try {
                await deleteApi(id)
                ElMessage.success('删除成功')
                onSuccess && onSuccess()
            } catch (e) {
                ElMessage.error('删除失败，请重试')
            }
        }).catch((action) => {
            if (action !== 'cancel' && action !== 'close') {
                ElMessage.error('删除失败，请重试')
            }
        })
    }
    return { handleDelete }
}