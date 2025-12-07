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

    // user
    getUserInfoData(data){
        return request({
            url:'/admin/user/list',
            method:"get",
            mock: false,
            data: data,
        })
    },
    toggleUserStatus(data){
        return request({
            url:'/admin/user/updateStatus',
            method:"post",
            mock: false,
            data: data,
        })
    },
    deleteUser(data){
        return request({
            url:'/admin/user/delete',
            method:"post",
            mock: false,
            data:data,
        })
    },
    addUser(data){
        return request({
            url:'/admin/user/register',
            method:"post",
            mock: false,
            data:data,
        })
    },
    editUser(data){
        return request({
            url:'/admin/user/update',
            method:"post",
            mock: false,
            data:data,
        })
    },


    // mall
    getGoodList(data){
        return request({
            url:'/admin/product/list',
            method:"get",
            mock: false,
            data:data
        })
    },

    getGoodDetail(data){
        return request({
            url:'/admin/product/detail',
            method:"get",
            mock: false,
            data:data
        })
    },

    addGood(data){
        return request({
            url:'/admin/product/add',
            method:"post",
            mock: false,
            data:data,
        })
    },
    deleteGood(data){
        return request({
            url:'/admin/product/del',
            method:"get",
            mock: false,
            data:data,
        })
    },
    editGood(data){
        return request({
            url:'/admin/product/edit',
            method:"post",
            mock: false,
            data:data,
        })
    },

    // 图片上传
    uploadImage(data) {
        return request({
            url: '/admin/upload/images',
            method: "post",
            mock: false,
            data: data,
            headers: { 'Content-Type': 'multipart/form-data' }
        })
    },
    
    // 图片清理
    cleanupImages(data) {
        return request({
            url: '/admin/image/cleanup',
            method: "post",
            mock: false,
            data: data
        })
    },
    
    // 商品图片更新（智能比对）
    updateProductImages(productId, images) {
        return request({
            url: '/admin/product/updateImages',
            method: "post",
            mock: false,
            data: images,
            params: { productId }
        })
    },

    // Categories
    getCateList(data){
        return request({
            url:'/admin/mall/catelist',
            method:"get",
            mock: false,
            data:data
        })
    },
    addCate(data){
        return request({
            url:'/admin/mall/addcat',
            method:"post",
            mock:false,
            data:data,
        })
    },
    updCate(data){
        return request({
            url:'/admin/mall/updcat',
            method:"post",
            mock:false,
            data:data,
        })
    },
    delCate(data){
        return request({
            url:'/admin/mall/delcat',
            method:"get",
            mock:false,
            data:data,
        })
    },


    // mallConf

    // getCatConf(data){
    //     return request({
    //         url:'/admin/mall/getcatconf',
    //         method:"get",
    //         mock: true,
    //         data:data
    //     })
    // },

    getCatConf(data){
        return request({
            url:'/admin/mallConf/list',
            method:"get",
            mock: false,
            data:data
        })
    },
    updateCatConf(data){
        return request({
            url:'/admin/mallConf/edit',
            method:"post",
            mock:false,
            data:data,
        })
    },
    getOneConf(data){
        return request({
            url:'/admin/mallConf/getone',
            method:"get",
            mock:false,
            data:data,
        })
    },
    delConf(data){
        return request({
            url:'/admin/mallConf/delete',
            method:"get",
            mock:false,
            data:data,
        })
    },

    // banner
    getBannerList(data) {
        return request({
            url: '/admin/banner/list',
            method: "get",
            mock: false,
            data: data
        })
    },
    
    addBanner(data) {
        return request({
            url: '/admin/banner/add',
            method: "post",
            mock: false,
            data: data
        })
    },
    
    updateBanner(data) {
        return request({
            url: '/admin/banner/update',
            method: "post",
            mock: false,
            data: data
        })
    },
    
    deleteBanner(data) {
        return request({
            url: '/admin/banner/delete',
            method: "get",
            mock: false,
            data: data
        })
    },
    
    // 图片清理
    cleanupImages(data) {
        return request({
            url: '/admin/image/cleanup',
            method: "post",
            mock: false,
            data: data
        })
    },

    // register and reset
    getEmailCode(data){
        return request({
            url:'/admin/email/code',
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