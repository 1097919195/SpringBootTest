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
    @RequestMapping("/getUserData")
    public ResponseData<UserData> getUserData(UserData userData) {
        return server.getData(userData);
    }
}
