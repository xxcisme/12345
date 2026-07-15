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
        console.log('请求拦截器 - URL:', config.url)
        console.log('请求拦截器 - token:', token ? '存在' : '不存在')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
            console.log('请求拦截器 - 已添加 Authorization header')
        } else {
            console.warn('请求拦截器 - 未找到 token，请求将不包含认证信息')
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

        // 根据后端错误码分别处理
        switch (code) {
            case 401:
                clearAuth()
                router.push({
                    name: '登录',
                    query: { redirect: router.currentRoute.value.fullPath }
                })
                break
            case 403:
                ElMessage.error(res.msg || '没有访问权限')
                if (window.history.length > 1) {
                    router.go(-1)
                } else {
                    router.push({ name: '首页' })
                }
                break
            case 400:
            case 404:
            case 405:
            case 423:
            case 429:
            case 500:
            case 501:
            case 999:
            default:
                ElMessage.error(res.msg || '请求失败')
                break
        }

        const err = new Error(res.msg || '请求失败')
        err.__handled = true
        return Promise.reject(err)
    },
    error => {
        // 业务错误已在响应拦截器中显示，此处避免重复提示
        if (error.__handled) {
            return Promise.reject(error)
        }
        // 网络错误或超时
        if (error.response) {
            const body = error.response.data
            ElMessage.error((body && body.msg) || error.message || '网络异常，请稍后重试')
        } else {
            ElMessage.error(error.message || '网络异常，请稍后重试')
        }
        return Promise.reject(error)
    }
)

export default service