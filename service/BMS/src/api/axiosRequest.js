import axios from 'axios';
import { ElMessage } from 'element-plus';
import config from "@/config";

// 创建 Axios 实例
const service = axios.create({
    baseURL: config.baseApi
});

// 请求拦截器
service.interceptors.request.use(function (config) {
    // 在请求发送之前做些什么
    return config;
}, function (error) {
    // 处理请求错误
    return Promise.reject(error);
});

// 常量定义
const NETWORK_ERROR = 'Network Error'; // 统一的网络错误提示
const SUCCESS_CODE = '00000'; // 成功码

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        // 检查响应数据是否存在
        if (!response || !response.data) {
            ElMessage.error(NETWORK_ERROR);
            return Promise.reject(NETWORK_ERROR);
        }

        const { code, data, message } = response.data;

        // 检查业务状态码
        if (code === SUCCESS_CODE) {
            return data;  // 返回实际数据
        } else {
            // 弹出错误提示
            const errorMsg = message || NETWORK_ERROR;
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

function request(options) {
    console.log(options);
    options.method = options.method || "get";
    if (options.method.toLowerCase() === "get") {
        options.params = options.data;
    }

    // 处理 mock
    let isMock = config.mock;
    if (typeof options.mock !== "undefined") {
        isMock = options.mock;
    }

    // 处理环境
    service.defaults.baseURL = isMock ? config.mockApi : config.baseApi;

    return service(options);
}

export default request;