package com.yuban.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.origin.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// CategoryMapper.java
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    // 自定义分页查询（复杂条件）
    Page<Category> selectByCondition(
            @Param("cat_pid") Long cat_pid,
            @Param("catname") String catname,
            Page<Category> page);

    // 根据 catname 查询 cat_id
    Long selectCatIdByCatName(@Param("catname") String catname)
}