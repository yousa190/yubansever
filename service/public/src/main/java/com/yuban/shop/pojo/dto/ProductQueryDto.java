package com.yuban.shop.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品查询参数DTO")
public class ProductQueryDto {
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "分类ID")
    private Long catId;
    
    @Schema(description = "品牌")
    private String brand;
    
    @Schema(description = "商品编号")
    private String productSn;
    
    @Schema(description = "是否上架")
    private Boolean onSale;
    
    @Schema(description = "是否新品")
    private Boolean isNew;
    
    @Schema(description = "是否热销")
    private Boolean isHot;
    
    @Schema(description = "最低价格")
    private BigDecimal minPrice;
    
    @Schema(description = "最高价格")
    private BigDecimal maxPrice;
    
    @Schema(description = "最低库存")
    private Integer minStock;
    
    @Schema(description = "最高库存")
    private Integer maxStock;
    
    @Schema(description = "页码，默认为1")
    private Integer page = 1;
    
    @Schema(description = "每页数量，默认为10")
    private Integer limit = 10;
}