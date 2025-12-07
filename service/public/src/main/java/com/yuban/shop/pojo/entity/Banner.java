package com.yuban.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("banner")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "轮播图实体类")
public class Banner {
    
    @TableId(type = IdType.AUTO)
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
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @TableLogic
    @Schema(description = "删除标志(0:未删除,1:已删除)")
    private Integer isDel;
}