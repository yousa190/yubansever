package com.yuban.shop.pojo.vo;

import com.yuban.shop.pojo.entity.ProductImage;
import com.yuban.shop.pojo.entity.ProductSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "商品信息VO")
public class ProductVo {
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品分类ID")
    private Long catId;
    
    @Schema(description = "分类名称")
    private String catName;
    
    @Schema(description = "品牌名称")
    private String brand;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "市场价")
    private BigDecimal marketPrice;
    
    @Schema(description = "成本价")
    private BigDecimal costPrice;
    
    @Schema(description = "商品库存")
    private Integer stock;
    
    @Schema(description = "销售数量")
    private Integer salesCount;
    
    @Schema(description = "商品编码")
    private String productSn;
    
    @Schema(description = "商品描述")
    private String productDesc;
    
    @Schema(description = "详情内容")
    private String detailContent;
    
    @Schema(description = "是否上架")
    private Boolean onSale;
    
    @Schema(description = "是否新品")
    private Boolean isNew;
    
    @Schema(description = "是否热门")
    private Boolean isHot;
    
    @Schema(description = "商品单位")
    private String unit;
    
    @Schema(description = "商品状态：1-正常，0-停用")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "上架时间")
    private LocalDateTime onSaleTime;
    
    @Schema(description = "商品图片列表")
    private List<ProductImage> images;
    
    @Schema(description = "商品规格参数列表")
    private List<ProductSpec> specs;
}