import { ref, computed, watch } from 'vue';
import { defineStore } from 'pinia';
import { useRoute, useRouter } from 'vue-router';
import router from "@/router/index.js";

// pinia init
function initstate() {
    return {
        isCollapse: false,
        tags: [
            {
                path: '/home',
                name: 'home',
                label: '首页',
                icon: 'home',
            },
        ],
        currentMenu: null,
        menuList: [],
        token: '',
        routerList: [],
    };
}

export const useAllDataStore = defineStore('allData', () => {
    const state = ref(initstate());

    // 菜单栏管理
    function selectMenu(val) {
        if (val.name === 'home') {
            state.value.currentMenu = null;
        } else {
            state.value.currentMenu = val;
            let index = state.value.tags.findIndex((item) => item.name === val.name);
            if (index === -1) state.value.tags.push(val);
        }
    }

    function updateMenu(tag) {
        let index = state.value.tags.findIndex((item) => item.name === tag.name);
        state.value.tags.splice(index, 1);
    }

    function updateMenuList(val) {
        state.value.menuList = val;
    }

    /**
     * 动态加载菜单并添加到路由
     * @param {Object} router - Vue Router 实例
     */
    function addMenu(router, type) {
        if (type === 'refresh') {
            const storedState = JSON.parse(localStorage.getItem('store'));
            if (storedState) {
                state.value = storedState;
                state.value.routerList = [];
            } else return;
        }

        const menu = state.value.menuList; // 菜单列表
        const modules = import.meta.glob('../views/**/*.vue'); // 自动导入的视图组件

        /**
         * 递归处理菜单项，生成路由配置
         * @param {Array} menuList - 菜单列表
         * @returns {Array} - 生成的路由数组
         */
        const generateRoutes = (menuList) => {
            return menuList.flatMap((item) => {
                if (item.children && item.children.length > 0) {
                    return generateRoutes(item.children);
                } else {
                    // 规范化 URL
                    const normalizeUrl = (url) => {
                        return url.replace(/^\/+/, '').replace(/\/+$/, ''); // 去除开头和结尾的斜杠
                    };
                    const url = `../views/${normalizeUrl(item.url)}.vue`;

                    // 调试信息
                    if (modules[url]) {
                        item.component = modules[url];
                        return [item];
                    } else {
                        console.warn(`未找到组件文件：${url}`);
                        return [];
                    }
                }
            });
        };

        // 清除之前的动态路由
        clearDynamicRoutes(router);

        // 递归生成路由
        const routeArr = generateRoutes(menu);

        // 添加路由到 Vue Router
        routeArr.forEach((route) => {
            router.addRoute('main', route);
        });

        // 同步到状态管理的路由列表
        state.value.routerList = routeArr;
    }

    /**
     * 清除动态路由
     * @param {Object} router - Vue Router 实例
     */
    function clearDynamicRoutes(router) {
        router.getRoutes().forEach((item) => {
            if (!['main', 'login', '404', 'register'].includes(item.name)) {
                router.removeRoute(item.name);
            }
        });
    }

    // 进行本地存储
    watch(
        () => state.value.token,
        (newToken) => {
            if (newToken) {
                localStorage.setItem('store', JSON.stringify(state.value));
            }
        }
    );

    function clean() {
        state.value.routerList.forEach((route) => {
            if (route) router.removeRoute(route.name);
        });
        state.value = initstate();
        localStorage.removeItem('token');
    }

    return {
        state,
        selectMenu,
        updateMenu,
        updateMenuList,
        addMenu,
        clean,
    };
});