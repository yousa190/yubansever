<script setup >

import {computed, ref,getCurrentInstance,onMounted,reactive} from "vue";
import * as echarts from "echarts"
const {proxy} =getCurrentInstance()



const getImgurl=(user)=>{
  return new URL(`../assets/images/${user}.jpg`,import.meta.url).href;
}

/* 20250121 :  后面可以实现一个最近的订单展示  */

// 折线图与柱状图的共用配置
const xOption =reactive({
  textstyle:{
    color: "#333",
  },
  legend:{},
  grid:{
    left:"20%",
  },
//   展示框
  tooltip:{
    trigger:"axis",

  },
  xAxis:{
    type:"category",
    data:[],
    axisLine:{
      lineStyle:{
        color:"#17b3a3",
      },
    },
    axisLabel:{
      interval:0,
      color:"#333",
    }
  },
  yAxis:{
    type:"value",
    axisLine:{
      lineStyle:{
        color:"#17b3a3",
      },
    }
  },
  color:["#2ec7c9","#b6a2de","#5ab1ef","#ffb980","#d87a80","#8d98b3"],
  series:[],

})
const pieOptions =reactive({
  tooltip:{
    trigger:"item",
  },
  legend:{},
  colorL:[
    "#0f78f4",
    "#dd536b",
    "#9462e5",
    "#a6a6a6",
    "#e1bb22",
    "#39c362",
    "#3ed1cf",
  ],
  series:[],
})
/* render  the table */
const labelData=ref({
  name:"商品名",
  count:"数量",
  price: "商品单价",
  total: "总价"
})
const tableData = ref([
  {
    name: "doll",
    count: 100,
    price: 20.0,
    total: 11
  },
  {
    name: "doll",
    count: 100,
    price: 20.0,
    total: 11
  },
  {
    name: "doll",
    count: 100,
    price: 20.0,
    total: 11
  }
])
const countData = ref([])
const chartData =ref([])
const observer =ref(null)

const getTableD =async () => {
  const data = await proxy.$api.getTableData();
  tableData.value = data.tableData
}

const getCountD =async () => {
  const data = await proxy.$api.getCountData();
  countData.value = data
}

const getChartD =async () => {
  const {orderData,videoData,userData} = await proxy.$api.getChartData();
  //
  xOption.xAxis.data=orderData.date
  xOption.series=Object.keys(orderData.data[0]).map(val=>{
    return {
      name: val,
      data: orderData.data.map(i => i[val]),
      type: "line"
    }
  })
  const aEcharts = echarts.init(proxy.$refs['echart'])
  aEcharts.setOption(xOption)

  //
  xOption.xAxis.data =userData.map(i=>i.date)
  xOption.series=[
    {
      name:"新增用户",
      data:userData.map(i=>i.new),
      type:"bar",
    },
    {
      name:"活跃用户",
      data:userData.map(i=>i.active),
      type:"bar",
    },
  ]
  const bEcharts = echarts.init(proxy.$refs['userchart'])
  bEcharts.setOption(xOption)

  //
  pieOptions.series =[{
    data:videoData,
    type:"pie"
  }]
  const cEcharts = echarts.init(proxy.$refs['videochart'])
  cEcharts.setOption(pieOptions)

//   页面监听
  observer.value=new ResizeObserver(()=>{
    aEcharts.resize()
    bEcharts.resize()
    cEcharts.resize()
  })

//   容器存在
  if (proxy.$refs['echart']){
    observer.value.observe(proxy.$refs['echart'])
  }

}

onMounted(()=>{
  getTableD()
  getCountD()
  getChartD()
})


</script>


<template >
  <el-row class="home" :gutter="20">
    <el-col :span="8" style="margin-top:20px ">
<!--   user_table   -->
      <el-card shadow="hover" class="usr_table">
        <div class="user_info">
          <img :src="getImgurl('user-img')" class="user_info">
          <div class="user-detail">
            <p class="uname">你好! name</p>
            <p class="ujob">job</p>
          </div>
        </div>
        <div class="login-info">
            <p>上次登陆时间: <span>???</span></p>
            <p>上次登陆地点: <span>???</span></p>
        </div>
      </el-card>
<!--   recent order.js-->
      <el-card shadow="hover" class="usr_table">
        <el-table :data="tableData">
          <el-table-column
              v-for="(val ,index) in  labelData"
              :key="index"
              :prop="index"
              :label="val"
          >
          </el-table-column>
        </el-table>
      </el-card>
    </el-col>

    <el-col :span="16" style="margin-top:20px ">
      <div class="items">
      <el-card
        :body-style="{ display: 'flex',padding: 0  }"
        v-for=" item in countData"
        :key="item.name">
        <component
        :is="item.icon" class="item-icons"
        :style="{ background: item.color  }"
        ></component>
        <div class="detail">
          <p class="num">${{item.value}}</p>
          <p class="txt">${{item.name}}</p>
        </div>
      </el-card>
      </div>
      <el-card class="home_chart">
        <div ref="echart" style="height: 280px"></div>
      </el-card>

      <div class="graph">
        <el-card>
          <div ref="userchart" style="height: 240px"></div>
        </el-card>
        <el-card>
          <div ref="videochart" style="height: 240px"></div>
        </el-card>
      </div>
    </el-col>
  </el-row>

</template>



<style scoped lang="less">
    .home{
      overflow: hidden;
      height: 100%;
      .user_info{
        display: flex;
        align-items: center;
        border-bottom: 1px solid #ccc;
        margin-bottom: 20px;
        img{
          width: 148px;
          height: 148px;
          border-radius: 10%;
          justify-content: center;
          align-items: center;
          margin-right: 15px;
        }

      }
      .user-detail{
        p{
          line-height: 40px;
        }
        .uname{
          font-size: 20px;
        }
        .ujob{
          font-size: 18px;
          color: #545c6a;
        }
      }
      .login-info{
        p{
          line-height: 30px;
          font-size: 18px;
          color: #333;
          //text-shadow: 0 0 5px #424242, 0 0 10px #2a2a2a, 0 0 15px #424242, 0 0 4px #2a2a2a, 0 0 10px #ff00de;
          span{
            color: #666;
            margin-left: 20px;
          }
        }
      }
      .usr_table{
        margin-top: 20px;
      }

      .items{
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        .el-card{
          width: 32%;
          margin-bottom: 20px;
        }

        .item-icons{
          width: 80px;
          height: 80px;
          font-size: 30px;
          text-align: center;
          line-height: 80px;
          color: #fff;
        }
        .detail{

          margin-left: 15px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .num{
            font-weight: bold;
            font-size: 28px;
            margin-bottom: 10px;
          }
          .txt{
            font-size: 14px;
            text-align: center;
            color: #999;
            overflow: hidden;
          }
        }
      }
      .graph{
        display: flex;
        justify-content: space-between;
        .el-card{
          width: 48%;
          height: 260px;
        }
      }
    }
</style>
