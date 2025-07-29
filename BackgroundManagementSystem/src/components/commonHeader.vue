<script setup>
import {computed,ref} from "vue";
import {useAllDataStore} from "@/stores/counter.js";
import {useRouter} from "vue-router";
import {ElementPlus } from '@element-plus/icons-vue'
const router =useRouter()
const store =useAllDataStore()
const getImgurl = (usr)=>{
  return new URL(`../assets/images/${usr}.jpg`,import.meta.url).href
}
// hidden Menu
const collapseMenu=()=>{
  store.state.isCollapse= !useAllDataStore().state.isCollapse
}

const exit=()=>{
  store.clean()
  router.push('/login')
}
const current =computed(()=>store.state.currentMenu)

// 处理语言切换
// 可用语言选项
const languageOptions = [
  { code: 'zh', name: '中文' },
  { code: 'en', name: 'English' },
  { code: 'ja', name: '日本語' }
]
const currentLang =computed(() => store.state.language)
const currentLangName = computed(() => {
  return languageOptions.find(lang => lang.code === currentLang.value)?.name
})
const handleCommand = (command) => {
  if (command === currentLang.value) return
  // 持久化存储
  store.selectLanguage(command)
}




</script>
<template>
<div class="header">
<!--  Header Left   -->
  <div class="header_left">
    <el-button size="small" @click=" collapseMenu " >
      <component class="icons" is="menu"></component>
    </el-button>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item v-if="current" :to="current.path" >{{current.label}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <!--  Header Right   -->
<!--  使用vue18n进行国际化-->
  <div class="header_right">
    <!-- 语言切换下拉菜单 -->
    <el-dropdown
        trigger="click"
        @command="handleCommand"
        class="language-switcher"
    >
      <!-- 触发按钮 -->
      <span class="dropdown-trigger" >
      <el-icon ><el-icon><ElementPlus /></el-icon> </el-icon>
      <span class="current-lang">{{ currentLangName }}</span>
      </span>

      <!-- 下拉菜单 -->
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
              v-for="lang in languageOptions"
              :key="lang.code"
              :command="lang.code"
              :class="{ active: lang.code === currentLang }"
          >
            <span class="flag-icon" :class="`flag-${lang.code}`"></span>
            {{ lang.name }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <el-dropdown>
    <span class="el-dropdown-link">
      <img :src="getImgurl('user-img')" class="avatar"/>
    </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item>个人中心</el-dropdown-item>
          <el-dropdown-item  @click="exit">退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</div>
</template>


<style scoped lang="less">
.header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #333;
}
.icons{
  width: 20px;
  height: 20px;
}
.header_left{
  display: flex;
  align-items: center;
  .el-button{
      margin-right: 20px;
  }
}

:deep(.el-breadcrumb__item span){
  color: #fff !important;
  cursor: pointer !important;
}

.header_right{
  padding-right: 8px;
  gap: 20px;
  .avatar{
     width: 40px;
     height: 40px;
    border-radius: 50%;
   }

  .language-switcher {
    cursor: pointer;
    margin-left: 20px;
    margin-top: 8px;
    margin-right: 20px;


    .dropdown-trigger {
      color: #e6e9ee;
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 12px;
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: var(--el-color-primary-light-9);
      }
      .current-lang {
        font-size: 14px;
        color: #e6e9ee;
        //color: var(--el-text-color-primary);
      }
    }
  }

  // 语言选项样式
  .el-dropdown-menu {
    min-width: 120px;

    .el-dropdown-item {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 12px;

      &.active {
        color: var(--el-color-primary);
        background: var(--el-color-primary-light-9);
      }

    }
  }

}

.example-showcase .el-dropdown-link {

  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>