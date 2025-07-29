<script setup>
import { ref } from 'vue'
import { Close } from '@element-plus/icons-vue'

const previewImages = ref(['https://picsum.photos/id/416/10'])

// 上传成功回调
const handleSuccess = (response, file) => {
  // 假设返回数据包含图片 URL（根据你的 API 调整）
  const imageUrl = URL.createObjectURL(file.raw)  // 本地预览方案
  previewImages.value.push(imageUrl)
}

// 删除图片
const removeImage = (index) => {
  previewImages.value.splice(index, 1)
}
</script>
<template>
  <!-- 使用 flex 布局横向排列 -->
  <div style="display: flex; align-items: center; gap: 10px">
    <!-- 上传按钮 -->
    <el-upload
        action="https://your-upload-api.com"
    :on-success="handleSuccess"
    :show-file-list="false"
    accept="image/*"
    >
    <el-button type="primary">上传商品图片</el-button>
    </el-upload>

    <!-- 图片预览区域 -->
    <div style="display: flex; gap: 8px; overflow-x: auto; max-width: 400px">
      <div
          v-for="(url, index) in previewImages"
          :key="index"
          style="position: relative;"
      >
        <img
            :src="url"
            style="height: 80px; object-fit: contain; border: 1px solid #ddd"
        >
        <!-- 删除按钮 -->
        <el-icon
            style="position: absolute; top: 2px; right: 2px; cursor: pointer"
            @click="removeImage(index)"
        >
          <Close />
        </el-icon>
      </div>
    </div>
  </div>
</template>
