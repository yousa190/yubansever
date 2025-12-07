package com.yuban.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("product_spec")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "商品规格参数实体类")
public class ProductSpec {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "规格参数ID")
    private Long specId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "规格名称")
    private String specName;
    
    @Schema(description = "规格值")
    private String specValue;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}