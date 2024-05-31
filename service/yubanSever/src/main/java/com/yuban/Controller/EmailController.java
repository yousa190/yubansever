package com.yuban.Controller;


import com.yuban.pojo.Result;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/email")
public class EmailController {


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.nickname}")
    private String nickname;

    @GetMapping("/code")
    public Result GetCode(@RequestParam String email){
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
        javaMailSender.send(message);

        return Result.success("发送成功");

    }
}
