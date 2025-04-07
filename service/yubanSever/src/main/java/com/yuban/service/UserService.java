package com.yuban.service;

import com.yuban.pojo.Result;
import com.yuban.pojo.UserData;
import org.apache.catalina.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService  {


    public Result selectList();

    public boolean namecheck(String username);

    public boolean mailcheck(String email);
    public Result regist(UserData userData );

}
