import service from '@/utils/request'

// 新闻管理
export function getAdminNewsList(params) {
    return service.get('/admin/news', { params })
}

export function addAdminNews(data) {
    return service.post('/admin/news', data)
}

export function updateAdminNews(data) {
    return service.put('/admin/news', data)
}

export function deleteAdminNews(id) {
    return service.delete(`/admin/news/${id}`)
}

// 公告管理
export function getAdminNoticesList(params) {
    return service.get('/admin/notices', { params })
}

export function addAdminNotice(data) {
    return service.post('/admin/notices', data)
}

export function updateAdminNotice(data) {
    return service.put('/admin/notices', data)
}

export function deleteAdminNotice(id) {
    return service.delete(`/admin/notices/${id}`)
}