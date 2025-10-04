package com.yuban.shop.pojo.entity;


// Category.java

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;



@Data
@TableName("category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

    @TableId(type = IdType.AUTO,value = "cat_id")
    private Long catId;

    @TableField("cat_name")
    private String catName;
    @TableField(value = "cat_pid")
    private Long catPid = 0L;
    @TableField("cat_level")
    private Integer catLevel;
    @TableField("is_del")
    private Boolean isDel;



}