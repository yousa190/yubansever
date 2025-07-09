<template>
  <div class="box">
    <div class="content">
      <div class="login-wrapper">
        <h1>易购商城</h1>
        <h2>登录</h2>
        <div class="login-form">
          <div class="username form-item">
            <span>使用邮箱或者手机号</span>
            <input type="text" class="input-item" v-model="username">
          </div>
          <div class="password form-item">
            <span>密码</span>
            <div class="password-input-wrapper">
              <input
                :type="showPassword ? 'text' : 'password'"
                class="input-item"
                v-model="password"
              >
              <span class="toggle-password" @click="showPassword = !showPassword">
                {{ showPassword ? '隐藏' : '显示' }}
              </span>
            </div>
          </div>
          <button class="login-btn" @click="handleLogin">登 录</button>
        </div>
        <div class="divider">
          <span class="line"></span>
          <span class="divider-text">其他方式登录</span>
          <span class="line"></span>
        </div>
        <div class="other-login-wrapper">
          <div class="other-login-item">
            <img src="@/assets/images/QQ.png" alt="">
          </div>
          <div class="other-login-item">
            <img src="@/assets/images/WeChat.png" alt="">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// 定义表单数据
const username = ref('');
const password = ref('');
const showPassword = ref(false);

// 处理登录逻辑
const handleLogin = () => {
  console.log('用户名:', username.value);
  console.log('密码:', password.value);
  // 这里可以添加实际的登录请求代码
};
</script>

<style lang="less" scoped>
/* 这里直接复用你提供的 style.scss 内容 */
* {
  margin: 0;
  padding: 0;
}
/*公共CSS*/
.box {
  width: 100vw;
  height: 100vh;
  background-color: rgb(29, 67, 89);
  .content {
    .login-wrapper {
      h1 {
        text-align: center;
      }
      .login-form {
        .form-item {
          margin: 20px 0;
          span {
            display: block;
            margin: 5px 20px;
            font-weight: 100;
          }
          .input-item {
            width: 100%;
            border-radius: 40px;
            padding: 20px;
            box-sizing: border-box;
            font-size: 20px;
            font-weight: 200;
            &:focus {
              outline: none;
            }
          }
        }
        .login-btn {
          width: 100%;
          border-radius: 40px;
          color: #fff;
          border: 0;
          font-weight: 100;
          margin-top: 10px;
          cursor: pointer;
        }
      }
      .divider {
        width: 100%;
        margin: 20px 0;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        span:nth-child(1) {
          flex: 1;
        }
        span:nth-child(3) {
          flex: 1;
        }
        .line {
          display: inline-block;
          max-width: 30%;
          width: 30%;
        }
        .divider-text {
          vertical-align: middle;
          margin: 0 20px;
          line-height: 0;
          display: inline-block;
          width: 100px;
        }
      }
      .other-login-wrapper {
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .other-login-item {
        border: 1px solid rgb(214, 222, 228);
        padding: 10px;
        margin: 10px;
        cursor: pointer;
      }
    }
  }
}
/*一般大于手机的尺寸CSS*/
@media (min-width: 767px) {
  .box {
    background-color: rgb(29, 67, 89);
    .content {
      width: 85vw;
      height: 90vh;
      background-size: 90% 100%;
      position: absolute;
      right: 15%;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      border-radius: 20px;
      background: #fff url("@/assets/images/login_two.jpg") no-repeat;

      .login-wrapper {
        width: 25vw;
        position: absolute;
        right: 15%;
        top: 50%;
        transform: translateY(-50%);
        h1 {
          text-align: center;
          font-size: 45px;
          color: rgb(250, 87, 87);
          margin-bottom: 40px;
        }
        h2 {
          text-align: center;
          font-size: 23px;
          color: rgb(119, 163, 196);
          margin-bottom: 40px;
        }
        .login-form {
          margin: 10px 0;
          .form-item {
            span {
              color: rgb(81, 100, 115);
            }
            .input-item {
              height: 60px;
              border: 1px solid rgb(214, 222, 228);
            }
          }
          .login-btn {
            height: 50px;
            background-color: rgb(59, 72, 89);
            font-size: 20px;
          }
        }
        .divider {
          .line {
            border-bottom: 1px solid rgb(214, 222, 228);
          }
        }
        .other-login-item {
          border-radius: 20px;
          img {
            width: 40px;
            height: 40px;
          }
        }

      }
    }

    .password-input-wrapper .toggle-password {
      font-size: 16px;
      right: 20px;
    }

    .password .input-item {
      padding-right: 80px !important;
    }
  }
}
/*手机端CSS*/
@media (max-width: 768px) {
  .box {
    .content {
      width: 100vw;
      height: 100vh;
      background: url("@/assets/images/login_bg_phone.png") no-repeat;
      background-size: 100% 100%;
      display: flex;
      align-items: flex-start;
      justify-content: center;
      .login-wrapper {
        width: 70%;
        height: 60%;
        padding-top: 15%;
        h1 {
          font-size: 30px;
          color: #fff;
        }
        .login-form {
          .form-item {
            margin: 10px 0;
            span {
              color: rgb(113, 129, 141);
            }
            .input-item {
              height: 30px;
              border: 1px solid rgb(113, 129, 141);
              background-color: transparent;
              color: #fff;
            }
          }
          .login-btn {
            height: 40px;
            background-color: rgb(235, 95, 93);
            font-size: 16px;
          }
        }
        .divider {
          .line {
            border-bottom: 1px solid #fff;
          }
          .divider-text {
            color: #fff;
          }
        }
        .other-login-item {
          border-radius: 15px;
          img {
            width: 35px;
            height: 35px;
          }
        }
      }
    }
  }
}

.password-input-wrapper {
  position: relative;

  .toggle-password {
    position: absolute;
    right: 15px;
    top: 50%;
    transform: translateY(-50%);
    color: rgb(113, 129, 141);
    cursor: pointer;
    font-size: 14px;
    user-select: none;

    &:hover {
      color: rgb(235, 95, 93);
    }
  }
}

/* 调整密码输入框的padding防止文字重叠 */
.password .input-item {
  padding-right: 60px !important;
}
</style>
