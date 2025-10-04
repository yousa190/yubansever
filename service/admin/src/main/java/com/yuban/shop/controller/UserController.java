package com.yuban.shop.controller;

import com.yuban.shop.pojo.dto.UserDeleteDto;
import com.yuban.shop.pojo.dto.UserDto;
import com.yuban.shop.pojo.dto.UserStatusDto;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.entity.UserData;
import com.yuban.shop.pojo.vo.UserVo;
import com.yuban.shop.service.UserService;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户管理控制器
 * 提供用户管理相关接口，包括用户注册、查询、更新、启用/禁用等功能
 */
@RestController
@RequestMapping("/admin/user")
@Tag(name = "后台用户管理接口", description = "用户注册、验证等相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "测试接口", description = "用于测试用户服务的基本功能")
    @GetMapping("/test")
    public Result test(){
        return userService.selectList();
    }

    @Operation(summary = "用户注册", description = "用户注册接口，无需验证码")
    @PostMapping("/register")
    public Result regist(
            @Parameter(description = "用户注册信息") @RequestBody UserDto userData) {
        log.info("用户注册：{}", userData);
        return userService.regist(userData);
    }


    @Operation(summary = "检查用户名是否已存在", description = "检查用户名是否已存在")
    @GetMapping("/checkUsername")
    public Result checkUsername(@RequestParam String username) {
        boolean isAvailable = userService.namecheck(username);
        if (isAvailable) {
            return Result.success("用户名可用");
        } else {
            return Result.error("用户名已被使用");
        }
    }

    @Operation(summary = "检查邮箱是否已存在", description = "检查邮箱是否已存在")
    @GetMapping("/checkEmail")
    public Result checkEmail(@RequestParam String email) {
        boolean isAvailable = userService.mailcheck(email);
        if (isAvailable) {
            return Result.success("邮箱可用");
        } else {
            return Result.error("邮箱已被使用");
        }
    }

    @Operation(summary = "获取用户列表", description = "分页获取用户列表，支持按用户名、邮箱、手机号搜索")
    @GetMapping("/list")
    public Result getUserList(
            @Parameter(description = "搜索关键词") @RequestParam(required = false, defaultValue = "") String keyWord,
            @Parameter(description = "搜索类型") @RequestParam(required = false, defaultValue = "username") String searchType,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        return userService.getUserList(keyWord, searchType, page, pageSize);
    }

    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{userId}")
    public Result getUserById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        UserVo userVo = userService.getUserById(userId);
        if (userVo != null) {
            return Result.success(userVo);
        } else {
            return Result.error("用户不存在");
        }
    }

    @Operation(summary = "更新用户信息", description = "更新用户信息，确保用户名和邮箱不重复")
    @PostMapping("/update")
    public Result updateUserInfo(
            @Parameter(description = "用户信息", required = true) @RequestBody UserDto userDto) {
        log.info("更新用户信息：{}", userDto);
        // 检查用户名是否重复
        if (!userService.namecheck(userDto.getUsername())) {
            UserData existingUser = userService.getOneByUsername(userDto.getUsername());
            if (existingUser != null && !existingUser.getUserId().equals(userDto.getUserId())) {
                return Result.error("用户名已被使用");
            }
        }
        
        // 检查邮箱是否重复
        if (!userService.mailcheck(userDto.getEmail())) {
            UserData existingUser = userService.getOneByEmail(userDto.getEmail());
            if (existingUser != null && !existingUser.getUserId().equals(userDto.getUserId())) {
                return Result.error("邮箱已被使用");
            }
        }
        
        boolean result = userService.updateUserInfo(userDto);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    @Operation(summary = "获取所有用户", description = "获取所有用户信息（用于下拉列表等场景）")
    @GetMapping("/all")
    public Result getAllUsers() {
        List<UserVo> userList = userService.getAllUsers();
        return Result.success(userList);
    }

    @Operation(summary = "启用/禁用用户", description = "根据用户ID和状态参数启用或禁用用户账号")
    @PostMapping("/updateStatus")
    public Result updateStatus(
            @Parameter(description = "用户状态信息", required = true) @RequestBody UserStatusDto userStatusDto) {
        log.info("更新用户状态：userId={}, status={}", userStatusDto.getUserId(), userStatusDto.getStatus());
        boolean result = userService.updateStatus(userStatusDto.getUserId(), userStatusDto.getStatus());
        if (result) {
            return Result.success("操作成功");
        } else {
            return Result.error("操作失败");
        }
    }

    @Operation(summary = "删除用户", description = "根据用户ID软删除用户")
    @PostMapping("/delete")
    public Result delete(
            @Parameter(description = "用户删除信息", required = true) @RequestBody UserDeleteDto userDeleteDto) {
        log.info("删除用户：userId={}", userDeleteDto.getUserId());
        boolean result = userService.deleteUserById(userDeleteDto.getUserId());
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    private Result checkExistence(MessagePassingQueue.Supplier<Boolean> check, String errorMsg) {
        return check.get() ? Result.success() : Result.error(errorMsg);
    }
}