<script setup>
import '@/components/home/SearchArea.vue'
import '@/components/home/HomeTopBanner.vue'
import '@/components/home/RightFloatMenu.vue'
</script>

<template>
  <div class="searchArea w">
    <search-area/>
  </div>

  <div class="top-swiper">
    <home-top-banner/>
  </div>

  <!--  右侧菜单栏-->
  <right-float-menu/>

</template>

<style scoped lang="less">
.searchArea{
  width: 100%;
  padding: 15px 15px 4px;
  background: #fff;
}

.top-swiper{
  position: relative;
  width: 100%;
  max-height: 480px;
}

</style>
