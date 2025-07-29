<script setup lang="js">
import goodTree from '@/components/goodTree.vue'
import goodImg from '@/components/goodimg.vue'
import WangEditor from '@/components/WangEditor.vue'
import {reactive, ref, onMounted, getCurrentInstance,nextTick} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const dialogVisible =ref(false)
const innerdialogVisible =ref(false)
const action =ref('add')
const goodForm=reactive({
  goodname: '',
  price: 0,
  num: 0,
  catname: '',
  image: 'https://picsum.photos/id/416/10',
  comment: ''
})
const {proxy} =getCurrentInstance()
const formInline =reactive({
  keyWord:'',
})
const tableLabels = reactive([
  {
    prop: 'good_id',
    label: 'id',
  },
  {
    prop: 'goodname',
    label: '商品名称',
    width: 200,
  },
  {
    prop: 'price',
    label: '商品价格',
    width: 200,
  },
  {
    prop: 'num',
    label: '商品数量',
    width: 200,
  },
  {
    prop: 'catname',
    label: '商品分类',
    width: 200,
  },
  {
    prop: 'image',
    label: '商品图片',
    width: 150,
  },
  {
    prop: 'comment',
    label: '商品描述',
    width: 300,
    showTooltip:true,
  },
])
const goodData = ref([
  {
    "good_id": 1,
    "goodname": "华为Mate50 Pro",
    "price": 5999.99,
    "num": 100,
    "catname": "一级类目/数码产品",
    "image": "https://picsum.photos/id/21/100/100",
    "comment": "旗舰级智能手机，搭载超光变XMAGE影像系统"
  },
  {
    "good_id": 2,
    "goodname": "小米电视ES65",
    "price": 1999.50,
    "num": 50,
    "catname": "二级类目/家用电器",
    "image": "https://picsum.photos/id/32/100/100",
    "comment": "4K超高清金属全面屏，支持MEMC运动补偿"
  },
  {
    "good_id": 3,
    "goodname": "索尼WH-1000XM5",
    "price": 299.00,
    "num": 200,
    "catname": "三级类目/音频设备",
    "image": "https://picsum.photos/id/43/100/100",
    "comment": "智能降噪头戴式耳机，30小时超长续航"
  },
  {
    "good_id": 4,
    "goodname": "Apple Watch Series 8",
    "price": 899.00,
    "num": 80,
    "catname": "二级类目/智能穿戴",
    "image": "https://picsum.photos/id/54/100/100",
    "comment": "全天候视网膜显示屏，支持血氧监测功能"
  },
  {
    "good_id": 5,
    "goodname": "联想拯救者Y9000P",
    "price": 8999.99,
    "num": 30,
    "catname": "一级类目/电脑整机",
    "image": "https://picsum.photos/id/65/100/100",
    "comment": "16英寸电竞笔记本，搭载RTX3070显卡"
  }
])
const resetForm = () => {
  if (proxy.$refs.formgood) {
    proxy.$refs.formgood.resetFields();
    goodForm.comment=''
  }
}
const config=reactive({
  goodname:'',
  page: 1,
})
const rules = reactive({
  goodname:[{ required: true, message: "商品名是必填项", trigger: "blur" }],
  catname: [{ required: true, message: "分类名是必选项", trigger: "blur" }],
  price:[{  required: true, message: "请输入价格", trigger: "blur" },
    {type:'number', message: "价格必须是数字", trigger: "change",}],
  num:[{  required: true, message: "请输入数量", trigger: "blur" },
    {type:'number', message: "数量必须是数字", trigger: "change",}],
})
const catinfo =ref({})

const setConfig=(data)=>{
  catinfo.value=data
}

const setCatVal =()=>{
  innerdialogVisible.value=false
  goodForm.catname=catinfo.value.catname
  // console.log(catinfo)
  console.log(goodForm)
}

const updatecomment=(data)=>{
  goodForm.comment=data.value
  // console.log(goodForm.comment)
}


const getgoodData= async ()=>{
  let res= await proxy.$api.getGoodList(config)
  goodData.value=res.list
  config.total=res.count
}


