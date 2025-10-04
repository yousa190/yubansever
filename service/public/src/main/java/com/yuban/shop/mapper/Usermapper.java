package com.yuban.shop.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuban.shop.pojo.entity.UserData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层接口
 * 继承自MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 */
@Mapper
public interface Usermapper  extends BaseMapper<UserData> {



}