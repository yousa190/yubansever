package com.yuban.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuban.shop.pojo.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}