import axios from 'axios';

import {ElMessage} from 'element-plus'
import config from "@/config";

const service = axios.create({
    baseURL: config.baseApi
})
// Add a request interceptor
service.interceptors.request.use(function (config) {
    // Do something before request is sent
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

// 常量定义
const NETWORK_ERROR = 'Network Error'; // 统一的网络错误提示
const SUCCESS_CODE = 200; // 成功的业务状态码

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        // 检查响应数据是否存在
        if (!response || !response.data) {
            ElMessage.error(NETWORK_ERROR);
            return Promise.reject(NETWORK_ERROR);
        }

        const { code, data, msg } = response.data;

        // 检查业务状态码
        if (code === SUCCESS_CODE) {
            return data; // 返回实际数据
        } else {
            // 弹出错误提示
            const errorMsg = msg || NETWORK_ERROR;
            ElMessage.error(errorMsg);
            return Promise.reject(errorMsg); // 返回被拒绝的 Promise
        }
    },
    (error) => {
        // 处理 HTTP 请求错误（如网络错误或服务器错误）
        const errorMsg = error.message || NETWORK_ERROR;
        ElMessage.error(errorMsg);
        return Promise.reject(errorMsg);
    }
);


function request(options){
    options.method=options.method||"get";
    if(options.method.toLowerCase()==="get"){
        options.params=options.data;
    }
    // 处理mock
    let isMock = config.mock
    if(typeof options.mock !== "undefined"){}
     isMock = options.mock

    // 处理环境
    if (config.env === 'prod'){
        service.defaults.baseURL= config.baseApi;
    }
    else {
        service.defaults.baseURL= isMock ? config.mockApi : config.baseApi;
    }
    return service(options);
}

export default request;