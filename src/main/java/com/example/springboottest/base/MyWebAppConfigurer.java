/** */
package com.example.springboottest.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
        registry.addInterceptor(check).addPathPatterns("/**").excludePathPatterns("/user/login","/user/register","/user/download");
        super.addInterceptors(registry);
    }

}