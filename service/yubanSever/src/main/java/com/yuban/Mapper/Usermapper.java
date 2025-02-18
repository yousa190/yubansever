package com.yuban.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuban.pojo.UserData;
import org.apache.ibatis.annotations.*;
import org.glassfish.pfl.basic.fsm.Guard;

@Mapper
public interface Usermapper  extends BaseMapper<UserData> {

}
