package com.yuban.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("product_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    
    @TableId(type = IdType.AUTO)
    private Long imageId;
    
    private Long productId;
    
    private String imageUrl;
    
    private Integer sortOrder;
    
    @TableField(value = "is_main")
    private Boolean main;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime createdAt;
}