import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// // 开发环境下引入模拟数据用于测试
// // 开发环境下进行前后端连通测试时注释掉这部分
// if (import.meta.env.DEV) {
//   import('./mock')
// }

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')