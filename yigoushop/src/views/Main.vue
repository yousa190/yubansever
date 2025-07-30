<script setup lang="js">
import '@/components/common/TopBar.vue'

</script>

<template>
  <div class="common-layout">

    <el-container class="common-container">

      <!--  define the Hearder component     -->
      <el-header class="el-header">
        <top-bar/>
      </el-header>

      <!--  define the Hearder  and Content component here     -->
      <el-container>

        <el-main class="main-container">
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
  background-color: rgba(253, 72, 72, 0.6);
}
.main-container {
  width: 100%;
}

</style>
