<script setup  language="JavaScript" >
import {useRoute, useRouter} from "vue-router";
import {computed, ref} from "vue";
import {useAllDataStore} from "@/stores/counter.js";

const store = useAllDataStore();
const handleOpen = (key, keyPath) => {
  // console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  // console.log(key, keyPath)
}

const router =useRouter()
const route =useRoute()


// const list =ref([
//   {
//     path: '/home',
//     name: 'home',
//     label: "首页",
//     icon: 'house',
//     url: '/home'
//   },
//   {
//     path: '/mall',
//     name: 'mall',
//     label: "商品管理",
//     icon: 'video-play',
//     url: '/mall'
//   },
//   {
//     path: '/user',
//     name: 'user',
//     label: "用户管理",
//     icon: 'user',
//     url: '/user'
//   },
//   {
//     path: '/other',
//     name: 'other',
//     label: "其他",
//     icon: 'location',
//     children: [
//       {
//         path: '/page1',
//         name: 'page1',
//         label: "第一页",
//         icon: 'setting',
//         url: '/page1'
//       },
//       {
//         path: '/page2',
//         name: 'page2',
//         label: "第二页",
//         icon: 'setting',
//         url: '/page2'
//       },
//     ]
//   }
// ])
const list =computed(()=>store.state.menuList)



const list1 =computed(() => list.value.filter(item => !item.children))
const list2 =computed(() => list.value.filter(item => item.children))

const isCollapse  =computed(()=>store.state.isCollapse)
const Dwidth =computed(()=>store.state.isCollapse? '64px': '180px')

const activeMenu =computed(()=>route.path)

const handleMenu =(item)=>{
  router.push(item.path)
  store.selectMenu(item)
}



</script>

<template>
    <el-aside width="Dwidth">
      <el-menu
          active-text-color="#ffd04b"
          background-color="#545c64"
          class="el-menu-vertical-demo"
          :default-active="activeMenu"
          text-color="#fff"
          @open="handleOpen"
          @close="handleClose"
          :collapse-transition="false"
          :collapse="isCollapse"
      >
        <h1 >
          <img src="/favicon.ico" alt="" class="favicon-icon">
          <span v-if =" !isCollapse" >后台管理系统</span>
        </h1>

        <el-menu-item  v-for="item in list1"
                       :key="item.path"
                       :index="item.path"
                       @click="handleMenu(item)"
        >
          <component class="icons" :is="item.icon" ></component>
          <span>{{item.label}}</span>
        </el-menu-item>
        <el-sub-menu  v-for="item in list2"
                      :key="item.path"
                      :index="item.path">
          <template #title>
            <component class="icons" :is="item.icon" ></component>
            <span>{{item.label}}</span>
          </template>
          <el-menu-item-group>
            <el-menu-item v-for="  (child,index ) in item.children"
                          :key="child.path"
                          :index="child.path"
                          @click="handleMenu(child)"
            >
              <component class="icons" :is="child.icon" ></component>
              <span>{{child.label}}</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>

      </el-menu>
    </el-aside>
</template>




<style scoped lang="less">
.favicon-icon{
  display: inline-block;
  line-height: 1;
  width: 1.5em;
  height: 1.5em;
  margin-top: 15px;
  margin-left: 5px;
}
  .icons{
    width: 18px;
    height: 18px;
    margin-right: 8px;
  }
  .el-menu{
    border-right: none !important;
    h1{
      font-size: 20px;
      line-height: 36px;
      color: #fff;
      text-align: center;
      overflow: hidden;

    }
  }
  .el-aside{
    height: 100%;
    background-color: #545c6a;
  }
</style>