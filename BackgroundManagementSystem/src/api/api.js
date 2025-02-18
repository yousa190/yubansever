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
            mock: true,
            data:{}
        })
    },
    getCountData(){
        return request({
            url:'/home/countdata',
            method:"get",
            mock: true,
            data:{}
        })
    },
    getChartData(){
        return request({
            url:'/home/chartdata',
            method:"get",
            mock: true,
            data:{}
        })
    },
    getUserInfoData(data){
        return request({
            url:'/home/userinfodata',
            method:"get",
            mock: true,
            data:data,
        })
    },
    deleteUser(data){
        return request({
            url:'/home/deleteuser',
            method:"get",
            mock: true,
            data:data,
        })
    },
    addUser(data){
        return request({
            url:'/home/adduser',
            method:"post",
            mock: true,
            data:data,
        })
    },
    editUser(data){
        return request({
            url:'/home/edituser',
            method:"post",
            mock: true,
            data:data,
        })
    },
    getMenu(params){
        return request({
            url:'/permission/getmenu',
            method:"post",
            mock: true,
            data:params,
        })
    },
    getEmailCode(data){
        return request({
            url:'/email/code',
            method:"get",
            mock: false,
            data:data,
        })
    }
}