package com.yuban.Controller;

import com.yuban.pojo.Result;
import com.yuban.pojo.UserData;
import com.yuban.service.UserService;
import com.yuban.service.impl.UserServiceimpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/check_name{user_name}")
    public Result check_name(@RequestParam String user_name){
        return  Result.success(userService.count_name(user_name));
    }

    @GetMapping("/check_email{email}")
    public Result check_email(@RequestParam String email){
        return  Result.success(userService.count_email(email));
    }

//    注冊事件

    @PostMapping("/sign")
    public Result sign( UserData userData){
        if (userService.count_email(userData.getEmail())==0)
        return Result.success(userService.sign(userData.getUserName(),userData.getEmail(),userData.getPwd()));
        else return Result.error("注冊錯誤!");
    }


//    登录事件
private static boolean containsAtSymbol(String input) {
    Pattern pattern = Pattern.compile(".*@.*");
    Matcher matcher = pattern.matcher(input);
    return matcher.matches();
}

//20240514:后续更新为下发令牌
    @PostMapping("")
    public Result login( String  name, String  pwd){
        UserData msg;
        if (!containsAtSymbol(name)&&userService.check_name(name))
        {
            msg=userService.getByName(name,pwd);
            log.info(String.valueOf(msg));
            if (msg==null)return Result.error("密码错误!!!");
            else if (msg.getUserId()>0)return Result.success(msg);
        }
        else if(containsAtSymbol(name)&&userService.check_email(name)) {
            msg = userService.getByemail(name,pwd);
            if (msg==null)return Result.error("密码错误!!!");
            else if (msg.getUserId()>0)Result.success(msg);
        }
        return Result.error("内部错误!!");
    }

//    密码找回
    @PostMapping("/find")
    public Result find( String code,String email){

        String emailKey  = "email_code_"+email;

        String storedToken = (String) redisTemplate.opsForValue().get(emailKey);

        if(code.equals(storedToken)){
            return Result.success("验证成功");
        }else {
            return Result.error("验证失败");
        }
    }

//    验证邮箱
    @GetMapping("/yanzheng")
    public Result Yz(@RequestParam String email){
        return Result.success(userService.yanzheng(email));
    }

    @PostMapping("/reset")
    public Result reset(String email ,String pwd){
        return Result.success(userService.reset(email,pwd));
    }

}
