import service from '@/utils/request'

// 个人信息
export function getProfile() {
    return service.get('/user/profile')
}

export function updateProfile(data) {
    return service.put('/user/profile', data)
}

export function changePassword(data) {
    return service.put('/user/password', data)
}

// 收藏
export function getFavorites(params) {
    return service.get('/user/favorites', { params })
}

export function addFavorite(data) {
    return service.post('/user/favorites', data)
}

export function cancelFavorite(resourceId) {
    return service.delete(`/user/favorites/${resourceId}`)
}

// 我的课程
export function getMyCourses(params) {
    return service.get('/user/courses', { params })
}

export function getMyCourseDetail(courseId) {
    return service.get(`/user/courses/${courseId}`)
}

// 我的实验
export function getMyExperiments(params) {
    return service.get('/user/experiments', { params })
}

export function getMyExperimentDetail(experimentId) {
    return service.get(`/user/experiments/${experimentId}`)
}

// 通知
export function getMessages(params) {
    return service.get('/user/messages', { params })
}

export function markMessagesRead(data) {
    return service.put('/user/messages/read', data)
}

// 实验室申请（用户）
export function submitLabApplication(data) {
    return service.post('/lab-applications', data)
}

export function getMyLabApplications(params) {
    return service.get('/lab-applications', { params })
}