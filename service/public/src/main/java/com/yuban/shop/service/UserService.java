package com.yuban.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuban.shop.pojo.dto.UserDto;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.entity.UserData;
import com.yuban.shop.pojo.vo.UserVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService extends IService<UserData> {


    public Result selectList();

    public boolean namecheck(String username);

    public boolean mailcheck(String email);
    
    public UserData getOneByUsername(String username);
    
    public UserData getOneByEmail(String email);

    public Result regist(UserDto userData );

    /**
     * 获取用户列表（分页）
     * @param keyWord 搜索关键词
     * @param searchType 搜索类型：username, email, phone
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页用户列表
     */
    public Result getUserList(String keyWord, String searchType, Integer page, Integer pageSize);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserVo getUserById(Long userId);

    /**
     * 更新用户信息
     * @param userDto 用户信息DTO
     * @return 是否更新成功
     */
    public boolean updateUserInfo(UserDto userDto);

    /**
     * 获取所有用户（用于下拉列表等场景）
     * @return 用户列表
     */
    public List<UserVo> getAllUsers();

    /**
     * 启用/禁用用户账号
     * @param userId 用户ID
     * @param status 状态：0-启用，1-禁用
     * @return 是否操作成功
     */
    public boolean updateStatus(Long userId, Integer status);

    /**
     * 软删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    public boolean deleteUserById(Long userId);
}