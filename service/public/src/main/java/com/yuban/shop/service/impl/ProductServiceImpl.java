package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.mapper.ProductMapper;
import com.yuban.shop.pojo.dto.ProductQueryDto;
import com.yuban.shop.pojo.entity.Product;
import com.yuban.shop.pojo.vo.ProductVo;
import com.yuban.shop.service.ProductService;
import com.yuban.shop.service.CategoryService;
import com.yuban.shop.service.ProductImageService;
import com.yuban.shop.service.ProductSpecService;
import com.yuban.shop.pojo.entity.Category;
import com.yuban.shop.pojo.entity.ProductImage;
import com.yuban.shop.pojo.entity.ProductSpec;
import com.yuban.shop.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Autowired
    private ProductSpecService productSpecService;
    
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
            productVo.setImages(productImageService.list(new LambdaQueryWrapper<ProductImage>()
                    .eq(ProductImage::getProductId, product.getProductId())
                    .orderByAsc(ProductImage::getSortOrder)));
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
    public boolean saveProduct(Product product) {
        // 保存商品信息
        boolean saved = this.saveOrUpdate(product);
        
        // 如果商品保存成功且存在图片列表，则保存图片信息
        if (saved && product.getImages() != null && !product.getImages().isEmpty()) {
            // 设置商品ID
            product.getImages().forEach(image -> image.setProductId(product.getProductId()));
            // 保存图片列表
            productImageService.saveOrUpdateBatch(product.getImages());
        }
        
        // 如果商品保存成功且存在规格参数列表，则保存规格参数信息
        if (saved && product.getSpecs() != null && !product.getSpecs().isEmpty()) {
            // 设置商品ID
            product.getSpecs().forEach(spec -> spec.setProductId(product.getProductId()));
            // 保存规格参数列表
            productSpecService.saveOrUpdateBatch(product.getSpecs());
        }
        
        return saved;
    }
    
    @Override
    public ProductVo getProductDetail(Long productId) {
        // 查询商品基本信息
        Product product = this.getById(productId);
        if (product == null || product.getIsDel()) {
            return null;
        }
        
        // 转换为ProductVo
        ProductVo productVo = BeanCopyUtils.copyBean(product, ProductVo.class);
        
        // 查询分类名称
        if (product.getCatId() != null) {
            Category category = categoryService.getById(product.getCatId());
            if (category != null) {
                productVo.setCatName(category.getCatName());
            }
        }
        
        // 查询商品图片列表
        List<ProductImage> images = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, productId)
                .orderByAsc(ProductImage::getSortOrder));
        productVo.setImages(images);
        
        // 查询商品规格参数列表
        List<ProductSpec> specs = productSpecService.list(new LambdaQueryWrapper<ProductSpec>()
                .eq(ProductSpec::getProductId, productId)
                .orderByAsc(ProductSpec::getSpecId));
        productVo.setSpecs(specs);
        
        return productVo;
    }
    
    @Override
    public Map<String, Object> queryProducts(ProductQueryDto queryDto) {
        Page<Product> pageParam = new Page<>(queryDto.getPage(), queryDto.getLimit());
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(Product::getIsDel, false); // 只查询未删除的商品
        
        // 添加各种查询条件
        queryWrapper.like(queryDto.getProductName() != null && !queryDto.getProductName().isEmpty(), 
                Product::getProductName, queryDto.getProductName());
        queryWrapper.eq(queryDto.getCatId() != null, Product::getCatId, queryDto.getCatId());
        queryWrapper.eq(queryDto.getBrand() != null && !queryDto.getBrand().isEmpty(), 
                Product::getBrand, queryDto.getBrand());
        queryWrapper.eq(queryDto.getProductSn() != null && !queryDto.getProductSn().isEmpty(), 
                Product::getProductSn, queryDto.getProductSn());
        queryWrapper.eq(queryDto.getOnSale() != null, Product::getOnSale, queryDto.getOnSale());
        queryWrapper.eq(queryDto.getIsNew() != null, Product::getIsNew, queryDto.getIsNew());
        queryWrapper.eq(queryDto.getIsHot() != null, Product::getIsHot, queryDto.getIsHot());
        
        // 价格区间查询
        if (queryDto.getMinPrice() != null) {
            queryWrapper.ge(Product::getPrice, queryDto.getMinPrice());
        }
        if (queryDto.getMaxPrice() != null) {
            queryWrapper.le(Product::getPrice, queryDto.getMaxPrice());
        }
        
        // 库存区间查询
        if (queryDto.getMinStock() != null) {
            queryWrapper.ge(Product::getStock, queryDto.getMinStock());
        }
        if (queryDto.getMaxStock() != null) {
            queryWrapper.le(Product::getStock, queryDto.getMaxStock());
        }
        
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
            productVo.setImages(productImageService.list(new LambdaQueryWrapper<ProductImage>()
                    .eq(ProductImage::getProductId, product.getProductId())
                    .orderByAsc(ProductImage::getSortOrder)));
            return productVo;
        }).collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("list", productVos);
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", queryDto.getPage());
        return response;
    }
}