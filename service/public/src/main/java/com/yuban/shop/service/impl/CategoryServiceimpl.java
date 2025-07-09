package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.mapper.CategoryMapper;
import com.yuban.shop.pojo.origin.Category;
import com.yuban.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceimpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Page<Category> getCategories(Long catPid, String catName, int page, int limit) {
        Page<Category> pageParam = new Page<>(page, limit);
        return categoryMapper.selectByCondition(catPid, catName, pageParam);
    }

    public void deleteCategory(Long catId) {
        // 1. 检查分类是否存在
        Category category = categoryMapper.selectById(catId);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        // 2. 递归删除所有子分类
        deleteChildrenRecursively(catId);

        // 3. 删除当前分类
        categoryMapper.deleteById(catId);

        // 4. 删除关联的规格组
//        QueryWrapper<SpecGroup> groupWrapper = new QueryWrapper<>();
//        groupWrapper.eq("cat_id", catId);
//        specGroupMapper.delete(groupWrapper);
    }

    /**
     * 递归删除子分类
     * @param parentId 父分类ID
     */
    private void deleteChildrenRecursively(Long parentId) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("cat_pid", parentId);
        List<Category> children = categoryMapper.selectList(wrapper);

        for (Category child : children) {
            // 递归删除子分类的子分类
            deleteChildrenRecursively(child.getCatId());
            // 删除当前子分类
            categoryMapper.deleteById(child.getCatId());
        }
    }
    @Override
    public Category addCategory(Category category) {
        // 层级计算逻辑同之前
        if (category.getCatPid() == 0) {
            category.setCatLevel(1);
        } else {
            Category parent = categoryMapper.selectById(category.getCatPid());
            category.setCatLevel(parent.getCatLevel() + 1);
        }
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public boolean updateCategory(Category categoryDTO) {
        // 1. 检查分类是否存在
        Category existingCategory = categoryMapper.selectById(categoryDTO.getCatId());
        if (existingCategory == null) {
            throw new RuntimeException("分类不存在");
        }

        // 2. 检查名称是否重复（同一父分类下不能重复）
        QueryWrapper<Category> nameCheckWrapper = new QueryWrapper<>();
        nameCheckWrapper.eq("cat_pid", categoryDTO.getCatPid())
                .eq("cat_name", categoryDTO.getCatName())
                .ne("cat_id", categoryDTO.getCatId()); // 排除自身
        if (categoryMapper.selectCount(nameCheckWrapper) > 0) {
            throw new RuntimeException("同一父分类下名称不能重复");
        }

        // 3. 检查新父分类是否有效
        if (categoryDTO.getCatPid()!=0&&!categoryDTO.getCatPid().equals(existingCategory.getCatPid())) {
            Category newParent = categoryMapper.selectById(categoryDTO.getCatPid());
            if (newParent == null) {
                throw new RuntimeException("父分类不存在");
            }
            if (newParent.getCatLevel() >= 3) { // 假设最大层级为3
                throw new RuntimeException("父分类层级已达上限");
            }
        }

        // 4. 更新分类信息
        Category updatedCategory = new Category();
        updatedCategory.setCatId(categoryDTO.getCatId());
        updatedCategory.setCatName(categoryDTO.getCatName());
        updatedCategory.setCatPid(categoryDTO.getCatPid());
        updatedCategory.setCatLevel(calculateNewLevel(categoryDTO.getCatPid()));
        categoryMapper.updateById(updatedCategory);

        // 5. 递归更新子分类层级
        updateChildrenLevel(updatedCategory.getCatId(), updatedCategory.getCatLevel() + 1);
        return true;
    }

    /**
     * 计算新层级（父分类层级 + 1）
     */
    private Integer calculateNewLevel(Long newParentId) {
        if (newParentId == 0) {
            return 1; // 根分类
        }
        Category parent = categoryMapper.selectById(newParentId);
        return parent.getCatLevel() + 1;
    }

    /**
     * 递归更新子分类层级
     * @param parentId 父分类ID
     * @param newLevel 新的层级
     */
    private void updateChildrenLevel(Long parentId, Integer newLevel) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("cat_pid", parentId);
        List<Category> children = categoryMapper.selectList(wrapper);

        for (Category child : children) {
            child.setCatLevel(newLevel);
            categoryMapper.updateById(child);
            updateChildrenLevel(child.getCatId(), newLevel + 1); // 递归更新
        }
    }
}



