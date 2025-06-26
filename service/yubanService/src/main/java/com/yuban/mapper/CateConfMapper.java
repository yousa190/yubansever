package com.yuban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.entity.origin.SpecGroup.SpecGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// SpecGroupMapper.java
@Mapper
public interface CateConfMapper extends BaseMapper<SpecGroup> {


    Page<SpecGroup> selectByCondition(
            @Param("catName") String catName,
            Page<SpecGroup> page);

}

