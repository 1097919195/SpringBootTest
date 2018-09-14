package com.example.springboottest.server;

import com.example.springboottest.bean.UserData;
import com.example.springboottest.utils.ResponseData;
import org.springframework.stereotype.Service;

@Service
public class UserServer {
    public ResponseData<UserData> getData(UserData userData){
        ResponseData<UserData> data = new ResponseData<>();

        UserData user = new UserData();
        user.setId(123);
        user.setAccount("zhangSan");
        user.setPassword("123456");

        data.setMessage("注册成功");
        data.setState(0);
        data.setData(user);
        return data;
    }

}
