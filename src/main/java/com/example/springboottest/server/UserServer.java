package com.example.springboottest.server;

import com.example.springboottest.bean.UserData;
import com.example.springboottest.dao.UserDao;
import com.example.springboottest.utils.NoteUtil;
import com.example.springboottest.utils.ResponseData;
import com.example.springboottest.utils.redis.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServer {
    @Autowired
    private UserDao userdao;
    @Autowired
    private JedisUtil jedisUtil;

    //模拟用户注册        http://localhost:8080/user/register?account=test2&password=123 (注意数据库字段可不可以为空的问题)
    public ResponseData<UserData> register(UserData userData){
        ResponseData<UserData> data = new ResponseData<>();
        String mdPassword = NoteUtil.md5(userData.getPassword());//对密码MD5 base64加密
        UserData man = userdao.findByUserAccount(userData);//查找注册的账户是否已存在
        if (man != null) {
            data.setMessage("用户已存在数据库中，注册失败");
            data.setState(1);
            data.setData(man);
            return data;
        }else {
            UserData user = new UserData();
            user.setId(userData.getId());
            user.setAccount(userData.getAccount());
            user.setPassword(mdPassword);
            user.setUserName(userData.getUserName());

            userdao.insert(user);

            data.setMessage("该用户没在数据库中，注册成功");
            data.setState(0);
            data.setData(user);
        }
        return data;
    }

    //模拟用户登录        http://localhost:8080/user/login?account=test&password=123456
    public ResponseData<UserData> login(UserData userData){
        ResponseData<UserData> data = new ResponseData<>();
        String mdPassword = NoteUtil.md5(userData.getPassword());//对密码MD5 base64加密
        UserData man = userdao.findByUserAccount(userData);//查找账户是否已存在
        if (man == null) {
            data.setMessage("用户不存在");
            data.setState(1);
            return data;
        }else if(mdPassword.equals(man.getPassword())){
            String token = UUID.randomUUID().toString();//设置登陆token
            man.setLoginToken(token);
            jedisUtil.set("userToken" + man.getId(), token, 120);//将token写入缓存

            data.setMessage("登陆成功");
            data.setState(0);
            data.setData(man);
        }else {
            data.setMessage("密码错误");
            data.setState(2);
        }
        return data;
    }

    //模拟用户修改密码      http://localhost:8080/user/change?password=123456&id=5&final_password=123321
    public ResponseData<UserData> change(UserData userData){
        ResponseData<UserData> data = new ResponseData<>();
        String oldPassword = NoteUtil.md5(userData.getPassword());//对密码MD5 base64加密
        UserData man = userdao.findByUserId(userData);//查找注册的账户是否已存在
        if (oldPassword.equals(man.getPassword())) {
            if (userData.getFinal_password() != null) {
                String final_password = NoteUtil.md5(userData.getFinal_password());
                man.setPassword(final_password);
                userdao.updatePassword(man);
                data.setState(0);
                data.setMessage("修改密码成功");
            }else {
                data.setState(1);
                data.setMessage("请输入新密码");
            }
        }else {
            data.setState(2);
            data.setMessage("原密码输入错误");
        }
        return data;
    }


}
