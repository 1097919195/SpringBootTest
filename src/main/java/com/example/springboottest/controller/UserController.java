package com.example.springboottest.controller;

import com.example.springboottest.server.UserServer;
import com.example.springboottest.bean.UserData;
import com.example.springboottest.utils.ResponseData;
import com.example.springboottest.utils.redis.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/hello")
    public String say() {
        return "Hello!";
    }

    @Autowired
    UserServer server;
    @Autowired
    private JedisUtil jedisUtil;

    @RequestMapping("/register")//注册
    public ResponseData<UserData> register(UserData userData) {
        return server.register(userData);
    }

    @RequestMapping("/login")//登录
    public ResponseData<UserData> login(UserData userData) {
        return server.login(userData);
    }

    @RequestMapping("/change")//修改密码
    public ResponseData<UserData> change(UserData userData) {
        return server.change(userData);
    }

    @RequestMapping("/quitLogin")//退出登录
    public ResponseData<String> quitLogin(String id, HttpServletRequest request, HttpServletResponse response) {
        ResponseData<String> data = new ResponseData<>();
        if (jedisUtil.getByKey("userToken" + id) != null) {
            jedisUtil.delByKey("userToken" + id);//清除token
            data.setTokenState(0);
        } else {
            data.setTokenState(1);
        }
        return data;
    }
}
