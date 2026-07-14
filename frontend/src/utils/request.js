import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { getToken, clearAuth } from './local_storage'

const base = import.meta.env.VITE_API_BASE_URL || ''
const version = import.meta.env.VITE_API_VERSION || '/api'
const baseURL = `${base}${version}`.replace(/\/+$/, '')

const service = axios.create({
    baseURL,
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
service.interceptors.request.use(
    config => {
        const token = getToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => Promise.reject(error)
)

// 响应拦截器
service.interceptors.response.use(
    response => {
        // 文件流（blob）直接返回完整响应对象
        if (response.config.responseType === 'blob') {
            return response
        }

        const res = response.data
        const code = Number(res.code)
        // 成功
        if (code === 200) {
            return res
        }
        // 业务错误统一提示
        ElMessage.error(res.msg || '请求失败')

        switch (code) {
            case 401:
                clearAuth()
                router.push({
                    name: '登录',
                    query: { redirect: router.currentRoute.value.fullPath }
                })
                break;
            case 403:
                if (window.history.length > 1) {
                    router.go(-1)
                } else {
                    router.push({ name: '首页' })
                }
                break;
            default:
                break;
        }

        return Promise.reject(new Error(res.msg || 'Error'))
    },
    error => {
        // 网络错误或超时
        ElMessage.error(error.message || '网络异常，请稍后重试')
        return Promise.reject(error)
    }
)

export default service