<script setup lang="js">
import goodTree from '@/components/goodTree.vue'
import goodImg from '@/components/goodImg.vue'
import WangEditor from '@/components/WangEditor.vue'
import {reactive, ref, onMounted, getCurrentInstance,nextTick} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const dialogVisible = ref(false)
const innerdialogVisible = ref(false)
const action = ref('add')
const selectedGoods = ref([]) // 用于存储选中的商品

// 图片上传组件引用
const goodImgRef = ref(null)

const goodForm=reactive({
  productId: null,
  productName: '',
  catId: null,
  catName: '',
  brand: '',
  price: 0,
  marketPrice: 0,
  costPrice: 0,
  stock: 0,
  salesCount: 0,
  productSn: '',
  productDesc: '',
  detailContent: '',
  onSale: true,
  isNew: false,
  isHot: false,
  unit: '',
  status: 1,
  images: []
})
const {proxy} =getCurrentInstance()
const formInline =reactive({
  productName: '',
  brand: '',
  productSn: '',
  catId: null,
  minPrice: null,
  maxPrice: null,
  minStock: null,
  maxStock: null
})
const tableLabels = reactive([
  {
    prop: 'productId',
    label: '商品ID',
  },
  {
    prop: 'productName',
    label: '商品名称',
    width: 200,
  },
  {
    prop: 'productSn',
    label: '商品编号',
    width: 150,
  },
  {
    prop: 'price',
    label: '价格',
    width: 120,
  },
  {
    prop: 'marketPrice',
    label: '市场价',
    width: 120,
  },
  {
    prop: 'stock',
    label: '库存',
    width: 120,
  },
  {
    prop: 'salesCount',
    label: '销量',
    width: 120,
  },
  {
    prop: 'catName',
    label: '商品分类',
    width: 200,
  },
  {
    prop: 'brand',
    label: '品牌',
    width: 150,
  },
  {
    prop: 'images',
    label: '商品图片',
    width: 150,
    showTooltip: true
  },
  {
    prop: 'onSale',
    label: '上架状态',
    width: 100,
    formatter: (row) => row.onSale ? '上架' : '下架'
  },
  {
    prop: 'isNew',
    label: '新品',
    width: 80,
    formatter: (row) => row.isNew ? '是' : '否'
  },
  {
    prop: 'isHot',
    label: '热销',
    width: 80,
    formatter: (row) => row.isHot ? '是' : '否'
  }
])
const goodData = ref([])
const resetForm = () => {
  if (proxy.$refs.formgood) {
    proxy.$refs.formgood.resetFields();
    goodForm.detailContent=''
  }
}
const config=reactive({
  productName: '',
  brand: '',
  productSn: '',
  catId: null,
  minPrice: null,
  maxPrice: null,
  minStock: null,
  maxStock: null,
  page: 1,
  pageSize: 10,
  total: 0
})
const rules = reactive({
  productName:[{ required: true, message: "商品名是必填项", trigger: "blur" }],
  catId: [{ required: true, message: "分类是必选项", trigger: "blur" }],
  price:[{  required: true, message: "请输入价格", trigger: "blur" },
    {type:'number', message: "价格必须是数字", trigger: "change",}],
  stock:[{  required: true, message: "请输入库存", trigger: "blur" },
    {type:'number', message: "库存必须是数字", trigger: "change",}],
  productDesc: [{ required: false, message: "请输入商品简介", trigger: "blur" }]
})
const catinfo =ref({})
const categoryParams = ref([])

const setConfig=(data)=>{
  catinfo.value=data
}

const setCatVal =()=>{
  innerdialogVisible.value=false
  goodForm.catId=catinfo.value.catId
  goodForm.catName=catinfo.value.catName
  // 保存分类ID，用于获取分类参数
  // TODO: 在这里调用接口获取分类参数
  // getCategoryParams(catinfo.value.catId)
  console.log(goodForm)
}

