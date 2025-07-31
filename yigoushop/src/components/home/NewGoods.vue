<script setup>
import MyPanel from '@/components/slots/MyPanel.vue'
import MoreGood from '@/components/slots/MoreGood.vue'
import { ref } from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import 'swiper/css'
import 'swiper/css/pagination'
import 'swiper/css/navigation'
import { Autoplay, Navigation } from 'swiper/modules'
import { ElImage } from 'element-plus'

const modules = [Autoplay, Navigation]

const products = ref([
  { id: 1, name: '商品1', price: '¥99', img1: '/bd1.webp', img2: '/bd2.webp' },
  { id: 2, name: '商品2', price: '¥129', img1: '/bd1.webp', img2: '/bd2.webp' },
  { id: 3, name: '商品3', price: '¥159', img1: '/bd1.webp', img2: '/bd2.webp' },
  { id: 4, name: '商品4', price: '¥89', img1: '/bd1.webp', img2: '/bd2.webp' },
  { id: 5, name: '商品5', price: '¥199', img1: '/bd1.webp', img2: '/bd2.webp' },
  { id: 6, name: '商品6', price: '¥299', img1: '/bd1.webp', img2: '/bd2.webp' },
])

// 数组存储每个商品的悬停状态（索引对应ID-1）
const hoverStates = ref(new Array(products.value.length).fill(false))
</script>

<template>
  <div class="home-new">
    <my-panel title="新鲜好物" subtitle="为你寻觅世间好物">
      <template #right>
        <MoreGood path="/" />
      </template>

      <template #content>
        <div class="panel-swiper">
          <Swiper
            :modules="modules"
            :slides-per-view="4"
            :space-between="16"
            :free-mode="true"
            :free-mode-sticky="false"
            :navigation="true"
            :loop="true"
            :autoplay="{ delay: 5000, disableOnInteraction: false }"
            class="product-swiper"
          >
            <SwiperSlide
              v-for="(product, index) in products"
            :key="product.id"
            class="product-slide"
            >
            <div
              class="product-card"
              @mouseenter="hoverStates[index] = true"
            @mouseleave="hoverStates[index] = false"
            >
            <a href="#" class="img-link" target="_blank">
              <div class="product-img">
                <el-image
                  :src="product.img1"
                  class="productImg default-img"
                  lazy
                  fit="cover"
                  alt="商品图片"
                  :style="{ opacity: hoverStates[index] ? 0 : 1 }"
                />
                <el-image
                  :src="product.img2"
                  class="productImg hover-img"
                  lazy
                  fit="cover"
                  alt="商品图片"
                  :style="{ opacity: hoverStates[index] ? 1 : 0 }"
                />
              </div>
            </a>

            <a href="#" class="name">
              <div class="product-name">{{ product.name }}</div>
            </a>
            <div class="product-price">{{ product.price }}</div>
        </div>
        </SwiperSlide>
        </Swiper>
  </div>
</template>
</my-panel>
</div>
</template>

<style lang="less" scoped>
/* 样式保持不变 */
.home-new {
  width: 100%;
  box-sizing: border-box;
  padding: 0 10px;

  .panel-swiper {
    width: 100%;
    padding: 10px 0;

    .product-swiper {
      width: 100%;
      height: auto;

      .swiper-button-prev,
      .swiper-button-next {
        color: #666;
        width: 36px;
        height: 36px;
        background: rgba(255, 255, 255, 0.8);
        border-radius: 50%;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        &:after {
          font-size: 16px;
        }
      }
    }

    .product-slide {
      width: calc(100% / 4 - 12px) !important;
      max-height: 480px;

      .product-card {
        width: 100%;
        height: 480px;
        background: #fff;
        border-radius: 8px;
        overflow: hidden;
        padding: 10px;
        transition: all 0.3s ease;
        text-align: center;

        &:hover {
          background-color: #f8f5f0;
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .img-link,
        .name {
          display: block;
          color: inherit;
          text-decoration: none;
          transition: color 0.3s ease;

          &:hover {
            color: #b4a078;
          }
        }

        .product-img {
          width: 100%;
          height: 360px;
          position: relative;
          overflow: hidden;
          margin: 0 auto 10px;
          background-color: #f5f5f5;

          .productImg {
            width: 100% !important;
            height: 100% !important;
            object-fit: cover;
            transition: opacity 0.3s ease;
          }

          .default-img, .hover-img {
            position: absolute;
            top: 0;
            left: 0;
          }
        }

        .product-name {
          padding: 8px 0 4px;
          font-size: 14px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          width: 100%;
          max-width: 200px;
          margin: 0 auto;
        }

        .product-price {
          padding: 0 0 10px;
          font-size: 16px;
          color: #ff3c28;
          font-weight: bold;
          margin: 0 auto;
        }
      }
    }
  }
}
</style>
