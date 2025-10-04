package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.exception.SystemException;
import com.yuban.shop.mapper.CategoryMapper;
import com.yuban.shop.pojo.dto.CategoryDto;
import com.yuban.shop.pojo.enums.HttpCodeEnum;
import com.yuban.shop.pojo.entity.Category;
import com.yuban.shop.pojo.vo.CategoryVo;
import com.yuban.shop.service.CategoryService;
import com.yuban.shop.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CategoryServiceimpl  extends ServiceImpl<CategoryMapper,Category> implements CategoryService {



    public Map<String, Object> getCategories(Long catPid, String catName, int page, int limit) {
        Page<Category> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(catPid!=null,Category::getCatPid,catPid);
        queryWrapper.eq(!catName.isBlank(),Category::getCatName,catName);
        Page<Category> pageData = baseMapper.selectPage( pageParam,queryWrapper);
        Map<String, Object> response = new HashMap<>();
        List<CategoryVo> res = BeanCopyUtils.copyBeanList(pageData.getRecords(), CategoryVo.class);
        response.put("list",res );
        response.put("count", pageData.getTotal());
        response.put("totalPage", pageData.getPages());
        response.put("currentPage", page);
        return response;
    }

    @Transactional
    public void deleteCategory(Long catId) {
        // 1. 检查分类是否存在
        Category category = baseMapper.selectById(catId);

        if (category == null) {
            throw new SystemException(HttpCodeEnum.CATEGORY_NOT_EXIST);
        }

        // 2. 递归删除所有子分类
        deleteChildrenRecursively(catId);

        // 3. 删除当前分类
        category.setIsDel(true);
        baseMapper.updateById(category);

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
        List<Category> children = baseMapper.selectList(wrapper);

        for (Category child : children) {
            // 递归删除子分类的子分类
            deleteChildrenRecursively(child.getCatId());
            // 删除当前子分类
            child.setIsDel(true);
            baseMapper.updateById(child);
        }
    }

    @Override
    @Transactional
    public Category addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto,category);
        if (category.getCatPid() == 0) {
            category.setCatLevel(1);
        } else {
            Category parent = baseMapper.selectById(category.getCatPid());
            category.setCatLevel(parent.getCatLevel() + 1);
        }
        category.setIsDel(false);
        baseMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public boolean updateCategory(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto,category);
        // 1. 检查分类是否存在
        Category existingCategory = baseMapper.selectById(category.getCatId());
        if (existingCategory == null) {
            throw new SystemException(HttpCodeEnum.CATEGORY_NOT_EXIST);
        }

        // 2. 检查名称是否重复（同一父分类下不能重复）
        QueryWrapper<Category> nameCheckWrapper = new QueryWrapper<>();
        nameCheckWrapper.eq("cat_pid", category.getCatPid())
                        .eq("cat_name", category.getCatName())
                        .ne("cat_id", category.getCatId()) // 排除自身
                        .eq("is_del", false); // 排除已删除的分类
        if (baseMapper.selectCount(nameCheckWrapper) > 0) {
            throw new SystemException(HttpCodeEnum.SAME_PARENT_CATEGORY_DUPLICATE_NAME);
        }

        // 3. 检查新父分类是否有效
        if (category.getCatPid()!=0 && !category.getCatPid().equals(existingCategory.getCatPid())) {
            Category newParent = baseMapper.selectById(category.getCatPid());
            if (newParent == null) {
                throw new SystemException(HttpCodeEnum.PARENT_CATEGORY_NOT_EXIST);
            }
            if (newParent.getCatLevel() >= 3) { // 假设最大层级为3
                throw new SystemException(HttpCodeEnum.PARENT_CATEGORY_LEVEL_EXCEED);
            }
        }

        // 4. 更新分类信息
        Category updatedCategory = category;
        updatedCategory.setCatId(category.getCatId());
        updatedCategory.setCatName(category.getCatName());
        updatedCategory.setCatPid(category.getCatPid());
        updatedCategory.setIsDel(existingCategory.getIsDel());
        updatedCategory.setCatLevel(calculateNewLevel(category.getCatPid()));
        baseMapper.updateById(updatedCategory);

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
        Category parent = baseMapper.selectById(newParentId);
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
        List<Category> children = baseMapper.selectList(wrapper);

        for (Category child : children) {
            child.setCatLevel(newLevel);
            baseMapper.updateById(child);
            updateChildrenLevel(child.getCatId(), newLevel + 1); // 递归更新
        }
    }
}



