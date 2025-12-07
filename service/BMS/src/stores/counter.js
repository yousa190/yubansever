import {ref, computed, watch, getCurrentInstance} from 'vue';
import { defineStore } from 'pinia';
import { useRoute, useRouter } from 'vue-router';
import router from "@/router/index.js";
import i18n from "@/lang/vuei8n.js";
import {ElMessage} from "element-plus";

// pinia init
function getInitialMenuList(Language) {
    return [
        {
            path: '/home',
            name: 'home',
            label: t('menu.home'),
            icon: 'house',
            url: 'Home',
            labelkey:'menu.home'
        },
        {
            path: '/user',
            name: 'user',
            label: t('menu.user'),
            icon: 'user',
            url: 'User',
            labelkey:'menu.user'
        },
        {
            path: '/mall',
            name: 'mall',
            label: t('menu.mall'),
            icon: 'Handbag',
            labelkey:'menu.mall',
            children: [
                {
                    path: 'list',
                    name: 'list',
                    label: t('menu.malllist'),
                    icon: 'ShoppingBag',
                    url: 'Mall',
                    labelkey:'menu.malllist'
                },
                {
                    path: 'config',
                    name: 'config',
                    label: t('menu.mallconfig'),
                    icon: 'Operation',
                    url: 'Mallconf',
                    labelkey:'menu.mallconfig'
                },
                {
                    path: 'categories',
                    name: 'categories',
                    label: t('menu.mallcategories'),
                    icon: 'Filter',
                    url: 'Categories',
                    labelkey:'menu.mallcategories'
                },
            ]
        },
        {
            path: '/rights',
            name: 'rights',
            label: t('menu.rights'),
            icon: 'List',
            labelkey:'menu.rights',
            children: [
                {
                    path: 'rolelist',
                    name: 'rolelist',
                    label: t('menu.rightsrolelist'),
                    icon: 'Files',
                    url: 'Rolelist',
                    labelkey:'menu.rightsrolelist',
                },
                {
                    path: 'rightssetting',
                    name: 'rightssetting',
                    label: t('menu.rightsrightssetting'),
                    icon: 'Lock',
                    url: 'Rights',
                    labelkey:'menu.rightsrightssetting',
                },
            ]
        },
        {
            path: '/order.js',
            name: 'order',
            label: t('menu.order'),
            icon: 'Box',
            url: 'Order',
            labelkey:'menu.order',
        },
        {
            path: '/system',
            name: 'system',
            label: t('menu.system'),
            icon: 'Setting',
            labelkey:'menu.system',
            children: [
                {
                    path: 'banner',
                    name: 'banner',
                    label: t('menu.systembanner'),
                    icon: 'Picture',
                    url: 'Banner',
                    labelkey:'menu.systembanner',
                },
                {
                    path: 'advertisement',
                    name: 'advertisement',
                    label: t('menu.systemadvertisement'),
                    icon: 'Document',
                    url: 'Advertisement',
                    labelkey:'menu.systemadvertisement',
                },
                {
                    path: 'review',
                    name: 'review',
                    label: t('menu.systemreview'),
                    icon: 'ChatLineRound',
                    url: 'Review',
                    labelkey:'menu.systemreview',
                }
            ]
        },
    ];
}


function initstate() {
    const storedLanguage = localStorage.getItem('lang') || 'zh';
    i18n.global.locale.value = storedLanguage;
    return {
        isCollapse: false,
        tags: [
            {
                path: '/home',
                name: 'home',
                label: t('menu.home'),
                icon: 'home',
                labelkey:'menu.home'
            },
        ],
        currentMenu: null,
        //    menuList中的item       {
        //                             path: '/home',
        //                             name: 'home',
        //                             label: t('menu.home'), // 使用 $t() 国际化
        //                             icon: 'house',
        //                             url: 'Home'
        //                             labelkey:'home'
        //                         },
        menuList: getInitialMenuList(storedLanguage),
        token: '',
        routerList: [],
        language: storedLanguage,
    };
}

function t(key){
    return i18n.t(key)
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
                // 恢复时重新计算label
                const recoverLabels = (items) => {

                    return items.map(item => {
                        const newItem = {
                            ...item,
                            label: t(item.labelkey)
                        };

                        if (item.children) {
                            newItem.children = recoverLabels(item.children);
                        }

                        return newItem;
                    })
                };

                state.value = {
                    ...storedState,
                    menuList: recoverLabels(storedState.menuList),
                    tags: recoverLabels(storedState.tags)
                };
            }
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
                    if (modules[url]) {
                        const routeConfig = {
                            path: item.path,
                            name: item.name,
                            component: modules[url],
                            label: t(item.labelkey), // 使用翻译键
                            icon: item.icon
                        };

                        return [routeConfig];
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

    function clean() {
        state.value.routerList.forEach((route) => {
            if (route) router.removeRoute(route.name);
        });
        state.value = initstate();
        localStorage.removeItem('token');
    }

    /**
     * 切换语言  更新动态路由,动态更新menulist,routerList,
     * @param {Object} command(传入示例'zh') -
     */
    function selectLanguage(command) {
        // 1. 设置 i18n 语言
        i18n.global.locale.value=command
        // 2. 更新本地存储
        localStorage.setItem('lang', command);
        state.value.language = command;

        // 3. 更新所有标签的label
        const updateLabels = (items) => {
            return items.map(item => {
                const newItem = {
                    ...item,
                    label: t(item.labelkey) // 重新计算label
                };
                if (newItem.children) {
                    newItem.children = updateLabels(newItem.children);
                }
                return newItem;
            });
        };

        // 更新菜单列表
        state.value.menuList = updateLabels(state.value.menuList);
        // 更新标签页
        state.value.tags = updateLabels(state.value.tags);

        // 4. 重新添加动态路由
        addMenu(router,'lang-change');
        // 5. 刷新当前页面,或者跳转到首页
        ElMessage.success(`语言已切换为${command}`);
    }



    // 进行本地存储
    watch(
        () => state.value,
        (newState) => {
            // 存储时清除动态计算的label
            const storageState = {
                ...newState,
                menuList: newState.menuList.map(item => ({
                    ...item,
                    label: undefined // 不存储动态label
                })),
                tags: newState.tags.map(tag => ({
                    ...tag,
                    label: undefined
                }))
            };
            localStorage.setItem('store', JSON.stringify(storageState));
        },
        { deep: true }
    );




    return {
        state,
        selectMenu,
        updateMenu,
        updateMenuList,
        addMenu,
        clean,
        selectLanguage,
    };
});