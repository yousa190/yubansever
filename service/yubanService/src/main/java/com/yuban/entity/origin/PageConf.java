package com.yuban.entity.origin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageConf {
    private Integer pageNo;
    private Integer pageSize;
}
