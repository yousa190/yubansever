<script setup >

import Home from "@/views/Home.vue";
import CommonHeader from "@/components/commonHeader.vue";
import CommonLeftSide from "@/components/commonLeftSide.vue";
import User from "@/views/User.vue";
import CommonTag from "@/components/CommonTag.vue";

</script>

<template>
 <div class="common-layout">
   <el-container class="common-container">
<!--  define the left component    -->
     <commonLeftSide/>
<!--  define the Hearder  and Content   component here     -->
     <el-container>
<!--  define the Hearder component     -->
        <el-header class="el-header">
          <commonHeader/>
        </el-header>

      <CommonTag/>
       <el-main class="right-main-container">
         <router-view></router-view>
       </el-main>
     </el-container >
   </el-container>
 </div>
</template>



<style scoped lang="less">
  .common-layout,.common-container {
    height: 100%;
  }
  .el-header{
    background-color: #333;
  }
</style>
