package com.yuban.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// CategoryMapper.java
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    // 自定义分页查询（复杂条件）

}