import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

export function useDetail(fetchApi, errorMsg = '加载详情失败', { autoLoad = true } = {}) {
    const route = useRoute()
    const detail = ref(null)
    const loading = ref(false)

    const loadDetail = async () => {
        loading.value = true
        try {
            const res = await fetchApi(route.params.id)
            detail.value = res.data
            return res.data
        } catch (error) {
            ElMessage.error(errorMsg)
        } finally {
            loading.value = false
        }
    }

    if (autoLoad) {
        onMounted(loadDetail)
    }

    return { detail, loading, loadDetail }
}