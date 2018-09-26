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
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/download", method = RequestMethod.GET)//下载文件测试（在网页中直接打开就好了 http://192.168.199.163:8080/user/download）
    public void testDownload(HttpServletResponse res) {
//        String fileName = "android.apk";
//        res.setHeader("content-type", "application/octet-stream");
//        res.setContentType("application/octet-stream");//告诉浏览器输出内容为流
        String fileName = "android.apk";
        res.setHeader("content-type", "application/vnd.android.package-archive");
        res.setContentType("application/vnd.android.package-archive");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);//Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。  
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File("d://"
                    + fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();//清空缓存用的，一般write()方法后必须跟一个flush()以释放buffer
                i = bis.read(buff);
            }
//            int i = 0;
//            while ((i = bis.read(buff)) != -1) {
//                os.write(buff, 0, buff.length);
//            }
//            os.flush();//清空缓存用的，一般write()方法后必须跟一个flush()以释放buffer
//            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

}
