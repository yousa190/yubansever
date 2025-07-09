package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.exception.SystemException;
import com.yuban.shop.mapper.CategoryMapper;
import com.yuban.shop.pojo.enums.HttpCodeEnum;
import com.yuban.shop.pojo.origin.Category;
import com.yuban.shop.pojo.vo.CategoryVo;
import com.yuban.shop.service.CategoryService;
import com.yuban.shop.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CategoryServiceimpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Map<String, Object> getCategories(Long catPid, String catName, int page, int limit) {
        Page<Category> pageParam = new Page<>(page, limit);
        Page<Category> pageData = categoryMapper.selectByCondition(catPid, catName, pageParam);
        Map<String, Object> response = new HashMap<>();
        List<CategoryVo> res = BeanCopyUtils.copyBeanList(pageData.getRecords(), CategoryVo.class);
        response.put("list",res );
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", page);
        return response;
    }

    public void deleteCategory(Long catId) {
        // 1. 检查分类是否存在
        Category category = categoryMapper.selectById(catId);

        if (category == null) {
            throw new SystemException(HttpCodeEnum.CATEGORY_NOT_EXIST);
        }

        // 2. 递归删除所有子分类
        deleteChildrenRecursively(catId);

        // 3. 删除当前分类
        category.setIsDel(true);
        categoryMapper.updateById(category);

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
            child.setIsDel(true);
            categoryMapper.updateById(child);
        }
    }

    @Override
    public Category addCategory(Category category) {

        if (category.getCatPid() == 0) {
            category.setCatLevel(1);
        } else {
            Category parent = categoryMapper.selectById(category.getCatPid());
            category.setCatLevel(parent.getCatLevel() + 1);
        }
        category.setIsDel(false);
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public boolean updateCategory(Category categoryDTO) {
        // 1. 检查分类是否存在
        Category existingCategory = categoryMapper.selectById(categoryDTO.getCatId());
        if (existingCategory == null) {
            throw new SystemException(HttpCodeEnum.CATEGORY_NOT_EXIST);
        }

        // 2. 检查名称是否重复（同一父分类下不能重复）
        QueryWrapper<Category> nameCheckWrapper = new QueryWrapper<>();
        nameCheckWrapper.eq("cat_pid", categoryDTO.getCatPid())
                        .eq("cat_name", categoryDTO.getCatName())
                        .ne("cat_id", categoryDTO.getCatId()) // 排除自身
                        .eq("is_del", false); // 排除已删除的分类
        if (categoryMapper.selectCount(nameCheckWrapper) > 0) {
            throw new SystemException(HttpCodeEnum.SAME_PARENT_CATEGORY_DUPLICATE_NAME);
        }

        // 3. 检查新父分类是否有效
        if (categoryDTO.getCatPid()!=0 && !categoryDTO.getCatPid().equals(existingCategory.getCatPid())) {
            Category newParent = categoryMapper.selectById(categoryDTO.getCatPid());
            if (newParent == null) {
                throw new SystemException(HttpCodeEnum.PARENT_CATEGORY_NOT_EXIST);
            }
            if (newParent.getCatLevel() >= 3) { // 假设最大层级为3
                throw new SystemException(HttpCodeEnum.PARENT_CATEGORY_LEVEL_EXCEED);
            }
        }

        // 4. 更新分类信息
        Category updatedCategory = new Category();
        updatedCategory.setCatId(categoryDTO.getCatId());
        updatedCategory.setCatName(categoryDTO.getCatName());
        updatedCategory.setCatPid(categoryDTO.getCatPid());
        updatedCategory.setIsDel(existingCategory.getIsDel());
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



