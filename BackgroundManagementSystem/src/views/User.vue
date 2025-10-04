<script setup lang="js">
import{ getCurrentInstance,ref,onMounted,reactive ,nextTick} from 'vue'
import {ElMessage, ElMessageBox} from "element-plus";
import axios from "axios";
import * as http from "node:http";
const tableData = ref([])
const tableLabels = reactive([
  {
    prop: 'userId',
    label: 'ID',
    width: 80,
  },
  {
    prop: 'username',
    label: '用户名',
    width: 120,
  },
  {
    prop: 'email',
    label: '邮箱',
    width: 200,
  },
  {
    prop: 'phone',
    label: '手机号',
    width: 120,
  },
  {
    prop: 'sex',
    label: '性别',
    width: 80,
  },
  {
    prop: 'avatar',
    label: '头像',
    width: 80,
  },
  {
    prop: 'level',
    label: '权限等级',
    width: 100,
  },
  {
    prop: 'lastLogin',
    label: '最后登录',
    width: 160,
  },
  {
    prop: 'createdAt',
    label: '创建时间',
    width: 160,
  },
  {
    prop: 'status',
    label: '状态',
    width: 80,
  }
])
const {proxy} =getCurrentInstance()
const action =ref('add')
const formInline =reactive({
  keyWord:'',
  searchType: 'username' // 搜索类型：username, email, phone
})
const config=reactive({
  username:'',
  page: 1,
  pageSize: 10,
  total: 0
})
const dialogVisible =ref(false)
const formUser =reactive({
  userId: null,
  username: '',
  email: '',
  phone: '',
  sex: 1,
  level: 1,
  avatar: 'http://localhost:8080/images/users/defaultAvatar.jpg',
  password: '' // 新增用户时的密码
})
const rules = reactive({
  username: [
    { required: true, message: "用户名是必填项", trigger: "blur" },
    { min: 2, max: 50, message: "用户名长度在2到50个字符", trigger: "blur" }
  ],
  email: [
    { required: true, message: "邮箱是必填项", trigger: "blur" },
    { type: "email", message: "邮箱格式需正确", trigger: "blur" }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
  password: [
    { required: true, message: "密码是必填项", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在6到20个字符", trigger: "blur" }
  ],
  level: [
    { required: true, message: "权限等级是必填项", trigger: "change" }
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
  Object.assign(formUser, {
    userId: null,
    username: '',
    email: '',
    phone: '',
    sex: 1,
    level: 1,
    avatar: 'http://localhost:8080/images/users/defaultAvatar.jpg',
    password: ''
  }); // 清空表单数据，设置默认值
  action.value = 'add'; // 设置为新增模式
  dialogVisible.value = true; // 打开弹窗
};

// 打开编辑用户弹窗
const openEditDialog = (row) => {
  action.value = 'edit'; // 设置为编辑模式
  dialogVisible.value = true; // 打开弹窗
  const sexValue = row.sex === "男" ? 1 : 0;
  const levelMap = {
    '普通用户': 1,
    '管理员': 2,
    '超级管理员': 3
  };
  const levelValue = levelMap[row.level] || 1; // 默认为普通用户

  nextTick(()=>{
    Object.assign(formUser, {
      ...row,
      sex: sexValue,// 强制赋值为数值
      level: levelValue, // 赋值为数值
      password: '' // 编辑时不显示密码
    }); // 将当前行数据赋值给表单
  })
};



const onSubmit = async () =>   {
  try {
    // 1. 表单校验
    const valid = await proxy.$refs.userForm.validate();
    if (!valid) return; // 校验失败直接退出



    const userData = {
      userId: formUser.userId,
      username: formUser.username,
      email: formUser.email,
      phone: formUser.phone,
      sex: formUser.sex , // 这里应根据性别转换为数字
      avatar: formUser.avatar,
      level:formUser.level,
    };

    console.log(userData)


    // 3. 根据操作类型调用接口
    let res;
    if (action.value === 'add') {
      res = await proxy.$api.addUser({
        ...userData,
        password: formUser.password
      });
    } else {
      res = await proxy.$api.editUser(userData);
    }

    // console.log(res)

    if (res === "更新成功" || res === "注册成功" ) {
      ElMessage({
        message: action.value === 'add' ? '新增成功' : '编辑成功',
        type: 'success',
        duration: 3000,
        showClose: true
      });

      setTimeout(() => {
        dialogVisible.value = false; // 隐藏编辑栏
        proxy.$refs.userForm.resetFields(); // 重置表单（避免下次打开有残留数据）
      }, 2000);

      getUserTableD();
    } else {
      ElMessage(res || '操作失败');
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
  try {
    // 构建请求参数
    const params = {
      keyWord: config.username,
      searchType: config.searchType,
      page: config.page,
      pageSize: config.pageSize
    }


    let res = await proxy.$api.getUserInfoData(params)


    if (res!= null) {
      tableData.value = res.list.map(item=>{

        return {
          ...item,
          sex: item.sex === 1 ? "男" : "女",
          level: getLevelText(item.level),
          status: item.status === 0 ? "正常" : "禁用",
          lastLogin: item.lastLogin ? formatDate(item.lastLogin) : "从未登录",
          createdAt: formatDate(item.createdAt)
        }
      })
      config.total = res.count
    } else {
      ElMessage.error(res.message || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败，请检查网络连接')
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '无数据'

  // 处理后台返回的 "YYYY-MM-DD HH" 格式
  // 补全为 "YYYY-MM-DD HH:00:00" 便于Date解析
  const fullDate = date.includes(':') ? date : `${date}:00:00`

  const d = new Date(fullDate)

  // 检查日期是否有效
  if (isNaN(d.getTime())) {
    return date // 解析失败时返回原始值
  }

  // 格式化为 "YYYY-MM-DD HH:MM:SS"
  return d.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-') // 将可能的/替换为-保持一致性
}

// 获取权限等级文本
const getLevelText = (level) => {
  const levelMap = {
    1: '普通用户',
    2: '管理员',
    3: '超级管理员'
  }
  return levelMap[level] || '未知'
}

const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.page = 1 // 重置到第一页
  config.username = formInline.keyWord
  config.searchType = formInline.searchType
  getUserTableD()
}

const  handleChange =(page)=>{
  config.page =page
  getUserTableD()
}

// 清空搜索
const clearSearch = () => {
  formInline.keyWord = ''
  formInline.searchType = 'username'
  config.username = ''
  config.searchType = 'username'
  config.page = 1
  getUserTableD()
}

// 分页大小改变
const handleSizeChange = (size) => {
  config.pageSize = size
  config.page = 1
  getUserTableD()
}

// 切换用户状态（启用/禁用）
const toggleUserStatus = (row) => {
  // 步骤1：文本转数值，确定当前状态（0=正常，1=禁用）
  const currentStatus = row.status === '正常' ? 0 : 1;
  // 步骤2：确定操作文本（正常→禁用，禁用→启用）
  const action = currentStatus === 0 ? '禁用' : '启用';
  // 步骤3：确定目标状态（正常→1，禁用→0）
  const targetStatus = currentStatus === 0 ? 1 : 0;

  ElMessageBox.confirm(`确定要${action}用户"${row.username}"吗？`).then(async () => {
    try {
      let data = {
        userId: row.userId,
        status: targetStatus // 传给后端正确的数值
      };

      let res = await proxy.$api.toggleUserStatus(data);

      if (res !== '操作成功') { // 根据实际后端成功标识判断
        throw new Error(res || `${action}失败`);
      }

      ElMessage({
        message: `${action}成功`,
        type: 'success',
        duration: 3000
      });
      getUserTableD(); // 刷新列表，更新状态
    } catch (error) {
      ElMessage({
        message: `${action}失败`,
        type: 'error',
        duration: 3000
      });
    }
  });
};

const delusr=(row)=>{
ElMessageBox.confirm("确定要删除用户吗？此操作不可恢复！").then(async ()=>{
      console.log(row.userId)
  let res = await proxy.$api.deleteUser({userId: row.userId})
  if (res === "删除成功") {
    ElMessage({
      showClose:false,
      message:"删除成功",
      type:"success",
      duration:3000,
      offset:150,
      center:true,
    })
    getUserTableD()
  } else {
    ElMessage({
      showClose:false,
      message:"删除失败",
      type:"error",
      duration:3000,
      offset:150,
      center:true,
    })
  }
}
)}




onMounted(()=>{
  getUserTableD()
})


</script>

<template>
  <div class="user-header">
    <div class="header-left">
      <el-button type="primary" @click="openAddDialog">新增用户</el-button>
      <el-button type="success" @click="getUserTableD">刷新</el-button>
    </div>
    <el-form :inline="true" :model="formInline"  @submit.native.prevent >
      <el-form-item label="搜索类型">
        <el-select v-model="formInline.searchType" placeholder="选择搜索类型" style="width: 120px">
          <el-option label="用户名" value="username"></el-option>
          <el-option label="邮箱" value="email"></el-option>
          <el-option label="手机号" value="phone"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input
          :placeholder="`请输入${formInline.searchType === 'username' ? '用户名' : formInline.searchType === 'email' ? '邮箱' : '手机号'}`"
          v-model="formInline.keyWord"
          @keyup.enter="handleSearch"
          style="width: 200px"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="clearSearch">清空</el-button>
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

        <!-- 自定义 status 列的渲染 -->
        <template v-if="item.prop === 'status'" #default="scope">
          <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" min-width="180">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="openEditDialog(scope.row)">
            编辑
          </el-button>
          <el-button
            link
            :type="scope.row.status === '正常' ? 'danger' : 'success'"
            size="small"
            @click="toggleUserStatus(scope.row)"
          >
            {{ scope.row.status === '正常' ? '禁用' : '启用' }}
          </el-button>
          <el-button link type="danger" size="small" @click="delusr(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        class="pager"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="config.pageSize"
        :total="config.total"
        @current-change="handleChange"
        @size-change="handleSizeChange"
        size="small"
    />
  </div>

  <el-dialog
      v-model="dialogVisible"
      :title="action === 'add' ? '新增用户' : '编辑用户'"
      width="50%"
      :before-close="handleClose"
  >
    <el-form :model="formUser" :rules="rules" ref="userForm" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名:" prop="username">
            <el-input v-model="formUser.username" placeholder="请输入用户名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="formUser.email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="手机号:" prop="phone">
            <el-input v-model="formUser.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="action === 'add'">
          <el-form-item label="密码:" prop="password">
            <el-input v-model="formUser.password" type="password" placeholder="请输入密码" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别:" prop="sex">
            <el-select v-model="formUser.sex" placeholder="请选择性别">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权限等级:" prop="level">
            <el-select v-model="formUser.level" placeholder="请选择权限等级">
              <el-option label="普通用户" :value="1" />
              <el-option label="管理员" :value="2" />
              <el-option label="超级管理员" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="头像:" prop="avatar">
            <div style="display: flex; align-items: center; gap: 20px;">
              <el-avatar
                  shape="circle"
                  :size="80"
                  :src="formUser.avatar">
              </el-avatar>
              <div>
                <el-input v-model="formUser.avatar" placeholder="头像URL" />
                <el-button type="primary" size="small" style="margin-top: 10px;">
                  上传头像
                </el-button>
              </div>
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row style="justify-content: flex-end; margin-top: 20px;">
        <el-form-item>
          <el-button @click="handleCancel">取消</el-button>
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
  margin-bottom: 20px;

  .header-left {
    display: flex;
    gap: 10px;
  }
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
