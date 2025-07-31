<script setup>
import '@/components/common/SearchArea.vue'
import '@/components/home/HomeTopBanner.vue'
import '@/components/common/RightFloatMenu.vue'
import '@/components/home/NewGoods.vue'
import NewGoods from '@/components/home/NewGoods.vue'
import {ref} from 'vue'
import { StarFilled } from '@element-plus/icons-vue'


const products = ref([
  {
    id: 1,
    name: '智能测温保温杯 316不锈钢长效保温保冷',
    description: 'LED屏显温度 · 防烫杯口',
    price: 129,
    originalPrice: 159,
    salesPercent: 78,
    limit: 2000,
    imageUrl: 'https://picsum.photos/400/400?random=1',
    type: 'today'
  },
  {
    id: 2,
    name: '主动降噪无线蓝牙耳机 长续航入耳式',
    description: 'ANC降噪 · 30小时续航',
    price: 299,
    originalPrice: 399,
    salesPercent: 65,
    limit: 1500,
    imageUrl: 'https://picsum.photos/400/400?random=2',
    type: 'today'
  },
  {
    id: 3,
    name: '迷你便携式榨汁机 家用水果汁杯 充电式',
    description: '6叶刀头 · 一键操作',
    price: 89,
    originalPrice: 129,
    salesPercent: 92,
    limit: 3000,
    imageUrl: 'https://picsum.photos/400/400?random=3',
    type: 'today'
  },
  {
    id: 4,
    name: '全棉四件套 简约纯色床上用品套件',
    description: '100%新疆棉 · 亲肤透气',
    price: 259,
    originalPrice: 359,
    salesPercent: 45,
    limit: 1200,
    imageUrl: 'https://picsum.photos/400/400?random=4',
    type: 'today'
  },
  {
    id: 5,
    name: '家用全自动咖啡机 意式美式一体',
    description: '一键操作 · 奶泡系统',
    price: 1299,
    originalPrice: 1599,
    salesPercent: 28,
    limit: 500,
    imageUrl: 'https://picsum.photos/400/400?random=5',
    type: 'week'
  },
  {
    id: 6,
    name: '超声波清洗机 家用小型首饰眼镜清洁器',
    description: '多档定时 · 大容量',
    price: 159,
    originalPrice: 229,
    salesPercent: 72,
    limit: 2000,
    imageUrl: 'https://picsum.photos/400/400?random=6',
    type: 'week'
  },
  {
    id: 7,
    name: '轻量便携折叠椅 户外露营钓鱼靠背椅',
    description: '承重200斤 · 牛津布面料',
    price: 129,
    originalPrice: 189,
    salesPercent: 58,
    limit: 1800,
    imageUrl: 'https://picsum.photos/400/400?random=7',
    type: 'week'
  }
]);
</script>

<template>
  <div class="top-swiper">
    <home-top-banner/>
  </div>

  <el-divider/>

  <div class="goods-container">
    <new-goods/>
  </div>

  <div class="goods-container">
    <good-lists />
    <good-lists />
    <good-lists />
  </div>




  <!--  右侧菜单栏-->
  <right-float-menu/>

</template>

<style scoped lang="less">


.top-swiper{
  position: relative;
  width: 100%;
  max-height: 500px;
}
.goods-container{
  position: relative;
  width: 100%;
  margin: 1px auto;
}
</style>
