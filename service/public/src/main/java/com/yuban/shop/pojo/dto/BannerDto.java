package com.yuban.shop.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "轮播图传输对象")
public class BannerDto {
    
    @Schema(description = "横幅ID")
    private Long id;
    
    @Schema(description = "横幅标题")
    private String title;
    
    @Schema(description = "横幅图片URL")
    private String imageUrl;
    
    @Schema(description = "跳转类型(0:公告,1:商品,2:广告)")
    private Integer targetType;
    
    @Schema(description = "目标ID(根据target_type关联announcement或product表)")
    private Long targetId;
    
    @Schema(description = "排序")
    private Integer sortOrder;
    
    @Schema(description = "状态(0:禁用,1:启用)")
    private Integer status;
}