import service from '@/utils/request'

// ---------- 师资管理 ----------
export function getAdminTeachers(params) {
    return service.get('/admin/teachers', { params })
}

export function getAdminTeacherDetail(id) {
    return service.get(`/admin/teachers/${id}`)
}

export function addAdminTeacher(data) {
    return service.post('/admin/teachers', data)
}

export function updateAdminTeacher(data) {
    return service.put('/admin/teachers', data)
}

export function deleteAdminTeacher(id) {
    return service.delete(`/admin/teachers/${id}`)
}

export function resetAdminTeacherPassword(id) {
    return service.put(`/admin/teachers/${id}/reset-password`)
}

// ---------- 课程管理 ----------
export function getAdminCourses(params) {
    return service.get('/admin/courses', { params })
}

export function addAdminCourse(data) {
    return service.post('/admin/courses', data)
}

export function updateAdminCourse(data) {
    return service.put('/admin/courses', data)
}

export function publishAdminCourse(id) {
    return service.put(`/admin/courses/${id}/publish`)
}

export function unpublishAdminCourse(id) {
    return service.put(`/admin/courses/${id}/unpublish`)
}

export function deleteAdminCourse(id) {
    return service.delete(`/admin/courses/${id}`)
}

// ---------- 实验管理 ----------
export function getAdminExperiments(params) {
    return service.get('/admin/experiments', { params })
}

export function addAdminExperiment(data) {
    return service.post('/admin/experiments', data)
}

export function updateAdminExperiment(data) {
    return service.put('/admin/experiments', data)
}

export function publishAdminExperiment(id) {
    return service.put(`/admin/experiments/${id}/publish`)
}

export function deleteAdminExperiment(id) {
    return service.delete(`/admin/experiments/${id}`)
}

// ---------- 教学计划管理 ----------
export function getAdminTeachingPlans(params) {
    return service.get('/admin/teaching-plans', { params })
}

export function addAdminTeachingPlan(data) {
    return service.post('/admin/teaching-plans', data)
}

export function updateAdminTeachingPlan(data) {
    return service.put('/admin/teaching-plans', data)
}

export function publishAdminTeachingPlan(id) {
    return service.put(`/admin/teaching-plans/${id}/publish`)
}

export function deleteAdminTeachingPlan(id) {
    return service.delete(`/admin/teaching-plans/${id}`)
}

// ---------- 成绩管理 ----------
export function getAdminGrades(params) {
    return service.get('/admin/grades', { params })
}

export function addAdminGrade(data) {
    return service.post('/admin/grades', data)
}

export function updateAdminGrade(data) {
    return service.put('/admin/grades', data)
}

export function publishAdminGrade(id) {
    return service.put(`/admin/grades/${id}/publish`)
}

export function getAdminGradeStatistics(params) {
    return service.get('/admin/grades/statistics', { params })
}