<script setup>
import {reactive, ref, onMounted, getCurrentInstance,nextTick} from "vue";
import { ElMessage, ElMessageBox } from 'element-plus'
import GoodTree from "@/components/goodTree.vue";

const {proxy} =getCurrentInstance()

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

// 搜索参数
const searchParams = reactive({
  keyword: '',
})
const config=reactive({
  page: 1,
  limit: 10,
  total: 0,
  catName:'',
  isdisabled:true
})
const tableLabels = reactive([
  {
    prop: 'groupId',
    label: '规格id',
    width: 200,
  },
  {
    prop: 'catId',
    label: '所属分类id',
    width: 250,
  },
  {
    prop: 'catName',
    label: '所属分类',
    width: 200,
  },
  {
    prop: 'groupConfig',
    label:'规格参数',
    width: 600,
    // 字符串显示
    formatter: (row) => {  // 使用箭头函数，并显式返回
      // console.log("row.groupConfig:", row.groupConfig); // 调试输出
      try {
        return JSON.stringify(row.groupConfig);
      } catch (error) {
        console.error("JSON.stringify error:", error); // 错误处理
        return 'JSON解析错误'; // 返回一个错误提示
      }
    },
    showTooltip:true
  }
])
const confList =ref([])
const confForm=reactive({
  groupID:'',
  catId:'',
  catName:'',
  // 问题:如何以字符串的形式显示?
  groupConfig:[],
})
const resetForm=()=>{
  Object.assign(confForm,{
    groupID:'',
    catId:'',
    catName:'',
    groupConfig:[],})
  dynamicValidateForm.tags = []
  nextTick(() => {
    if (formRef.value) {
      formRef.value.resetFields();
      formRef.value.clearValidate();
    }
  });
}
const cate =ref({})
// 动态参数列表数据
const dynamicValidateForm = reactive({
  tags: [
    //   {
    //     value: '',
    //     title:'',
    //     children:[
    //       {
    //         value: '',
    //         title:'',
    //       }
    //     ]
    //   },
    //   {
    //     value: '',
    //     title:'',
    //     children:[]
    //   },
  ],
})

// 对话框相关状态
const dialogVisible = ref(false)
const innerdialogVisible = ref(false)
const dialogType = ref('edit')



// 加载参数列表
const load = async () => {
  // 实际调用API接口
  // ElMessage({message:'加载参数...', duration:1000})
  let res= await proxy.$api.getCatConf(config)
  confList.value = res.list
  config.total=res.count
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  load()
}

// 处理搜索
const handleSearch = (event) => {
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.catName =searchParams.keyword
  load()
}


const openDialog=(type)=>{
  dialogVisible.value = true
  dialogType.value = type
}

const setCate=(data)=>{
  cate.value=data
  config.isdisabled=false
}

const setCateVal=()=>{
  if (cate){
    confForm.catId=cate.value.catId
    confForm.catName=cate.value.catName
  }
  else resetForm()
  innerdialogVisible.value=false
}

const innerCancel= () =>{
  innerdialogVisible.value = false
  config.isdisabled=true
  Object.assign(cate,{})
}

// 取消编辑
const handleCancel=()=>{
  resetForm()
  dynamicValidateForm.tags=[]
  dialogVisible.value=false
}

const handleEdit = async (row) => {
  try {
    let res=await proxy.$api.getOneConf({catId:row.catId})
    confForm.groupID=res.groupID
    confForm.catId=res.catId
    confForm.catName=res.catName
    confForm.groupConfig=res.groupConfig
    dynamicValidateForm.tags = Array.isArray(res.groupConfig) ? [...res.groupConfig] : [];
    openDialog('edit')
  } catch (error) {
    // 4. 统一错误处理
    ElMessage.error(error.message || '获取配置信息失败，请重试')
  }
}

// 查看
const handleCheck = async (row) => {
  try {
    let res = await proxy.$api.getOneConf({ catId: row.catId });
    confForm.groupID = res.groupID;
    confForm.catId = res.catId;
    confForm.catName = res.catName;
    confForm.groupConfig = res.groupConfig;
    dynamicValidateForm.tags = Array.isArray(res.groupConfig) ? [...res.groupConfig] : [];
    openDialog('check'); // 打开对话框，并传递'check'类型
  } catch (error) {
    ElMessage.error(error.message || '获取配置信息失败，请重试');
  }
};

// 添加删除方法
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该配置？', '警告', {
      type: 'warning'
    })
     let res =  await proxy.$api.delConf({groupId:id})
    if (res==='删除成功')showMessage("success",'删除成功')
    else showMessage("error",'error:'+res)
    load()
  } catch (e) {
    if (e) {
      showMessage("error",'删除失败')
    }
  }
}

// 表单引用
const formRef = ref()



// 添加参数项
const addtag = () => {
  dynamicValidateForm.tags.push({
    value: '',
    title:'',
    children:[]
  })
}

const addChildTag=(index)=>{
  if (index !== -1) dynamicValidateForm.tags[index].children.push({
    value: '',
    title:'',
  })
}

const removeChildTag =(index,i) =>{
  if (index !== -1) dynamicValidateForm.tags[index].children.splice(i,1)
}
// 删除参数项
const removetag = (index) => {
  if (index !== -1) {
    dynamicValidateForm.tags.splice(index, 1)
  }
}

