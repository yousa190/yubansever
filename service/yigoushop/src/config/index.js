// 环境配置 - 优化版


// 获取当前环境（Vite环境变量），默认生产环境
const env = import.meta.env.MODE || "production";

// 基础默认配置（所有环境都会继承的配置）
const baseConfig = {
  // 环境标识
  env,
  // API请求超时时间（毫秒）
  timeout: 10000,
  // 全局默认主题
  theme: "default"
};

// 各环境专属配置
const envSpecificConfig = {
  // 开发环境（本地开发）
  development: {
    baseApi: "http://localhost:8080/", // 开发环境API地址
    mockApi: "/api",                     // 本地Mock服务地址
    mock: false,                               // 开发环境默认启用Mock
    debug: true                               // 开发环境开启调试模式
  },

  // 测试环境（测试服务器）
  test: {
    baseApi: "http://localhost:8080/",
    mockApi: "/api",
    mock: false,                              // 测试环境通常使用真实接口
    debug: true
  },

  // 生产环境（正式服务器）
  production: {
    baseApi: "https://api.example.com",
    mockApi: "",                              // 生产环境不启用Mock
    mock: false,                              // 强制关闭Mock
    debug: false                              // 生产环境关闭调试
  }
};

// 合并默认配置和当前环境配置（环境配置优先级更高）
const finalConfig = {
  ...baseConfig,
  ...(envSpecificConfig[env] || envSpecificConfig.production) // 容错处理：环境配置不存在时默认用生产环境配置
};

export default finalConfig;
