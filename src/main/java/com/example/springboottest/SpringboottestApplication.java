package com.example.springboottest;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class SpringboottestApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringboottestApplication.class, args);
        SpringApplication application = new SpringApplication(SpringboottestApplication.class);
        //关闭banner横幅
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    /**
     *      * 文件上传配置
     *      * @return
     *      
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("10240KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }

}
