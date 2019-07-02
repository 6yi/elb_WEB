package com.lzheng.elbweb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * @ClassName tokenDao
 * @Author lzheng
 * @Date 2019/7/2 19:14
 * @Version 1.0
 * @Description:
 */

@Component
public class tokenDao {

    private static Jedis jedis;

    static {
            jedis=new Jedis("59.110.173.180",6378);
            jedis.auth("lzheng");
    }

    public static Jedis getJedis() {
        return jedis;
    }


    public static void setJedis(Jedis jedis) {
        tokenDao.jedis = jedis;
    }

    public String queryToken(){
        return jedis.get("token");
    }
    public String queryUserId(){
        return jedis.get("userid");
    }

    public String queryCookie(){
        return jedis.get("cookie");
    }

    public void setToken(String token){
        jedis.set("token",token);
        jedis.expire("token",600);
    }

    public void setUserId(String userid){
        jedis.set("userid",userid);
        jedis.expire("userid",600);
    }

    public void setCookie(String cookie){
        jedis.set("cookie",cookie);
        jedis.expire("cookie",600);
    }



    //    public static void main(String[] args) {
//        String host="59.110.173.180";
//        Jedis jedis=new Jedis(host,6378);
//        jedis.auth("lzheng");
////        jedis.expire("token",1);
//        System.out.println(jedis.get("token"));
//    }



}
