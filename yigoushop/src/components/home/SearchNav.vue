<script setup >
</script>

<template>
  <ul class="header-nav">
    <li><router-link to="/home">首页</router-link></li>
    <li><a href="#">家具</a>
    <div class="layer">
      <ul  class="layer-list">
        <li>
          <a href="#">
            <img src="@/assets/images/QQ.png" alt="商品图片">
            <span>商品名称</span>
          </a>
        </li>
        <li>
          <a href="#">
            <img src="@/assets/images/QQ.png" alt="商品图片">
            <span>商品名称</span>
          </a>
        </li>
        <li>
          <a href="#">
            <img src="@/assets/images/QQ.png" alt="商品图片">
            <span>商品名称</span>
          </a>
        </li>
      </ul>
    </div>
    </li>
    <li><a href="#">宠物用品</a></li>
    <li><a href="#">服饰鞋包</a></li>
    <li><a href="#">美食酒水</a></li>
    <li><a href="#">个护清洁</a></li>
    <li><a href="#">母婴亲子</a></li>
    <li><a href="#">运动旅行</a></li>
    <li><a href="#">数码家电</a></li>

  </ul>

</template>

<style lang="less">
@import url("@/assets/styles/variables.less");

.header-nav {
  display:flex;
  li{
    position:relative;
    float: left;
    padding: 4px;
    font-weight: bolder;
    height: 40px;
    line-height: 40px;
    a{
      padding-bottom: 8px;
    }
    a:hover{
      color:@helpColor;
      border-bottom: 4px solid @helpColor;
    }
  }
  .router-link-exact-active {
    color: @helpColor;
    border-bottom: 4px solid @helpColor;
  }
  .layer{
    position: absolute;
    left: -70px;
    top: 50px;
    width:1600px;
    height:210px;
    background-color: #fff;
    border-radius:10px;
    border: 1px solid #f0f0f0;
    box-shadow: 1px 1px 1px #e6e6e5;
    .layer-list{
      li{
        box-sizing: border-box;
        a{
          height: 80px;
          overflow: hidden;
          img{
            width:50px;
            height:50px;
            justify-content: center;
            align-items: center;
          }
        }
        a:hover{
          color: @helpColor;
          border-bottom: 0;
        }
      }
    }
  }

}
</style>
