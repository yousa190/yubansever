package com.yuban.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @TableId(type = IdType.AUTO)
    private Long productId;
    
    private String productName;
    
    private Long catId;
    
    private String brand;
    
    private BigDecimal price;
    
    private BigDecimal marketPrice;
    
    private BigDecimal costPrice;
    
    private Integer stock;
    
    private Integer salesCount;
    
    private String productSn;
    
    private String productDesc;
    
    private String detailContent;
    
    @TableField(value = "is_on_sale")
    private Boolean onSale;
    
    @TableField(value = "is_new")
    private Boolean isNew;
    
    @TableField(value = "is_hot")
    private Boolean isHot;
    
    @TableField(value = "is_del")
    private Boolean isDel;
    
    private String unit;
    
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime onSaleTime;
    
    @TableField(exist = false)
    private List<ProductImage> images;
}