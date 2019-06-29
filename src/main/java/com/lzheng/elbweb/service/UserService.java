package com.lzheng.elbweb.service;

import com.lzheng.elbweb.entities.MD5;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName UserService
 * @Author lzheng
 * @Date 2019/6/28 10:30
 * @Version 1.0
 * @Description:
 */

@Service
public class UserService {
    private static Pattern pattern = Pattern.compile("\"xm\":.{4,5}");

    public List<String> query(String password, String username){
        String val="";
        String userid="";
        String cookie="";
        List<String> parms=new ArrayList<>();

        //默认使用我的ID和账号去查询水电
        String passwordCont = "password=ddd286eb2672e5c8998ccbc1bfc21fa1";
        String SignCont = "sign=2";
        if(!username.startsWith("2")){
            SignCont="sign=3";
        }

        String userNameCont = "account=13650010553";


        try {
            URL url=new URL("https://smart.ccdgut.edu.cn/app/user/login.do");
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setUseCaches(true);
            //伪造请求头redirect
            con.setRequestProperty("User-Agent", "MI 6/1.8(Android;8.0.0;1080x1790;s;46000;wifi)");
            con.setRequestProperty("DevicesId", "MI 6/866654034887460");
            con.setRequestProperty("XPS-PUSHID", "AtDGuURXRLxp4Lo7cwFwbOfS___dBGmcoQG0oaiWPFK3");
            con.setRequestProperty("XPS-ClientCode", "csxy");
            con.setRequestProperty("schoolCode", "csxy");
            con.setRequestProperty("Content-Type", " application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", "68");
            con.setRequestProperty("Host", "smart.ccdgut.edu.cn");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Accept-Encoding", "gzip");
            //请求参数
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            DataOutputStream out = new DataOutputStream(con
                    .getOutputStream());
            if (!username.equals("default")) {
                passwordCont = "password="+ MD5.getStringMD5String(MD5.getStringMD5String("jw134#%pqNLVfn"+password));
                System.out.println("psw==="+passwordCont);
                userNameCont="account="+username;
            }

            out.writeBytes(passwordCont+"&"+SignCont+"&"+userNameCont);
            //流用完记得关
            out.flush();
            out.close();
            Map<String, List<String>> headers = con.getHeaderFields();
            Set<String> keys = headers.keySet();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            line = reader.readLine();


            Matcher matcher = pattern.matcher(line);
            String result="";
            //查找符合规则的子串
            while(matcher.find()){
                //获取字符串
                result=matcher.group();
            }


            String name=result.split(":")[1].replaceAll("\"","");


            val = con.getHeaderField("Xps-Usertoken");
            userid=con.getHeaderField("Xps-UserId");
            cookie=con.getHeaderField("Set-Cookie");

            parms.add(val);
            parms.add(userid);
            parms.add(cookie);
            parms.add(name);

            reader.close();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parms;
    }

}
