package com.lzheng.elbweb.service;

import com.lzheng.elbweb.entities.MD5;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName workService
 * @Author lzheng
 * @Date 2019/6/28 14:36
 * @Version 1.0
 * @Description:
 */

@Service
public class workService {

    //正则
    private static Pattern pattern = Pattern.compile("<pre.+>[\\w\\W]*<.+pre>");
    private static Pattern pattern2 = Pattern.compile("<span class=\"fl\".+>[\\w\\W]*<.+span>");

    public List<String> get_ip() throws IOException, JSONException {
        URL url1=new URL("http://121.199.42.16/VAD/GetIp.aspx?act=get&num=1&time=60&plat=0&re=0&type=0&ow=1");
        StringBuilder json = new StringBuilder();
        URLConnection yc = url1.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(),"utf-8"));//防止乱码
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            json.append(inputLine);
        }
        in.close();
        JSONObject jsonObj = new JSONObject(json.toString());
        //int ip=jsonObj.getInt("IP");
        String json2=jsonObj.getString("data");
        JSONObject jsonObj2 = new JSONObject(json2.replaceAll("\\[","").replaceAll("]",""));
        String ip=jsonObj2.getString("IP");
        String port=jsonObj2.getString("Port");
        List<String> list=new ArrayList<>();
        list.add(ip);
        list.add(port);
        System.out.println("获取代理ip成功："+ip);
        return list;
    }

    //查水电
    public String query(String loudong,String sushe,String token,String  userid){
        StringBuilder builder=new StringBuilder();
        String tokenname = "token="+token;
        String sushename="sushe="+sushe;
        String loudongname="loudong="+loudong;
        try {
            URL url1=new URL("http://121.199.42.16/VAD/GetIp.aspx?act=get&num=1&time=60&plat=0&re=0&type=0&ow=1");
            List<String> list=get_ip();
//            System.getProperties().setProperty("proxySet", "true");
//            System.setProperty("http.proxyHost", list.get(0));
//            System.setProperty("http.proxyPort", list.get(1));
            SocketAddress addr = new InetSocketAddress(list.get(0), Integer.parseInt(list.get(1)));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            URL url=new URL("https://smart-ccdgut.com/elecharge/data.php");
            HttpURLConnection con= (HttpURLConnection) url.openConnection(proxy);
            //System.out.println(getHtml("http://www.ip138.com/ip2city.asp"));
            con.setRequestMethod("POST");
            con.setUseCaches(true);
            //伪造请求头redirect
            con.setRequestProperty("Host", "smart-ccdgut.com");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            con.setRequestProperty("Origin", "https://smart-ccdgut.com");
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            con.setRequestProperty("Referer", "https://smart-ccdgut.com/elecharge/index.php?needLogin=1&XPS-UserId="+userid+"&token="+token);
            con.setRequestProperty("Accept-Encoding","gzip, deflate, br");
            con.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            //请求参数
            System.out.println("准备尝试！");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            DataOutputStream out = new DataOutputStream(con
                    .getOutputStream());
            out.writeBytes(sushename+"&"+loudongname+"&"+tokenname+"&isBind=0");
            //流用完记得关
            out.flush();
            out.close();
            //获取响应
            InputStream stream = new GZIPInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"utf-8"));
            System.out.println("获取到了！");
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return builder.toString();
    }

    //查课表
    public List<String[]> queryClass(String token,String userid) {
        try {
            String cokie=querCookie(userid,token);
            URL url = new URL("http://wx.ccdgut.edu.cn/ccdgutwx//zhwebapp/gong/chakebiao.jsp");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(true);
            String ip = "58.110.173.180";
            con.setRequestProperty("X-Forwarded-For",ip);
            con.setRequestProperty("HTTP_X_FORWARDED_FOR",ip);
            con.setRequestProperty("HTTP_CLIENT_IP",ip);
            con.setRequestProperty("REMOTE_ADDR",ip);

            con.setRequestProperty("Referer", "http://wx.ccdgut.edu.cn/ccdgutwx/zhwebapp/index.jsp?type=1&needLogin=1&XPS-UserId="+userid+"&token="+token+"&");
            con.setRequestProperty("Host", "wx.ccdgut.edu.cn");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            con.setRequestProperty("Cookie", cokie+"; UM_distinctid=1691e9022e195c-04ddb1b4f29ea4-57b153d-100200-1691e9022e2976; pgv_pvi=489231360; JSESSIONID=D1DCBD8014E5B0F81C58B09B2D6C0F11");
            con.setDoOutput(true);
            con.setDoInput(true);
//            con.connect();

            con.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder builder=new StringBuilder();
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                builder.append(line);
            }

            Matcher matcher = pattern2.matcher(builder.toString());
            String result="";
            //查找符合规则的子串
            while(matcher.find()){
                //获取字符串
                result=matcher.group();
            }
            String results=result.replaceAll("<span class=\"fl\">","").replaceAll("</span>","");
            String[] sts=results.split("<br/><br/><br/>");
            List<String[]> list=new ArrayList<>();
            int i=0;
            for (String str:sts){
//                System.out.println(str);
                String[] days=str.replaceAll("<br/><br/>","").split("((-------------------)|(<br/>))");
                list.add(days);
            }
            ArrayList<String[]> list2=new ArrayList<>();

            con.disconnect();
            return list;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    //查成绩
    public String[] queryResult(String token,String userid) {
        try {
            String cokie=querCookie(userid,token);

            URL url = new URL("http://wx.ccdgut.edu.cn/ccdgutwx//zhwebapp/gong/chachengji.jsp");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(true);
            String ip = "58.110.173.180";
            con.setRequestProperty("X-Forwarded-For",ip);
            con.setRequestProperty("HTTP_X_FORWARDED_FOR",ip);
            con.setRequestProperty("HTTP_CLIENT_IP",ip);
            con.setRequestProperty("REMOTE_ADDR",ip);
            con.setRequestProperty("Referer", "http://wx.ccdgut.edu.cn/ccdgutwx/zhwebapp/index.jsp?type=1&needLogin=1&XPS-UserId="+userid+"&token="+token+"&");
            con.setRequestProperty("Host", "wx.ccdgut.edu.cn");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            con.setRequestProperty("Cookie", cokie+"; UM_distinctid=1691e9022e195c-04ddb1b4f29ea4-57b153d-100200-1691e9022e2976; pgv_pvi=489231360; JSESSIONID=D1DCBD8014E5B0F81C58B09B2D6C0F11");
            con.setDoOutput(true);
            con.setDoInput(true);
//            con.connect();

            con.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder builder=new StringBuilder();
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                builder.append(line);
            }

            Matcher matcher = pattern.matcher(builder.toString());
            String result="";
            //查找符合规则的子串
            while(matcher.find()){
                //获取字符串
                result=matcher.group();
            }
            String results[]=result.substring(16,result.length()-6).split("---------------------------------");
           for (String str:results){
               System.out.println(str);
           }
            con.disconnect();
            return results;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public String querCookie(String userid,String token){
        String[] cokies;
        try {
            URL url = new URL("http://wx.ccdgut.edu.cn/ccdgutwx/zhwebapp/index.jsp?type=1&needLogin=1&XPS-UserId="+userid+"&token="+token+"&");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(true);
            String ip = "58.110.173.180";
            con.setRequestProperty("X-Forwarded-For",ip);
            con.setRequestProperty("HTTP_X_FORWARDED_FOR",ip);
            con.setRequestProperty("HTTP_CLIENT_IP",ip);
            con.setRequestProperty("REMOTE_ADDR",ip);
            con.setRequestProperty("Host", "wx.ccdgut.edu.cn");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            con.setRequestProperty("Cookie", "Cookie: JSESSIONID=DC4D474A03F72EA3DAE2A003E8939; JSESSIONID=C7F48231C5BF27FB7C3AC0864F6B571A");
            con.setRequestProperty("X-Requested-With", "com.zc.dgcsxy");
            con.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            while ((line = reader.readLine()) != null){
//                System.out.println(line);
//            }
            cokies=con.getHeaderField("Set-Cookie").split(";");
            return cokies[0];
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

}
