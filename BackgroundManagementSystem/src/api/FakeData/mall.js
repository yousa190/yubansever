import Mock from 'mockjs';


// 常量定义
const DEFAULT_PAGE = 1;
const DEFAULT_LIMIT = 10;
const TOTAL_COUNT = 50;
let goodList = [];
let size=51
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


/**
 * 生成商品模拟数据
 * @param {number} count 生成数量，默认5条
 * @param {object} options 配置选项
 * @returns {Array} 商品数据列表
 */
function generateGoodsData(count = 5, options = {}) {
    // 类目层级配置
    const categories = {
        digital: ['一级类目/数码产品', '二级类目/智能穿戴', '三级类目/配件周边'],
        home: ['一级类目/家用电器', '二级类目/厨房电器', '三级类目/清洁家电'],
        life: ['一级类目/生活百货', '二级类目/个护健康', '三级类目/家居装饰']
    };

    // 核心数据模板
    const template = {
        'good_id|+1': 1,
        'goodname': () => {
            const brands = ['华为', '小米', '索尼', 'Apple', '联想', '戴森', '美的'];
            const types = ['Pro', 'Max', 'ES', 'Series', 'Luxury', 'Smart', '旗舰版'];
            return `${Mock.Random.pick(brands)}${Mock.Random.ctitle(2,4)}${Mock.Random.pick(types)}`;
        },
        'price|10-9999.2': 1,
        'num|0-1000': 1,
        'catname': () => {
            const categoryGroup = Mock.Random.pick(Object.values(categories));
            return Mock.Random.pick(categoryGroup);
        },
        'image': () => `https://picsum.photos/id/${Mock.Random.integer(1,1000)}/100/100`,
        'comment': () => {
            const features = [
                '全面屏设计，视觉效果震撼',
                '智能AI算法，使用更便捷',
                '超长续航，满足全天使用',
                '专业级性能配置',
                '环保材料，通过多项认证'
            ];
            return `${Mock.Random.ctitle(6,12)}，${Mock.Random.pick(features)}`;
        }
    };

    // 合并自定义配置
    if(options.fixedImageId) {
        template.image = `https://picsum.photos/id/${options.fixedImageId}/100/100`;
    }

    // 生成数据
    return Mock.mock({
        [`list|${count}`]: [template]
    }).list;
}

goodList=generateGoodsData(TOTAL_COUNT);




export default {
    /**
     * 获取商品列表
     * @param {Object} config - 请求配置
     * @param {string} config.url - 请求 URL，包含查询参数
     * @returns {Object} - 返回分页后的分类列表
     */
    getgoodList(config) {
        const { goodname, page = DEFAULT_PAGE, limit = DEFAULT_LIMIT } = parseUrlParams(config.url);

        // 过滤商品列表
        const filteredList = goodname
            ? goodList.filter(good => good.goodname.includes(goodname))
            : goodList;

        // 执行分页
        const paginatedList = paginate(filteredList, page, limit);

        return {
            code: 200,
            data: {
                list: paginatedList,
                count: filteredList.length, // 此处应该返回过滤后的总条数
                totalPage: Math.ceil(filteredList.length / limit), // 计算总页数
                currentPage: page // 记录当前页数
            },
        };
    },

    /**
     * 删除商品
     * @param {Object} config - 请求配置
     * @param {string} config.url - 请求 URL，包含查询参数
     * @returns {Object} - 返回操作结果
     */
    deletegood(config) {
        const { good_id } = parseUrlParams(config.url);
        const goodId = parseInt(good_id, 10);
        if (!good_id) {
            return {
                code: -999,
                msg: '参数错误',
            };
        }

        goodList = goodList.filter(good => good.good_id !== goodId);
        return {
            code: 200,
            msg: '删除成功',
        };
    },

    /**
     * 添加商品
     * @param {Object} config - 请求配置
     * @param {string} config.body - 请求体，包含用户信息
     * @returns {Object} - 返回操作结果
     */
    addgood(config) {
        try {
            const { goodname, price, num, catname, image,comment } = typeof config.body === 'string' ? JSON.parse(config.body) : config.body;;
            goodList.unshift({
                good_id: size++,
                goodname,
                price,
                num,
                catname,
                image,
                comment
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
     * 编辑商品
     * @param {Object} config - 请求配置
     * @param {string} config.body - 请求体，包含用户信息
     * @returns {Object} - 返回操作结果
     */
    editgood(config) {
        try {
            const {good_id,goodname, price, num, catname, image,comment } = JSON.parse(config.body);
            const good = goodList.find(good => good.good_id === good_id);
            if (good) {
                good.goodname = goodname;
                good.price = price;
                good.num = num;
                good.catname = catname;
                good.image =image;
                good.comment =comment;
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


}