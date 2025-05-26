<script setup >
import {nextTick, reactive, ref} from "vue";

const dialogVisible =ref(false)
const action =ref('add')
const orderForm=reactive({
})
const formInline =reactive({
    keyWord:'',
  })
const config=reactive({
  user_id:'',
  page: 1,
})
const tableLabels = reactive([

])
const orderData=ref([])
const resetForm = () => {
  if (proxy.$refs.formorder) {
    proxy.$refs.formorder.resetFields();
  }
}

const openAddDialog = ()=>{
  action.value = 'add';
  dialogVisible.value = true;

  // 等待 DOM 更新后操作表单
  nextTick(() => {
    resetForm()
  });
}


const handleSearch=(event)=>{
  if (event) {
    event.preventDefault(); // 阻止默认行为
  }
  config.user_id =formInline.keyWord
}

</script>

<template>
  <div class="order-header">
    <el-button type="primary" @click="openAddDialog"  ><el-icon><Plus /></el-icon>添加订单</el-button>
    <el-form :inline="true" :model="formInline"   @submit.native.prevent >
      <el-form-item   >
        <el-input placeholder="请输入订单号" v-model="formInline.keyWord"
                  @keyup.enter="handleSearch"
                  prefix-icon="Search"
        ></el-input>

      </el-form-item>
      <el-form-item  >
        <el-button   type="primary" @click="handleSearch">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div class="table-container">
      <el-card class="data-card" shadow="hover">
        <el-table class="order-table" :row-class-name="tableRowClassName"  :data="orderData"  type="" stripe border ref="table">
          <el-table-column v-for="item in tableLabels "
                           :key="item.prop"
                           :width="item.width ? item.width : '120px'"
                           :prop="item.prop"
                           :label="item.label"
                           show-overflow-tooltip ></el-table-column>
          <el-table-column fixed="right" label="操作" min-width="140" width="240">
            <template #default="scope">
              <div class="action-buttons">
                <el-button  class="edit-btn"  link type="primary" size="small" @click="openEditDialog(scope.row)" >
                  <el-icon><Edit /></el-icon> Edit
                </el-button>
                <el-button  class="delete-btn" link type="danger" size="small" @click="delgood(scope.row)">
                  <el-icon><Delete /></el-icon>Delete
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

      </el-card>
  </div>
</template>

<style scoped lang="less">
.order-header{
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.table-container {
  .data-card {
    border-radius: 12px;
    overflow: hidden;
    border: none;

    :deep(.el-card__body) {
      padding: 0;
    }
  }

  .order-table {
    --el-table-border-color: #ebeef5;
    --el-table-header-bg-color: #f8f9fc;

    :deep(.el-table__header) {
      th {
        font-weight: 600;
        color: #303133;
        background: #f8f9fc !important;
      }
    }

    :deep(.el-table__row) {
      transition: all 0.2s;

      &:hover {
        transform: translateX(4px);
        box-shadow: 4px 0 8px rgba(64,158,255,0.1);
      }

      td {
        padding: 12px 0;
      }
    }

    .product-image {
      width: 80px;
      height: 80px;
      border-radius: 8px;
      border: 1px solid #ebeef5;
    }

    .action-buttons {
      display: flex;
      gap: 8px;

      .edit-btn {
        color: #409eff;
        &:hover {
          color: lighten(#409eff, 10%);
        }
      }

      .delete-btn {
        color: #f56c6c;
        &:hover {
          color: lighten(#f56c6c, 10%);
        }
      }
    }
  }
}

.pagination-container {
  padding: 24px;
  display: flex;
  justify-content: center;

  .smart-pagination {
    :deep(.btn-prev),
    :deep(.btn-next) {
      border-radius: 8px;
      padding: 0 12px;
    }

    :deep(.number) {
      border-radius: 8px;
      transition: all 0.2s;

      &.active {
        background: linear-gradient(135deg, #409eff, #337ecc);
        color: #fff;
      }

      &:hover {
        transform: translateY(-2px);
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {

  .table-container {
    overflow-x: auto;

    .order-table {
      min-width: 800px;
    }
  }
}
</style>