import Mock from 'mockjs';

// 常量定义
const DEFAULT_PAGE = 1;
const DEFAULT_LIMIT = 10;
const TOTAL_COUNT = 50;
let cateList = [];

// 自增 ID 计数器
let catIdCounter = 1;

// 生成器配置
const CATEGORY_CONFIG = {
    maxLevel: 3,      // 最大层级深度
    levelDistribution: [5, 10, 30], // 各层级节点数占比（1级:5%, 2级:15%, 3级:30%）
    childRange: {     // 每个节点的子节点数量范围
        1: [3, 5],     // 1级分类子节点数
        2: [2, 4],     // 2级分类子节点数
        3: [0]         // 3级分类无子节点
    }
};

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

// 生成分类路径（示例：家用电器/大家电/空调）
function generateCategoryPath(level) {
    const segments = [];
    for (let i = 0; i < level; i++) {
        segments.push(Mock.Random.ctitle(2, 4) + Mock.Random.pick(['', '类', '产品']));
    }
    return segments.join('/');
}

// 生成分类树的核心逻辑
function generateCategoryTree() {
    const cateList = [];
    const idMap = new Map(); // 用于快速查找父节点

    // 生成各层级节点
    Array.from({ length: CATEGORY_CONFIG.maxLevel }).forEach((_, level) => {
        const currentLevel = level + 1;
        const count = Math.floor(TOTAL_COUNT * (CATEGORY_CONFIG.levelDistribution[level] / 50));

        Array.from({ length: count }).forEach(() => {
            const parentOptions = currentLevel === 1
                ? null
                : Array.from(idMap.values())
                    .filter(item => item.cat_level === currentLevel - 1);

            const node = Mock.mock({
                cat_id: catIdCounter++, // 修改为自增
                catname: generateCategoryPath(currentLevel),
                cat_pid: parentOptions?.length
                    ? Mock.Random.pick(parentOptions).cat_id
                    : 0,
                cat_level: currentLevel,
                child: []
            });

            // 维护父子关系
            if (node.cat_pid !== 0) {
                const parent = idMap.get(node.cat_pid);
                parent?.child.push(node);
            }

            idMap.set(node.cat_id, node);
            cateList.push(node);
        });
    });

    return cateList; // 返回所有节点
}



cateList=generateCategoryTree()



// 工具函数：分页逻辑
function paginate(list, page = DEFAULT_PAGE, limit = DEFAULT_LIMIT) {
    const start = (page - 1) * limit;
    const end = start + limit;
    return list.slice(start, end);
}


// 规格管理
// 规格组数据模型
let specGroups = Mock.mock({
    'list|50-100': [{
        'group_id|+1': 1,
        'group_name': '@ctitle(2,4)规格',
        'cat_id': () => Mock.Random.pick(cateList.map(c => c.cat_id)),
        'show_type|1': 1, // 1-文字
    }]
}).list;






export default {
    /**
     * 获取分类列表（新增 cat_pid 参数过滤）
     * @param {Object} config - 请求配置
     * @param {string} config.url - 请求 URL，包含查询参数
     * @returns {Object} - 返回分页后的分类列表
     */
    getcateList(config) {
        // 解析参数（新增 cat_pid 数字类型转换）
        const {
            catname,
            cat_pid,
            page = DEFAULT_PAGE,
            limit = DEFAULT_LIMIT
        } = parseUrlParams(config.url);

        // 组合过滤条件
        const filteredList = cateList.filter(cat => {
            let match = true;

            // 1. 匹配 catname（如果存在）
            if (catname) {
                match = match && cat.catname.includes(catname);
            }

            // 2. 匹配 cat_pid（如果存在参数）
            if (typeof cat_pid !== 'undefined') {
                match = match && (cat.cat_pid === Number(cat_pid));
            }

            return match;
        });

        // 执行分页
        const paginatedList = paginate(filteredList, page, limit);

        return {
            code: '00000',
            data: {
                list: paginatedList,
                count: filteredList.length,
                totalPage: Math.ceil(filteredList.length / limit),
                currentPage: page
            },
        };
    },

    /**
     * 删除分类
     * @param {Object} config - 请求配置
     * @param {string} config.url - 请求 URL，包含查询参数
     * @returns {Object} - 返回操作结果
     * （已支持数字 ID）
     */
    deleteCat(config) {
        const { cat_id } = parseUrlParams(config.url);
        // 类型安全判断
        if (typeof cat_id !== 'number') {
            return {
                code: -999,
                msg: '参数类型错误',
            };
        }
        cateList = cateList.filter(category => category.cat_id !== cat_id);
        return {
            code: '00000',
            msg: '删除成功',
        };
    },

    /**
     * 添加分类（使用自增 ID）
     * @param {Object} config - 请求配置
     * @param {string} config.body - 请求体，包含分类信息
     * @returns {Object} - 返回操作结果
     */
    addCategorie(config) {
        try {
            const { catname, cat_pid } = JSON.parse(config.body);
            cateList.unshift({
                cat_id: catIdCounter++, // 使用同一计数器
                catname,
                cat_pid: Number(cat_pid), // 确保类型一致
                cat_level: Number(cat_level),
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
     * 编辑分类
     * @param {Object} config - 请求配置
     * @param {string} config.body - 请求体，包含分类信息
     * @returns {Object} - 返回操作结果
     */
    editCategorie(config) {
        try {
            const { cat_id,catname, cat_pid, cat_level } = JSON.parse(config.body);
            const category = cateList.find(cate => cate.cat_id === cat_id);
            if (category) {
                category.catname = catname;
                category.cat_pid = cat_pid;
                category.cat_level = cat_level;
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


    // 获取规格组列表
    getSpecGroups(config) {
        const {
            group_id,
            cat_id,
            page = DEFAULT_PAGE,
            limit = DEFAULT_LIMIT } = parseUrlParams(config.url);
        const filtered=specGroups.filter(s => {
            let match = true;

            if (group_id) {
                match = match && s.group_id.includes(group_id);
            }


            if (typeof cat_id !== 'undefined') {
                match = match && (s.cat_id === Number(cat_id));
            }
            return match;
        });

        const paginatedList = paginate(filtered, page, limit);

        return { code: '00000',
                data: {
                list: paginatedList,
                count: filtered.length,
                totalPage: Math.ceil(filtered.length / limit),
                currentPage: page
            }, };
    },
    // 添加规格组
    addSpecGroup(config) {
        const newGroup = JSON.parse(config.body);
        specGroups.unshift({
            group_id: specGroups.length + 1,
            ...newGroup
        });
        return { code: '00000', msg: '添加成功' };
    }
};
