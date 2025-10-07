package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.mapper.ProductImageMapper;
import com.yuban.shop.pojo.entity.ProductImage;
import com.yuban.shop.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {
    
    @Autowired
    private ProductImageMapper productImageMapper;
    
    @Override
    @Transactional
    public boolean addProductImages(Long productId, List<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return false;
        }
        
        for (ProductImage image : images) {
            image.setProductId(productId);
            image.setCreatedAt(LocalDateTime.now());
            productImageMapper.insert(image);
        }
        
        return true;
    }
    
    @Override
    public List<ProductImage> getImagesByProductId(Long productId) {
        LambdaQueryWrapper<ProductImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductImage::getProductId, productId);
        queryWrapper.orderByAsc(ProductImage::getSortOrder);
        return productImageMapper.selectList(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean deleteImagesByProductId(Long productId) {
        LambdaQueryWrapper<ProductImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductImage::getProductId, productId);
        return productImageMapper.delete(queryWrapper) > 0;
    }
    
    @Override
    @Transactional
    public boolean setMainImage(Long imageId) {
        // 先将该商品下的所有图片设为非主图
        ProductImage image = productImageMapper.selectById(imageId);
        if (image == null) {
            return false;
        }
        
        // 将同商品下的所有图片设为非主图
        LambdaQueryWrapper<ProductImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductImage::getProductId, image.getProductId());
        List<ProductImage> images = productImageMapper.selectList(queryWrapper);
        for (ProductImage img : images) {
            img.setMain(false);
            productImageMapper.updateById(img);
        }
        
        // 将指定图片设为主图
        image.setMain(true);
        return productImageMapper.updateById(image) > 0;
    }
}