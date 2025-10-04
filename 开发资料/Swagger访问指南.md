# Swagger API文档访问指南

## 问题分析

根据您提供的日志：
```
2025-10-03T13:04:02.137+08:00  INFO 16156 --- [nio-8080-exec-7] o.springdoc.api.AbstractOpenApiResource  : Init duration for springdoc-openapi is: 544 ms
2025-10-03T13:04:09.875+08:00  WARN 16156 --- [nio-8080-exec-2] o.s.web.servlet.PageNotFound             : No mapping for GET /swagger-ui/index.html
```

**问题原因**：
1. SpringDoc已经成功初始化（544ms）
2. 但是访问路径映射有问题

## 解决方案

### 1. 正确的访问路径

SpringDoc OpenAPI 3.x的默认访问路径：

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

### 2. 配置调整

在 `application.yml` 中添加以下配置：

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
    tagsSorter: alpha
    doc-expansion: none
  show-actuator: true
```

### 3. 如果仍然无法访问

如果上述路径仍然无法访问，请尝试以下路径：

- `http://localhost:8080/swagger-ui.html` (简化路径)
- `http://localhost:8080/swagger-ui/` (目录路径)

## 验证步骤

### 1. 检查依赖是否正确加载

启动应用时，应该看到类似的日志：
```
INFO 16156 --- [main] o.springdoc.api.AbstractOpenApiResource  : Init duration for springdoc-openapi is: XXX ms
```

### 2. 检查OpenAPI文档是否可访问

先访问：`http://localhost:8080/v3/api-docs`
如果这个可以访问，说明SpringDoc配置正确。

### 3. 检查Swagger UI

然后访问：`http://localhost:8080/swagger-ui/index.html`

## 常见问题解决

### 问题1：404错误
**原因**：路径映射问题
**解决**：检查SpringDoc配置，确保路径正确

### 问题2：依赖冲突
**原因**：可能存在其他Swagger依赖冲突
**解决**：检查pom.xml，确保只使用SpringDoc依赖

### 问题3：配置文件位置
**原因**：SwaggerConfig位置不正确
**解决**：确保SwaggerConfig在public模块中，并被admin模块正确引用

## 测试接口

配置成功后，您应该能看到以下接口分组：

1. **用户管理接口** - 用户注册、验证等
2. **后台分类参数相关接口** - 分类配置管理
3. **商品分类管理接口** - 商品分类增删改查
4. **邮件服务接口** - 邮箱验证码发送
5. **文件上传接口** - 图片上传
6. **文件下载接口** - 图片下载
7. **用户登录接口** - 登录相关（待实现）

## 下一步

1. 重启应用
2. 访问 `http://localhost:8080/v3/api-docs` 确认JSON文档可访问
3. 访问 `http://localhost:8080/swagger-ui/index.html` 访问UI界面
4. 如果仍有问题，请检查应用启动日志中的SpringDoc相关信息
