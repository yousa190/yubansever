// order.mock.js
import Mock from 'mockjs';

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
// 常量定义
const DEFAULT_PAGE = 1;
const DEFAULT_LIMIT = 10;
const TOTAL_COUNT = 100;
let orderList = [];
let orderIdCounter = 1001;



// 生成初始订单数据
function generateOrdersData(count = TOTAL_COUNT) {
  return Mock.mock({
    [`list|${count}`]: [{
      'order_id|+1': orderIdCounter,
      'user_id|+1':1,
      'goods|1-5': [{
        'good_id|+1': 1,
        name: '@ctitle(6, 12)',
        'price|10-9999.2': 1,
        'num|1-5': 1,
        'total': function() {
          return this.price * this.quantity;
        }
      }],
      'order_total': function() {
        return this.goods.reduce((sum, item) => sum + item.total, 0);
      },
      'status|1': ['pending', 'paid', 'shipped', 'completed', 'cancelled'],
      'created_at': '@datetime("yyyy-MM-dd HH:mm:ss")',
      'paid_at': function() {
        return this.status !== 'pending' ? '@datetime("yyyy-MM-dd HH:mm:ss")' : null;
      },
      payment_method: () => Mock.Random.pick(['alipay', 'wechat', 'bank']),
      shipping_address: '@county(true)'
    }]
  }).list;
}

orderList = generateOrdersData();

export default {
  /**
   * 获取订单列表
   */
  getOrderList(config) {
    const params = parseUrlParams(config.url);
    const {
      page = DEFAULT_PAGE,
      limit = DEFAULT_LIMIT,
      status,
      order_id,
      user_id,
      start_date,
      end_date
    } = params;

    // 过滤逻辑
    let filtered = orderList;

    if (order_id) {
      filtered = filtered.filter(o => o.order_id === order_id);
    }

    if (status) {
      filtered = filtered.filter(o => o.status === status);
    }

    if (user_id) {
      filtered = filtered.filter(o => o.user_id === user_id);
    }

    if (start_date && end_date) {
      filtered = filtered.filter(o => {
        const createDate = new Date(o.created_at);
        return createDate >= new Date(start_date) &&
               createDate <= new Date(end_date);
      });
    }

    // 排序（默认按创建时间倒序）
    const sorted = [...filtered].sort((a, b) =>
      new Date(b.created_at) - new Date(a.created_at));

    // 分页
    const paginated = paginate(sorted, page, limit);

    return {
      code: '00000',
      data: {
        list: paginated,
        count: filtered.length,
        totalPage: Math.ceil(filtered.length / limit),
        currentPage: page
      }
    };
  },

  /**
   * 创建订单
   */
  createOrder(config) {
    try {
      const orderData = typeof config.body === 'string'
        ? JSON.parse(config.body)
        : config.body;


      // 生成新订单
      const newOrder = {
        ...orderData,
        user_id:1,
        order_id: orderIdCounter++,
        created_at: new Date().toISOString(),
        status: 'pending',
        goods: orderData.goods.map(item => ({
          ...item,
          total: item.price * item.num
        }))
      };

      orderList.unshift(newOrder);

      return {
        code: '00000',
        data: newOrder
      };
    } catch (error) {
      return {
        code: 500,
        msg: '服务器错误: ' + error.message
      };
    }
  },

  /**
   * 更新订单状态
   */
  updateOrderStatus(config) {
    const { order_id, status } = typeof config.body === 'string'
      ? JSON.parse(config.body)
      : config.body;

    const order = orderList.find(o => o.order_id == order_id);
    if (!order) {
      return {
        code: 404,
        msg: '订单不存在'
      };
    }

    // 状态机验证
    const validTransitions = {
      pending: ['paid', 'cancelled'],
      paid: ['shipped', 'cancelled'],
      shipped: ['completed'],
      cancelled: [],
      completed: []
    };

    if (!validTransitions[order.status].includes(status)) {
      return {
        code: 4002,
        msg: '无效的状态变更'
      };
    }

    // 执行状态变更
    order.status = status;
    if (status === 'paid') {
      order.paid_at = new Date().toISOString();
    }

    return {
      code: '00000',
      data: order
    };
  },

  /**
   * 删除订单（软删除）
   */
  deleteOrder(config) {
    const { order_id } = parseUrlParams(config.url);
    const index = orderList.findIndex(o => o.order_id == order_id);

    if (index === -1) {
      return {
        code: 404,
        msg: '订单不存在'
      };
    }

    // 软删除标记
    orderList[index].status = 'archived';

    return {
      code: '00000',
      msg: '订单已归档'
    };
  }
};
