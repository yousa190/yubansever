package com.yuban.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.shop.pojo.origin.categoryConfig.SpecGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// SpecGroupMapper.java
@Mapper
public interface CategoryConfigMapper extends BaseMapper<SpecGroup> {


    Page<SpecGroup> selectByCondition(
            @Param("catName") String catName,
            Page<SpecGroup> page);

}

