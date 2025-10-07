package com.yuban.shop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.entity.Product;
import com.yuban.shop.pojo.entity.ProductImage;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.service.ProductImageService;
import com.yuban.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/product")
@Tag(name = "商品管理接口", description = "商品的增删改查相关接口")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductImageService productImageService;

    @Operation(summary = "添加商品", description = "添加商品信息，不包括图片")
    @PostMapping("/add")
    public Result addProduct(
            @Parameter(description = "商品信息", required = true) @RequestBody Product product) {
        log.info("添加商品：{}", product);
        try {
            Product savedProduct = productService.addProduct(product);
            return Result.success(savedProduct);
        } catch (Exception e) {
            log.error("添加商品失败", e);
            return Result.error("添加商品失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "添加商品图片", description = "为指定商品添加图片")
    @PostMapping("/addImages")
    public Result addProductImages(
            @Parameter(description = "商品ID", required = true) @RequestParam Long productId,
            @Parameter(description = "图片列表", required = true) @RequestBody List<ProductImage> images) {
        log.info("为商品添加图片，商品ID：{}，图片数量：{}", productId, images.size());
        try {
            boolean result = productImageService.addProductImages(productId, images);
            if (result) {
                return Result.success("图片添加成功");
            } else {
                return Result.error("图片添加失败");
            }
        } catch (Exception e) {
            log.error("添加商品图片失败", e);
            return Result.error("添加商品图片失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取商品列表", description = "分页获取商品列表，支持按商品名称、分类ID、品牌搜索")
    @GetMapping("/list")
    public Result getProductList(
            @Parameter(description = "商品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long catId,
            @Parameter(description = "品牌") @RequestParam(required = false) String brand,
            @Parameter(description = "页码，默认为1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量，默认为10") @RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Object> result = productService.getProductList(productName, catId, brand, page, limit);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            return Result.error("获取商品列表失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取商品详情", description = "根据商品ID获取商品详情，包括图片信息")
    @GetMapping("/detail")
    public Result getProductDetail(
            @Parameter(description = "商品ID", required = true) @RequestParam Long productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product == null) {
                return Result.error("商品不存在");
            }
            
            return Result.success(product);
        } catch (Exception e) {
            log.error("获取商品详情失败", e);
            return Result.error("获取商品详情失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "更新商品", description = "更新商品信息")
    @PostMapping("/update")
    public Result updateProduct(
            @Parameter(description = "商品信息", required = true) @RequestBody Product product) {
        log.info("更新商品：{}", product);
        try {
            boolean result = productService.updateProduct(product);
            if (result) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败，商品不存在");
            }
        } catch (Exception e) {
            log.error("更新商品失败", e);
            return Result.error("更新商品失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "删除商品", description = "删除指定商品（软删除）")
    @GetMapping("/delete")
    public Result deleteProduct(
            @Parameter(description = "商品ID", required = true) @RequestParam Long productId) {
        try {
            productService.deleteProduct(productId);
            // 同时删除商品图片
            productImageService.deleteImagesByProductId(productId);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除商品失败", e);
            return Result.error("删除商品失败：" + e.getMessage());
        }
    }
}