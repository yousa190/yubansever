import './assets/styles/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import "@/api/mock.js"
import api from '@/api/api.js'

import App from '@/App.vue'
import router from '@/router'


const app = createApp(App)
const pinia = createPinia();
app.use(pinia)
app.use(router)


// 全局属性
app.config.globalProperties.$api = api;



app.mount('#app')
