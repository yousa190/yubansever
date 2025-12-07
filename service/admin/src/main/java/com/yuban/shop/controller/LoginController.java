package com.yuban.shop.controller;

import com.yuban.shop.pojo.entity.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/login")
@Tag(name = "用户登录接口", description = "用户登录、注销等相关接口")
public class LoginController {

    @Operation(summary = "用户登录", description = "用户登录验证，成功后返回JWT Token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登录成功", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Result.class))),
        @ApiResponse(responseCode = "400", description = "用户名或密码错误", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/login")
    public Result login(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "密码", required = true) @RequestParam String password) {
        // TODO: 实现用户登录功能
        // 需要实现的功能：
        // 1. 用户登录验证
        // 2. JWT Token生成
        // 3. 登录状态管理
        return Result.success("登录功能待实现");
    }

    @Operation(summary = "用户注销", description = "用户注销登录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "注销成功", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/logout")
    public Result logout() {
        // TODO: 实现用户注销功能
        return Result.success("注销功能待实现");
    }
}