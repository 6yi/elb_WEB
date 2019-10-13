package com.lzheng.elbweb;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElbWebApplicationTests {

    @Test
    public void contextLoads() {
        Date date = new Date(); //获取当前的系统时间。
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -20);
        String time=dateFormat.format(date);
        String time2=dateFormat.format(calendar.getTime());
        Jedis jedis=new Jedis("59.110.173.180",1234);

        //jedis.set("test", "1");
        String number=jedis.get("test");
        System.out.println(number);
    }

    public String[] get_time(){
        Date date = new Date(); //获取当前的系统时间。
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -20);
        String time=dateFormat.format(date);
        String time2=dateFormat.format(calendar.getTime());
        String[] strs={time,time2};
        return  strs;
    }
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
    @Test
    public void query2(){
        StringBuilder builder=new StringBuilder();
        String areas="09";
        String rooms="311";
        try{
            String []times=get_time();
            URL url=new URL("https://smart-ccdgut.com/electric"+"/areas/"+areas+"/rooms/"+rooms+"/types/0?from="+times[1]+"&to="+times[0]);
            List<String> list=get_ip();
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Host", "smart-ccdgut.com");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.connect();
            InputStream stream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"utf-8"));
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            con.disconnect();
            System.out.println(builder);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
