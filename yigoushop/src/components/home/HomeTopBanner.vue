<!-- src/components/home/HomeTopBanner.vue -->
<script setup lang="js">
import { onMounted, reactive, ref } from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
// 引入swiper样式
import 'swiper/css'
import 'swiper/css/pagination' // 轮播图底面的小圆点
import 'swiper/css/navigation' // 轮播图两边的左右箭头
import 'swiper/css/scrollbar'  // 轮播图的滚动条

import { Autoplay, Pagination, Navigation, Scrollbar, EffectFade } from 'swiper/modules'
const modules = [Autoplay, Pagination, Navigation, Scrollbar, EffectFade]

const dataList = reactive([
  {
    name: "img1",
    url: "#",
    src: "/topbanner.webp"
  },
  {
    name: "img2",
    url: "#",
    src: "/topbanner.webp"
  }
])

// 增加轮播图加载完成后的动画效果
const swiperLoaded = ref(false)
onMounted(() => {
  // 延迟标记加载完成，触发入场动画
  setTimeout(() => {
    swiperLoaded.value = true
  }, 100)
})
</script>

<template>
  <!-- 增加容器确保居中显示 -->
  <div class="swiper-container">
    <swiper
      :modules="modules"
      :loop="true"
      :slides-per-view="1"
      :space-between="0"
    :autoplay="{ delay: 4000, disableOnInteraction: false }"
    navigation
    :pagination="{ clickable: true }"
    :effect="swiperLoaded ? 'fade' : 'slide'"
    :speed="800"
    class="home-swiper"
    :class="{ 'loaded': swiperLoaded }"
    >
    <swiper-slide v-for="(item, index) in dataList" :key="index">
      <a :href="item.url" class="banner-link">
        <el-image
          :src="item.src"
          :fit="'cover'"
          class="banner-image"
        ></el-image>
      </a>
    </swiper-slide>
    </swiper>
  </div>
</template>

<style scoped lang="less">
// 容器样式确保居中并限制最大宽度
.swiper-container {
  width: 100%;
  max-width: 1800px;
  margin: 0 auto;    // 水平居中
  padding: 0 15px;   // 与全局容器内边距保持一致
  box-sizing: border-box;
}

// 轮播图基础样式
.home-swiper {
  width: 100%;
  height: 320px;
  border-radius: 2px;  // 增加圆角美化
  overflow: hidden;    // 裁剪超出部分
  transition: opacity 0.5s ease;
  opacity: 0;

  &.loaded {
    opacity: 1;  // 加载完成后显示
  }

  // 轮播图片样式优化
  .banner-link {
    display: block;
    width: 100%;
    height: 100%;
  }

  .banner-image {
    width: 100%;
    height: 100%;
    transition: transform 0.8s ease;  // 图片缩放动画

    &:hover {
      transform: scale(1.02);  // 鼠标悬停时轻微放大
    }
  }
}

// 响应式调整
@media (max-width: 768px) {
  .home-swiper {
    height: 200px;  // 移动端减小高度
  }
}
</style>
