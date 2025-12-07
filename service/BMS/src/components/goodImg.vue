<script setup>
import { ref, watch } from 'vue'
import { Close } from '@element-plus/icons-vue'
import api from '@/api/api.js'
import { ElMessage } from 'element-plus'

// 定义props接收父组件传递的图片数据
const props = defineProps({
  images: {
    type: Array,
    default: () => []
  }
})

// 定义事件，用于向父组件传递更新后的图片数据
const emit = defineEmits(['update:images'])

// 本地图片列表
const previewImages = ref([])

// 跟踪所有已上传的图片（包括已删除的）
const uploadedImages = ref([])

// 监听props变化
watch(() => props.images, (newImages) => {
  previewImages.value = [...newImages]
  // 初始化时，将现有图片添加到已上传列表中
  newImages.forEach(img => {
    // 确保图片对象有正确的字段
    if (!img.imageId) img.imageId = null
    if (!img.productId) img.productId = null
    if (!img.sortOrder) img.sortOrder = 0
    if (img.imageUrl && !uploadedImages.value.includes(img.imageUrl)) {
      uploadedImages.value.push(img.imageUrl)
    }
  })
}, { immediate: true })

// 上传图片前的处理
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('上传图片只能是图片格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isImage && isLt2M
}

// 自定义上传处理
const customUpload = async (options) => {
  const { file, onSuccess, onError } = options
  const formData = new FormData()
  formData.append('img', file)
  formData.append('type', 'goods')

  try {
    const response = await api.uploadImage(formData)
    console.log('上传成功:', response)
    // 处理上传成功
    if (response && response.url) {
      // 根据后端实际返回的格式处理
      const imageUrl = response.url
      const imageName = response.alt || file.name
      const newImage = {
        imageId: null,
        productId: null,
        imageUrl: imageUrl,
        imageName: imageName,
        sortOrder: previewImages.value.length,
        main: previewImages.value.length === 0
      }
      previewImages.value.push(newImage)
      // 添加到已上传图片列表
      uploadedImages.value.push(imageUrl)
      // 通知父组件数据更新
      emit('update:images', previewImages.value)
      // 调用 Element Plus 的成功回调
      onSuccess(response, file)
    } else {
      onError(new Error('上传失败'))
    }
  } catch (error) {
    // 处理上传失败
    onError(error)
  }
}

// 删除图片
const removeImage = (index) => {
  // 从预览列表中移除，但不从已上传列表中移除
  previewImages.value.splice(index, 1)
  // 重新设置主图和排序
  previewImages.value.forEach((img, i) => {
    img.sortOrder = i
    img.main = i === 0
  })
  // 通知父组件数据更新
  emit('update:images', previewImages.value)
}

// 设置主图
const setMainImage = (index) => {
  if (index === 0) return // 如果已经是主图则不操作
  const [mainImage] = previewImages.value.splice(index, 1)
  previewImages.value.unshift(mainImage)
  // 重新设置主图和排序
  previewImages.value.forEach((img, i) => {
    img.sortOrder = i
    img.main = i === 0
  })
  // 通知父组件数据更新
  emit('update:images', previewImages.value)
}

// 清理未使用的图片
const cleanupUnusedImages = async () => {
  // 禁用图片清理功能
  /*
  // 获取当前预览中的图片URL
  const currentImageUrls = previewImages.value.map(img => img.imageUrl)

  // 调用后端接口清理未使用的图片
  try {
    await api.cleanupImages({
      uploadedImages: uploadedImages.value,
      usedImages: currentImageUrls
    })

    // 更新已上传图片列表为当前正在使用的图片
    uploadedImages.value = [...currentImageUrls]
  } catch (error) {
    console.error('清理未使用图片失败:', error)
    ElMessage.error('清理未使用图片失败')
  }
  */
}

// 定义暴露给父组件的方法
defineExpose({
  cleanupUnusedImages
})
</script>

<template>
  <div class="image-upload-container">
    <!-- 上传按钮 -->
    <el-upload
      :http-request="customUpload"
      :before-upload="beforeUpload"
      :show-file-list="false"
      accept="image/*"
      class="upload-button"
    >
      <el-button type="primary">上传商品图片</el-button>
    </el-upload>

    <!-- 图片预览区域 -->
    <div class="image-preview-container">
      <div
        v-for="(image, index) in previewImages"
        :key="index"
        class="image-item"
        :class="{ 'main-image': image.main }"
      >
        <el-image
          :src="image.imageUrl"
          class="preview-image"
          fit="cover"
          :preview-src-list="previewImages.map(img => img.imageUrl)"
          :initial-index="index"
          preview-teleported
        />
        <div class="image-actions">
          <el-icon
            class="remove-icon"
            @click="removeImage(index)"
          >
            <Close />
          </el-icon>
          <el-button
            v-if="!image.main"
            size="small"
            type="primary"
            @click="setMainImage(index)"
            class="set-main-btn"
          >
            设为主图
          </el-button>
        </div>
        <div v-if="image.main" class="main-tag">主图</div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.image-upload-container {
  .upload-button {
    margin-bottom: 15px;
  }

  .image-preview-container {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;

    .image-item {
      position: relative;
      border: 1px solid #dcdfe6;
      border-radius: 6px;
      overflow: hidden;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        border-color: #409eff;
      }

      &.main-image {
        border-color: #409eff;
      }

      .preview-image {
        width: 120px;
        height: 120px;
        display: block;
      }

      .image-actions {
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s;

        .remove-icon {
          color: white;
          font-size: 18px;
          cursor: pointer;
          margin-right: 5px;
        }

        .set-main-btn {
          margin-left: 5px;
        }
      }

      &:hover .image-actions {
        opacity: 1;
      }

      .main-tag {
        position: absolute;
        top: 0;
        left: 0;
        background: #409eff;
        color: white;
        font-size: 12px;
        padding: 2px 5px;
        border-radius: 0 0 4px 0;
      }
    }
  }
}
</style>
