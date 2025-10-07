package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.mapper.ProductMapper;
import com.yuban.shop.pojo.entity.Product;
import com.yuban.shop.pojo.vo.ProductVo;
import com.yuban.shop.service.ProductService;
import com.yuban.shop.service.CategoryService;
import com.yuban.shop.service.ProductImageService;
import com.yuban.shop.pojo.entity.Category;
import com.yuban.shop.pojo.entity.ProductImage;
import com.yuban.shop.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductImageService productImageService;
    
    @Override
    public Map<String, Object> getProductList(String productName, Long catId, String brand, int page, int limit) {
        Page<Product> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(productName != null && !productName.isEmpty(), Product::getProductName, productName);
        queryWrapper.eq(catId != null, Product::getCatId, catId);
        queryWrapper.eq(brand != null && !brand.isEmpty(), Product::getBrand, brand);
        queryWrapper.eq(Product::getIsDel, false); // 只查询未删除的商品
        queryWrapper.orderByDesc(Product::getCreatedAt); // 按创建时间倒序
        
        Page<Product> pageData = productMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为ProductVo并添加分类名称和图片列表
        List<ProductVo> productVos = pageData.getRecords().stream().map(product -> {
            ProductVo productVo = BeanCopyUtils.copyBean(product, ProductVo.class);
            // 查询分类名称
            if (product.getCatId() != null) {
                Category category = categoryService.getById(product.getCatId());
                if (category != null) {
                    productVo.setCatName(category.getCatName());
                }
            }
            // 查询商品图片列表
            List<ProductImage> images = productImageService.getImagesByProductId(product.getProductId());
            productVo.setImages(BeanCopyUtils.copyBeanList(images, com.yuban.shop.pojo.vo.ProductImageVo.class));
            return productVo;
        }).collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("list", productVos);
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", page);
        
        return response;
    }
    
    @Override
    @Transactional
    public Product addProduct(Product product) {
        // 设置默认值
        product.setIsDel(false);
        product.setSalesCount(product.getSalesCount() == null ? 0 : product.getSalesCount());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        // 如果没有设置状态，默认为1（正常）
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        
        // 如果没有设置单位，默认为"件"
        if (product.getUnit() == null || product.getUnit().isEmpty()) {
            product.setUnit("件");
        }
        
        // 插入数据库
        productMapper.insert(product);
        return product;
    }
    
    @Override
    @Transactional
    public boolean updateProduct(Product product) {
        Product existingProduct = productMapper.selectById(product.getProductId());
        if (existingProduct == null) {
            return false;
        }
        
        // 保留一些关键字段不被更新
        product.setIsDel(existingProduct.getIsDel());
        product.setCreatedAt(existingProduct.getCreatedAt());
        product.setUpdatedAt(LocalDateTime.now());
        
        return productMapper.updateById(product) > 0;
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setIsDel(true); // 软删除
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.updateById(product);
    }
    
    @Override
    public Product getProductById(Long productId) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getProductId, productId);
        queryWrapper.eq(Product::getIsDel, false);
        Product product = productMapper.selectOne(queryWrapper);
        
        // 获取商品图片
        if (product != null) {
            List<ProductImage> images = productImageService.getImagesByProductId(productId);
            product.setImages(images);
        }
        
        return product;
    }
}