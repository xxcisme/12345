import service from '@/utils/request'

// 媒体资源
export function getResources(params) {
    return service.get('/resources', { params })
}

export function getResourceDetail(id) {
    return service.get(`/resources/${id}`)
}

export function scoreResource(id, data) {
    return service.post(`/resources/${id}/score`, data)
}

// 实验室
export function getLaboratories(params) {
    return service.get('/laboratories', { params })
}

export function getLaboratoryDetail(id) {
    return service.get(`/laboratories/${id}`)
}

// 设备
export function getDevices(params) {
    return service.get('/devices', { params })
}

export function getDeviceDetail(id) {
    return service.get(`/devices/${id}`)
}