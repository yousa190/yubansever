// 引入Mock.js
import Mock from 'mockjs'

// 生成分类数据
const categoryResponse = Mock.mock({
  'code': "00000",
  'message': '获取分类数据成功',
  // 响应数据（分类列表）
  'data|8': [
    {
      // 主分类id：随机生成5位整数（10000-99999）确保唯一性
      'id': () => Mock.Random.integer(10000, 99999),
      'url': '#',
      // 主分类名称（保持不变）
      'name': function() {
        const mainCategories = [
          '家用电器', '手机数码', '电脑办公', '家居家装',
          '服装鞋帽', '美妆个护', '母婴用品', '食品饮料'
        ]
        return mainCategories[this.$index] || Mock.Random.word(2, 4)
      },
      // 子分类，每个主分类有3-5个子分类
      'catedatalist|3-5': [
        {
          // 子分类id：随机生成8位整数（10000000-99999999）确保唯一性
          'id': () => Mock.Random.integer(10000000, 99999999),
          'name': function() {
            const subCategoryMap = {
              '家用电器': ['冰箱', '彩电', '空调', '洗衣机', '厨房电器'],
              '手机数码': ['智能手机', '平板电脑', '耳机音响', '智能手表', '移动电源'],
              '电脑办公': ['笔记本电脑', '台式机', '打印机', 'U盘', '键盘鼠标'],
              '家居家装': ['床上用品', '灯具照明', '厨卫用品', '家具', '装饰画'],
              '服装鞋帽': ['男装', '女装', '童装', '鞋靴', '内衣'],
              '美妆个护': ['面部护理', '彩妆', '香水', '洗发水', '沐浴露']
            }

            return  Mock.Random.word(2, 4)
          },
          'url': '#',
          'src': () => `https://picsum.photos/id/${Mock.Random.integer(1, 200)}/300/300`
        }
      ]
    }
  ]
})

export default {
  categoryResponse
}
