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



}
