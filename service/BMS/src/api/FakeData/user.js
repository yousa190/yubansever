import Mock from 'mockjs';

// 常量定义
const DEFAULT_PAGE = 1;
const DEFAULT_LIMIT = 10;
const TOTAL_COUNT = 200;

// 工具函数：解析 URL 参数
function parseUrlParams(url) {
    if (typeof url !== 'string' || !url.includes('?')) return {};
    const queryIndex = url.indexOf('?');
    const hashIndex = url.indexOf('#', queryIndex + 1);
    const queryString = hashIndex === -1
        ? url.slice(queryIndex + 1)
        : url.slice(queryIndex + 1, hashIndex);
    const searchParams = new URLSearchParams(queryString);
    const params = {};
    for (const [key, value] of searchParams.entries()) {
        // 如需处理重复键，取消以下注释
        // if (key in params) {
        //     params[key] = Array.isArray(params[key]) ? [...params[key], value] : [params[key], value];
        // } else {
        //     params[key] = value;
        // }
        params[key] = value; // 默认行为：后出现的同名参数覆盖之前的
    }
    return params;
}

// 工具函数：分页逻辑
function paginate(list, page = DEFAULT_PAGE, limit = DEFAULT_LIMIT) {
    const start = (page - 1) * limit;
    const end = start + limit;
    return list.slice(start, end);
}

// 模拟用户数据
let userList = [];
for (let i = 0; i < TOTAL_COUNT; i++) {
    userList.push(
        Mock.mock({
            user_id: Mock.mock('@integer(1, 10000)'),
            username:  Mock.mock('@cname'),
            sex: Mock.Random.integer(0, 1),
            email: Mock.mock('@email'),
            avatar:'http://localhost:8080/images/users/defaultAvatar.jpg',
            last_login: Mock.Random.date(),
            created_at: Mock.Random.date(),
            level:Mock.Random.integer(1,2,3)
        })
    );
}

export default {
    /**
     * 获取用户列表
     * @param {Object} config - 请求配置
     * @param {string} config.url - 请求 URL，包含查询参数
     * @returns {Object} - 返回分页后的用户列表
     */
    getuserList(config) {
        const { username, page = DEFAULT_PAGE, limit = DEFAULT_LIMIT } = parseUrlParams(config.url);

        // 过滤用户列表
        const filteredList = username
            ? userList.filter(user => user.username.includes(username))
            : userList;
        // 分页
        const paginatedList = paginate(filteredList, page, limit);

        return {
            code: '00000',
            data: {
                list: paginatedList,
                count: filteredList.length, // 数据总条数
            },
        };
    },

    /**
     * 删除用户
     * @param {Object} config - 请求配置
     * @param {string} config.url - 请求 URL，包含查询参数
     * @returns {Object} - 返回操作结果
     */
    deleteUser(config) {
        const { user_id } = parseUrlParams(config.url);
        const userId = parseInt(user_id, 10);
        if (!user_id) {
            return {
                code: -999,
                msg: '参数错误',
            };
        }

        userList = userList.filter(user => user.user_id !== userId);
        return {
            code: '00000',
            msg: '删除成功',
        };
    },

    /**
     * 添加用户
     * @param {Object} config - 请求配置
     * @param {string} config.body - 请求体，包含用户信息
     * @returns {Object} - 返回操作结果
     */
    addUser(config) {
        try {
            const { username, email, avatar, level, sex } = JSON.parse(config.body);
            userList.unshift({
                user_id: Mock.mock('@integer(1, 10000)'),
                username,
                email,
                avatar,
                level,
                sex,
            });
            return {
                code: '00000',
                data: {
                    code: '00000',
                    message: '添加成功',
                },
            };
        } catch (error) {
            return {
                code: -999,
                msg: '参数解析错误',
            };
        }
    },

    /**
     * 编辑用户
     * @param {Object} config - 请求配置
     * @param {string} config.body - 请求体，包含用户信息
     * @returns {Object} - 返回操作结果
     */
    editUser(config) {
        try {
            const { user_id,username, email, avatar, level, sex } = JSON.parse(config.body);
            const user = userList.find(user => user.user_id === user_id);
            if (user) {
                user.username = username;
                user.email = email;
                user.avatar = avatar;
                user.level = level;
                user.sex = parseInt(sex);
            }
            return {
                code: '00000',
                data: {
                    code: '00000',
                    message: '编辑成功',
                },
            };
        } catch (error) {
            return {
                code: -999,
                msg: '参数解析错误',
            };
        }
    },
};
