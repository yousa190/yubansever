<script setup >
import { ArrowUp, Service,ShoppingCart } from '@element-plus/icons-vue'
</script>
<template>
  <div class="parentBox">
    <div class="contantsBox">
      <div>
        <span><el-icon><ShoppingCart /></el-icon></span>
        <span>购物车</span>
      </div>
      <div>
        <span><el-icon><Service /></el-icon></span>
        <span>联系客服</span>
      </div>
      <div>
        <span><el-icon><ArrowUp /></el-icon></span>
        <span>回到顶部</span>
      </div>
    </div>
  </div>
</template>
<style lang="less" scoped>
  .contantsBox {
    div {
      transition: all 0.7s;
      position: fixed;
      right: -127px;
      width: 180px;
      background: rgba(96, 96, 96, 0.6);
      color: #fff;
      padding: 8px 10px;
      cursor: pointer;
      display: flex;
      align-items: center;
      span:last-child {
        margin-left: 16px;
      }
      img {
        width: 32px;
        height: 32px;
        vertical-align: middle;
      }
    }
    div:nth-child(1) {
      bottom: 197px;
    }
    div:nth-child(2) {
      bottom: 148px;
    }
    div:nth-child(3) {
      bottom: 100px;
    }
    div:hover {
      right: 0px;
      cursor: auto;
      span {
        cursor: pointer;
      }
    }
    div:not(:last-child) {
      border-bottom: 1px solid #fff;
    }
  }
// 隐藏浏览器滚动条
//::-webkit-scrollbar {
//  display: none;
//}
</style>
