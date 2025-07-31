<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    required: false,
    default: 'yigou',
  }
})
</script>

<template>
  <div class="my-panel">
    <el-container class="panel-container"> <!-- 添加容器类名 -->
      <!-- 头部区域 -->
      <el-header class="header-container">
        <div class="header-title">
          <h3>
            {{ props.title }}
            <small>{{ props.subtitle }}</small>
          </h3>
        </div>
        <!-- 右侧插槽 -->
        <div class="header-right">
          <slot name="right" />
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="content-area"> <!-- 添加内容区域类名 -->
        <slot name="banner" />
        <slot name="content" />
      </el-main>
    </el-container>
  </div>
</template>

<style scoped lang="less">
.my-panel {



  height: auto;
  overflow: visible;

  /* 容器基础样式 */
  .panel-container {
    height: auto;
    display: flex;
    flex-direction: column; /* 垂直排列头部和内容 */
  }

  .header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    /* 确保头部不限制高度 */
    height: auto;
    min-height: 60px; /* 保持最小高度，避免内容过短时变形 */
  }

  .header-title {
    flex: 1;

    h3 {
      font-size: 28px;
      text-align: left;

      small {
        font-size: 16px;
        color: #999;
        margin-left: 14px;
      }
    }
  }

  .header-right {
    white-space: nowrap;
    /* 允许右侧内容自然换行（如需） */
    /* white-space: normal; */
  }

  /* 内容区域关键样式 */
  .content-area {
    flex: none; /* 取消弹性伸缩，避免被压缩 */
    padding: 0; /* 移除默认内边距（根据需要调整） */
    overflow: visible !important; /* 强制允许内容溢出 */
    height: auto !important; /* 确保高度自动适应内容 */
  }
}

/* 移除element-ui可能自带的高度限制 */
::v-deep .el-container {
  height: auto !important;
}

::v-deep .el-main {
  padding: 0;
  height: auto !important;
  overflow: visible !important;
}
</style>
