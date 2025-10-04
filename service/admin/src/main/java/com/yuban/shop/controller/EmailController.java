package com.yuban.shop.controller;


import cn.hutool.core.util.RandomUtil;
import com.yuban.shop.exception.SystemException;
import com.yuban.shop.pojo.enums.HttpCodeEnum;
import com.yuban.shop.pojo.entity.Result;
import com.yuban.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/email")
@Tag(name = "邮件服务接口", description = "邮件发送、验证码等相关接口")
public class EmailController {


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;



    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.nickname}")
    private String nickname;

    @Operation(summary = "发送邮箱验证码", description = "向指定邮箱发送6位数字验证码，有效期5分钟")
    @GetMapping("/code")
    public Result GetCode(
            @Parameter(description = "接收验证码的邮箱地址", required = true) @RequestParam String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(nickname+'<'+sender+'>');
        message.setTo(email);
        message.setSubject("来自"+nickname+"的邮箱验证码");
        String code =  RandomUtil.randomNumbers(6);

        redisTemplate.opsForValue().set("email_code_"+email, code, 300000, TimeUnit.MILLISECONDS);

        String content = "【验证码】您的验证码为：" + code + " 。 验证码五分钟内有效，逾期作废。\n\n\n" +
                "------------------------------\n\n\n" ;
        message.setText(content);

        // 发送邮件
        try{
            javaMailSender.send(message);
        }catch (Exception e){
            throw new SystemException(HttpCodeEnum.MAIL_SEND_ERROR);
        }


        return Result.success("发送成功");

    }
}
