package com.yuban;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
        "com.yuban",          // 默认扫描
        "com.yuban.service",  // 明确添加服务包
        "com.yuban.controller" // 明确添加控制器包
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}