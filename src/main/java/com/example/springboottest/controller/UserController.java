package com.example.springboottest.controller;

import com.example.springboottest.server.UserServer;
import com.example.springboottest.bean.UserData;
import com.example.springboottest.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/hello")
    public String say() {
        return "Hello!";
    }

    @Autowired
    UserServer server;

    @RequestMapping("/register")
    public ResponseData<UserData> register(UserData userData) {
        return server.register(userData);
    }

    @RequestMapping("/login")
    public ResponseData<UserData> login(UserData userData) {
        return server.login(userData);
    }

    @RequestMapping("/change")
    public ResponseData<UserData> change(UserData userData) {
        return server.change(userData);
    }
}
