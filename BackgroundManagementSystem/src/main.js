import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router/index.js'
import "@/assets/less/index.less"
import "@/api/mock.js"
import api from "@/api/api.js"
import {useAllDataStore} from "@/stores/counter.js";
import  i18n from '@/lang/vuei8n.js'

// 创建 Vue 应用
const app = createApp(App);

// 注册 Pinia 到应用
const pinia = createPinia();
app.use(pinia);

// 国际化
app.use(i18n);


// 使用 store
const store = useAllDataStore();

// 全局属性
app.config.globalProperties.$api = api;

// 添加动态路由
store.addMenu(router, "refresh");

// 路由守卫
// function isRouteExists(to) {
//     // 检查路由是否存在，可以通过 name 或 path
//     return router.getRoutes().filter((item=>item.path===to.path)).length > 0;
// }
//
// router.beforeEach((to, from) => {
//     const publicRoutes = ['/login', '/register']; // 定义公共路由
//     const isPublicRoute = publicRoutes.includes(to.path); // 判断当前路由是否为公共路由
//
//     // 如果用户未登录且访问的不是公共路由，则跳转到登录页
//     if (!isPublicRoute && !store.state.token ) {
//         return { name: 'login' };
//     }
//
// })

// 注册插件
app.use(router);
app.use(ElementPlus);

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

// 挂载应用到 DOM
app.mount('#app');