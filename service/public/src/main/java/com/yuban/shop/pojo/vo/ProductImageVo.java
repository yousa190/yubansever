package com.yuban.shop.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductImageVo {
    
    private Long imageId;
    
    private Long productId;
    
    private String imageUrl;
    
    private Integer sortOrder;
    
    private Boolean main;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime createdAt;
}