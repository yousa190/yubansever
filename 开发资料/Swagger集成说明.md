# Swagger API文档集成说明

## 概述

已成功为项目集成Swagger 3.x (SpringDoc OpenAPI) 用于生成和管理API文档。

## 集成内容

### 1. 依赖配置

在 `service/pom.xml` 中添加了以下依赖：

```xml
<!-- Swagger 3.x 版本 (SpringDoc OpenAPI) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

### 2. 配置类

创建了 `SwaggerConfig.java` 配置类：

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电商平台API文档")
                        .version("1.0.0")
                        .description("电商平台后台管理系统API接口文档")
                        .contact(new Contact()
                                .name("YuBan")
                                .email("19959771806@163.com"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("开发环境")
                ));
    }
}
```

### 3. Controller注解

为所有Controller添加了完整的API注解：

#### 用户管理接口 (`UserController`)
- `@Tag(name = "用户管理接口", description = "用户注册、验证等相关接口")`
- 包含用户注册、邮箱检查、用户名检查等接口

#### 分类配置接口 (`CategoryConfigController`)
- `@Tag(name = "后台分类参数相关接口", description = "分类配置管理相关接口")`
- 包含分类配置的增删改查接口

#### 商品分类接口 (`CategoryController`)
- `@Tag(name = "商品分类管理接口", description = "商品分类的增删改查相关接口")`
- 包含商品分类管理相关接口

#### 邮件服务接口 (`EmailController`)
- `@Tag(name = "邮件服务接口", description = "邮件发送、验证码等相关接口")`
- 包含邮箱验证码发送接口

#### 文件上传接口 (`UploadController`)
- `@Tag(name = "文件上传接口", description = "图片上传、文件管理等相关接口")`
- 包含图片上传接口

#### 文件下载接口 (`DownloadController`)
- `@Tag(name = "文件下载接口", description = "图片文件下载等相关接口")`
- 包含图片文件下载接口

#### 用户登录接口 (`LoginController`)
- `@Tag(name = "用户登录接口", description = "用户登录、注销等相关接口")`
- 预留了登录功能接口（待实现）

## 访问方式

启动项目后，可以通过以下地址访问API文档：

- **Swagger UI界面**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON文档**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML文档**: `http://localhost:8080/v3/api-docs.yaml`

## 注解说明

### 类级别注解

```java
@Tag(name = "接口分组名称", description = "接口分组描述")
```

### 方法级别注解

```java
@Operation(summary = "接口简要说明", description = "接口详细描述")
```

### 参数注解

```java
@Parameter(description = "参数描述", required = true)
```

### 响应注解

```java
@ApiResponse(responseCode = "200", description = "成功响应")
@ApiResponse(responseCode = "400", description = "请求参数错误")
```

## 配置选项

可以在 `application.yml` 中添加以下配置来自定义Swagger行为：

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs  # API文档路径
  swagger-ui:
    path: /swagger-ui.html  # Swagger UI路径
    operationsSorter: method  # 接口排序方式
    tagsSorter: alpha  # 标签排序方式
    doc-expansion: none  # 文档展开方式
  show-actuator: true  # 显示actuator端点
```

## 安全配置

如果需要为Swagger添加安全配置，可以在配置类中添加：

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(...)
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                    .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
}

private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
}
```

## 使用建议

1. **接口文档维护**: 每次添加新接口时，记得添加相应的API注解
2. **参数描述**: 为所有参数添加清晰的描述信息
3. **响应示例**: 可以为复杂响应添加示例数据
4. **版本管理**: 在API版本更新时，及时更新文档版本信息
5. **测试功能**: 可以直接在Swagger UI中测试接口功能

## 注意事项

1. **生产环境**: 建议在生产环境中禁用Swagger UI，只保留API文档JSON
2. **敏感信息**: 避免在API文档中暴露敏感信息
3. **性能影响**: Swagger注解不会影响运行时性能
4. **依赖版本**: 当前使用的是SpringDoc OpenAPI 2.0.2版本，兼容Spring Boot 3.x

## 后续扩展

可以进一步扩展的功能：

1. **接口分组**: 按模块对接口进行更细致的分组
2. **响应示例**: 为复杂响应添加示例数据
3. **错误码文档**: 统一错误码的文档说明
4. **接口测试**: 集成接口自动化测试
5. **文档导出**: 支持导出PDF或其他格式的API文档