const updatecomment=(data)=>{
  goodForm.detailContent=data.value
  // console.log(goodForm.comment)
}

const getgoodData= async ()=>{
  let res= await proxy.$api.getGoodList(config)
  goodData.value=res.list
  config.total=res.count
}

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedGoods.value = selection
}

// 批量删除商品
const batchDeleteGoods = async () => {
  if (selectedGoods.value.length === 0) {
    ElMessage({
      message: '请先选择要删除的商品',
      type: 'warning'
    })
    return
  }

  ElMessageBox.confirm(
    `确定要删除选中的${selectedGoods.value.length}个商品吗？此操作不可恢复！`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 执行批量删除
      const deletePromises = selectedGoods.value.map(item => 
        proxy.$api.deleteGood({ productId: item.productId })
      )
      
      await Promise.all(deletePromises)
      
      ElMessage({
        message: '批量删除成功',
        type: 'success'
      })
      
      // 清空选择
      selectedGoods.value = []
      // 重新加载数据
      getgoodData()
    } catch (error) {
      ElMessage({
        message: '批量删除失败',
        type: 'error'
      })
    }
  })
}

// 批量上架/下架商品
const batchToggleSaleStatus = async (onSale) => {
  if (selectedGoods.value.length === 0) {
    ElMessage({
      message: '请先选择商品',
      type: 'warning'
    })
    return
  }

  const statusText = onSale ? '上架' : '下架'
  
  ElMessageBox.confirm(
    `确定要将选中的${selectedGoods.value.length}个商品${statusText}吗？`,
    '操作确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 执行批量操作
      const updatePromises = selectedGoods.value.map(item => {
        const updatedItem = { ...item, onSale }
        return proxy.$api.editGood(updatedItem)
      })
      
      await Promise.all(updatePromises)
      
      ElMessage({
        message: `批量${statusText}成功`,
        type: 'success'
      })
      
      // 清空选择
      selectedGoods.value = []
      // 重新加载数据
      getgoodData()
    } catch (error) {
      ElMessage({
        message: `批量${statusText}失败`,
        type: 'error'
      })
    }
  })
}

// 批量设置热销/新品状态
const batchSetStatus = async (field, value) => {
  if (selectedGoods.value.length === 0) {
    ElMessage({
      message: '请先选择商品',
      type: 'warning'
    })
    return
  }

  const statusText = field === 'isHot' ? (value ? '设为热销' : '取消热销') : 
                    field === 'isNew' ? (value ? '设为新品' : '取消新品') : ''

  ElMessageBox.confirm(
    `确定要将选中的${selectedGoods.value.length}个商品${statusText}吗？`,
    '操作确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 执行批量操作
      const updatePromises = selectedGoods.value.map(item => {
        const updatedItem = { ...item, [field]: value }
        return proxy.$api.editGood(updatedItem)
      })
      
      await Promise.all(updatePromises)
      
      ElMessage({
        message: `批量${statusText}成功`,
        type: 'success'
      })
      
      // 清空选择
      selectedGoods.value = []
      // 重新加载数据
      getgoodData()
    } catch (error) {
      ElMessage({
        message: `批量${statusText}失败`,
        type: 'error'
      })
    }
  })
}

const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  // 将查询条件从 formInline 复制到 config
  Object.keys(formInline).forEach(key => {
    config[key] = formInline[key];
  });
  // 重置页码为第一页
  config.page = 1;
  getgoodData()
}

const handleClear = () => {
  // 清空查询条件
  Object.keys(formInline).forEach(key => {
    if (key === 'minPrice' || key === 'maxPrice' || key === 'minStock' || key === 'maxStock') {
      formInline[key] = null;
    } else {
      formInline[key] = '';
    }
  });
  
  // 同步清空 config 中的查询条件
  Object.keys(config).forEach(key => {
    if (key !== 'page' && key !== 'pageSize' && key !== 'total') {
      if (key === 'minPrice' || key === 'maxPrice' || key === 'minStock' || key === 'maxStock') {
        config[key] = null;
      } else {
        config[key] = '';
      }
    }
  });
  
  // 重新加载数据
  getgoodData();
}

