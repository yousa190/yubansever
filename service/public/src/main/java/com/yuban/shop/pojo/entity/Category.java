package com.yuban.shop.pojo.entity;


// Category.java

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;



@Data
@TableName("category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "商品分类实体类")
public class Category {

    @TableId(type = IdType.AUTO,value = "cat_id")
    @Schema(description = "分类ID")
    private Long catId;

    @TableField("cat_name")
    @Schema(description = "分类名称")
    private String catName;
    
    @TableField(value = "cat_pid")
    @Schema(description = "父分类ID")
    private Long catPid = 0L;
    
    @TableField("cat_level")
    @Schema(description = "分类层级")
    private Integer catLevel;
    
    @TableField("is_del")
    @Schema(description = "是否删除：false-未删除，true-已删除")
    private Boolean isDel;



}