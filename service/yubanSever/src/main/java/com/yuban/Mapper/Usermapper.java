package com.yuban.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuban.pojo.UserData;
import com.yuban.service.UserService;
import org.apache.ibatis.annotations.*;
import org.glassfish.pfl.basic.fsm.Guard;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface Usermapper  extends BaseMapper<UserData> {



}
