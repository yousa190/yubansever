package controller;

import com.yuban.shop.pojo.dto.UserDto;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.pojo.entity.UserData;
import com.yuban.shop.pojo.vo.UserVo;
import com.yuban.shop.service.UserService;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台用户管理控制器
 * 提供前台用户注册、登录、信息验证等接口
 */
@RestController
@RequestMapping("/shop/user")
@Tag(name = "前台用户管理接口", description = "用户注册、验证等相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;



    @Operation(summary = "测试接口", description = "用于测试用户服务的基本功能")
    @GetMapping("/test")
    public Result test(){
        return userService.selectList();
    }

    @Operation(summary = "邮箱重复检查", description = "检查邮箱是否已被注册")
    @GetMapping("/mailcheck")
    public Result mailcheck(
            @Parameter(description = "要检查的邮箱地址", required = true) @RequestParam String email) {
        return checkExistence(() -> userService.mailcheck(email), "邮箱已被使用");
    }

    @Operation(summary = "用户名重复检查", description = "检查用户名是否已被使用")
    @GetMapping("/namecheck")
    public Result namecheck(
            @Parameter(description = "要检查的用户名", required = true) @RequestParam String name) {
        return checkExistence(() -> userService.namecheck(name), "名称已被使用");
    }

    @Operation(summary = "用户注册", description = "用户注册接口，需要邮箱验证码")
    @PostMapping("/register")
    public Result regist(
            @Parameter(description = "用户注册信息") @ModelAttribute UserDto userData,
            @Parameter(description = "邮箱验证码", required = true) @RequestParam String code ){

        System.out.println(code);
        String redisKey="email_code_" + userData.getEmail();
        String cachedCode = (String) redisTemplate.opsForValue().get(redisKey);
        if (cachedCode == null) {
            return Result.error("验证码已过期或未发送");
        }
        if (!cachedCode.equals(code)) {
            return Result.error("验证码不正确");
        }

        // 验证通过后删除存储的验证码（防止重复使用）
        redisTemplate.delete(redisKey);

        return  userService.regist(userData);
    }




    private Result checkExistence(MessagePassingQueue.Supplier<Boolean> check, String errorMsg) {
        return check.get() ? Result.success() : Result.error(errorMsg);
    }


}