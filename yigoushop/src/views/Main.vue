<script setup lang="js">
import '@/components/common/TopBar.vue'
import '@/components/common/SearchArea.vue'
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
          <div class="searchArea w">
            <search-area/>
          </div>

          <div class="router-area w" >
            <router-view></router-view>
          </div>
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

.searchArea{
  width: 100%;
  padding: 15px 15px 4px;
  background: rgba(255, 255, 255, 0.8);
}

.router-area{
  width: 100%;
  padding: 15px 15px 4px;
  background: rgba(255, 255, 255, 0.8);
}

.main-container {
  width: 100%;
}

</style>
