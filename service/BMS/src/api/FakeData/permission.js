import Mock from 'mockjs';
import  i18n  from  '@/lang/vuei8n.js';
// 获取国际化函数
export function t(key) {
    return i18n.t(key);
}
export default {




    getMenu: config => {
        const {id, pwd} = JSON.parse(config.body)

//先判断用户是否存在
//判断账号和密码是否对应
//menuList用于后面做权限分配，也就是用户可以展示的菜单

            return {
                code: '00000',
                data: {
                    menuList: [
                        {
                            path: '/home',
                            name: 'home',
                            label: t('menu.home'), // 使用 $t() 国际化
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
                            label: t('menu.order.js'),
                            icon: 'Box',
                            url: 'Order',
                            labelkey:'menu.order.js',
                        },
                    ],

                    token: Mock.Random.guid(),
                    message: "登陆成功"
                },
            }
    }
}
