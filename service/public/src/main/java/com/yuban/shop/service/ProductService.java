package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.dto.ProductQueryDto;
import com.yuban.shop.pojo.entity.Product;
import com.yuban.shop.pojo.vo.ProductVo;

import java.util.Map;

public interface ProductService extends IService<Product> {
    
    /**
     * 获取商品列表（基础查询）
     * @param productName 商品名称
     * @param catId 分类ID
     * @param brand 品牌
     * @param page 页码
     * @param limit 每页数量
     * @return 商品列表
     */
    Map<String, Object> getProductList(String productName, Long catId, String brand, int page, int limit);
    
    /**
     * 保存商品（新增或更新）
     * @param product 商品信息
     * @return 是否保存成功
     */
    boolean saveProduct(Product product);
    
    /**
     * 根据ID获取商品详情
     * @param productId 商品ID
     * @return 商品详情
     */
    ProductVo getProductDetail(Long productId);
    
    /**
     * 商品多条件查询
     * @param queryDto 查询参数
     * @return 商品列表
     */
    Map<String, Object> queryProducts(ProductQueryDto queryDto);
}