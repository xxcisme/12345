const PHONE_PATTERN = /^1[3-9]\d{9}$/

const EMAIL_PATTERN = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

const PASSWORD_PATTERNS = {
    hasLower: /[a-z]/,
    hasUpper: /[A-Z]/,
    hasDigit: /\d/,
    hasSpecial: /[!@#$%^&*(),.?":{}|<>]/
}

export function isValidPhone(phone) {
    return PHONE_PATTERN.test(phone)
}

export function isValidEmail(email) {
    return EMAIL_PATTERN.test(email)
}

export function getPasswordStrength(password) {
    if (!password || password.length < 6) {
        return { level: 0, label: '弱', color: '#F56C6C' }
    }

    let score = 0
    if (password.length >= 8) score++
    if (password.length >= 12) score++
    if (PASSWORD_PATTERNS.hasLower.test(password)) score++
    if (PASSWORD_PATTERNS.hasUpper.test(password)) score++
    if (PASSWORD_PATTERNS.hasDigit.test(password)) score++
    if (PASSWORD_PATTERNS.hasSpecial.test(password)) score++

    if (score <= 2) {
        return { level: 1, label: '弱', color: '#F56C6C' }
    } else if (score <= 4) {
        return { level: 2, label: '中', color: '#E6A23C' }
    } else {
        return { level: 3, label: '强', color: '#67C23A' }
    }
}

export function isValidPassword(password) {
    if (!password || password.length < 6 || password.length > 20) {
        return false
    }
    return getPasswordStrength(password).level >= 2
}

export const phoneRule = (trigger = 'blur') => ({
    required: true,
    validator: (_rule, value, callback) => {
        if (!value) {
            callback(new Error('请输入手机号'))
        } else if (!isValidPhone(value)) {
            callback(new Error('请输入正确的手机号'))
        } else {
            callback()
        }
    },
    trigger
})

export const passwordRule = (trigger = 'blur') => ({
    required: true,
    min: 6,
    max: 20,
    validator: (_rule, value, callback) => {
        if (!value) {
            callback(new Error('请输入密码'))
        } else if (value.length < 6) {
            callback(new Error('密码长度不能少于6位'))
        } else if (value.length > 20) {
            callback(new Error('密码长度不能超过20位'))
        } else if (!isValidPassword(value)) {
            callback(new Error('密码需包含大小写字母和数字'))
        } else {
            callback()
        }
    },
    trigger
})

export const emailRule = (trigger = 'blur') => ({
    validator: (_rule, value, callback) => {
        if (value && !isValidEmail(value)) {
            callback(new Error('请输入正确的邮箱地址'))
        } else {
            callback()
        }
    },
    trigger
})

export const requiredRule = (label, trigger = 'blur') => ({
    required: true,
    message: `请输入${label}`,
    trigger
})

export const confirmPasswordRule = (formRef, trigger = 'blur') => ({
    required: true,
    validator: (_rule, value, callback) => {
        if (!value) {
            callback(new Error('请再次输入密码'))
        } else if (value !== formRef.value.password) {
            callback(new Error('两次输入密码不一致'))
        } else {
            callback()
        }
    },
    trigger
})