package com.yuban.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuban.shop.exception.SystemException;
import com.yuban.shop.mapper.Usermapper;
import com.yuban.shop.pojo.dto.UserDto;
import com.yuban.shop.pojo.enums.HttpCodeEnum;
import com.yuban.shop.pojo.vo.UserVo;
import com.yuban.shop.utils.BeanCopyUtils;
import com.yuban.shop.utils.PasswordUtil;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.entity.UserData;
import com.yuban.shop.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 * 实现了用户相关的业务逻辑，包括用户注册、查询、更新等操作
 */
@Service
public class UserServiceimpl  extends ServiceImpl<Usermapper,UserData> implements UserService {

    @Autowired
    private Usermapper usermapper;


    @Override
    public Result selectList() {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getEmail,"19959771806@163.com").or().eq(UserData::getUsername,"yuban");
        List<UserData> res =usermapper.selectList(lambdaQuery);
        return Result.success(res);
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return true-用户名可用，false-用户名已存在
     */
    public boolean namecheck(String username) {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getUsername,username);
        List<UserData> res =usermapper.selectList(lambdaQuery);
        return res.isEmpty();
    }

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱地址
     * @return true-邮箱可用，false-邮箱已存在
     */
    public boolean mailcheck(String email) {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getEmail,email);
        List<UserData> res =usermapper.selectList(lambdaQuery);
        return res.isEmpty();
    }
    
    @Override
    public UserData getOneByUsername(String username) {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getUsername, username);
        return usermapper.selectOne(lambdaQuery);
    }
    
    @Override
    public UserData getOneByEmail(String email) {
        LambdaQueryWrapper<UserData> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(UserData::getEmail, email);
        return usermapper.selectOne(lambdaQuery);
    }

    @Override
    @Transactional
    public Result regist(UserDto userDataDto) {
        UserData userData = new UserData();
        BeanUtils.copyProperties(userDataDto, userData);
        if (!mailcheck(userData.getEmail())){
            throw new SystemException(HttpCodeEnum.EMAIL_ALREADY_USED);
        }
        else if (!namecheck(userData.getUsername())){
            throw new SystemException(HttpCodeEnum.NAME_DUPLICATE);
        };
        // 密码加密
        userData.setPasswordHash(PasswordUtil.hashPassword(userData.getPasswordHash()));


        int result = usermapper.insert(userData);
        if (result > 0) {
            return Result.success("注册成功");
        } else {
            return Result.error("注册失败，请稍后重试!");
        }
    }

    @Override
    public Result getUserList(String keyWord, String searchType, Integer page, Integer pageSize) {
        try {
            // 创建分页对象
            Page<UserData> pageData = new Page<>(page, pageSize);
            
            // 创建查询条件
            LambdaQueryWrapper<UserData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(UserData::getIsDel, false);
            // 根据搜索类型和关键词添加查询条件
            if (StringUtils.hasText(keyWord)) {
                switch (searchType) {
                    case "username":
                        queryWrapper.like(UserData::getUsername, keyWord);
                        break;
                    case "email":
                        queryWrapper.like(UserData::getEmail, keyWord);
                        break;
                    case "phone":
                        queryWrapper.like(UserData::getPhone, keyWord);
                        break;
                    default:
                        // 默认按用户名搜索
                        queryWrapper.like(UserData::getUsername, keyWord);
                        break;
                }
            }
            
            // 按创建时间倒序排列
            queryWrapper.orderByDesc(UserData::getCreatedAt);
            
            // 执行分页查询
            Page<UserData> result = usermapper.selectPage(pageData, queryWrapper);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("list", BeanCopyUtils.copyBeanList(result.getRecords(), UserVo.class));
            data.put("count", result.getTotal());
            data.put("totalPage", result.getPages());
            data.put("currentPage", page);
            data.put("pageSize", pageSize);
            
            return Result.success(data);
            
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }

    @Override
    public UserVo getUserById(Long userId) {
        UserData userData = usermapper.selectById(userId);
        return BeanCopyUtils.copyBean(userData, UserVo.class);
    }

    @Override
    public boolean updateUserInfo(UserDto userDto) {
        UserData userData = BeanCopyUtils.copyBean(userDto, UserData.class);
        return usermapper.updateById(userData) > 0;
    }

    @Override
    public List<UserVo> getAllUsers() {
        List<UserData> userDataList = usermapper.selectList(null);
        return BeanCopyUtils.copyBeanList(userDataList, UserVo.class);
    }

    @Override
    @Transactional
    public boolean updateStatus(Long userId, Integer status) {
        UserData userData = new UserData();
        userData.setUserId(Math.toIntExact(userId));
        userData.setStatus(status);
        userData.setUpdatedAt(new Date());
        return usermapper.updateById(userData) > 0;
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long userId) {
        UserData userData = new UserData();
        userData.setUserId(Math.toIntExact(userId));
        userData.setIsDel(1); // 软删除标记：1-已删除
        userData.setUpdatedAt(new Date());
        return usermapper.updateById(userData) > 0;
    }
}