const openAddDialog = ()=>{
  action.value = 'add';
  Object.assign(goodForm, {
    productId: null,
    productName: '',
    catId: null,
    catName: '',
    brand: '',
    price: 0,
    marketPrice: 0,
    costPrice: 0,
    stock: 0,
    salesCount: 0,
    productSn: '',
    productDesc: '',
    detailContent: '',
    onSale: true,
    isNew: false,
    isHot: false,
    unit: '',
    status: 1,
    images: []
  });
  dialogVisible.value = true;

  // 等待 DOM 更新后操作表单
  nextTick(() => {
      resetForm()
  });
}

const openEditDialog=async (row)=>{
  action.value = 'edit';
  // 获取商品详情
  let res = await proxy.$api.getGoodDetail({productId: row.productId})
  
  // 确保图片数据格式正确
  if (res.images && Array.isArray(res.images)) {
    res.images = res.images.map((img, index) => ({
      ...img,
      imageName: img.imageName || `Image_${index + 1}`,
      sortOrder: img.sortOrder !== undefined ? img.sortOrder : index,
      main: img.main !== undefined ? img.main : index === 0
    }));
  } else {
    res.images = [];
  }
  
  Object.assign(goodForm, res);
  dialogVisible.value = true;
}

const handleClose = async (done) => {
  dialogVisible.value = false
  // 重置表单数据
  resetForm()
  // 重新获取用户列表数据
  getgoodData();
  // 关闭弹窗
  done();
};

const handleCancel = async () => {
  dialogVisible.value = false;
  resetForm()
}

const delgood=(row)=>{
  ElMessageBox.confirm("是否删除该商品?").then(async ()=>{
        await proxy.$api.deleteGood({productId: row.productId})
        ElMessage({
          showClose:false,
          message:"删除成功",
          type:"success",
          duration:3000,
          offset:150,
          center:true,
        })
        getgoodData()
      }
  ).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除',
        })
      })
}

const  onSubmit= async ()=>{
  try {
    // 1. 表单校验
    const valid = await proxy.$refs.formgood.validate();
    if (!valid) return; // 校验失败直接退出

    // 3. 根据操作类型调用接口
    let res;
    let imageRes = null;
    if (action.value === 'add') {
      // 新增商品
      res = await proxy.$api.addGood(goodForm);
    } else {
      // 编辑商品需要传递商品ID
      // 分别调用商品信息更新和图片更新接口
      res = await proxy.$api.editGood({
        ...goodForm,
        images: undefined, // 不传递图片数据到商品更新接口
        specs: undefined // 不传递规格参数到商品更新接口
      });
      
      // 调用专门的图片更新接口
      if (goodForm.productId && goodForm.images) {
        imageRes = await proxy.$api.updateProductImages(
          goodForm.productId,
          goodForm.images
        );
      }
      
      // 调用专门的规格参数更新接口（如果需要）
      // 这里可以根据业务需求决定是否需要单独处理规格参数
    }

    console.log(res, imageRes)

    // 注意：由于 axios 拦截器只返回 data 字段，所以直接判断 res 是否为 truthy 值
    if (res && (imageRes === null || imageRes)) { 
      // 接口调用成功
      ElMessage({
        message: action.value === 'add' ? '新增成功' : '编辑成功',
        type: 'success',
        duration: 3000,
        showClose: true
      });
      dialogVisible.value = false;
      resetForm()
      getgoodData();
    } else {
      ElMessage('操作失败'); // 显示错误信息
    }
  } catch (error) {
    // 5. 统一错误处理
    console.error('表单提交异常:', error);
    ElMessage({
      message: error.response?.data?.message || '表单有误,请正确填写',
      type: 'error',
      duration: 5000,
      showClose: true
    });
  }
}

