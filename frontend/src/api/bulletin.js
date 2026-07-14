import service from '@/utils/request'

export function getNewsList(params) {
    return service.get('/news', { params })
}

export function getNewsDetail(id) {
    return service.get(`/news/${id}`)
}

export function getNoticesList(params) {
    return service.get('/notices', { params })
}

export function getNoticeDetail(id) {
    return service.get(`/notices/${id}`)
}