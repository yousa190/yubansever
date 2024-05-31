package com.yuban.service.impl;

import com.yuban.Mapper.Usermapper;
import com.yuban.pojo.UserData;
import com.yuban.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private Usermapper usermapper;



    @Override
    public Integer count_name(String user_name) {
        return usermapper.count_name(user_name);
    }

    @Override
    public Integer count_email(String userEmail) {
        return usermapper.count_email(userEmail);
    }

    @Override
    public boolean sign(String userName, String emaill, String pwd) {
        return usermapper.sign(userName,emaill,pwd);
    }

    @Override
    public boolean check_name(String name) {
        return usermapper.check_name(name);
    }


    @Override
    public boolean check_email(String name) {
        return usermapper.check_email(name);
    }

    @Override
    public UserData getByName(String name,String pwd) {
        return usermapper.get_msgByname(name,pwd);
    }

    @Override
    public UserData getByemail(String email,String pwd) {
        return usermapper.get_msgByemail(email,pwd);
    }
}
