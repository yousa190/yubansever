<script setup lang="js">
import {reactive, ref, onMounted, getCurrentInstance, nextTick} from "vue";
import CatTree from "@/components/CatTree.vue";
import {ElMessage, ElMessageBox} from "element-plus";

const {proxy} =getCurrentInstance()
const dialogVisible =ref(false)
const innerdialogVisible =ref(false)
const action =ref('add')
const selectedLevel=ref('')
const formInline =reactive({
  keyWord:'',
  catPid: null
})
const catForm=reactive({
    catId:0,
    catName:'',
    catPid:0,
    catLevel:1
})
const rules = reactive({
  catName: [
    { required: true, message: "分类名是必填项", trigger: "blur" }
  ],
});
const config=reactive({
  catName:'',
  catPid: null,
  page: 1,
  limit: 10,
  total: 0
})
const tableData = ref([])
const tableLabels = reactive([
  {
    prop: 'catId',
    label: 'id',
    width: 200,
  },
  {
    prop: 'catName',
    label: '分类名称',
    width: 200,
  },
  {
    prop: 'catPid',
    label: '父级id',
    width: 200,
  },
  {
    prop: 'catLevel',
    label: '分类层级',
    width: 200,
  },

])

// 公共消息提示方法
const showMessage = (type, message) => {
  ElMessage({
    showClose: false,
    message,
    type,
    duration: 3000,
    offset: 150,
    center: true
  });
};

const getCatelist= async ()=>{
  let res= await proxy.$api.getCateList(config)
  // console.log(res.list)
  tableData.value = res.list.map(item=>{
    let  level;
    if (item.catLevel===1){
      level="一级"
    }
    else if (item.catLevel===2){
      level="二级"
    }
    else if (item.catLevel===3)level="三级"
    return {
      ...item,
      catLevel: level
    }
  })
  config.total=res.count
}

const resetForm = () => {
  if (proxy.$refs.Formcat) {
    Pname.value=''
    Object.assign(catForm,{
      catId:'',
      catName:'',
      catPid:0,
      catLevel:1
    });
    proxy.$refs.Formcat.resetFields(); // 重置表单
  }
}

const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.catName =formInline.keyWord
  config.catPid = formInline.catPid
  config.page = 1 // 搜索时重置到第一页
  getCatelist()
}

const handleClear = () => {
  // 清空搜索条件
  formInline.keyWord = ''
  formInline.catPid = null
  // 重置配置参数
  config.catName = ''
  config.catPid = null
  config.page = 1
  config.total = 0
  // 重新加载数据
  getCatelist()
}

const openAddDialog = () => {
  Object.assign(catForm, {
    catId:0,
    catName:'',
    catPid:0,
    catLevel:1
  }); // 清空表单数据
  action.value = 'add'; // 设置为新增模式
  dialogVisible.value = true; // 打开弹窗
};

const openEditDialog=(row)=>{
  action.value = 'edit'; // 设置为编辑模式
  nextTick(()=>{
    // 将当前行数据赋值给表单，保留原有的分类层级和父级分类信息
    Object.assign(catForm, {...row});
    // console.log(catForm)
  })
  dialogVisible.value = true; // 打开弹窗
}

const handleClose = (done) => {
  dialogVisible.value = false
  // 重置表单数据
  resetForm()
  // 重新获取用户列表数据
  getCatelist();
  // 关闭弹窗
  done();
};

const handleCancel = () => {
  dialogVisible.value = false;
  resetForm()
}

const onSubmit=async ()=>{
  // 确保 catLevel 是数字类型
  if (typeof catForm.catLevel === 'string') {
    if (catForm.catLevel === '一级') {
      catForm.catLevel = 1;
    } else if (catForm.catLevel === '二级') {
      catForm.catLevel = 2;
    } else {
      catForm.catLevel = 3;
    }
  }

  if (action.value === 'add') {
    // 添加时不需要 catId
    delete catForm.catId;
    try {
      const res = await proxy.$api.addCate(catForm);
      if (res === 'success !' || res === 'success') {
        showMessage("success", '添加成功');
        getCatelist();
        dialogVisible.value = false;
        resetForm();
      } else {
        showMessage("error", '添加失败: ' + res);
      }
    } catch (error) {
      showMessage("error", '添加失败: ' + error.message);
    }
  } else if (action.value === 'edit') {
    try {
      const res = await proxy.$api.updCate(catForm);
      if (res === 'success !' || res === '更新成功') {
        showMessage("success", '更新成功');
        getCatelist();
        dialogVisible.value = false;
        resetForm();
      } else {
        showMessage("error", '更新失败: ' + res);
      }
    } catch (error) {
      showMessage("error", '更新失败: ' + error.message);
    }
  }
}

const delcat=(row)=>{
  ElMessageBox.confirm(`确定要删除分类 "${row.catName}" 吗？此操作不可恢复！`, "删除确认", {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      const res = await proxy.$api.delCate({catId: row.catId});
      if (res === 'success!' || res === '删除成功') {
        showMessage("success", '删除成功');
        getCatelist();
      } else {
        showMessage("error", '删除失败: ' + res);
      }
    } catch (error) {
      showMessage("error", '删除失败: ' + error.message);
    }
  }).catch(() => {
    showMessage("info", '已取消删除');
  });
}

