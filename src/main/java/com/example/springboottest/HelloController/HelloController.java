package com.example.springboottest.HelloController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String say() {
        return "Hello!";
    }


    @RequestMapping("/getMap")
    public Map<String, Object> getMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errorCode", "200");
        result.put("errorMsg", "成功");
        return result;
    }
}
