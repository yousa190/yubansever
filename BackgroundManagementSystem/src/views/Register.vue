<script setup>
import {getCurrentInstance, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import { debounce } from 'lodash';

const router=useRouter()
const form = reactive({
  username: '',
  password: '',
  rPassword: '',
  email: '',
  phone: '',
  sex: "1",
})


const {proxy} =getCurrentInstance()
const identifyCode = ref(''); // 验证码输入
const IAgree = ref(false); // 同意状态
const isDisabled = ref(false); // 按钮是否禁用
const countdown = ref(60); // 倒计时秒数


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
const onSubmit = () => {
  console.log('submit!')
}
const reConf=()=>{
  if (form.password !== form.rPassword)
    ElMessage.error({
      message:"密码与确认密码不一致.",
      center:true,
      duration:2000,

    });
}

const validateName= async()=>{
    if (form.username) {
      const res = await proxy.$api.checkName({name: form.username});
      if (res) {
        ElMessage.error({
          id: 'name',
          message: res,
          duration: 1000,         // 不自动关闭
        })
      }
    }
}

const validateEmailStrict= debounce( async (email)=>{
  const regex =  /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/

  if (!form.email||form.email.length===0){
    ElMessage.error({
      id:'mail',
      message:'邮箱为空!'
    });
    return false
  }
  else if ( !regex.test(email)){
    ElMessage.error({
      id:'mail',
      message:"邮箱格式错误!",
      center:true,
      duration:2000,
    });
    return false
  }
  // 是否已存在
  else {
    const msg=await proxy.$api.checkEmail({email: form.email,});
    if (msg){
      ElMessage.error({
        id:'mail',
        message: msg,
        duration: 0,         // 不自动关闭
        showClose: true,     // 显示关闭按钮（若默认不显示）
      })
      return false
    }
  }
  return true
},1000)

const validatePhoneNumber=debounce ((phoneNumber)=>{
  // 定义正则表达式
  const phoneRegex = /^(?:\+?\d{1,3}[- ]?)?\(?\d{2,5}\)?[- ]?\d{3,5}[- ]?\d{3,4}$/;
  if (!form.phone||form.phone.length===0){
    ElMessage.error({
      id:'phone',
      message:'手机号为空!'
    });
  }
  // 测试输入的电话号码是否符合正则表达式
  else if (!phoneRegex.test(phoneNumber)){
    ElMessage.error({
      id:'phone',
      message:"手机号格式错误!",
      center:true,
      duration:2000,
    });
  }
},1000)




const getIdentifyCode =async () => {
  if (isDisabled.value||!validateEmailStrict(form.email)) return false; // 如果按钮被禁用，则不执行
  // TODO: 这里添加获取验证码的逻辑，例如发送网络请求
    const msg= await proxy.$api.getEmailCode({email: form.email,}) ;
    if (msg!=='发送成功')return false;
    ElMessage.success('发送成功!')
  // 设置按钮为禁用状态
  isDisabled.value = true;
  countdown.value = 60; // 重置倒计时
  // 开始倒计时
  const timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer);
      isDisabled.value = false; // 启用按钮
    }
  }, 1000);

  return true
};

const back=()=>{
  clear()
  router.push('/login')
}

// 检测表单是否有空值的函数
const checkForm = () => {
  for (const key in form) {
    if (form[key] === null || form[key] === undefined || form[key].trim() === '') {
      return { isValid: false }; // 返回无效状态
    }
  }
  return { isValid: true }; // 返回有效状态
};

const handlesubmit = async () => {
  const validationResult = checkForm();
  if (validationResult.isValid && identifyCode) {
    // 调试输出
    console.log('提交时验证码:', identifyCode);
    console.log('表单数据:', JSON.parse(JSON.stringify(form)));

    const formData = new FormData();
    // 确保字段顺序正确
    const fields = [
      ['username', form.username],
      ['email', form.email],
      ['passwordHash', form.password],
      ['sex', form.sex],
      ['phone', form.phone],
      ['code', identifyCode.value]
    ];

    fields.forEach(([key, value]) => {
      if (!value) console.warn(`\(  {key} 值为空`);
      formData.append(key, value);
    });


    try {
      const res = await proxy.$api.register(formData);
      if (res === "注册成功!"){
        ElMessage.success(res)
        setTimeout(back, 3000);
      }
      else ElMessage.error(res);

    } catch (error) {
      ElMessage.error(`发送请求失败: ${error.message}`);
    }
  } else {
    ElMessage.error('请填写所有必填项并输入验证码');
  }
};

const clear=()=>{
  form.username = '';
  form.password = '';
  form.rPassword = '';
  form.email = '';
  form.phone = '';
  form.sex = '1';
  identifyCode.value = '';
  IAgree.value = false;
}
onMounted(()=> {
  renderCanvas();
})
</script>

<template>
  <div class="container">
    <canvas width="100%" height="100%" />
    <el-card class="mainContent">
      <el-button type="primary" size="small" @click="back">返回登入</el-button>
      <div slot="header" class="header">
        <h2>用户注册</h2>
      </div>
      <el-divider></el-divider>
      <el-form :model="form" label-width="100px" style="max-width: 600px">
        <el-form-item label="用户名称:">
          <el-input v-model="form.username" placeholder="请输入用户名称" prefix-icon="User" @blur="validateName" />
        </el-form-item>

        <el-form-item label="邮箱:">
          <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="Message" @blur="validateEmailStrict(form.email)" />
        </el-form-item>

        <el-form-item label="手机号:">
          <el-input v-model="form.phone" placeholder="请输入手机号" prefix-icon="Phone" @blur="validatePhoneNumber(form.phone)" />
        </el-form-item>

        <el-form-item label="密码：">
          <el-input type="password" v-model="form.password" prefix-icon="Lock" />
        </el-form-item>

        <el-form-item label="确认密码：">
          <el-input type="password" v-model="form.rPassword" prefix-icon="Lock" @blur="reConf" />
        </el-form-item>

        <el-form-item label="性别:">
          <el-radio-group v-model="form.sex">
            <el-radio value="1">男</el-radio>
            <el-radio value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>



        <el-form-item label="验证码：">
          <el-row class="input-row">
            <el-input placeholder="请输入验证码" v-model="identifyCode" />
            <el-button type="primary" @click="getIdentifyCode"
                       :disabled="isDisabled"
                       plain>  {{ isDisabled ? countdown + '秒' : '获取验证码' }}</el-button>
          </el-row>
        </el-form-item>

        <el-divider></el-divider>
        <el-form-item>
          <el-checkbox v-model="IAgree" label="同意用户使用准则"></el-checkbox>
        </el-form-item>
        <el-form-item style="padding-left: 30px; ">
          <el-button type="primary" v-if="IAgree" @click="handlesubmit">注册</el-button>
          <el-button @click="clear">清空</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped lang="less">
* {
  font-family: 'Poppins', system-ui;
}

.container {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  background: url("/background.jpg") no-repeat center center;
  background-size: cover;
  overflow: hidden;
  position: relative;
}

canvas {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 0;
}

.mainContent {
  width: 60%;
  max-width: 700px;
  margin: 10% auto;
  padding: 40px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}



.header {
  text-align: center;
  h2{
    font-size: 2em;
    font-weight: bolder;
    color: #6b6b6b;
  }
}

.input-row {
  display: flex;
  align-items: center;

}

.input-row .el-input {
  flex: 1;
  margin-right: 10px;
}

.el-divider {
  margin: 20px 0;
}
</style>