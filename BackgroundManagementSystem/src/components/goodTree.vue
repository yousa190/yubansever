
<script setup>
import {getCurrentInstance} from "vue";


const {proxy} =getCurrentInstance()
const goodprops = {
  label: 'catname',
  children: 'child',
  isLeaf: 'leaf'
}

// 定义可触发的事件
const emit = defineEmits(['config-sent']);

const loadNode = async (node, resolve) => {
  // console.log(node)
  if (node.level === 0) {
    let res= await proxy.$api.getCateList({cat_pid:0})
    return resolve(res.list)
  }
  else {
    let res= await proxy.$api.getCateList({cat_pid:node.data.cat_id})
    // console.log(res)
    return resolve(res.list)
  }

  // console.log(res)
}

// 获取数据
const click=(data,node)=>{
//   传递父组件
  emit('config-sent', data);
}

</script>
<template>
  <el-tree
      style="max-width: 600px"
      :props="goodprops"
      :load="loadNode"
      lazy
      accordion
      @node-click="click"
  />


</template>