const handlePageChange =(page)=>{
  config.page=page
  getgoodData()
}

// 批量操作处理函数
const handleBatchCommand = (command) => {
  switch (command) {
    case 'delete':
      batchDeleteGoods()
      break
    case 'onSale':
      batchToggleSaleStatus(true)
      break
    case 'offSale':
      batchToggleSaleStatus(false)
      break
    case 'setHot':
      batchSetStatus('isHot', true)
      break
    case 'cancelHot':
      batchSetStatus('isHot', false)
      break
    case 'setNew':
      batchSetStatus('isNew', true)
      break
    case 'cancelNew':
      batchSetStatus('isNew', false)
      break
    default:
      break
  }
}

onMounted(()=>{
  getgoodData()
})
</script>

<template>
  <div class="good-header">
    <div class="header-left">
      <el-button type="primary" @click="openAddDialog">添加商品</el-button>
      
      <!-- 批量操作按钮 -->
      <el-dropdown v-if="selectedGoods.length > 0">
        <el-button type="danger">
          批量操作 <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="batchDeleteGoods">批量删除</el-dropdown-item>
            <el-dropdown-item @click="batchToggleSaleStatus(true)">批量上架</el-dropdown-item>
            <el-dropdown-item @click="batchToggleSaleStatus(false)">批量下架</el-dropdown-item>
            <el-dropdown-item @click="batchSetStatus('isHot', true)">设为热销</el-dropdown-item>
            <el-dropdown-item @click="batchSetStatus('isHot', false)">取消热销</el-dropdown-item>
            <el-dropdown-item @click="batchSetStatus('isNew', true)">设为新品</el-dropdown-item>
            <el-dropdown-item @click="batchSetStatus('isNew', false)">取消新品</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      
      <el-tag v-if="selectedGoods.length > 0" type="danger" effect="dark">
        已选择 {{ selectedGoods.length }} 项
      </el-tag>
    </div>
    
    <el-form :inline="true" :model="formInline" @submit.native.prevent class="search-form">
      <el-row :gutter="20">
        <el-col :span="5">
          <el-form-item label="商品名称">
            <el-input 
              placeholder="请输入商品名" 
              v-model="formInline.productName"
              @keyup.enter="handleSearch"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="5">
          <el-form-item label="品牌">
            <el-input 
              placeholder="请输入品牌" 
              v-model="formInline.brand"
              @keyup.enter="handleSearch"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="5">
          <el-form-item label="商品编号">
            <el-input 
              placeholder="请输入商品编号" 
              v-model="formInline.productSn"
              @keyup.enter="handleSearch"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="5">
          <el-form-item label="最低价格">
            <el-input 
              placeholder="最低价格" 
              v-model.number="formInline.minPrice"
              @keyup.enter="handleSearch"
            ></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="4">
          <el-form-item label="最高价格">
            <el-input 
              placeholder="最高价格" 
              v-model.number="formInline.maxPrice"
              @keyup.enter="handleSearch"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="5">
          <el-form-item label="最低库存">
            <el-input 
              placeholder="最低库存" 
              v-model.number="formInline.minStock"
              @keyup.enter="handleSearch"
            ></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="5">
          <el-form-item label="最高库存">
            <el-input 
              placeholder="最高库存" 
              v-model.number="formInline.maxStock"
              @keyup.enter="handleSearch"
            ></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="14">
          <el-form-item class="search-buttons">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleClear">清空</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
  <div class="table">
    <el-card>
      <el-table :data="goodData"  type="" stripe border ref="table" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column v-for="item in tableLabels "
                         :key="item.prop"
                         :width="item.width ? item.width : '120px'"
                         :prop="item.prop"
                         :label="item.label"
                         :formatter="item.formatter"
                         :show-overflow-tooltip="item.showTooltip" >
          <template v-if="item.prop === 'images'" #default="scope">
            <div class="image-cell" v-if="scope.row.images && scope.row.images.length > 0">
              <el-image
                :src="scope.row.images[0].imageUrl"
                class="table-image"
                :preview-src-list="scope.row.images.map(img => img.imageUrl)"
                preview-teleported
                fit="cover"
              />
              <span v-if="scope.row.images.length > 1" class="image-count">
                +{{ scope.row.images.length - 1 }}
              </span>
            </div>
            <span v-else>暂无图片</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="140" width="240">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="openEditDialog(scope.row)" >
              Edit
            </el-button>
            <el-button link type="danger" size="small" @click="delgood(scope.row)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
            class="pager"
            background
            layout="prev, pager, next, jumper, total"
            :page-size="config.pageSize"
            :total="config.total"
            prev-text="上一页"
            next-text="下一页"
            @current-change="handlePageChange"
            :hide-on-single-page="true"
        />
      </div>

    </el-card>
  </div>
  <el-dialog
      v-model="dialogVisible"
      :title="action === 'add' ? '新增商品' : '编辑商品'"
      width="80%"
      :style="{ 'max-width': '1200px' }"
      :before-close="handleClose"
      class="custom-dialog"
  >
    <!-- 嵌套对话框 -->
    <el-dialog
        :title="'商品分类'"
        width="35%"
        v-model="innerdialogVisible"
        append-to-body
        class="inner-dialog"
    >
      <good-tree  @config-sent="setConfig" />

      <el-row style="justify-content: flex-end">
        <el-form-item>
          <el-button type="primary" @click="innerdialogVisible=false">取消</el-button>
          <el-button type="primary" @click="setCatVal">确定</el-button>
        </el-form-item>
      </el-row>
    </el-dialog>

    <el-form  ref="formgood"  :model="goodForm" :rules="rules"   >
      <el-tabs type="border-card">
        <el-tab-pane label="基础信息">
          <div class="form-section">
            <el-row>
              <el-col :span="12">
                <el-form-item label="商品名称:" prop="productName">
                  <el-input v-model="goodForm.productName" placeholder="请输入商品名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品分类:" prop="catId">
                  <el-button type="primary" @click="innerdialogVisible=true" size="default">选择商品分类</el-button>
                  <el-text v-if="goodForm.catName" size="large" type="success">{{ goodForm.catName }}</el-text>
                  <el-text v-else type="danger">请选择商品分类</el-text>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="12">
                <el-form-item label="品牌:" prop="brand">
                  <el-input v-model="goodForm.brand" placeholder="请输入品牌" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品编号:" prop="productSn">
                  <el-input v-model="goodForm.productSn" placeholder="请输入商品编号" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="8">
                <el-form-item label="价格:" prop="price">
                  <el-input v-model.number="goodForm.price" placeholder="请输入价格" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="市场价:" prop="marketPrice">
                  <el-input v-model.number="goodForm.marketPrice" placeholder="请输入市场价" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="成本价:" prop="costPrice">
                  <el-input v-model.number="goodForm.costPrice" placeholder="请输入成本价" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="8">
                <el-form-item label="库存:" prop="stock">
                  <el-input v-model.number="goodForm.stock" placeholder="请输入库存" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="销量:" prop="salesCount">
                  <el-input v-model.number="goodForm.salesCount" placeholder="请输入销量" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位:" prop="unit">
                  <el-input v-model="goodForm.unit" placeholder="请输入单位" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="24">
                <el-form-item label="商品简介:" prop="productDesc">
                  <el-input 
                    v-model="goodForm.productDesc" 
                    type="textarea"
                    :rows="3"
                    placeholder="请输入商品简介" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="24">
                <el-form-item label="状态设置:">
                  <el-checkbox v-model="goodForm.onSale">
                    上架
                  </el-checkbox>
                  <el-checkbox v-model="goodForm.isNew">
                    新品
                  </el-checkbox>
                  <el-checkbox v-model="goodForm.isHot">
                    热销
                  </el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="详细内容">
          <div class="form-section">
            <el-row>
              <el-col :span="24">
                <el-form-item label="详细内容:" prop="detailContent">
                  <div class="editor-wrapper">
                    <wang-editor  :Content="goodForm.detailContent"  @comment-sent="updatecomment" />
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="商品图片">
          <div class="form-section">
            <el-alert
              title="图片上传说明"
              type="info"
              description="第一张图片将作为主图显示在商品列表中，您可以点击图片设置为主图"
              show-icon
              style="margin-bottom: 20px;"
            />
            <el-row>
              <el-col :span="24">
                <div class="upload-section">
                  <good-img 
                    ref="goodImgRef"
                    :images="goodForm.images" 
                    @update:images="goodForm.images = $event" 
                  />
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
      
      <!-- 操作按钮 -->
      <div class="action-buttons" >
        <el-form-item>
          <el-button type="primary" @click="handleCancel"  >取消</el-button>
          <el-button type="info" @click="resetForm"  >重置</el-button>
          <el-button type="primary" @click="onSubmit">确定</el-button>
        </el-form-item>
      </div>
    </el-form>
  </el-dialog>
