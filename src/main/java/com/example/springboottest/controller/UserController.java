package com.example.springboottest.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.example.springboottest.server.UserServer;
import com.example.springboottest.bean.UserData;
import com.example.springboottest.utils.AppPush;
import com.example.springboottest.utils.ResponseData;
import com.example.springboottest.utils.redis.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;


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

    @RequestMapping("/notifyChannel")//阿里云通知推送测试
    public ResponseData<String> notifyChannel(String id, HttpServletRequest request, HttpServletResponse response) {

        AppPush.push();
        ResponseData<String> data = new ResponseData<>();
//        if (jedisUtil.getByKey("userToken" + id) != null) {
//            jedisUtil.delByKey("userToken" + id);//清除token
//            data.setTokenState(0);
//        } else {
//            data.setTokenState(1);
//        }
        data.setTokenState(0);
        return data;
    }

    @RequestMapping("/uploadMore")//多个文件上传测试    https://blog.csdn.net/linzhiqiang0316/article/details/77016997
    public ResponseData<String> upload(String id, HttpServletRequest request, HttpServletResponse response) {
        File fileDir = new File("FileDir");
        if (!fileDir.isDirectory()) {//创建目录
            fileDir.mkdirs();
        }
        ResponseData<String> data = new ResponseData<>();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("files");
        String name = params.getParameter("name");
        System.out.println("name:" + name);
        String _id = params.getParameter("id");
        System.out.println("_id:" + id);
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            fileDir + File.separator + new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    data.setMessage("上传失败," + e.getMessage());
                    return data;
                }
            } else {
                data.setMessage("上传失败，因为文件是空的");
                return data;
            }
        }
        data.setMessage("上传成功");
        return data;
    }

    @RequestMapping("/upload")//单个文件上传      https://blog.csdn.net/linzhiqiang0316/article/details/77016997
    @ResponseBody
    public ResponseData<String> handleFileUpload(String id, @RequestParam("file") MultipartFile file) {
        File fileDir = new File("FileDir");
        if (!fileDir.isDirectory()) {//创建目录
            fileDir.mkdirs();
        }
        ResponseData<String> data = new ResponseData<>();
        if (!file.isEmpty()) {
            try {
                /*
                 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到
                 * d:/files大家是否能实现呢？ 等等;
                 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；
                 * 3、文件格式; 4、文件大小的限制;
                 */
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                                fileDir + File.separator + new File(file.getOriginalFilename())));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                data.setMessage("上传失败," + e.getMessage());
                return data;
            } catch (IOException e) {
                e.printStackTrace();
                data.setMessage("上传失败," + e.getMessage());
                return data;
            }

            data.setMessage("上传成功");
            return data;

        } else {
            data.setMessage("上传失败，因为文件是空的");
            return data;
        }
    }

}
