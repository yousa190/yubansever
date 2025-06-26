package com.yuban.controller;

import com.yuban.utils.RedisCache;
import com.yuban.entity.origin.Result;
import com.yuban.entity.origin.UserData;
import com.yuban.service.UserService;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisTemplate;



    @GetMapping("/test")
    public Result test(){

        return userService.selectList();
    }

    private Result checkExistence(MessagePassingQueue.Supplier<Boolean> check, String errorMsg) {
        return check.get() ? Result.success() : Result.error(errorMsg);
    }

    @GetMapping("/mailcheck")
    public Result mailcheck(@RequestParam String email) {
        return checkExistence(() -> userService.mailcheck(email), "邮箱已被使用");
    }

    @GetMapping("/namecheck")
    public Result namecheck(@RequestParam String name) {
        return checkExistence(() -> userService.namecheck(name), "名称已被使用");
    }
    @PostMapping("/register")
    public Result regist(@ModelAttribute UserData userData,@RequestParam String code ){

        System.out.println(code);
        String redisKey="email_code_" + userData.getEmail();
        String cachedCode = (String) redisTemplate.getCacheObject(redisKey);
        if (cachedCode == null) {
            return Result.error("验证码已过期或未发送");
        }
        if (!cachedCode.equals(code)) {
            return Result.error("验证码不正确");
        }
        // 验证通过后删除存储的验证码（防止重复使用）
        redisTemplate.deleteObject(redisKey);

        return  userService.regist(userData);
    }

}
