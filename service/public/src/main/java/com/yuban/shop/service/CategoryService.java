package com.yuban.shop.service;

import com.yuban.shop.pojo.origin.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Transactional
public interface CategoryService {
    public Map<String, Object> getCategories(Long catPid, String catName, int page, int limit);

    public void deleteCategory(Long catId);

    public Category addCategory(Category category);

    /**
     * 编辑分类
     * @param Category 编辑参数
     */
    public boolean updateCategory(Category Category);

}

