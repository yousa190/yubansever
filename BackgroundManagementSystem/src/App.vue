<script setup>
import {getCurrentInstance} from "vue";
import {ElMessage} from "element-plus";
import {useAllDataStore} from "@/stores/counter.js";

const {proxy} =getCurrentInstance()
const store = useAllDataStore()
const getMenu=async ()=>{
  const res= await proxy.$api.getMenu()
  if (res.menuList) {
    ElMessage.success({
      message: res.message,
    })
  }else ElMessage.error("登陆错误")
  store.updateMenuList(res.menuList)
  store.state.token=res.token
  store.addMenu(router)
}
</script>

<template>
  <router-view></router-view>
</template>

<style >
#app{
  width: 100% ;
  height: 100%;
  overflow: hidden;
}
</style>