const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.goodname =formInline.keyWord
  getgoodData()
}


const openAddDialog = ()=>{
  action.value = 'add';
  dialogVisible.value = true;

  // 等待 DOM 更新后操作表单
  nextTick(() => {
      resetForm()
  });
}
const openEditDialog=(row)=>{
  action.value = 'edit';
  Object.assign(goodForm, row);
  dialogVisible.value = true;
}

const handleClose = (done) => {
  dialogVisible.value = false
  // 重置表单数据
  resetForm()
  // 重新获取用户列表数据
  getgoodData();
  // 关闭弹窗
  done();
};

const handleCancel = () => {
  dialogVisible.value = false;
  resetForm()
}




const delgood=(row)=>{
  ElMessageBox.confirm("是否删除?").then(async ()=>{
        await proxy.$api.deleteGood(row)
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
  )}

const  onSubmit= async ()=>{
  try {
    // 1. 表单校验
    const valid = await proxy.$refs.formgood.validate();
    if (!valid) return; // 校验失败直接退出


    // 3. 根据操作类型调用接口
    let res;
    if (action.value === 'add') {
      res = await proxy.$api.addGood(goodForm);
    } else {
      res = await proxy.$api.addGood(goodForm); // 补充编辑逻辑
    }

    console.log(res)

    if (res.code === 200) { // 接口返回 code=200 表示成功
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
      ElMessage(res.message || '操作失败'); // 显示后端返回的错误信息
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

onMounted(()=>{
  getgoodData()
})
</script>

<template>
  <div class="good-header">
  <el-button type="primary" @click="openAddDialog"  >添加商品</el-button>
  <el-form :inline="true" :model="formInline"   @submit.native.prevent >
    <el-form-item   >
      <el-input placeholder="请输入商品名" v-model="formInline.keyWord"
                @keyup.enter="handleSearch"
      ></el-input>

    </el-form-item>
    <el-form-item  >
      <el-button   type="primary" @click="handleSearch">搜索</el-button>
    </el-form-item>
  </el-form>
</div>
  <div class="table">
    <el-card>
      <el-table :data="goodData"  type="" stripe border ref="table">
        <el-table-column type="selection" width="55" />
        <el-table-column v-for="item in tableLabels "
                         :key="item.prop"
                         :width="item.width ? item.width : '120px'"
                         :prop="item.prop"
                         :label="item.label"
                         :show-overflow-tooltip="item.showTooltip" >
          <template v-if="item.prop === 'image'"  #default="scope">
            <img
                :src="scope.row[item.prop]"
                alt="商品图片"
                class="goods-image"
            />
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

    <el-form  ref="formgood"  :inline="true" :model="goodForm" :rules="rules"   >
      <!-- 分类选择 -->
      <div class="category-section">
        <el-row>
          <el-col :span="14">
            <el-button type="primary" @click="innerdialogVisible=true" size="default">选择商品分类</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="商品分类:" prop="catname">
              <el-text v-if="goodForm.catname "  size="large" type="success">{{ goodForm.catname }}</el-text>
              <el-text v-else>请选择商品分类</el-text>
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!-- 其他表单项 -->
      <el-row>
        <el-col :span="18">
          <el-form-item label="商品名称:" prop="goodname">
            <el-input v-model="goodForm.goodname" placeholder="请输入商品名" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 商品价格/数量 -->
      <el-row>
        <el-col :span="18">
          <el-form-item label="商品价格:" prop="price">
            <el-input v-model.number="goodForm.price" placeholder="请输入商品价格" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <el-form-item label="商品数量:" prop="num">
            <el-input v-model.number="goodForm.num" placeholder="请输入商品数量" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 图片上传 -->
      <el-row>
        <el-col :span="15">
          <div class="upload-section">
            <good-img/>
          </div>
        </el-col>
      </el-row>

      <!-- 富文本编辑器 -->
      <el-row>
        <el-form-item   prop="comment">
          <div class="editor-wrapper">
            <wang-editor  :Content="goodForm.comment"  @comment-sent="updatecomment" />
          </div>
        </el-form-item >
      </el-row>

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
}
</style>