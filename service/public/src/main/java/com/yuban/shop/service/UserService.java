package com.yuban.shop.service;

import com.yuban.shop.pojo.origin.Result;
import com.yuban.shop.pojo.origin.UserData;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService  {


    public Result selectList();

    public boolean namecheck(String username);

    public boolean mailcheck(String email);
    public Result regist(UserData userData );

}
