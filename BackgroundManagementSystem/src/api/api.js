/*
*
* Unified api management throughout the project
*
* */

import request from "@/api/axiosRequest.js";

export default {
    getMenu(params){
        return request({
            url:'/permission/getmenu',
            method:"post",
            mock: true,
            data:params,
        })
    },
    // User
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


    // mall
    getGoodList(data){
        return request({
            url:'/good/goodlist',
            method:"get",
            mock: true,
            data:data
        })
    },

    addGood(data){
        return request({
            url:'/good/addgood',
            method:"post",
            mock: true,
            data:data,
        })
    },
    deleteGood(data){
        return request({
            url:'/good/deletegood',
            method:"get",
            mock: true,
            data:data,
        })
    },

    // Categories
    getCateList(data){
        return request({
            url:'/mall/catelist',
            method:"get",
            mock: false,
            data:data
        })
    },
    addCate(data){
        return request({
            url:'/mall/addcat',
            method:"post",
            mock:false,
            data:data,
        })
    },
    updCate(data){
        return request({
            url:'/mall/updcat',
            method:"post",
            mock:false,
            data:data,
        })
    },
    delCate(data){
        return request({
            url:'/mall/delcat',
            method:"get",
            mock:false,
            data:data,
        })
    },


    // mallConf

    // getCatConf(data){
    //     return request({
    //         url:'/mall/getcatconf',
    //         method:"get",
    //         mock: true,
    //         data:data
    //     })
    // },

    getCatConf(data){
        return request({
            url:'/mallConf/list',
            method:"get",
            mock: false,
            data:data
        })
    },
    updateCatConf(data){
        return request({
            url:'/mallConf/edit',
            method:"post",
            mock:false,
            data:data,
        })
    },
    getOneConf(data){
        return request({
            url:'/mallConf/getone',
            method:"get",
            mock:false,
            data:data,
        })
    },
    delConf(data){
        return request({
            url:'/mallConf/delete',
            method:"get",
            mock:false,
            data:data,
        })
    },

    // register and reset
    getEmailCode(data){
        return request({
            url:'/email/code',
            method:"get",
            mock: false,
            data:data,
        })
    },
    checkEmail(data){
        return request({
            url:'/user/mailcheck',
            method:"get",
            mock: false,
            data:data,
        })
    },
    checkName(data){
        return request({
            url:'/user/namecheck',
            method:"get",
            mock: false,
            data:data,
        })
    },
    register(data){
        return request({
            url:'/user/register',
            method:"post",
            mock: false,
            data:data,
        })
    }
}