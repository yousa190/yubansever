package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.mapper.ProductSpecMapper;
import com.yuban.shop.pojo.entity.ProductSpec;
import com.yuban.shop.service.ProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec> implements ProductSpecService {
    
    @Autowired
    private ProductSpecMapper productSpecMapper;
}