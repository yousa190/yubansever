package com.yuban.shop.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageConfig {
    private Integer pageNo;
    private Integer pageSize;
}
