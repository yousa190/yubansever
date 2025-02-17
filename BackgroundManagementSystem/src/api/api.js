/*
*
* Unified api management throughout the project
*
* */

import request from "@/api/axiosRequest.js";

// request the home_left data
export default {
    getTableData(){
        return request({
            url:'/home/tabledata',
            method:"get",
            mock: false,
            data:{}
        })
    },
    getCountData(){
        return request({
            url:'/home/countdata',
            method:"get",
            mock: false,
            data:{}
        })
    },
    getChartData(){
        return request({
            url:'/home/chartdata',
            method:"get",
            mock: false,
            data:{}
        })
    },
    getUserInfoData(data){
        return request({
            url:'/home/userinfodata',
            method:"get",
            mock: false,
            data:data,
        })
    },
    deleteUser(data){
        return request({
            url:'/home/deleteuser',
            method:"get",
            mock: false,
            data:data,
        })
    },
    addUser(data){
        return request({
            url:'/home/adduser',
            method:"post",
            mock: false,
            data:data,
        })
    },
    editUser(data){
        return request({
            url:'/home/edituser',
            method:"post",
            mock: false,
            data:data,
        })
    },
    getMenu(params){
        return request({
            url:'/permission/getmenu',
            method:"post",
            mock: false,
            data:params,
        })
    },

}