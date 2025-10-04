package com.yuban.shop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {

//        // 开启详细日志
//        System.setProperty("logging.level.root", "DEBUG");
//        System.setProperty("logging.level.org.springframework", "TRACE");
//        System.setProperty("logging.level.com.baomidou", "DEBUG");
//
//        // 允许循环依赖
//        System.setProperty("spring.main.allow-circular-references", "true");

        SpringApplication.run(Application.class, args);
    }
}