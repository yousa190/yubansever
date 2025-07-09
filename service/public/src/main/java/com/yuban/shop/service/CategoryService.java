package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.origin.Category;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CategoryService {
    public Page<Category> getCategories(Long catPid, String catName, int page, int limit);

    public void deleteCategory(Long catId);

    public Category addCategory(Category category);

    /**
     * 编辑分类
     * @param Category 编辑参数
     */
    public boolean updateCategory(Category Category);

}

