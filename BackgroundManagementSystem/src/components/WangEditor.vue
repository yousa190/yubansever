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
// 定义可触发的事件
const emit = defineEmits(['comment-sent','reset-comment']);
// 配置项
const mode = 'default' // 或 'simple'
const toolbarConfig = {
  excludeKeys: [
    'group-image',    // 隐藏整个图片组（上传+网络图片）
    'group-video',     // 隐藏整个视频组
    'insertTable',     // 隐藏表格
  ] , }
const editorConfig = {
  placeholder: '请输入商品描述...',
  MENU_CONF: {},

}


// 编辑器实例（必须用 shallowRef）
const editorRef = shallowRef(null)
// 最大字数限制
const maxLength = 5000

// 计算属性
const currentCount = computed(() => stripHtmlTags(editorContent.value).length)
const isOverLimit = computed(() =>currentCount.value > maxLength)





// 去除 HTML 标签的函数
const stripHtmlTags = (html) => {
  if (!html) return ''; // 增加判空处理
  return html.replace(/<[^>]*>/g, '') || '';
};



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
  if (!editor) return; // 增加判空处理

  const content = editor.getHtml()
  const plainText = stripHtmlTags(content)

  // 超过限制时截断内容


  if(plainText.length<=maxLength) emit('comment-sent', editorContent);
}

// 监听父组件内容变化
watch(() => props.Content, (newVal) => {
  editorContent.value = newVal
}, { immediate: true })

// 模拟异步获取内容
onMounted(() => {
})

</script>

<template>
  <div  class="textA">
    <div class="product-header">
      <div class="title-wrapper">
        <h2 class="main-title">商品介绍</h2>

        <el-tooltip content="请详细描述商品特性、使用场景等关键信息" placement="top">
          <el-icon class="tip-icon">
            <InfoFilled />
          </el-icon>
        </el-tooltip>
      </div>
      <!-- 字数统计 -->
      <div class="word-counter">

      <span :class="{ 'limit-exceeded': isOverLimit }">
        {{ currentCount }}/{{ maxLength }}
      </span>
        <el-text
            v-if="isOverLimit"
            type="danger"
            class="limit-alert"
        >超出字数限制</el-text>
        <span class="sub-title">(建议500-1000字)</span>
      </div>
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

    .product-header {
        margin: 10px auto;
        display: flex;
      .title-wrapper {
        margin-left: 20px;
        display: flex;
        align-items: center;
        gap: 6px;

        .main-title {
          margin: 0;
          font-size: 16px;
          color: #303133;
        }

        .tip-icon {
          color: #909399;
          cursor: help;
        }
      }

      .sub-title {
        font-size: 12px;
        color: #909399;
        display: block;
        margin-top: 4px;
      }
    }

    .word-counter {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 2px;
      font-size: 14px;
      margin-left: 40px;

      .limit-exceeded {
        color: red;
      }

      .limit-alert {
        color: red;
        margin-left: 10px;
      }
    }
  }

</style>