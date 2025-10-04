# Swagger API文档故障排除指南

## 当前问题分析

您遇到的问题是：
```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.
There was an unexpected error (type=Not Found, status=404).
```

## 已完成的修复

### 1. 依赖配置修复
- ✅ 为public模块添加了Swagger依赖
- ✅ 为admin模块添加了Swagger依赖
- ✅ 在application.yml中添加了SpringDoc配置

### 2. 配置类修复
- ✅ 修改了启动类，添加了组件扫描配置
- ✅ 创建了测试配置类用于验证

## 故障排除步骤

### 步骤1：重新编译和启动
```bash
# 清理并重新编译项目
mvn clean compile

# 重启应用
```

### 步骤2：检查启动日志
启动应用时，查找以下关键日志：

**成功启动的标志：**
```
INFO --- [main] o.springdoc.api.AbstractOpenApiResource  : Init duration for springdoc-openapi is: XXX ms
INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080
```

**如果看到这些日志，说明Swagger配置成功。**

### 步骤3：测试访问路径

按顺序尝试以下路径：

1. **OpenAPI JSON文档**（最简单）：
   ```
   http://localhost:8080/v3/api-docs
   ```
   如果这个能访问，返回JSON格式的API文档，说明Swagger配置正确。

2. **Swagger UI界面**：
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **备用路径**：
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

### 步骤4：如果仍然404

#### 检查依赖是否正确加载
在启动日志中查找是否有SpringDoc相关的错误信息。

#### 检查配置类是否被扫描到
在SwaggerConfig类中添加日志：
```java
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        System.out.println("=== SwaggerConfig is being loaded ===");
        return new OpenAPI()
                .info(new Info()
                        .title("电商平台API文档")
                        .version("1.0.0"));
    }
}
```

#### 检查端口是否正确
确认应用启动在8080端口：
```
INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080
```

## 常见问题解决

### 问题1：依赖冲突
**症状**：启动时报错，找不到相关类
**解决**：检查是否有其他Swagger依赖冲突

### 问题2：配置类未被扫描
**症状**：启动正常，但Swagger不工作
**解决**：确保@ComponentScan包含配置类所在包

### 问题3：路径映射问题
**症状**：404错误
**解决**：检查application.yml中的springdoc配置

## 验证清单

请按以下清单检查：

- [ ] 应用成功启动，无错误日志
- [ ] 启动日志中包含SpringDoc初始化信息
- [ ] 端口8080正常监听
- [ ] 访问 `/v3/api-docs` 返回JSON文档
- [ ] 访问 `/swagger-ui.html` 显示UI界面

## 下一步操作

1. **重启应用**
2. **检查启动日志**，确认SpringDoc初始化成功
3. **先访问** `http://localhost:8080/v3/api-docs`
4. **再访问** `http://localhost:8080/swagger-ui.html`

如果按照以上步骤仍然无法访问，请提供：
1. 完整的启动日志
2. 访问 `http://localhost:8080/v3/api-docs` 的结果
3. 任何错误信息

