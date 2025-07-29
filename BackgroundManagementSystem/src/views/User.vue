<script setup lang="js">
import{ getCurrentInstance,ref,onMounted,reactive ,nextTick} from 'vue'
import {ElMessage, ElMessageBox} from "element-plus";
import axios from "axios";
import * as http from "node:http";
const tableData = ref([])
const tableLabels = reactive([
  {
    prop: 'user_id',
    label: 'id',
  },
  {
    prop: 'username',
    label: '姓名',
  },
  {
    prop: 'sex',
    label: '性别',
  },
  {
    prop: 'email',
    label: '邮箱',
    width: 200,
  },
  {
    prop: 'avatar',
    label: '头像',
    width: 150,
  },
  {
    prop: 'level',
    label: '权限',
  }
])
const {proxy} =getCurrentInstance()
const action =ref('add')
const formInline =reactive({
  keyWord:'',
})
const config=reactive({
  username:'',
  page: 1,
})
const dialogVisible =ref(false)
const formUser =reactive({
  sex: '1',
  level:'1'
})
const rules = reactive({
  username: [
    { required: true, message: "用户名是必填项", trigger: "blur" }
  ],
  email: [
    { required: true, message: "邮箱是必填项", trigger: "blur" },
    { type: "email", message: "邮箱格式需正确", trigger: "blur" }
  ],
  level: [
    { required: true,  trigger: "change" }
  ],
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
  Object.assign(formUser, { username:'',level:'1',sex: '1' }); // 清空表单数据，设置默认性别为“男”
  action.value = 'add'; // 设置为新增模式
  dialogVisible.value = true; // 打开弹窗
};

// 打开编辑用户弹窗
const openEditDialog = (row) => {
  action.value = 'edit'; // 设置为编辑模式
  dialogVisible.value = true; // 打开弹窗
  nextTick(()=>{
    Object.assign(formUser, {...row,sex: '' +row.sex}); // 将当前行数据赋值给表单
  })
};



const onSubmit = async () =>   {
  try {
    // 1. 表单校验
    const valid = await proxy.$refs.userForm.validate();
    if (!valid) return; // 校验失败直接退出


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
  config.username =formInline.keyWord
  getUserTableD()
}

const  handleChange =(page)=>{
  config.page =page
  getUserTableD()
}

const delusr=(row)=>{
ElMessageBox.confirm("是否删除?").then(async ()=>{
  await proxy.$api.deleteUser(row)
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
)}




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
    <el-table :data="tableData">
      <el-table-column
          v-for="item in tableLabels"
          :key="item.prop"
          :width="item.width ? item.width : '120px'"
          :prop="item.prop"
          :label="item.label"
      >
        <!-- 自定义 avatar 列的渲染 -->
        <template v-if="item.prop === 'avatar'" #default="scope">
          <img
              :src="scope.row[item.prop]"
              alt="用户头像"
              class="userimg"
          />
        </template>
      </el-table-column>
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
        layout="prev, pager, next  "
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
        <el-col :span="17">
          <el-form-item label="用户名:" prop="name">
            <el-input v-model="formUser.username" placeholder="请输入用户名" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <el-form-item label="邮箱 :" prop="email">
            <el-input v-model="formUser.email" placeholder="请输入邮箱"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item class="select-clear" label="  性别:  " prop="sex">
            <el-select v-model="formUser.sex" placeholder="请选择">
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="select-clear" label=" 权限:  " prop="level">
            <el-select v-model="formUser.level" placeholder="请选择">
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item label="头像" prop="avatar">
          <el-avatar
              shape="circle"
              @error='true'
              :size="110"
              :src="formUser.avatar">
          </el-avatar>
          <el-col :span="12">
            <el-button type="primary" size="small" >上传头像</el-button>
          </el-col>
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
  height: 80%;
  max-height: 900px;
  .userimg{
    display: block;
    width: 25px;
    height: 25px;
    border-radius: 4px; /* 现代圆角设计 */
    transition: all 0.3s ease-in-out;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1); /* 柔和投影 */
    border: 1px solid #f0f0f0; /* 浅色边框 */
    object-fit: cover; /* 保持比例填充容器 */
    background-color: #f8f9fa; /* 加载时占位背景 */
    aspect-ratio: 1; /* 强制1:1比例 */
  }
  .userimg:hover {
    transform: scale(1.05); /* 微放大效果 */
    box-shadow: 0 4px 12px rgba(0,0,0,0.15); /* 加强投影 */
    cursor: zoom-in; /* 光标提示可缩放 */
  }
  .pager{
    position: absolute;
    right: 10px;
    bottom: 50px;
  }
  .el-table{
    width: 100%;
    height: 80%;
  }
}
.select-clear{
  display: flex;
}


</style>