// 提交表单
const submitForm =  (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      confForm.groupConfig = dynamicValidateForm.tags
      let res = await proxy.$api.updateCatConf(confForm);
        if (res==='更新成功')showMessage('success',res)
         else showMessage('error',res)
      resetForm()
      load()
      dialogVisible.value=false
    } else {
      showMessage("error", '表单不完整 !')
    }
  })

}







onMounted(() => {
  load()
})
</script>

<template>
  <div class="param-container">
    <!-- 搜索和操作区域 -->
    <div class="header">
      <el-button type="primary" @click="openDialog('add')">新建参数</el-button>

      <div class="search-area">
        <el-input
            v-model="searchParams.keyword"
            placeholder="请输入所属分类"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
            style="width: 240px; margin-right: 16px"
        >
          <template #append>
            <el-button icon="Search" @click="handleSearch" />
          </template>
        </el-input>

      </div>
    </div>

    <!-- 参数表格 -->
    <el-table
        :data="confList"
        stripe
        border
        style="margin-top: 20px"
    >
      <el-table-column v-for="item in tableLabels "
                       :key="item.prop"
                       :width="item.width ? item.width : '120px'"
                       :prop="item.prop"
                       :label="item.label"
                       :formatter="item.formatter"
                       :show-overflow-tooltip="item.showTooltip" ></el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleCheck(row)">查看</el-button>
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row.groupId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          v-model:current-page="config.page"
          v-model:page-size="config.limit"
          :total="config.total"
          layout="total, prev, pager, next, jumper"
          @current-change="load"
          @size-change="handleSizeChange"
      />
    </div>

    <!-- 参数编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogType === 'edit' ? '编辑参数' : '参数查看'"
        width="700px"
    >
      <!-- 分类选择和规格按钮区域 -->
      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="24">
          <el-button
              type="primary"
              @click="innerdialogVisible=true"
              size="small"
              :disabled="dialogType === 'check'"
          >
            <el-text v-if="!confForm.catName">请选择商品分类</el-text>
            <el-text v-else>{{ confForm.catName }}</el-text>
          </el-button>
        </el-col>

        <el-col :span="24" v-if="confForm.catName">
          <el-button
              type="success"
              plain
              style="margin-top: 10px"
              @click="addtag"
              :disabled="dialogType === 'check'"
          >
            新增规格
          </el-button>
        </el-col>
      </el-row>

      <el-divider />

      <!-- 表单区域 -->
      <el-form
          ref="formRef"
          style="max-width: 700px; padding: 20px;"
          :model="dynamicValidateForm"
          label-width="120px"
          label-position="left"
      >
        <el-form-item
            v-for="(tag, index) in dynamicValidateForm.tags"
            :key="index"
            :label="(index+1) + ': ' + (tag.title || '未命名')"
            :prop="'tags.' + index + '.title'"
            :rules="[{ required: true, message: '标签不能为空', trigger: 'blur' }]"
            style="margin-bottom: 20px;"
        >
          <div style="display: flex; align-items: center; gap: 10px;">
            <el-input
                v-model="tag.title"
                placeholder="请输入参数"
                style="flex: 1;"
            />
            <el-button size="mini" @click.prevent="addChildTag(index)" :disabled="dialogType === 'check'">新增子组</el-button>
            <el-button size="mini" type="danger" @click.prevent="removetag(index)" :disabled="dialogType === 'check'">删除</el-button>
          </div>

          <!-- 子标签层级 -->
          <div v-if="tag.children" style="margin-top: 10px; padding-left: 20px; border-left: 2px solid #ebeef5;">
            <el-form-item
                v-for="(item, i) in tag.children"
                :key="i"
                :label="'子组 ' + (i+1)"
                :prop="'tags.' + index + '.children.' + i + '.title'"
                :rules="[{ required: true, message: '子标签不能为空', trigger: 'blur' }]"
                style="margin-bottom: 15px;"
            >
              <div style="display: flex; align-items: center; gap: 10px;">
                <el-input
                    v-model="item.title"
                    placeholder="请输入子参数"
                    style="flex: 1;"
                />
                <el-button size="mini" type="danger" @click.prevent="removeChildTag(index, i)" :disabled="dialogType === 'check'">删除</el-button>
              </div>
            </el-form-item>
          </div>
        </el-form-item>

      </el-form>
      <el-divider />

      <!-- 底部按钮组 -->
      <div style="display: flex; justify-content: flex-end; gap: 10px; padding: 10px 20px;">
        <el-button @click="resetForm()" :disabled="dialogType === 'check'">重置</el-button>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="submitForm(formRef)"   :disabled="dialogType === 'check'">确认</el-button>
      </div>
    </el-dialog>

    <!-- 嵌套对话框   -->
    <el-dialog
        :title="'商品分类'"
        width="35%"
        v-model="innerdialogVisible"
        append-to-body
        class="inner-dialog"
    >
      <good-tree  @config-sent="setCate" />

      <el-row style="justify-content: flex-end">
        <el-form-item>
          <el-button  @click="innerCancel">取消</el-button>
          <el-button type="primary" @click="setCateVal" :disabled=" config.isdisabled " >确定</el-button>
        </el-form-item>
      </el-row>
    </el-dialog>
  </div>
</template>



<style lang="less" scoped>
.param-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-area {
  display: flex;
  gap: 16px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}


</style>