package com.yuban.shop.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.origin.Category;
import com.yuban.shop.pojo.origin.Result;
import com.yuban.shop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mall")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/catelist")
    public Result getCategories(
            @RequestParam(required = false,defaultValue = "") String catName,
            @RequestParam(required = false) Long catPid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = categoryService.getCategories(catPid, catName, page, limit);

        return Result.success(response);
    }

    @GetMapping("/delcat")
    public Result deleteCategory(@RequestParam Long catId) {
        categoryService.deleteCategory(catId);
        return Result.success("success!");
    }

    @PostMapping("/addcat")
    public Result addCategory( @RequestBody  Category category) {
        log.info(category.toString());
        Category saved = categoryService.addCategory(category);
        return Result.success("success!");
    }

    @PostMapping("/updcat")
    public Result updCategory( @RequestBody   Category category) {
        log.info(category.toString());
        categoryService.updateCategory(category);
        return Result.success("success!");
    }
}