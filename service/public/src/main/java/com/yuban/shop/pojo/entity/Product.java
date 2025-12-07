package com.yuban.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "商品信息实体类")
public class Product {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品分类ID")
    private Long catId;
    
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
    
    @TableField(value = "is_on_sale")
    @Schema(description = "是否上架：false-下架，true-上架")
    private Boolean onSale;
    
    @TableField(value = "is_new")
    @Schema(description = "是否新品")
    private Boolean isNew;
    
    @TableField(value = "is_hot")
    @Schema(description = "是否热门")
    private Boolean isHot;
    
    @TableField(value = "is_del")
    @Schema(description = "是否删除：false-未删除，true-已删除")
    private Boolean isDel;
    
    @Schema(description = "商品单位")
    private String unit;
    
    @Schema(description = "商品状态：1-正常，0-停用")
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    @Schema(description = "上架时间")
    private LocalDateTime onSaleTime;
    
    @TableField(exist = false)
    @Schema(description = "商品图片列表")
    private List<ProductImage> images;
    
    // 添加商品规格参数列表
    @TableField(exist = false)
    @Schema(description = "商品规格参数列表")
    private List<ProductSpec> specs;
}