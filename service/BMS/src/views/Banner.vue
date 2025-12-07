<template>
  <div class="banner-management">
    <!-- 页面标题和操作按钮 -->
    <div class="banner-header">
      <h1>轮播图管理</h1>
      <el-button type="primary" @click="openAddDialog">添加轮播图</el-button>
    </div>

    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchBanners">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 轮播图列表 -->
    <el-card>
      <el-table :data="bannerList" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" width="150"></el-table-column>
        <el-table-column prop="imageUrl" label="图片" width="200">
          <template #default="scope">
            <el-image 
              :src="scope.row.imageUrl" 
              class="banner-image"
              fit="cover"
              :preview-src-list="[scope.row.imageUrl]"
              preview-teleported>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="targetType" label="跳转类型" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.targetType === 0 ? 'warning' : (scope.row.targetType === 1 ? 'success' : 'primary')">
              {{ scope.row.targetType === 0 ? '公告' : (scope.row.targetType === 1 ? '商品' : '广告') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetId" label="目标ID" width="100"></el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch 
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)">
            </el-switch>
            <span :class="['status-text', { 'enabled': scope.row.status === 1 }]">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除该轮播图吗？" @confirm="deleteBanner(scope.row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="600px"
      @close="handleDialogClose">
      <el-form :model="bannerForm" :rules="formRules" ref="bannerFormRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="bannerForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            :http-request="customUpload"
            :before-upload="beforeImageUpload"
            :show-file-list="false"
            accept="image/*"
            class="image-uploader">
            <img v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" class="image-preview">
            <el-icon v-else class="image-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="跳转类型" prop="targetType">
          <el-select v-model="bannerForm.targetType" placeholder="请选择跳转类型">
            <el-option label="公告" :value="0"></el-option>
            <el-option label="商品" :value="1"></el-option>
            <el-option label="广告" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标ID" prop="targetId">
          <el-input-number v-model="bannerForm.targetId" :min="1" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="bannerForm.sortOrder" :min="0" controls-position="right"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch 
            v-model="bannerForm.status"
            :active-value="1"
            :inactive-value="0">
          </el-switch>
          <span :class="['status-text', { 'enabled': bannerForm.status === 1 }]">
            {{ bannerForm.status === 1 ? '启用' : '禁用' }}
          </span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api/api.js'

const { proxy } = getCurrentInstance()

// 数据相关
const bannerList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const uploadedImages = ref([]) // 用于跟踪已上传的图片

// 表单引用
const bannerFormRef = ref(null)

// 搜索表单
const searchForm = reactive({
  title: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 轮播图表单
const bannerForm = reactive({
  id: null,
  title: '',
  imageUrl: '',
  targetType: 0,
  targetId: null,
  sortOrder: 0,
  status: 1
})

// 表单验证规则
const formRules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' }
  ],
  imageUrl: [
    { required: true, message: '请上传图片', trigger: 'change' }
  ],
  targetType: [
    { required: true, message: '请选择跳转类型', trigger: 'change' }
  ],
  targetId: [
    { required: true, message: '请输入目标ID', trigger: 'blur' }
  ]
}

// 生命周期钩子
onMounted(() => {
  fetchBannerList()
})

// 获取轮播图列表
const fetchBannerList = async () => {
  try {
    const params = {
      title: searchForm.title,
      page: pagination.currentPage,
      limit: pagination.pageSize
    }
    const res = await proxy.$api.getBannerList(params)
    bannerList.value = res.list
    pagination.total = res.count
  } catch (err) {
    ElMessage.error('获取轮播图列表失败: ' + err.message)
  }
}

// 搜索轮播图
const searchBanners = () => {
  pagination.currentPage = 1
  fetchBannerList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.title = ''
  pagination.currentPage = 1
  fetchBannerList()
}

// 打开添加对话框
const openAddDialog = () => {
  dialogTitle.value = '添加轮播图'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogTitle.value = '编辑轮播图'
  isEdit.value = true
  Object.assign(bannerForm, row)
  // 将当前图片添加到已上传列表中
  if (row.imageUrl) {
    uploadedImages.value = [row.imageUrl]
  } else {
    uploadedImages.value = []
  }
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  Object.assign(bannerForm, {
    id: null,
    title: '',
    imageUrl: '',
    targetType: 0,
    targetId: null,
    sortOrder: 0,
    status: 1
  })
  // 清空已上传图片记录
  uploadedImages.value = []
}

// 图片上传前检查
const beforeImageUpload = (rawFile) => {
  const isImage = rawFile.type.startsWith('image/')
  const isLt2M = rawFile.size / 1024 / 1024 < 2

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
  formData.append('type', 'banner')

  try {
    const response = await api.uploadImage(formData)
    console.log('上传成功:', response)
    // 处理上传成功
    if (response && response.url) {
      // 根据后端实际返回的格式处理
      bannerForm.imageUrl = response.url
      // 添加到已上传图片列表
      uploadedImages.value.push(response.url)
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

// 清理未使用的图片
const cleanupUnusedImages = async () => {
  // 获取当前使用的图片URL
  const currentImageUrl = bannerForm.imageUrl

  // 过滤出未使用的图片
  const unusedImages = uploadedImages.value.filter(url => url !== currentImageUrl)

  // 如果有未使用的图片，调用后端接口清理
  if (unusedImages.length > 0) {
    try {
      await proxy.$api.cleanupImages({
        imageUrls: unusedImages
      })
      // 更新已上传图片列表为当前正在使用的图片
      uploadedImages.value = currentImageUrl ? [currentImageUrl] : []
    } catch (error) {
      console.error('清理未使用图片失败:', error)
      ElMessage.error('清理未使用图片失败')
    }
  }
}

// 关闭对话框
const handleDialogClose = async () => {
  bannerFormRef.value.resetFields()
  // 清理未使用的图片
  await cleanupUnusedImages()
}

// 处理用户取消操作
const handleCancel = async () => {
  dialogVisible.value = false
  // 清理未使用的图片
  await cleanupUnusedImages()
}

// 提交表单
const submitForm = () => {
  bannerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let res
        if (isEdit.value) {
          // 编辑
          res = await proxy.$api.updateBanner(bannerForm)
        } else {
          // 添加
          res = await proxy.$api.addBanner(bannerForm)
        }
        
        ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
        dialogVisible.value = false
        // 清空已上传图片记录
        uploadedImages.value = []
        fetchBannerList()
      } catch (err) {
        ElMessage.error((isEdit.value ? '编辑失败: ' : '添加失败: ') + err.message)
      }
    }
  })
}

// 删除轮播图
const deleteBanner = async (id) => {
  try {
    await proxy.$api.deleteBanner({ id })
    ElMessage.success('删除成功')
    fetchBannerList()
  } catch (err) {
    ElMessage.error('删除失败: ' + err.message)
  }
}

// 处理状态变更
const handleStatusChange = async (row) => {
  try {
    await proxy.$api.updateBanner(row)
    ElMessage.success('状态更新成功')
  } catch (err) {
    // 如果更新失败，恢复原状态
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败: ' + err.message)
  }
}

// 分页相关方法
const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchBannerList()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  fetchBannerList()
}
</script>

<style scoped lang="less">
.banner-management {
  padding: 20px;
  
  .banner-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h1 {
      margin: 0;
      font-size: 24px;
      color: #303133;
    }
  }
  
  .search-card {
    margin-bottom: 20px;
    
    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }
  
  .banner-image {
    width: 100%;
    height: 80px;
    border-radius: 4px;
  }
  
  .status-text {
    margin-left: 10px;
    font-size: 14px;
    
    &.enabled {
      color: #67c23a;
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  
  .image-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    width: 178px;
    height: 178px;
    
    &:hover {
      border-color: var(--el-color-primary);
    }
    
    .image-preview {
      width: 178px;
      height: 178px;
      display: block;
    }
    
    .image-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      text-align: center;
      line-height: 178px;
    }
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>