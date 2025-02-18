import { ref, computed,watch } from 'vue'
import { defineStore } from 'pinia'
import { useRoute, useRouter} from "vue-router";
// pinia init
function initstate(){
  return {
    isCollapse: false,
    tags :[
      {
        path:'/home',
        name:'home',
        label:'首页',
        icon:'home'
      }
    ],
    currentMenu: null,
    menuList:[],
    token:'',
    routerList:[]
  }
}
export const useAllDataStore = defineStore('allData', () => {
  const state =ref(initstate())

  // 菜单栏管理
  function selectMenu(val){
    if (val.name === 'home'){
      state.value.currentMenu = null
    }
    else{
        state.value.currentMenu = val
     let index= state.value.tags.findIndex((item)=>item.name===val.name)
      index ===-1 ? state.value.tags.push(val) :""
    }
  }
  function updateMenu(tag){
    let index =state.value.tags.findIndex((item)=>item.name===tag.name)
    state.value.tags.splice(index,1)
    console.log(state.value.tags)
  }
  function updateMenuList(val){
      state.value.menuList = val
  }

    /**
     * 动态加载菜单并添加到路由
     * @param {Object} router - Vue Router 实例
     */
    function addMenu(router,type) {
        if (type==='refresh'){
            if (JSON.parse(localStorage.getItem('store'))){
                state.value=JSON.parse(localStorage.getItem('store'))
                state.value.routerList=[]
            }else return
        }

        const menu = state.value.menuList; // 菜单列表
        const modules = import.meta.glob("../views/**/*.vue"); // 自动导入的视图组件
        const routeArr = []; // 用于存储最终的路由

        /**
         * 递归处理菜单项，生成路由配置
         * @param {Array} menuList - 菜单列表
         */
        const generateRoutes = (menuList) => {
            menuList.forEach((item) => {
                if (item.children && item.children.length > 0) {
                    // 如果有子菜单，递归处理
                    generateRoutes(item.children);
                } else {
                    // 动态获取组件路径
                    const url = `../views/${item.url}.vue`;
                    if (modules[url]) {
                        // 动态绑定组件到路由
                        item.component = modules[url];
                        routeArr.push(item);
                    } else {
                        // 如果路径未匹配到组件，输出警告
                        console.warn(`未找到组件文件：${url}`);
                    }
                }
            });
        };


        // 清除之前的动态路由
        router.getRoutes().forEach((item) => {
            if (item.name=='main' || item.name=='login'||item.name=='404'||item.name=='register') {
                return
            }else{
                router.removeRoute(item.name);
            }
        })

        // 递归生成路由
        generateRoutes(menu);

        // 添加路由到 Vue Router
        routeArr.forEach((route) => {
            const addedRoute = router.addRoute("main", route); // 将路由添加到 `main` 子路由下
            state.value.routerList.push(addedRoute); // 同步到状态管理的路由列表
        });
    }

    // 进行本地存储
    watch(state,(newObj)=>{
        if(!newObj.token)return;
        localStorage.setItem("store",JSON.stringify(newObj));
    },{deep:true});


  function clean(){
      state.value.routerList.forEach((item)=>{
        if (item)item()
      })
      state.value=initstate()
      localStorage.removeItem('token')
  }

  return {
          state,
          selectMenu,
          updateMenu,
          updateMenuList,
          addMenu,
          clean
    }
})
