<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { watch,onBeforeUnmount, ref, shallowRef, onMounted,computed } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


// 编辑器内容
const props = defineProps({
  Content:{
    type: String,    // 数据类型
    required: false,  // 是否必须
    default: '' // 默认值
  }
})
const editorContent = ref('')

// 监听父组件内容变化
watch(() => props.Content, (newVal) => {
    editorContent.value = newVal
}, { immediate: true })


// 编辑器实例（必须用 shallowRef）
const editorRef = shallowRef()
// 最大字数限制
const maxLength = 500

// 定义可触发的事件
const emit = defineEmits(['comment-sent','reset-comment']);




// 计算属性
const currentCount = computed(() => {
  return stripHtmlTags(editorContent.value).length
})
const isOverLimit = computed(() => {
  return currentCount.value > maxLength
})
// 去除 HTML 标签的函数
const stripHtmlTags = (html) => {
  const tmp = document.createElement('div')
  tmp.innerHTML = html
  return tmp.textContent || tmp.innerText || ''
}

// 配置项
const mode = 'default' // 或 'simple'
const toolbarConfig = {
  excludeKeys: [
    'group-image',    // 隐藏整个图片组（上传+网络图片）
    'group-video',     // 隐藏整个视频组
    'insertTable',     // 隐藏表格
  ] , }
const editorConfig = {
  placeholder: '请输入商品介绍...',
  MENU_CONF: {},

}


// 模拟异步获取内容
onMounted(() => {
})

// 销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

// 编辑器创建回调
const handleCreated = (editor) => {
  editorRef.value = editor
}

const handleChange=(editor)=>{
  const content = editor.getHtml()
  const plainText = stripHtmlTags(content)

  // 超过限制时截断内容
  if (plainText.length > maxLength) {
    const truncatedText = plainText.slice(0, maxLength)
    editor.dangerouslyInsertHtml(truncatedText)
  }

  editorContent.value = editor.getHtml()
  emit('comment-sent', editorContent);
}


</script>

<template>
  <div  class="textA">
    <!-- 字数统计 -->
    <div class="word-counter">
      <span :class="{ 'limit-exceeded': isOverLimit }">
        {{ currentCount }}/{{ maxLength }}
      </span>
      <el-alert
          v-if="isOverLimit"
          title="超出字数限制"
          type="error"
          :closable="false"
          show-icon
          class="limit-alert"
      />
    </div>
    <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editorRef"
        :default-config="toolbarConfig"
        :mode="mode"
    />
    <Editor
        style="height: 500px; overflow-y: hidden;"
        v-model="editorContent"
        :default-config="editorConfig"
        :mode="mode"
        @on-created="handleCreated"
        @on-change="handleChange"
    />

  </div>
</template>

<style scoped lang="less">
  .textA{
    border: 1px solid #ccc;
    max-width: 850px;
    width: 100%;
    max-height:400px ;
  }

</style>