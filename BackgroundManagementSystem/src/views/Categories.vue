<script setup lang="js">
import {reactive, ref, onMounted, getCurrentInstance, nextTick} from "vue";

const {proxy} =getCurrentInstance()
const dialogVisible =ref(false)
const action =ref('add')
const selectedLevel=ref('')
const formInline =reactive({
  keyWord:'',
})
const catForm=reactive({
    catname:'',
    cat_pid:'',
    cat_level:1
})
const rules = reactive({
  catname: [
    { required: true, message: "分类名是必填项", trigger: "blur" }
  ],
});
const config=reactive({
  catname:'',
  page: 1,
  flatten : 'true' // 数据扁平化选项
})
const tableData = ref([])
const categoryOptions=ref([])
const tableLabels = reactive([
  {
    prop: 'cat_id',
    label: 'id',
    width: 200,
  },
  {
    prop: 'catname',
    label: '分类名称',
    width: 200,
  },
  {
    prop: 'cat_pid',
    label: '父级id',
    width: 200,
  },
  {
    prop: 'cat_level',
    label: '分类层级',
    width: 200,
  },

])
const getCatelist= async ()=>{
  let res= await proxy.$api.getCateList(config)
  tableData.value = res.list.map(item=>{
    let  level;
    if (item.cat_level===1){
      level="一级"
    }
    else if (item.cat_level===2){
      level="二级"
    }
    else if (item.cat_level===3)level="三级"
    return {
      ...
          item,
      cat_level: level
    }
  })
  config.total=res.count
}



const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.catname =formInline.keyWord
  getCatelist()
}

// 转换树形结构
const convertToCascaderOptions =(cateList)=>{
  const map = {};
  const result = [];

  // 将 cateList 中的每个节点转换为 Cascader 所需的格式
  cateList.forEach(cat => {
    const { cat_id, catname, cat_pid } = cat;
    map[cat_id] = { value: cat_id, label: catname, children: [] };
  });

  // 重新构建树形结构
  cateList.forEach(cat => {
    const { cat_id, cat_pid } = cat;
    if (cat_pid !== 0) {
      map[cat_pid].children.push(map[cat_id]);
    } else {
      result.push(map[cat_id]);
    }
  });

  return result;
}



const openAddDialog = () => {
  categoryOptions.value=convertToCascaderOptions(tableData.value)
  Object.assign(catForm, { }); // 清空表单数据
  action.value = 'add'; // 设置为新增模式
  dialogVisible.value = true; // 打开弹窗
};

const openEditDialog=()=>{
  Object.assign(catForm, { }); // 清空表单数据
  action.value = 'edit'; // 设置为编辑模式
  dialogVisible.value = true; // 打开弹窗
}


const handleClose = (done) => {
  dialogVisible.value = false
  // 重置表单数据
  if (proxy.$refs.catForm) {
    proxy.$refs.catForm.resetFields();
  }
  // 重新获取用户列表数据
  getCatelist();
  // 关闭弹窗
  done();
};


const handleCancel = () => {
  dialogVisible.value = false;
  if (proxy.$refs.catForm) {
    proxy.$refs.catForm.resetFields(); // 重置表单
  }
}

const onSubmit=()=>{
  catForm.cat_pid=catForm.cat_pid[catForm.cat_pid.length-1];
  console.log(catForm)
}

const delcat=()=>{

}

const handlePageChange =(page)=>{
  config.page=page
  getCatelist()
}

onMounted(()=>{
  getCatelist()
})
</script>

<template>
  <div class="cat-header">
    <el-button type="primary" @click="openAddDialog" size="large" >添加分类</el-button>
    <el-form :inline="true" :model="formInline"  @submit.native.prevent >
      <el-form-item label="请输入">
        <el-input placeholder="分类名" v-model="formInline.keyWord"
                  @keyup.enter="handleSearch"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-card >
    <el-header>商品分类</el-header>
    <el-table :data="tableData" border>
      <el-table-column
          v-for="item in tableLabels"
          :key="item.prop"
          :width="item.width ? item.width : '120px'"
          :prop="item.prop"
          :label="item.label"

      >
        <template v-if="item.prop === 'cat_level'" #default="scope">
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
          :page-size="config.pageSize"
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
    <el-form :inline="true" :model="catForm" :rules="rules" >
      <el-row>
        <el-col :span="17">
          <el-form-item label="分类名:" prop="catname">
            <el-input v-model="catForm.catname" placeholder="请输入分类名" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="16">
          <el-form-item label="父级分类:" >
            <el-cascader
                :props="{
                  checkStrictly:'true'
                }"
                :options="categoryOptions"
                :placeholder="'请选择分类'"
                v-model="catForm.cat_pid"
                @change="handleCategoryChange"
                clearable
                :change-on-select="true"
            />
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