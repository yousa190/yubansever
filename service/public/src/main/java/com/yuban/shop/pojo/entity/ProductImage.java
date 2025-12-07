package com.yuban.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("product_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "商品图片实体类")
public class ProductImage {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "图片ID")
    private Long imageId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "图片URL")
    private String imageUrl;
    
    @Schema(description = "排序")
    private Integer sortOrder;
    
    @TableField(value = "is_main")
    @Schema(description = "是否主图")
    private Boolean main;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}