</template>

<style scoped lang="less">
.good-header{
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;

  .header-left {
    display: flex;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;

    .el-dropdown {
      margin-left: 10px;
    }
  }
}

.search-form {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 20px;

  :deep(.el-form-item__label) {
    font-weight: 500;
  }

  :deep(.el-select) {
    width: 100%;
  }

  :deep(.el-input__wrapper) {
    width: 100%;
  }

  .search-buttons {
    display: flex;
    gap: 10px;
    align-items: center;
    height: 100%;
  }
}

.table {
  position: relative;
  height: 80%;
  max-height: 900px;
}

/* 商品图片容器样式 */
.goods-image {
  /* 基础布局 */
  display: block;
  width: 100%;
  height: 20px;

  /* 视觉设计 */
  border-radius: 8px; /* 现代圆角设计 */
  box-shadow: 0 2px 8px rgba(0,0,0,0.1); /* 柔和投影 */
  border: 1px solid #f0f0f0; /* 浅色边框 */

  /* 过渡动画 */
  transition: all 0.3s ease-in-out;

  /* 图片处理 */
  object-fit: cover; /* 保持比例填充容器 */
  background-color: #f8f9fa; /* 加载时占位背景 */

  /* 响应式保障 */
  min-height: 80px; /* 最小显示高度 */
  aspect-ratio: 1; /* 强制1:1比例 */
}

