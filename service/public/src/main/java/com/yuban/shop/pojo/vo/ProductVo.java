package com.yuban.shop.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVo {
    
    private Long productId;
    
    private String productName;
    
    private Long catId;
    
    private String catName;
    
    private String brand;
    
    private BigDecimal price;
    
    private BigDecimal marketPrice;
    
    private BigDecimal costPrice;
    
    private Integer stock;
    
    private Integer salesCount;
    
    private String productSn;
    
    private String productDesc;
    
    private String detailContent;
    
    private Boolean onSale;
    
    private Boolean isNew;
    
    private Boolean isHot;
    
    private Boolean isDel;
    
    private String unit;
    
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime onSaleTime;
    
    private List<ProductImageVo> images;
}