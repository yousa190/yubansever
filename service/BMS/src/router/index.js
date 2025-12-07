import { createRouter, createWebHistory } from 'vue-router';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // 使用 Web History 模式
  routes: [
    {
      path: '/',
      name: 'main',
      redirect:'login',
      component: () => import('@/views/main.vue'), // 懒加载主布局组件
      children: [
        // {
        //   path: 'home',
        //   name: 'home',
        //   component: () => import('@/views/Home.vue'),
        // },
        // {
        //   path: 'user',
        //   name: 'user',
        //   component: () => import('@/views/User.vue'),
        // },
        // {
        //   path: 'mall',
        //   name: 'mall',
        //   component: () => import('@/views/Mall.vue'),
        // },
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login.vue'),
    },
    {
      path: '/404',
      name: '404',
      component: () => import('@/views/404.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue'),
    },
  ],
});

export default router;