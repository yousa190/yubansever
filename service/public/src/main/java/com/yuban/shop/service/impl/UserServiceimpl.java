package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuban.shop.mapper.Usermapper;
import com.yuban.shop.utils.PasswordUtil;
import com.yuban.shop.pojo.origin.Result;
import com.yuban.shop.pojo.origin.UserData;
import com.yuban.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private Usermapper usermapper;


    @Override
    public Result selectList() {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getEmail,"19959771806@163.com").or().eq(UserData::getUsername,"yuban");
        List<UserData> res =usermapper.selectList(lambdaQuery);
        return Result.success(res);
    }

    public boolean namecheck(String username) {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getUsername,username);
        List<UserData> res =usermapper.selectList(lambdaQuery);
        return res.isEmpty();
    }

    public boolean mailcheck(String email) {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getEmail,email);
        List<UserData> res =usermapper.selectList(lambdaQuery);
        return res.isEmpty();
    }

    @Override
    public Result regist(UserData userData) {
        if (!mailcheck(userData.getEmail()))return Result.error("邮箱已经被使用!!!");
        else if (!namecheck(userData.getUsername()))return Result.error("名称重复!!!");
//        密码加密
        userData.setPasswordHash(PasswordUtil.hashPassword(userData.getPasswordHash()));


        int result = usermapper.insert(userData);
        if (result > 0) {
            return Result.success("注册成功!");
        } else {
            return Result.error("注册失败，请稍后重试!");
        }
    }

}
