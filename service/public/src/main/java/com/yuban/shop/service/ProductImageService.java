package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.entity.ProductImage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProductImageService extends IService<ProductImage> {
    
    /**
     * 为商品添加图片
     * @param productId 商品ID
     * @param images 图片列表
     * @return 是否添加成功
     */
    boolean addProductImages(Long productId, List<ProductImage> images);
    
    /**
     * 根据商品ID获取所有图片
     * @param productId 商品ID
     * @return 图片列表
     */
    List<ProductImage> getImagesByProductId(Long productId);
    
    /**
     * 删除商品的所有图片
     * @param productId 商品ID
     * @return 是否删除成功
     */
    boolean deleteImagesByProductId(Long productId);
    
    /**
     * 设置商品的主图
     * @param imageId 图片ID
     * @return 是否设置成功
     */
    boolean setMainImage(Long imageId);
    
    /**
     * 更新商品图片（比对后更新）
     * @param productId 商品ID
     * @param newImages 新的图片列表
     * @return 是否更新成功
     */
    boolean updateProductImages(Long productId, List<ProductImage> newImages);
}