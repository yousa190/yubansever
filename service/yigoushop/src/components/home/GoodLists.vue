<script setup>
import { ref } from 'vue';

// 定义状态记录当前hover的商品索引
const hoverIndex = ref(-1);
</script>


<template>
  <div class="home-goods">
    <my-panel title="人气推荐">
      <template #right>
        <MoreGood path="/" />
      </template>

      <template #banner>
        <div class="banner">
          <el-image :src="'/jujia.webp'" lazy fit="cover"></el-image>
        </div>
      </template>

      <template #content>
        <div class="item-list">
          <ul>
            <li
              class="item"
              v-for="i in 4"
              :key="i"
              @mouseenter="hoverIndex = i"
              @mouseleave="hoverIndex = -1"
            >
              <div class="item-content">
                <div class="product">
                  <a href="#" class="item-link" target="_blank">
                    <div class="img-container">
                      <el-image
                        :src="`/bd1.webp`"
                        class="productImg default-img"
                        lazy
                        fit="cover"
                        alt="商品图片"
                        :style="{ opacity: hoverIndex === i ? 0 : 1 }"
                      />
                      <el-image
                        :src="`/bd2.webp`"
                        class="productImg hover-img"
                        lazy
                        fit="cover"
                        alt="商品图片"
                        :style="{ opacity: hoverIndex === i ? 1 : 0 }"
                      />
                    </div>
                  </a>
                </div>
                <div class="product-info">
                  <div class="info-tag" />
                  <h4 class="name">
                    <a href="#" class="item-link">
                      <span>商品{{i}}</span>
                    </a>
                  </h4>

                  <p class="price">¥{{ 100 + i * 10 }}</p>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </template>
    </my-panel>
  </div>
</template>


<style scoped lang="less">
.home-goods {
  width: 100%;
  box-sizing: border-box;
  padding: 0 10px;

  .banner {
    margin-bottom: 20px;
    width: 100%;
    max-height: 380px;

    .el-image {
      width: 100%;
      height: 100%;
      img {
        width: 100% !important;
        object-fit: cover;
      }
    }
  }

  .item-list {
    width: 100%;
    clear: both;

    ul {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
      padding: 0;
      margin: 80px 0 0;
      list-style: none;
    }

    .item {
      width: calc(25% - 12px);
      margin: 0;
      border-radius: 8px; /* 统一圆角 */
      transition: all 0.3s ease; /* 统一过渡效果 */
      will-change: transform, box-shadow; /* 优化动画性能 */

      &:nth-child(-n+4) {
        margin-top: -16px;
      }

      // 优化后的悬停样式
      &:hover {
        background-color: #f8f5f0; /* 与新鲜好物保持一致的背景色 */
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08); /* 统一阴影效果 */
        transform: translateY(-4px) scale(1.03); /* 轻微上浮+缩放 */
        z-index: 10; /* 确保hover项在最上层 */
      }

      .item-content {
        width: 100%;
        height: 381px;
        box-sizing: border-box;
        padding: 10px; /* 增加内边距 */

        .product {
          background-color: #f5f5f5; /* 统一图片背景色 */
          margin-bottom: 8px;
          overflow: hidden;
          height: 285px; /* 固定图片高度 */
          border-radius: 4px; /* 图片容器圆角 */

          .item-link {
            display: block;
            width: 100%;
            height: 100%;
          }

          // 图片容器：双图叠加
          .img-container {
            position: relative; /* 作为图片定位基准 */
            width: 100%;
            height: 100%;

            .productImg {
              width: 100% !important;
              height: 100% !important;
              object-fit: cover; /* 确保图片铺满容器 */
              transition: opacity 0.3s ease; /* 平滑过渡效果 */
            }

            // 图片叠加布局
            .default-img, .hover-img {
              position: absolute;
              top: 0;
              left: 0;
            }
          }
        }

        .product-info {
          padding: 0 5px;
          text-align: center; /* 整体文字居中 */

          .info-tag {
            height: 10px;
            font-size: 0;
            color: #fff;
            width: 200px;
            margin: 0 auto 3px;
            overflow: hidden;
          }

          .name{
            width: 100%;
            max-width: 200px; /* 限制最大宽度 */
            margin: 0 auto 4px;
            max-height: 40px;
            line-height: 20px;
            display: -webkit-box;
            -webkit-line-clamp: 2; /* 最多显示2行 */
            -webkit-box-orient: vertical;
            overflow: hidden;

            .item-link {
              color: inherit;
              text-decoration: none;
              transition: color 0.3s ease;

              &:hover {
                color: #b4a078; /* 统一悬停文字颜色 */
              }
            }
          }

          p {
            margin: 5px 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          .price {
            font-size: 16px;
            line-height: 13px;
            color: #ff3c28; /* 统一价格颜色 */
            padding: 1px 0 9px;
            margin: 0 auto 3px;
            text-align: center;
            width: 100%;
            font-weight: bold; /* 价格加粗 */
          }
        }
      }
    }
  }
}
</style>
