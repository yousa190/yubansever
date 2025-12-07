package com.yuban.shop.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "用户信息DTO")
public class UserDto {
    @Schema(description = "用户唯一标识")
    private Integer userId; // 用户唯一标识
    
    @Schema(description = "登录账号（唯一）")
    private String username; // 登录账号（唯一）
    
    @Schema(description = "验证邮箱（唯一）")
    private String email; // 验证邮箱（唯一）
    
    @Schema(description = "联系电话")
    private String phone; // 联系电话
    
    @Schema(description = "性别：1-男，0-女")
    private Integer sex; // 性别：1-男，0-女
    
    @Schema(description = "头像地址")
    private String avatar; // 头像地址

    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "最后登录时间")
    private Date lastLogin; // 最后登录时间

    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "创建时间")
    private Date createdAt; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "更新时间")
    private Date updatedAt; // 更新时间

    @Schema(description = "用户权限等级：1-普通用户，2-管理员，3-超级管理员")
    private Integer level; // 用户权限等级：1-普通用户，2-管理员，3-超级管理员

    @Schema(description = "启用禁用：1-禁用，0-启用")
    private Integer status; // 启用禁用：1-禁用，0-启用
}