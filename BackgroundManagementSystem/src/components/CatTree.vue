<script setup>
import { getCurrentInstance } from "vue";

const { proxy } = getCurrentInstance();
const goodprops = {
  label: 'catName',
  children: 'child',
  isLeaf: (data, node) => {
    // 判断是否为叶子节点的逻辑
    return data.catLevel >= 3 || node.level >= 2
  }
};

// 定义可触发的事件
const emit = defineEmits(['config-sent']);
const click=(data,node)=>{
//   传递父组件
  emit('config-sent', data);
}

const loadNode = async (node, resolve) => {
  try {
    if (node.level === 0) {
      // 加载第一层（level 0）
      const res = await proxy.$api.getCateList({ catPid: 0 })
      return resolve(res.list || [])
    }

    if (node.level >= 2) {  // 当层级超过2时不再加载
      return resolve([])
    }

    // 加载子层（level 1）
    const res = await proxy.$api.getCateList({
      catPid: node.data.catId
    })

    // 自动设置叶子节点状态
    const processedList = (res.list || []).map(item => ({
      ...item,
      // 如果已经是第三层或父级层级超过限制，标记为叶子节点
      leaf: item.catLevel >= 3 || node.level >= 1
    }))

    resolve(processedList)
  } catch (error) {
    console.error('加载节点失败:', error)
    resolve([])
  }
}
</script>

<template>
  <el-tree
      :props="goodprops"
      :load="loadNode"
      lazy
      accordion
      highlight-current
      @node-click="click"
  />
</template>