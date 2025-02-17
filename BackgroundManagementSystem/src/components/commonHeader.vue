<script setup>
import {computed} from "vue";
import {useAllDataStore} from "@/stores/counter.js";
import {useRouter} from "vue-router";
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
  <div class="header_right">
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
   .avatar{
     width: 40px;
     height: 40px;
    border-radius: 50%;
   }
}
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>