
// WangEditor
const { createEditor, createToolbar } = window.wangEditor

const editorConfig =  {
    placeholder: '请输入内容...',
    onChange(editor) {
        const html = editor.getHtml()
        console.log('editor content', html)
        // 也可以同步到 <textarea>
    },

    MENU_CONF: {}
}




//图片上传
editorConfig.MENU_CONF['uploadImage']={
    fieldName: 'img',

    server: 'http://localhost:8080/upload/images',

    maxFileSize: 1024 * 1024 * 1024,

    // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
    allowedFileTypes: ["image/*"],
    headers: {
    },
    withCredentials: false,
// 自定义插入图片
     customInsert(res, insertFn) {  // TS 语法
        // customInsert(res, insertFn) {                  // JS 语法
        // res 即服务端的返回结果
        // 从 res 中找到 url alt href ，然后插入图片
        insertFn(res.data.url, res.data.alt, res.data.href)
     },


}


//
const editor = createEditor({
    selector: '#editor-container',
    html: '<p><br></p>',
    config: editorConfig,
    mode: 'default', // or 'simple'
})

const toolbarConfig = {}

const toolbar = createToolbar({
    editor,
    selector: '#toolbar-container',
    config: toolbarConfig,
    mode: 'default', // or 'simple'
})