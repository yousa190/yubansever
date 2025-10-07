package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
public interface ProductService extends IService<Product> {
    
    /**
     * 获取商品列表
     * @param productName 商品名称（模糊搜索）
     * @param catId 分类ID
     * @param brand 品牌
     * @param page 页码
     * @param limit 每页数量
     * @return 商品列表
     */
    Map<String, Object> getProductList(String productName, Long catId, String brand, int page, int limit);
    
    /**
     * 添加商品
     * @param product 商品信息
     * @return 添加的商品
     */
    Product addProduct(Product product);
    
    /**
     * 更新商品
     * @param product 商品信息
     * @return 是否更新成功
     */
    boolean updateProduct(Product product);
    
    /**
     * 删除商品（软删除）
     * @param productId 商品ID
     */
    void deleteProduct(Long productId);
    
    /**
     * 根据ID获取商品详情
     * @param productId 商品ID
     * @return 商品详情
     */
    Product getProductById(Long productId);
}