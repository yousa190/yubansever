<script setup lang="js">
import {defineProps} from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'
const props = defineProps({
  path :{
    type:String,
    required:true,
    default: "#"
  }
})
</script>

<template>
  <router-link :to="props.path" class="more">
    <span>查看全部 <el-icon><ArrowRight/></el-icon> </span>
  </router-link>
</template>

<style scoped lang="less">
.more{
  span{
    color: #999999;
    font-size: 16px;
    line-height: 26px;
    margin-right: 5px;
    margin-top: 5px;

  }
}
</style>
