<script setup lang="js">
import {watch, reactive,ref,onMounted,getCurrentInstance} from "vue";
import { User, Lock } from "@element-plus/icons-vue"; // 引入 Element Plus 的图标
import {useAllDataStore} from "@/stores/counter.js";
import {useRouter,useRoute} from 'vue-router'
import {ElMessage} from "element-plus";
const {proxy} =getCurrentInstance()
const store = useAllDataStore()
const router = useRouter()
const route = useRoute()

// 登录按钮的逻辑处理
const handleLogin = async () => {
  const res= await proxy.$api.getMenu(loginForm)
  if (res.menuList) {
    ElMessage.success({
      message: res.message,
    })
  }else ElMessage.error("登陆错误")
  store.updateMenuList(res.menuList)
  store.state.token=res.token
  store.addMenu(router)
  router.push('/home')

};
const renderCanvas =()=>{
  //画布
  const canvas =document.querySelector('canvas');
  /* 设置画布宽高 */
  canvas.width =window.innerWidth;
  canvas.height =window.innerHeight;
  /*使用2D绘图 */
  const ctx =canvas.getContext('2d');
  /* 樱花数量 */
  const sakura_sum =40;
  const sakuraArr =[];

  /* sakura class */
  class sakura{
    constructor(){
      this.x =Math.random()*canvas.width;
      this.y =(Math.random()*canvas.height*2)-canvas.height;
      /* 随机生成花瓣宽高 */
      this.width =Math.random()*15+25;
      this.height =Math.random()*15+20;
      //透明度
      this.opacity =this.width/50;
      //设置随机数
      this.rotate=Math.random();
      //speed
      this.xSpeed =Math.random()*0.5+1;
      this.ySpeed =Math.random()+1.5;
      this.rotateSpeed =Math.random()*0.02;
    }
//draw
    draw(){
      /* 防止超过canvas */
      if(this.x>canvas.width||this.y>canvas.height){
        this.x =-sakuraImg.height;
        this.y =(Math.random()*canvas.height*2)-canvas.height;
        this.rotate=Math.random();
        this.xSpeed =Math.random()*2+1;
        this.ySpeed =Math.random()+1.5;
        this.rotateSpeed =Math.random()*0.02;
      }
      //canvas透明基数
      ctx.globalAlpha =this.opacity;
      ctx.drawImage(
          sakuraImg,
          this.x,
          this.y,
          this.width*(0.6+Math.abs(Math.cos(this.rotate))/3),
          this.height*(0.8+Math.abs(Math.sin(this.rotate))/5)
      );
    }
    animate() {
      this.x +=this.xSpeed +mouseX*5;
      this.y +=this.ySpeed +mouseX*2;
      this.rotate+=this.rotateSpeed;
      this.draw();

    }
  }

  /* 渲染 */
  function render(){
    /* 清空屏幕 */
    ctx.clearRect(0,0,canvas.width,canvas.height);
    //
    sakuraArr.forEach(sakura =>sakura.animate());
    //告诉浏览器调用指定函数进行渲染
    window.requestAnimationFrame(render);

  }
//加载图片
  const sakuraImg =new Image();
  sakuraImg.src = '/sakura.jpg';
  sakuraImg.addEventListener('load',()=>{
    for(let i=0;i<sakura_sum;++i){
      sakuraArr.push(new sakura());
    }
    render();
  })

  let mouseX =0;
//移动监听
  function touchHeader(e){
    //客户端区域的水平座标
    mouseX =(e.clientX||e.touches[0].clientX)*0.5/window.innerWidth;
  }
  window.addEventListener('mousemove',touchHeader);
  window.addEventListener('touchmove',touchHeader);
}
const loginForm =reactive({
  name:'',
  id:'',
  pwd:'',
})


onMounted(()=> {
renderCanvas();
})
 </script>

<template>
  <div class="container">
    <canvas width="100%" height="100%" />
    <div class="loginBody">
      <el-form
          :model="loginForm"
          class="login-container"
      >
        <h1  class="anim1" >Welcome</h1>
        <!-- 输入账号 -->
        <el-form-item>
          <el-input
              class="anim2"
              v-model="loginForm.id"
              placeholder="请输入账号"
          >
            <template #suffix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 输入密码 -->
        <el-form-item>
          <el-input  class="anim2"
              v-model="loginForm.pwd"
              type="password"
              placeholder="请输入密码"
          >
            <template #suffix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item >
          <el-button class="anim2" type="primary" @click="handleLogin">登录</el-button>
          <el-button class="anim2" type="info">注册</el-button>
        </el-form-item>


      </el-form>

    </div>

  </div>

</template>

<style scoped lang="less">
*{
  font-family: poppins,system-ui;
}
.container {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    background: url("/background.jpg") no-repeat center center; /* 背景图片居中，并且不重复 */
    background-size: cover; /* 背景图片铺满页面，保持宽高比 */
    justify-content: center;
    align-items: center;
    //display: flex;
    overflow: hidden; /* 防止出现滚动条 */
}

canvas {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1; /* canvas 层级 */
  //background: no-repeat  url("/background.jpg");
}

.loginBody {
  //display: flex;
  position: relative; /* 保证登录框在 canvas 上方 */
  z-index: 1000; /* 确保登录框位于最上层 */
  width: 30%;
  max-width: 450px;
  margin: 100px auto; /* 居中显示 */
  padding: 35px 30px;
  background: rgba(255, 255, 255, 0.168); /* 半透明背景，让樱花效果可见 */
  text-align: center;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.13); /* 添加阴影效果 */
  border-radius: 10px;
  border:1px solid rgba(255, 255, 255, 0.329);
  backdrop-filter: blur(10px);

  h1{
    font-weight: 1000;
    text-align: center;
    margin-bottom: 20px;
    font-size: 2.2em ;
    color: #383636;

    animation: reloadA 1s ease-in-out forwards;
    opacity: 0;
  }
  :deep(.el-form-item__content){
    justify-content: center;
  }
  input{
    background: transparent;
    font-size: 1.2rem;
    color: #383636;
    padding: 5px 25px;
  }
  input:hover{

    font-size: 1.2rem;
    color: #383636;
    padding: 5px 25px;
  }

  @keyframes reloadA{
    from{
      transform: translateY(250px);
    }
    to{
      transform: translateY(0);
      opacity: 1;
    }
  }

  .anim1{
    animation: reloadA 0.7s ease-in-out forwards;
    opacity: 0;
    animation-delay: 0.1s;
  }
  .anim2{
    animation: reloadA 0.9s ease-in-out forwards;
    opacity: 0;
    animation-delay: 0.1s;
  }

}


</style>