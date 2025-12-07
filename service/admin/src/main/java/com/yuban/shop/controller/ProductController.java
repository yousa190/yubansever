package com.yuban.shop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.dto.ProductQueryDto;
import com.yuban.shop.pojo.entity.Product;
import com.yuban.shop.pojo.entity.ProductImage;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.vo.ProductVo;
import com.yuban.shop.service.ProductService;
import com.yuban.shop.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
@Tag(name = "商品管理接口", description = "商品管理相关接口")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductImageService productImageService;

    @Operation(summary = "获取商品列表", description = "分页获取商品列表，支持按商品名称、分类ID、品牌搜索")
    @GetMapping("/list")
    public Result getProducts(
            @Parameter(description = "商品名称，支持模糊搜索") @RequestParam(required = false) String productName,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long catId,
            @Parameter(description = "品牌") @RequestParam(required = false) String brand,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = productService.getProductList(productName, catId, brand, page, limit);
        return Result.success(response);
    }

    @Operation(summary = "添加商品", description = "添加新商品")
    @PostMapping("/add")
    public Result addProduct(@RequestBody Product product) {
        boolean saved = productService.saveProduct(product);
        if (saved) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    @Operation(summary = "编辑商品", description = "编辑商品信息")
    @PostMapping("/edit")
    public Result editProduct(@RequestBody Product product) {
        boolean updated = productService.saveProduct(product);
        if (updated) {
            return Result.success("编辑成功");
        } else {
            return Result.error("编辑失败");
        }
    }

    @Operation(summary = "删除商品", description = "根据商品ID删除商品")
    @GetMapping("/del")
    public Result deleteProduct(@Parameter(description = "商品ID") @RequestParam Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setIsDel(true);
        boolean updated = productService.updateById(product);
        if (updated) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    @Operation(summary = "获取商品详情", description = "根据商品ID获取商品详细信息")
    @GetMapping("/detail")
    public Result getProductDetail(@Parameter(description = "商品ID") @RequestParam Long productId) {
        ProductVo productVo = productService.getProductDetail(productId);
        if (productVo != null) {
            return Result.success(productVo);
        } else {
            return Result.error("商品不存在");
        }
    }

    @Operation(summary = "商品多条件查询", description = "支持多种条件组合查询商品")
    @PostMapping("/query")
    public Result queryProducts(@RequestBody ProductQueryDto queryDto) {
        Map<String, Object> response = productService.queryProducts(queryDto);
        return Result.success(response);
    }
    
    @Operation(summary = "更新商品图片", description = "更新指定商品的图片列表")
    @PostMapping("/updateImages")
    public Result updateProductImages(
            @Parameter(description = "商品ID") @RequestParam Long productId,
            @Parameter(description = "图片列表") @RequestBody List<ProductImage> images) {
        try {
            boolean result = productImageService.updateProductImages(productId, images);
            if (result) {
                return Result.success("图片更新成功");
            } else {
                return Result.error("图片更新失败");
            }
        } catch (Exception e) {
            return Result.error("图片更新异常：" + e.getMessage());
        }
    }
}