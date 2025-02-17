import Mock from 'mockjs';

// 常量定义
const DEFAULT_PAGE = 1;
const DEFAULT_LIMIT = 10;
const TOTAL_COUNT = 200;

// 工具函数：解析 URL 参数
function parseUrlParams(url) {
    if (!url || !url.includes('?')) return {};
    const searchParams = new URLSearchParams(url.split('?')[1]);
    const params = {};
    for (const [key, value] of searchParams.entries()) {
        params[key] = value;
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
            id: Mock.Random.guid(),
            name: Mock.Random.cname(),
            addr: Mock.mock('@county(true)'),
            'age|18-60': 1,
            birth: Mock.Random.date(),
            sex: Mock.Random.integer(0, 1),
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
        const { name, page = DEFAULT_PAGE, limit = DEFAULT_LIMIT } = parseUrlParams(config.url);

        // 过滤用户列表
        const filteredList = name
            ? userList.filter(user => user.name.includes(name))
            : userList;

        // 分页
        const paginatedList = paginate(filteredList, page, limit);

        return {
            code: 200,
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
        const { id } = parseUrlParams(config.url);
        if (!id) {
            return {
                code: -999,
                msg: '参数错误',
            };
        }

        userList = userList.filter(user => user.id !== id);
        return {
            code: 200,
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
            const { name, addr, age, birth, sex } = JSON.parse(config.body);
            userList.unshift({
                id: Mock.Random.guid(),
                name,
                addr,
                age,
                birth,
                sex,
            });
            return {
                code: 200,
                data: {
                    code: 200,
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
            const { id, name, addr, age, birth, sex } = JSON.parse(config.body);
            const user = userList.find(user => user.id === id);
            if (user) {
                user.name = name;
                user.addr = addr;
                user.age = age;
                user.birth = birth;
                user.sex = parseInt(sex);
            }
            return {
                code: 200,
                data: {
                    code: 200,
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