/* 悬停交互效果 */
.goods-image:hover {
  transform: scale(1.05); /* 微放大效果 */
  box-shadow: 0 4px 12px rgba(0,0,0,0.15); /* 加强投影 */
  cursor: zoom-in; /* 光标提示可缩放 */
}

.image-cell {
  display: flex;
  align-items: center;
  gap: 5px;
}

.table-image {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  cursor: pointer;
}

.image-count {
  font-size: 12px;
  color: #999;
}

/* 全局 CSS 文件 */
.el-row {
  margin: 10px 0;
}

.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: flex-start;
  position: relative;
  padding-right: 10px;

  &:deep(.pager) {
    position: static !important; // 取消绝对定位
    margin-bottom: 20px;

    .btn-prev,
    .btn-next,
    .number {
      min-width: 32px;
      height: 32px;
      line-height: 32px;
      border-radius: 4px;
      margin: 0 2px;
      transition: all 0.3s;

      &:hover {
        background: #f5f7fa;
        transform: translateY(-1px);
      }
    }

    // 当前页高亮
    .number.active {
      background: var(--el-color-primary);
      color: white;
      font-weight: 500;

      &:hover {
        background: var(--el-color-primary-light-3);
      }
    }

    // 跳转输入框样式
    .el-pagination__jump {
      margin-left: 16px;

      .el-input__inner {
        width: 60px;
        margin: 0 8px;
        text-align: center;
      }
    }

    // 总数样式
    .el-pagination__total {
      padding-left: 22px;
      margin-left: 16px;
      color: #606266;
    }
  }
}


