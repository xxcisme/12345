import { ElMessage } from 'element-plus'

export function usePublish(publishApi, unpublishApi, loadData) {
    const handlePublish = async (id) => {
        await publishApi(id)
        ElMessage.success('发布成功')
        loadData()
    }

    const handleUnpublish = async (id) => {
        if (unpublishApi) {
            await unpublishApi(id)
            ElMessage.success('下架成功')
            loadData()
        }
    }

    return { handlePublish, handleUnpublish }
}