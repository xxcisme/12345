import { ROLE_MAP } from './constants'
import { getUser } from './local_storage'

export function getCurrentUser() {
    return getUser()
}

export function getCurrentUserRole() {
    const user = getCurrentUser()
    if (!user) return null
    return ROLE_MAP[user.role] || null
}

export function hasRole(...roles) {
    const userRole = getCurrentUserRole()
    if (!userRole) return false
    return roles.includes(userRole)
}

export function isAdmin() {
    return hasRole('admin')
}

export function isTeacher() {
    return hasRole('teacher')
}

export function isStudent() {
    return hasRole('student')
}

export function hasAnyRole(...roles) {
    return hasRole(...roles)
}

export function hasAllRoles(...roles) {
    const userRole = getCurrentUserRole()
    if (!userRole) return false
    return roles.every(role => role === userRole)
}

export function isLoggedIn() {
    return !!getCurrentUser()
}

export function isGuest() {
    return !isLoggedIn()
}

export function checkPermission(meta) {
    if (!meta) return true

    if (meta.requiresAuth && !isLoggedIn()) {
        return false
    }

    if (meta.roles && meta.roles.length > 0) {
        return hasAnyRole(...meta.roles)
    }

    return true
}