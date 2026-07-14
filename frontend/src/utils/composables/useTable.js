import { reactive, toRefs } from 'vue'

export function useTable(fetchFn, defaultParams = {}) {
    const state = reactive({
        list: [],
        total: 0,
        loading: false,
        pageNo: 1,
        pageSize: 10,
        params: { ...defaultParams }
    })

    const loadData = async () => {
        state.loading = true
        try {
            const res = await fetchFn({
                pageNo: state.pageNo,
                pageSize: state.pageSize,
                ...state.params
            })
            state.list = res.data?.records || res.data || []
            state.total = res.data?.total || 0
        } catch (e) {
            // 错误已在拦截器处理
        } finally {
            state.loading = false
        }
    }

    const handleSizeChange = (size) => {
        state.pageSize = size
        state.pageNo = 1
        loadData()
    }

    const handleCurrentChange = (page) => {
        state.pageNo = page
        loadData()
    }

    const resetParams = () => {
        state.params = { ...defaultParams }
        state.pageNo = 1
        loadData()
    }

    // 初次加载
    loadData()

    return {
        ...toRefs(state),
        loadData,
        handleSizeChange,
        handleCurrentChange,
        resetParams
    }
}