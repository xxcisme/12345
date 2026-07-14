import {
	defineStore
} from 'pinia'
import {
	ref
} from 'vue'

export const useUserStore = defineStore('user', () => {
	const token = ref('mock-token-123456')
	const username = ref('管理员')
	const role = ref('admin') // 'admin' | 'teacher' | 'student' | 'social'
	const isLoggedIn = ref(true)

	const login = (userData) => {
		token.value = userData.token
		username.value = userData.username
		role.value = userData.role
		isLoggedIn.value = true
	}

	const logout = () => {
		token.value = ''
		username.value = ''
		role.value = ''
		isLoggedIn.value = false
	}

	return {
		token,
		username,
		role,
		isLoggedIn,
		login,
		logout
	}
})