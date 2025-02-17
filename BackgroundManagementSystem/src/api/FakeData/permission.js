import Mock from 'mockjs';
export default {
    getMenu: config => {
        const {id, pwd} = JSON.parse(config.body)
//先判断用户是否存在
//判断账号和密码是否对应
//menuList用于后面做权限分配，也就是用户可以展示的菜单
        if (id === 'admin' && pwd === 'admin') {
            return {
                code: 200,
                data: {
                    menuList: [
                        {
                            path: '/home',
                            name: 'home',
                            label: "首页",
                            icon: 'house',
                            url: 'Home'
                        },
                        {
                            path: '/mall',
                            name: 'mall',
                            label: "商品管理",
                            icon: 'video-play',
                            url: 'Mall'
                        },
                        {
                            path: '/user',
                            name: 'user',
                            label: "用户管理",
                            icon: 'user',
                            url: 'User'
                        },
                        {
                            path: '/other',
                            name: 'other',
                            label: "其他",
                            icon: 'location',
                            children: [
                                {
                                    path: '/page1',
                                    name: 'page1',
                                    label: "第一页",
                                    icon: 'setting',
                                    url: 'Page1'
                                },
                                {
                                    path: '/page2',
                                    name: 'page2',
                                    label: "第二页",
                                    icon: 'setting',
                                    url: 'Page2'
                                },
                            ]
                        }],
                    token: Mock.Random.guid(),
                    message: "登陆成功"
                },
            }
        }
        else if (id === 'yuban' && pwd === 'yuban') {
            return {
                code: 200,
                data: {
                    menuList: [
                        {
                            path: '/home',
                            name: 'home',
                            label: "首页",
                            icon: 'house',
                            url: 'Home'
                        },
                        {
                            path: '/user',
                            name: 'user',
                            label: "用户管理",
                            icon: 'user',
                            url: 'User'
                        }
                        ],
                    token: Mock.Random.guid(),
                    message: "获取成功"
                },
            }
        }
        else{
            return {
                code:-999,
                data:{
                    message:"登陆错误"
                }
            }
        }
    }
}