/** */
package com.example.springboottest.base;

import com.example.springboottest.utils.HttpTool;
import com.example.springboottest.utils.ResponseData;
import com.example.springboottest.utils.redis.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * @功能:
 * @项目名:myspringboot
 * @作者:wangjz
 * @日期:2018年7月5日上午10:06:38
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private HttpTool httpTool;

    static Logger logger = Logger.getLogger("----");

    /**
     * @方法重写 预处理回调方法，实现处理器的预处理（如登录检查），第三个参数为响应的处理器；
     * 返回值：true表示继续流程（如调用下一个拦截器或处理器）；
     * false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResponseData<String> data = new ResponseData<String>();
        StringBuffer url = request.getRequestURL();//得到完整的请求路径
        System.out.println("当前请求路径" + url);
        String token = request.getHeader("userToken");
        String id = request.getHeader("userId");
        String userToken = jedisUtil.getByKey("userToken"+request.getParameter("id"));//数据库中取得token

        if (null == token || token.isEmpty()) {
            data.setState(403);
            data.setMessage("token没有认证通过!原因为：客户端请求参数中无token信息");
        }
        logger.info("header" + token);
        logger.info("redis" + userToken);

        if (token != null && token.equals(userToken)) {
            jedisUtil.set("userToken" + request.getParameter("id"), token, 7200);//将token写入缓存,并延长存活时间
            return true;
        } else {
            data.setTokenState(1);
            httpTool.responseOutWithJson(response, data);
            return false;
        }
    }

    /**
     * @方法重写
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @方法重写
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }

}
