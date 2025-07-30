<script setup >
import { getCurrentInstance, onMounted, ref ,reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const { proxy } = getCurrentInstance()
const topCategoryList = reactive([])

const loadAllData = async () => {
  try {
    const res = await proxy.$api.getSearchNavLayerCategoryList();
    if (res) {
      // 这里可以将数据赋值给 topCategoryList
      topCategoryList.push(...res);

    }
  } catch (error) {
    console.error('请求分类数据失败:', error);
    // 可以添加用户提示，如 ElMessage.error('加载分类失败')
  }
};

onMounted(() => {
  loadAllData()
})
</script>

<template>
  <ul class="header-nav">
    <li><router-link to="/home">首页</router-link></li>
    <li class="has-layer"   v-for=" item in topCategoryList" :key="item.id" >
      <a href="#" class="parent-link">{{item.name}}</a>
      <div class="layer">
        <ul class="layer-list">
          <li v-for="subitem in item.catedatalist " :key="subitem.id">
            <a :href="subitem.url">
              <img :src="subitem.src" alt="商品图片">
              <span>{{subitem.name}}</span>
            </a>
          </li>

        </ul>
      </div>
    </li>
<!--    <li><a href="#">宠物用品</a></li>-->
<!--    <li><a href="#">服饰鞋包</a></li>-->
<!--    <li><a href="#">美食酒水</a></li>-->
<!--    <li><a href="#">个护清洁</a></li>-->
<!--    <li><a href="#">母婴亲子</a></li>-->
<!--    <li><a href="#">运动旅行</a></li>-->
<!--    <li><a href="#">数码家电</a></li>-->
  </ul>
</template>

<style lang="less">
@import url("@/assets/styles/variables.less");

.header-nav {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    position: relative;
    padding: 0 12px;
    font-weight: bolder;
    height: 40px;
    line-height: 40px;

    a {
      display: inline-block;
      padding-bottom: 8px;
      text-decoration: none;
      color: inherit;
    }

    a:hover {
      color: @helpColor;
      border-bottom: 4px solid @helpColor;
    }
  }

  .router-link-exact-active {
    color: @helpColor;
    border-bottom: 4px solid @helpColor;
  }

  // 核心：控制父元素和layer的hover状态
  .has-layer {
    // 父元素本身被hover时显示layer
    &:hover .layer {
      visibility: visible;
      opacity: 1;
      transform: translateX(-50%) translateY(0);
    }

    // 父元素链接样式修正
    .parent-link {
      position: relative;
      z-index: 115; // 确保链接在layer上方，避免hover中断
    }
  }

  .layer {
    // 用visibility替代display，保持元素占位（避免hover闪烁）
    visibility: hidden;
    opacity: 0;
    transform: translateX(-50%) translateY(10px); // 轻微上移动画
    transition: all 0.3s ease; // 平滑过渡

    z-index: 114;
    position: absolute;
    left: 150px;
    top: 50px;
    transform: translateX(-50%);
    width: 500px;
    max-width: 90vw;
    padding: 20px;
    background-color: #fff;
    border-radius: 10px;
    border: 1px solid #f0f0f0;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);

    // 关键：layer自身被hover时保持显示
    &:hover {
      visibility: visible;
      opacity: 1;
      transform: translateX(-50%) translateY(0);
    }

    .layer-list {
      display: flex;
      flex-wrap: wrap;
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        flex: 0 1 calc(33.33% - 12px);
        box-sizing: border-box;
        padding: 10px;
        height: auto;


        a {
          display: flex;
          flex-direction: column;
          align-items: center;
          text-align: center;
          padding: 10px;

          img {
            width: 50px;
            height: 50px;
            margin-bottom: 8px;
            object-fit: contain;
          }

          span {
            font-size: 14px;
            white-space: nowrap;
          }
        }

        a:hover {
          color: @helpColor;
          border-bottom: none;
          background-color: #f9f9f9;
          border-radius: 6px;
        }
      }
    }
  }
}
</style>
