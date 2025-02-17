<script setup lang="js">
import{ getCurrentInstance,ref,onMounted,reactive ,nextTick} from 'vue'
import {ElMessage, ElMessageBox} from "element-plus";
const tableData = ref([])
const tableLabels = reactive([
  {
    prop: 'name',
    label: '姓名',
  },
  {
    prop: 'age',
    label: '年龄',
  },
  {
    prop: 'sex',
    label: '性别',
  },
  {
    prop: 'birth',
    label: '出生日期',
    width: 200
  },
  {
    prop: 'addr',
    label: '地址',
    width: 300
  }
])
const {proxy} =getCurrentInstance()
const action =ref('add')
const formInline =reactive({
  keyWord:'',
})
const config=reactive({
  name:'',
  page: 1,
})
const dialogVisible =ref(false)
const formUser =reactive({
  sex: '1'
})
const rules = reactive({
  name: [
    { required: true, message: "姓名是必填项", trigger: "blur" }
  ],
  age: [
    { required: true, message: "年龄是必填项", trigger: "blur" },
    { type: "number", message: "年龄必须是数字" }
  ],
  sex: [
    { required: true, message: "性别是必选项", trigger: "change" }
  ],
  birth: [
    { required: true, message: "出生日期是必选项", trigger: "change" }
  ],
  addr: [
    { required: true, message: "地址是必填项", trigger: "blur" }
  ]
});
const handleClose = (done) => {
  dialogVisible.value = false
  // 重置表单数据
  if (proxy.$refs.userForm) {
    proxy.$refs.userForm.resetFields();
  }
  // 重新获取用户列表数据
  getUserTableD();
  // 关闭弹窗
  done();
};



const handleCancel = () => {
  dialogVisible.value = false;
  if (proxy.$refs.userForm) {
    proxy.$refs.userForm.resetFields(); // 重置表单
  }
}
// 打开新增用户弹窗
const openAddDialog = () => {
  action.value = 'add'; // 设置为新增模式
  dialogVisible.value = true; // 打开弹窗
  Object.assign(formUser, { sex: '1' }); // 清空表单数据，设置默认性别为“男”
};

// 打开编辑用户弹窗
const openEditDialog = (row) => {
  action.value = 'edit'; // 设置为编辑模式
  dialogVisible.value = true; // 打开弹窗
  nextTick(()=>{
    Object.assign(formUser, {...row,sex: '' +row.sex}); // 将当前行数据赋值给表单

  })
};


// 时间格式化
const timeFormat = (time, format = 'YYYY-MM-DD') => {
  const date = new Date(time);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return format
      .replace('YYYY', year)
      .replace('MM', month)
      .replace('DD', day);
};


const onSubmit = async () =>   {
  try {
    // 1. 表单校验
    const valid = await proxy.$refs.userForm.validate();
    if (!valid) return; // 校验失败直接退出

    // 2. 统一处理日期格式 (支持时间戳/Date对象/字符串)
    if (formUser.birth && !/^\d{4}-\d{2}-\d{2}$/.test(formUser.birth)) {
      formUser.birth = timeFormat(formUser.birth);
    }

    // 3. 根据操作类型调用接口
    let res;
    if (action.value === 'add') {
      res = await proxy.$api.addUser(formUser);
    } else {
      res = await proxy.$api.editUser(formUser); // 补充编辑逻辑
    }

    console.log(res)

    if (res.code === 200) { // 假设接口返回 code=200 表示成功
      ElMessage({
        message: action.value === 'add' ? '新增成功' : '编辑成功',
        type: 'success',
        duration: 3000,
        showClose: true
      });
      dialogVisible.value = false;
      proxy.$refs.userForm.resetFields();
      getUserTableD();
    } else {
      ElMessage(res.message || '操作失败'); // 显示后端返回的错误信息
    }
  } catch (error) {
    // 5. 统一错误处理
    console.error('表单提交异常:', error);
    ElMessage({
      message: error.response?.data?.message || '请求失败，请检查网络',
      type: 'error',
      duration: 5000,
      showClose: true
    });
  }
};

const getUserTableD =async () => {
  let res= await proxy.$api.getUserInfoData(config)
  tableData.value = res.list.map(item=>{
    return {
      ...
          item,
      sex: item.sex === 1 ? "男 " : "女"
    }
  })
  config.total=res.count
}

const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.name =formInline.keyWord
  getUserTableD()
}

const  handleChange =(page)=>{
  config.page =page
  getUserTableD()
}

const delusr=(id)=>{
ElMessageBox.confirm("是否删除?").then(async ()=>{
  await proxy.$api.deleteUser(id)
  ElMessage({
    showClose:false,
    message:"删除成功",
    type:"success",
    duration:3000,
    offset:150,
    center:true,
  })
  getUserTableD()
}
)
}




onMounted(()=>{
  getUserTableD()
})


</script>

<template>
  <div class="user-header">
    <el-button type="primary" @click="openAddDialog">新增</el-button>
    <el-form :inline="true" :model="formInline"  @submit.native.prevent >
      <el-form-item label="请输入">
        <el-input placeholder="用户名" v-model="formInline.keyWord"
                  @keyup.enter="handleSearch"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div class="table">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column
          v-for="item in tableLabels"
          :key="item.prop"
          :width="item.width ? item.width : '120px'"
          :prop="item.prop"
          :label="item.label"
      />
      <el-table-column fixed="right" label="操作" min-width="120">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="openEditDialog(scope.row)">
            Edit
          </el-button>
          <el-button link type="danger" size="small" @click="delusr(scope.row)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        class="pager"
        background
        layout="prev, pager, next"
        :page-size="config.pageSize"
        :total="config.total"
        @current-change="handleChange"
        size="small"
    />
  </div>

  <el-dialog
      v-model="dialogVisible"
      :title="action === 'add' ? '新增用户' : '编辑用户'"
      width="35%"
      :before-close="handleClose"
  >
    <el-form :inline="true" :model="formUser" :rules="rules" ref="userForm">
      <el-row>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="formUser.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄" prop="age">
            <el-input v-model.number="formUser.age" placeholder="请输入年龄" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item class="select-clear" label="性别" prop="sex">
            <el-select v-model="formUser.sex" placeholder="请选择">
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期" prop="birth">
            <el-date-picker
                v-model="formUser.birth"
                type="date"
                placeholder="请选择日期"
                style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item label="地址" prop="addr">
          <el-input v-model="formUser.addr" placeholder="请输入地址" />
        </el-form-item>
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
.user-header{
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.table{
  position: relative;
  height: 560px;
  .pager{
    position: absolute;
    right: 10px;
    bottom: 50px;
  }
  .el-table{
    width: 100%;
    height: 520px;
  }
}
.select-clear{
  display: flex;
}
</style>