// 对话框整体样式
.el-dialog {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  :deep(.el-dialog__header) {
    margin: 0 ;
    padding: 12px 12px;
    background: #f8f9fa;
    border-bottom: 1px solid #e9ecef;

    .el-dialog__title {
      font-size: 18px;
      color: #2c3e50;
      font-weight: 600;
    }
  }

  :deep(.el-dialog__body) {
    padding: 24px;
  }
}

// 表单容器
.el-form {
  .el-row {
    margin-bottom: 18px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}

// 表单项样式
.el-form-item {
  //width: 100%;
  margin-right: 0;

  :deep(.el-form-item__label) {
    font-weight: 500;
    color: #495057;
    padding-right: 15px;
    width: 120px; // 固定标签宽度
    text-align: right;
  }

  .el-input, .el-text {
    width: 100%;
    max-width: 400px;
  }
}

// 分类选择区域
.category-section {
  margin-bottom: 24px;

  .el-button {
    padding: 10px 20px;
    border-radius: 6px;
  }

  .el-text {
    margin-left: 15px;
    font-size: 14px;

    &[type="success"] {
      color: #10b981;
      font-weight: 500;
    }

    &[type="danger"] {
      color: #f56565;
    }
  }
}

// 按钮组样式
.action-buttons {
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
  margin-top: 24px;

  .el-button {
    padding: 10px 24px;
    border-radius: 6px;
    margin-left: 12px;

    + .el-button {
      margin-left: 12px;
    }
  }
}

// 嵌套对话框样式
.inner-dialog {
  :deep(.el-dialog__body) {
    padding: 20px;

    .el-row {
      margin-top: 20px;
    }
  }
}

// 富文本编辑器容器
.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;

  &:hover {
    border-color: #c0c4cc;
  }
}

// 图片上传区域
.upload-section {
  // 增加容器高度
  margin-left: 22px;
  min-height: 160px; // 最小高度
  padding: 25px 30px; // 上下增加内边距
  border: 2px dashed #c0c4cc; // 加粗虚线
  border-radius: 8px;
  background: #fafafa;
  margin-bottom: 15px;
  display: flex;
  align-items: center; // 内容垂直居中
  justify-content: center; // 内容水平居中
  transition: all 0.3s ease;


  // 悬停效果
  &:hover {
    border-color: #409eff;
    background: #f0f7ff;
    transform: translateY(-2px); // 轻微上移效果
  }
  // 内部上传组件样式
  :deep(.el-upload) {
    width: 100%;
    .el-button {
      padding: 12px 28px; // 加大按钮尺寸
      font-size: 14px;
    }
  }

  // 预览图容器
  .preview-wrapper {
    height: 140px; // 固定预览高度
    display: flex;
    align-items: center;
    gap: 15px;
  }
}

.form-section {
  padding: 20px 0;
}

.param-group {
  margin-bottom: 20px;

  h4 {
    margin: 10px 0;
    color: #333;
    font-weight: 600;
  }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .goods-image {
    border-radius: 4px; /* 更紧凑的圆角 */
    box-shadow: 0 1px 4px rgba(0,0,0,0.1);
  }

  .pagination-wrapper {
    padding: 0 8px;
    &:deep(.pager) {
      .btn-prev,
      .btn-next,
      .number {
        min-width: 28px;
        height: 28px;
        line-height: 28px;
        margin: 0 1px;
      }

      .el-pagination__jump {
        display: none;
      }

      .el-pagination__total {
        display: none;
      }
    }
  }
  
  .search-form {
    padding: 10px;
    
    :deep(.el-row) {
      display: flex;
      flex-wrap: wrap;
    }
    
    :deep(.el-col) {
      width: 100%;
      max-width: 100%;
      flex: 0 0 100%;
      margin-bottom: 10px;
    }
  }
}
</style>