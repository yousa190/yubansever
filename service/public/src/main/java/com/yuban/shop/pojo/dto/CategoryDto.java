package com.yuban.shop.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CategoryDto {


    private Long catId;

    private String catName;

    private Long catPid = 0L;

    private Integer catLevel;

    private Boolean isDel;


}
