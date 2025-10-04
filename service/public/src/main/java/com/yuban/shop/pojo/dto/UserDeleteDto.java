package com.yuban.shop.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户删除DTO
 * 用于接收删除用户的请求参数
 */
@Data
@Schema(description = "用户删除参数")
public class UserDeleteDto {
    
    @Schema(description = "用户ID")
    private Long userId;
}