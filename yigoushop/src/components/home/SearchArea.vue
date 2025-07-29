<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search, ShoppingCart, StarFilled, User } from '@element-plus/icons-vue'
import '@/components/home/SearchNav.vue'

const router = useRouter()
const searchQuery = ref('')
const cartCount = ref(12) // 购物车商品数量，实际项目中从状态管理获取

// 处理搜索提交
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    // 跳转到搜索结果页，携带搜索参数
    router.push({
      path: '/search',
      query: { keyword: searchQuery.value }
    })
  }
}

// 处理购物车点击
const goToCart = () => {
  router.push('/cart')
}

// 处理回车键搜索
const handleKeydown = (e) => {
  if (e.key === 'Enter') {
    handleSearch()
  }
}
</script>

<template>
  <div class="topSearchArea">
    <div class="container">
      <!-- 品牌标识 -->
      <div class="top-logo">
        <router-link to="/home" class="logo-link">
          <h1 class="logo">易购商城</h1>
        </router-link>
      </div>

      <!-- 搜索栏 -->
      <div class="search-container">
        <div class="search-box">
          <div class="search-icon">
            <Search />
          </div>
          <input
            type="text"
            v-model="searchQuery"
            @keydown="handleKeydown"
            placeholder="搜索商品、品牌或分类"
            class="search-input"
          >
          <button @click="handleSearch" class="search-btn">搜索</button>
        </div>
        <div class="hot-search">
          <span class="hot-title">热门搜索:</span>
          <a href="#" class="hot-item">夏季新品</a>
          <a href="#" class="hot-item">家居好物</a>
          <a href="#" class="hot-item">零食饮料</a>
          <a href="#" class="hot-item">美妆护肤</a>
          <a href="#" class="hot-item">数码配件</a>
        </div>
      </div>

      <!-- 购物车和用户入口 -->
      <div class="cart-user">
        <div class="user-entry" @click="router.push('/user')">
          <User class="icon" />
          <span class="text">我的</span>
        </div>
        <div class="cart-entry" @click="goToCart">
          <div class="cart-icon-wrapper">
            <ShoppingCart class="icon" />
            <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
          </div>
          <span class="text">购物车</span>
        </div>
      </div>
    </div>

    <el-divider>
      <el-icon><star-filled /></el-icon>
      <el-icon><star-filled /></el-icon>
      <el-icon><star-filled /></el-icon>
    </el-divider>

    <div class="container search-nav">
      <search-nav/>
    </div>

  </div>
</template>

<style scoped lang="less">
// 基础变量，可提取到全局变量文件
@primary-color: #ff4d4f; // 主色调，参照网易严选的红色系
@text-color: #333;
@text-secondary: #666;
@text-placeholder: #999;
@border-color: #e5e5e5;
@bg-color: #f5f5f5;
@hover-color: #f9f9f9;

.topSearchArea {
  background-color: #fff;

  padding: 15px 0;
  padding-bottom: 0;

  .container {
    width: 100%;
    margin: 0 auto;
    padding: 0 15px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .top-logo {
    .logo-link {
      text-decoration: none;
    }

    .logo {
      font-size: 28px;
      font-weight: 700;
      color: @primary-color;
      margin: 0;
      letter-spacing: -1px;
      transition: opacity 0.2s;

      &:hover {
        opacity: 0.9;
      }
    }
  }

  .search-container {
    flex: 1;
    max-width: 650px;
    margin: 0 30px;
  }

  .search-box {
    display: flex;
    align-items: center;
    background-color: @bg-color;
    border-radius: 30px;
    height: 44px;
    padding: 0 15px;
    transition: background-color 0.2s, box-shadow 0.2s;

    &:hover {
      background-color: #f0f0f0;
      box-shadow: 0 1px 2px rgba(0,0,0,0.05);
    }

    .search-icon {
      color: @text-placeholder;
      margin-right: 8px;
      width: 20px;
      height: 20px;
    }

    .search-input {
      flex: 1;
      background: transparent;
      border: none;
      outline: none;
      font-size: 14px;
      color: @text-color;
      height: 100%;

      &::placeholder {
        color: @text-placeholder;
        font-size: 14px;
      }
    }

    .search-btn {
      background-color: @primary-color;
      color: white;
      border: none;
      border-radius: 20px;
      padding: 6px 16px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: background-color 0.2s, transform 0.1s;

      &:hover {
        background-color: #e04344;
      }

      &:active {
        transform: scale(0.98);
      }
    }
  }

  .hot-search {
    display: flex;
    align-items: center;
    margin-top: 8px;
    font-size: 12px;

    .hot-title {
      color: @text-placeholder;
      margin-right: 10px;
    }

    .hot-item {
      color: @text-secondary;
      margin-right: 15px;
      text-decoration: none;
      transition: color 0.2s;

      &:hover {
        color: @primary-color;
        text-decoration: underline;
      }
    }
  }

  .cart-user {
    display: flex;
    align-items: center;
    gap: 25px;
  }

  .user-entry, .cart-entry {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    color: @text-color;
    text-decoration: none;
    transition: color 0.2s, transform 0.2s;
    position: relative;

    &:hover {
      color: @primary-color;
      transform: translateY(-2px);
    }

    .icon {
      width: 24px;
      height: 24px;
      margin-bottom: 3px;
    }

    .text {
      font-size: 12px;
    }
  }

  .cart-icon-wrapper {
    position: relative;
  }

  .cart-badge {
    position: absolute;
    top: -5px;
    right: -8px;
    background-color: @primary-color;
    color: white;
    font-size: 12px;
    width: 18px;
    height: 18px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
  }

  .search-nav{
    margin-top: 15px;
  }
}

// 响应式调整
@media (max-width: 992px) {
  .topSearchArea {
    padding: 10px 0;

    .container {
      flex-wrap: wrap;
    }

    .search-container {
      order: 3;
      width: 100%;
      max-width: none;
      margin: 10px 0 0;
    }

    .top-logo, .cart-user {
      margin-bottom: 5px;
    }
  }
}

@media (max-width: 576px) {
  .hot-search {
    display: none;
  }

  .top-logo .logo {
    font-size: 24px;
  }

  .cart-user {
    gap: 15px;
  }
}
</style>