const handlePageChange =(page)=>{
  config.page=page
  getCatelist()
}

const parent =ref({})
const Pname=ref('')
const setParent =(data)=>{
  parent.value=data;
}

const isDescendant = (parentCatId, targetCatId) => {
  // 这里简化处理，实际应该通过完整的树形结构来判断
  // 在实际项目中，可以通过后端接口获取完整的分类树来准确判断
  // 此处为演示目的，暂时只做简单的直接父子关系判断
  if (parentCatId === targetCatId) {
    return true;
  }
  // 更复杂的子孙关系判断应该通过后端实现
  return false;
};

const SetParentVal=(data)=>{
  innerdialogVisible.value=false
  if (data){
    // 检查不能选择自己作为父分类
    if (data.catId === catForm.catId) {
      ElMessage({
        showClose: false,
        message: "不能选择自己作为父分类",
        type: "warning",
        duration: 3000,
        offset: 150,
        center: true
      });
      return;
    }

    // 检查分类层级，确保不超过三级
    if (data.catLevel >= 3) {
      ElMessage({
        showClose: false,
        message: "不能选择三级分类作为父分类",
        type: "warning",
        duration: 3000,
        offset: 150,
        center: true
      });
      return;
    }

    // 检查不能选择自己的子孙分类作为父分类
    // 注意：这里需要完整的分类树结构才能准确判断
    // 在实际项目中，应该从后端获取完整的分类树结构进行判断
    if (isDescendant(catForm.catId, data.catId)) {
      ElMessage({
        showClose: false,
        message: "不能选择自己的子孙分类作为父分类",
        type: "warning",
        duration: 3000,
        offset: 150,
        center: true
      });
      return;
    }

    catForm.catPid=data.catId;
    catForm.catLevel=data.catLevel+1;
    Pname.value=data.catName
  }
  else{
    catForm.catLevel=1
    catForm.catPid=0
    Pname.value=''
  }
}

onMounted(()=>{
  getCatelist()
})
</script>

<template>
  <div class="cat-header">
    <el-button type="primary" @click="openAddDialog" size="large" >添加分类</el-button>
    <el-form :inline="true" :model="formInline"  @submit.native.prevent >
      <el-form-item label="分类名称">
        <el-input placeholder="请输入分类名" v-model="formInline.keyWord"
                  @keyup.enter="handleSearch"
        ></el-input>
      </el-form-item>
      <el-form-item label="父级分类ID">
        <el-input placeholder="请输入父级分类ID" v-model.number="formInline.catPid"
                  @keyup.enter="handleSearch"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleClear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-card >
    <el-table :data="tableData" border>
      <el-table-column
          v-for="item in tableLabels"
          :key="item.prop"
          :width="item.width ? item.width : '120px'"
          :prop="item.prop"
          :label="item.label"

      >
        <template v-if="item.prop === 'catLevel'" #default="scope">
          <el-tag size="small"    :type="{
    '一级': 'primary',
    '二级': 'success',
    '三级': 'warning'
  }[scope.row[item.prop]]">{{scope.row[item.prop]}}</el-tag>

        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" min-width="140">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="openEditDialog(scope.row)" >
            Edit
          </el-button>
          <el-button link type="danger" size="small" @click="delcat(scope.row)">
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
          :page-size="config.limit"
          :total="config.total"
          prev-text="上一页"
          next-text="下一页"
          @current-change="handlePageChange"
          :hide-on-single-page="true"
      />
    </div>
  </el-card>

  <el-dialog
      v-model="dialogVisible"
      :title="action === 'add' ? '新增分类' : '编辑分类'"
      width="35%"
      :before-close="handleClose"
  >
    <!-- 嵌套对话框 -->
    <el-dialog
        :title="'商品分类'"
        width="35%"
        v-model="innerdialogVisible"
        append-to-body
        class="inner-dialog"
    >
      <CatTree @config-sent="setParent"  />

      <el-row style="justify-content: flex-end">
        <el-form-item>
          <el-button type="primary" @click="innerdialogVisible=false">取消</el-button>
          <el-button type="primary" @click="SetParentVal(parent)">确定</el-button>
        </el-form-item>
      </el-row>
    </el-dialog>

    <el-form  ref="Formcat" :inline="true" :model="catForm" :rules="rules" >
      <el-row>
        <el-col :span="17">
          <el-form-item label="分类名:" prop="catName">
            <el-input v-model="catForm.catName" placeholder="请输入分类名" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="30">
          <el-form-item label="父级分类(可选):" >
            <el-text v-if="Pname"  size="large" type="success">{{ Pname }}</el-text>
            <el-button  v-if="Pname" size="small" type="danger"  @click="SetParentVal" >取消</el-button>
            <el-button   type="primary" @click="innerdialogVisible=true" size="small">父级分类</el-button>
          </el-form-item>

        </el-col>
      </el-row>
      <el-row style="justify-content: flex-end">
        <el-form-item>
          <el-button type="primary" @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="onSubmit">确定</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<style scoped lang="less">
.cat-header{
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 10px;
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

// 移动端适配
@media (max-width: 768px) {
  .cat-header {
    flex-direction: column;
    align-items: stretch;
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
