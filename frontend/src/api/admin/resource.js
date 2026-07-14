import service from '@/utils/request'

// 媒体资源管理
export function getAdminResources(params) {
    return service.get('/admin/resources', { params })
}

export function addAdminResource(data) {
    return service.post('/admin/resources', data, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}

export function updateAdminResource(data) {
    return service.put('/admin/resources', data)
}

export function deleteAdminResource(id) {
    return service.delete(`/admin/resources/${id}`)
}

export function auditAdminResource(id, data) {
    return service.put(`/admin/resources/${id}/audit`, data)
}

// 实验室管理
export function addAdminLaboratory(data) {
    return service.post('/admin/laboratories', data)
}

export function updateAdminLaboratory(data) {
    return service.put('/admin/laboratories', data)
}

export function deleteAdminLaboratory(id) {
    return service.delete(`/admin/laboratories/${id}`)
}

// 设备管理
export function addAdminDevice(data) {
    return service.post('/admin/devices', data)
}

export function updateAdminDevice(data) {
    return service.put('/admin/devices', data)
}

export function deleteAdminDevice(id) {
    return service.delete(`/admin/devices/${id}`)
}

// 实验室申请审批
export function getAdminLabApplications(params) {
    return service.get('/admin/lab-applications', { params })
}

export function auditAdminLabApplication(id, data) {
    return service.put(`/admin/lab-applications/${id}/audit`, data)
}