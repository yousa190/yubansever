package com.yuban.shop.controller;
import com.yuban.shop.pojo.dto.CategoryDto;
import com.yuban.shop.pojo.entity.Category;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mall")
@Tag(name = "商品分类管理接口", description = "商品分类的增删改查相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取分类列表", description = "分页获取商品分类列表，支持按分类名称和父级分类筛选")
    @GetMapping("/catelist")
    public Result getCategories(
            @Parameter(description = "分类名称，支持模糊搜索") @RequestParam(required = false,defaultValue = "") String catName,
            @Parameter(description = "父级分类ID") @RequestParam(required = false) Long catPid,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = categoryService.getCategories(catPid, catName, page, limit);

        return Result.success(response);
    }

    @Operation(summary = "删除分类", description = "根据分类ID删除商品分类")
    @GetMapping("/delcat")
    public Result deleteCategory(
            @Parameter(description = "要删除的分类ID", required = true) @RequestParam Long catId) {
        categoryService.deleteCategory(catId);
        return Result.success("删除成功");
    }

    @Operation(summary = "添加分类", description = "新增商品分类")
    @PostMapping("/addcat")
    public Result addCategory(
            @Parameter(description = "分类信息", required = true) @RequestBody CategoryDto category) {
        log.info(category.toString());
        Category saved = categoryService.addCategory(category);
        return Result.success("添加成功");
    }

    @Operation(summary = "更新分类", description = "更新商品分类信息")
    @PostMapping("/updcat")
    public Result updCategory(
            @Parameter(description = "分类信息", required = true) @RequestBody CategoryDto category) {
        log.info(category.toString());
        categoryService.updateCategory(category);
        return Result.success("更新成功");
    }
}