import service from '@/utils/request'

// ---------- 用户管理 ----------
export function getAdminUsers(params) {
    return service.get('/admin/users', { params })
}

export function getAdminUserDetail(id) {
    return service.get(`/admin/users/${id}`)
}

export function addAdminUser(data) {
    return service.post('/admin/users', data)
}

export function updateAdminUser(data) {
    return service.put('/admin/users', data)
}

export function toggleAdminUserStatus(id, data) {
    return service.put(`/admin/users/${id}/status`, data)
}

export function deleteAdminUser(id) {
    return service.delete(`/admin/users/${id}`)
}

// ---------- 日志管理 ----------
export function getAdminLogs(params) {
    return service.get('/admin/logs', { params })
}

// 导出日志（返回文件流）
export function exportAdminLogs(params) {
    return service.get('/admin/logs/export', { params, responseType: 'blob' })
}

// 清理日志（传 { daysToKeep }）
export function cleanAdminLogs(params) {
    return service.delete('/admin/logs/clean', { params })
}