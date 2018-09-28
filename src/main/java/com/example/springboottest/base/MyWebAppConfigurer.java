/** */
package com.example.springboottest.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @功能:
 * @项目名:myspringboot
 * @作者:wangjz
 * @日期:2018年7月5日上午10:08:18
 */

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
	@Autowired
	private CheckTokenInterceptor check;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(check).addPathPatterns("/**").excludePathPatterns("/user/login","/user/register","/user/download","/OTA/**","/FileDir/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File fileDir = new File("FileDir");
        if (!fileDir.isDirectory()) {//创建目录
            fileDir.mkdirs();
        }
        //和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8081/OTA/1.jpg
        //其中OTA表示访问的前缀。"file:D:/OTA/"是文件真实的存储路径
        registry.addResourceHandler("/OTA/**").addResourceLocations("file:D:/OTA/");
        registry.addResourceHandler("/FileDir/**").addResourceLocations("file:"+fileDir.getAbsolutePath()+"/");
    }

}