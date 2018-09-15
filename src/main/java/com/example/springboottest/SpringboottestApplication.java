package com.example.springboottest;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringboottestApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringboottestApplication.class, args);
        SpringApplication application = new SpringApplication(SpringboottestApplication.class);
        //关闭banner横幅
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
