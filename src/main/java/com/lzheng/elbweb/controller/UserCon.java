package com.lzheng.elbweb.controller;
import com.lzheng.elbweb.entities.MD5;
import com.lzheng.elbweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName User
 * @Author lzheng
 * @Date 2019/6/27 20:20
 * @Version 1.0
 * @Description:
 */

@Controller()
public class UserCon {
        @Autowired
        private UserService service;
        @PostMapping("/user/login")
        public String login(@RequestParam("username")String username,
                            @RequestParam("password")String password,String urlName, HttpServletRequest request){

                //先去获取token和userID，这步应该交给Service层来做才对，考完试再改吧！
                List<String> val=service.query(password,username);
                //判断是否登录成功
                if (val.get(1)!=null){
                        //登录成功则拼凑成完整的链接

                        String cxs="https://smart-ccdgut.com/elecharge/index.php"+"?type=1&needLogin=1&XPS-UserId="+val.get(1)+"&&token="+val.get(0)+"&";

                        //Session保存token和ID，方便调用其它接口

                        request.getSession().setAttribute("token",val.get(0));
                        request.getSession().setAttribute("userid",val.get(1));
                        request.getSession().setAttribute("cookie",val.get(2));
                        request.getSession().setAttribute("username",val.get(3));
                        return "index";

                }else {
                        request.setAttribute("msg","账号或者密码错误");
                        //返回到登录界面
                        return "login";
                }

        }

        @GetMapping("/user/login")
        public String login(){
              return "login";

        }

        @GetMapping("/user/query")
        public String cx( HttpServletRequest request){
                request.getSession().setAttribute("l","query");
                return "query";
        }

        @GetMapping("/user/recharge")
        public String cz( HttpServletRequest request){
                request.getSession().setAttribute("l","recharge");
                return "recharge";
        }

//        @RequestMapping("cz")
//        public String cz(){
//                String cxs="http://wx.ccdgut.edu.cn/ccdgutwx/zhwebapp/gong/chongshuidian.jsp?needLogin=1&XPS-UserId=78283&token="+first()+"&";
//                return "redirect:"+cxs;
//
//        }



}
