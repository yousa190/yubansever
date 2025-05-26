package com.yuban.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuban.pojo.SpecGroup.SpecGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// SpecGroupMapper.java
@Mapper
public interface CateConfMapper extends BaseMapper<SpecGroup> {


    Page<SpecGroup> selectByCondition(
            @Param("catName") String catName,
            Page<SpecGroup> page);

}

