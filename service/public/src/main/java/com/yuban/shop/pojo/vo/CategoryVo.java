package com.yuban.shop.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    private Long catId;
    private String catName;
    private Long catPid = 0L;
    private Integer catLevel;
}
