package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.dto.CategoryDto;
import com.yuban.shop.pojo.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Transactional
public interface CategoryService  extends IService<Category>  {
    public Map<String, Object> getCategories(Long catPid, String catName, int page, int limit);

    public void deleteCategory(Long catId);

    public Category addCategory(CategoryDto categoryDto);

    /**
     * 编辑分类
     * @param categoryDto 编辑参数
     */
    public boolean updateCategory(CategoryDto categoryDto);

}

