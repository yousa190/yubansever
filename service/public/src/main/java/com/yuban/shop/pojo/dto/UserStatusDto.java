package com.yuban.shop.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户状态更新DTO
 * 用于接收更新用户状态的请求参数
 */
@Data
@Schema(description = "用户状态更新参数")
public class UserStatusDto {
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "状态：0-启用，1-禁用")
    private Integer status;
}