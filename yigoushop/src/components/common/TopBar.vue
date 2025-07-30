<script setup lang="js">
import { reactive, ref, onUnmounted } from 'vue'
import { useAllDataStore } from '@/stores/userinfo.js'
import { useRouter } from 'vue-router'
import { User, ArrowDown,Setting, Star, ShoppingCart } from '@element-plus/icons-vue'

const store = useAllDataStore()
const router = useRouter()

const user = reactive(store.state.user)
const dropdownOpen = ref(false)

// 切换下拉菜单状态
const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

// 打开下拉菜单
const openDropdown = () => {
  dropdownOpen.value = true
}

// 关闭下拉菜单
const closeDropdown = () => {
  dropdownOpen.value = false
}

// 点击外部关闭下拉菜单
const handleClickOutside = (event) => {
  const dropdown = document.querySelector('.user-dropdown')
  if (dropdown && !dropdown.contains(event.target)) {
    dropdownOpen.value = false
  }
}

// 监听点击事件，用于关闭下拉菜单
document.addEventListener('click', handleClickOutside)

const handleCommand = (command) => {
  // 先关闭下拉菜单
  dropdownOpen.value = false

  switch(command) {
    case 'info':
      router.push('/user/info');
      break;
    case 'orders':
      router.push('/user/orders');
      break;
    case 'collect':
      router.push('/user/collect');
      break;
    case 'settings':
      router.push('/user/settings');
      break;
    case 'logout':
      handleLogout();
      break;
  }
}

const handleLogout = () => {
  store.updateUser({ name: '', token: '' })
  router.push('/login')
}

// 清理事件监听
const cleanup = () => {
  document.removeEventListener('click', handleClickOutside)
}

// 组件卸载时清理
onUnmounted(cleanup)
</script>

<template>
  <div class="header">
    <div class="header-left">
      <router-link to="/home">
        <h1>logo</h1>
      </router-link>
    </div>

    <div class="w">
      <ul>
        <template v-if="user.token === '114514'">
          <!-- 用户下拉菜单 - 支持悬停和点击 -->
          <li
            class="user-dropdown"
            @click.stop
            @mouseenter="openDropdown"
            @mouseleave="closeDropdown"
          >
            <div class="dropdown-trigger" @click="toggleDropdown">
              <div class="user-avatar">
                <img :src="`https://picsum.photos/seed/${user.name}/40/40`" :alt="user.name" />
                <User v-if="!user.name" class="default-avatar" />
              </div>
              <span class="user-name">{{ user.name || '用户' }}</span>
              <ArrowDown class="dropdown-icon" :class="{ 'rotate': dropdownOpen }" />
            </div>

            <!-- 修改为ul列表结构，使用flex布局 -->
            <ul class="dropdown-menu" :class="{ 'show': dropdownOpen }">
              <li class="dropdown-item" @click="handleCommand('info')">
                <User class="menu-icon" />
                <span>个人信息</span>
              </li>
              <li class="dropdown-item" @click="handleCommand('orders')">
                <ShoppingCart class="menu-icon" />
                <span>我的订单</span>
              </li>
              <li class="dropdown-divider"></li>
              <li class="dropdown-item" @click="handleCommand('logout')">
                <Logout class="menu-icon" />
                <span>退出登录</span>
              </li>
            </ul>
          </li>
        </template>

        <template v-else>
          <li><a href="/login">登录/注册</a></li>
        </template>
        <li><a href="#">会员</a></li>
        <li><a href="#">甄选</a></li>
        <li><a href="#">客服服务</a></li>
      </ul>
    </div>
  </div>
</template>

<style scoped lang="less">
@import url("@/assets/styles/variables.less");

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  //height: 100%;

  .header-left {
    margin-left: 20px;
  }

  ul {
    font-size: 12px;
    display: flex;
    line-height: 52px;
    justify-content: flex-end;
    padding: 2px;
    margin: 0;

    li {
      margin: 0 5px;
      list-style: none;
      position: relative;

      a {
        font-size: 16px;
        padding: 0 6px;
        color: #7a7979;
        border-left: 1px solid #ccc;
        text-decoration: none;

        &:hover {
          color: @xtxColor;
        }
      }

      &:first-child a {
        border-left: none;
      }
    }
  }

  .user-dropdown {
    position: relative;
    cursor: pointer;
    padding: 0 5px; /* 增加悬停感应区域 */

    .dropdown-trigger {
      display: flex;
      align-items: center;
      padding: 0 6px;
      color: #ccc;
      transition: color 0.2s;

      &:hover {
        color: @xtxColor;
      }
    }

    .user-avatar {
      width: 28px;
      height: 28px;
      border-radius: 50%;
      overflow: hidden;
      margin-right: 6px;
      background-color: #f0f0f0;
      display: flex;
      align-items: center;
      justify-content: center;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .default-avatar {
        width: 16px;
        height: 16px;
        color: #999;
      }
    }

    .user-name {
      margin-right: 4px;
    }

    .dropdown-icon {
      width: 14px;
      height: 14px;
      transition: transform 0.2s ease;
    }

    .rotate {
      transform: rotate(180deg);
    }

    // 下拉菜单ul样式 - 使用flex布局
    .dropdown-menu {
      position: absolute;
      top: 100%;
      right: 0;
      min-width: 160px;
      width: 100%;
      max-width: 200px; /* 限制最大宽度 */
      max-height: 400px;
      background-color: #fff;
      border-radius: 4px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      padding: 0; /* 移除默认内边距 */
      margin-top: 5px;
      z-index: 1000;
      opacity: 0;
      visibility: hidden;
      transform: translateY(10px);
      transition: all 0.2s ease;
      display: flex; /* 使用flex布局 */
      flex-direction: column; /* 垂直排列 */

      &.show {
        opacity: 1;
        visibility: visible;
        transform: translateY(0);
      }

      // 下拉菜单项样式
      .dropdown-item {
        display: flex;
        align-items: center;
        padding: 2px 6px;
        color: #333;
        transition: background-color 0.2s;
        width: 100%; /* 确保占满宽度 */
        box-sizing: border-box; /* 包含内边距计算 */
        margin: 0; /* 移除默认margin */
        white-space: nowrap; /* 防止文本换行 */

        &:hover {
          background-color: #f5f7fa;
        }

        .menu-icon {
          margin-right: 8px;
          width: 16px;
          height: 16px;
          color: #666;
        }
      }

      // 分隔线样式
      .dropdown-divider {
        height: 1px;
        background-color: #eee;
        margin: 0;
        padding: 0;
      }
    }
  }
}
</style>
