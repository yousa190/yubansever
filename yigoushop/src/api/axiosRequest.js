import axios from 'axios';
import { ElMessage, ElLoading } from 'element-plus';
import config from "@/config";

// 创建 Axios 实例，使用环境配置中的超时时间
const service = axios.create({
  timeout: config.timeout, // 从环境配置中获取超时时间
});

// 加载状态管理（可选，根据需求启用）
let loadingInstance = null;
const showLoading = (options) => {
  // 支持通过请求参数单独控制是否显示加载动画
  if (options.loading !== false) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: '加载中...',
      background: 'rgba(0, 0, 0, 0.1)'
    });
  }
};

const hideLoading = () => {
  if (loadingInstance) {
    loadingInstance.close();
    loadingInstance = null;
  }
};

// 请求拦截器
service.interceptors.request.use(
  (reqConfig) => {
    // 1. 显示加载动画（如果需要）
    showLoading(reqConfig);

    // 2. 添加认证信息（示例：从localStorage获取token）
    const token = localStorage.getItem('token');
    if (token) {
      reqConfig.headers.Authorization = `Bearer ${token}`;
    }

    // 3. 开发环境打印请求信息
    if (config.debug) {
      console.log(`[请求] ${reqConfig.method.toUpperCase()} ${reqConfig.url}`);
      if (reqConfig.data) console.log('请求数据:', reqConfig.data);
    }

    return reqConfig;
  },
  (error) => {
    hideLoading(); // 错误时关闭加载
    ElMessage.error('请求参数错误');
    return Promise.reject(error);
  }
);

// 常量定义（可根据后端实际约定调整）
const RESPONSE_CODES = {
  SUCCESS: '00000',       // 业务成功
  DATA_ERROR: '40000',    // 数据错误
  UNAUTHORIZED: '40100',  // 未授权（token失效等）
  FORBIDDEN: '40300'      // 权限不足
};

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    hideLoading(); // 关闭加载动画

    // 开发环境打印响应信息
    if (config.debug) {
      console.log(`[响应] ${response.config.url}`, response.data);
    }

    const { code, data, message } = response.data || {};

    // 1. 处理业务状态码
    if (code === RESPONSE_CODES.SUCCESS) {
      return data; // 直接返回业务数据，简化上层调用
    }

    // 2. 特殊错误处理
    switch (code) {
      case RESPONSE_CODES.UNAUTHORIZED:
        // 示例：token失效时跳转登录页
        ElMessage.error('登录已过期，请重新登录');
        setTimeout(() => {
          window.location.href = '/login';
        }, 1000);
        break;
      case RESPONSE_CODES.FORBIDDEN:
        ElMessage.error('您没有权限执行此操作');
        break;
      default:
        // 通用错误提示
        ElMessage.error(message || '操作失败，请稍后重试');
    }

    return Promise.reject(message || '业务处理失败');
  },
  (error) => {
    hideLoading(); // 关闭加载动画

    // 处理HTTP网络错误
    let errorMsg = '网络异常，请检查网络连接';
    if (error.response) {
      // 有响应但状态码非2xx
      const { status, statusText } = error.response;
      switch (status) {
        case 404:
          errorMsg = `请求地址不存在 (404)`;
          break;
        case 500:
          errorMsg = `服务器内部错误 (500)`;
          break;
        case 403:
          errorMsg = `权限不足 (403)`;
          break;
        default:
          errorMsg = `请求失败 (${status}): ${statusText}`;
      }
    } else if (error.request) {
      // 无响应（如超时、跨域等）
      errorMsg = '请求超时或服务器未响应';
    }

    // 开发环境打印详细错误
    if (config.debug) {
      console.error('[请求错误]', error);
    }

    ElMessage.error(errorMsg);
    return Promise.reject(errorMsg);
  }
);

/**
 * 核心请求函数
 * @param {Object} options - 请求配置
 * @param {string} options.url - 请求地址
 * @param {string} [options.method='get'] - 请求方法
 * @param {Object} [options.data] - 请求数据（post/put等）
 * @param {Object} [options.params] - URL参数（get等）
 * @param {boolean} [options.mock] - 是否强制使用mock（优先级高于全局配置）
 * @param {boolean} [options.loading=true] - 是否显示加载动画
 * @returns {Promise} 请求结果
 */
function request(options) {
  // 1. 处理默认请求方法
  options.method = (options.method || 'get').toLowerCase();

  // 2. 处理get请求的参数（自动将data转为params）
  if (options.method === 'get' && options.data) {
    options.params = { ...options.params, ...options.data };
    options.data = undefined; // 清除get请求的data，避免axios警告
  }

  // 3. 处理mock逻辑（优先级：请求参数 > 全局配置）
  const useMock = options.mock ?? config.mock;
  // 动态设置baseURL（mock地址/真实接口地址）
  options.baseURL = useMock ? config.mockApi : config.baseApi;

  return service(options);
}

// 快捷请求方法（简化调用）
['get', 'post', 'put', 'delete', 'patch'].forEach(method => {
  request[method] = (url, options = {}) => {
    return request({ ...options, url, method });
  };
});

export default request;
