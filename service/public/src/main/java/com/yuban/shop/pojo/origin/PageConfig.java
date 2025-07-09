package com.yuban.shop.pojo.origin;

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
