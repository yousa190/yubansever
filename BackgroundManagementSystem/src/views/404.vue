<template>
  <div class="not-found">
    <div class="content">
      <h1 class="status-code">404</h1>
      <p class="message">抱歉，您访问的页面不存在...</p>
      <el-button @click="goHome" class="go-home">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from 'vue-router'

const router = useRouter()
const goHome =  ()=>{
  router.push('/')
}

</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f7f8fa;
  color: #2c3e50;
  font-family: "Arial", sans-serif;
}

.content {
  text-align: center;
}

.status-code {
  font-size: 8rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #ff6b6b;
}

.message {
  font-size: 1.5rem;
  color: #666;
  margin-bottom: 2rem;
}

.go-home {
  font-size: 1.2rem;
  color: #fff;
  background-color: #409eff;
  padding: 0.8rem 1.5rem;
  border-radius: 4px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.go-home:hover {
  background-color: #66b1ff;
}
</style>
