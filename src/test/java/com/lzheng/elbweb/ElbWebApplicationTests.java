package com.lzheng.elbweb;

import com.lzheng.elbweb.dao.tokenDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElbWebApplicationTests {


    @Test
    public void contextLoads() {
       Jedis jedis=tokenDao.getJedis();
       jedis.set("token","123");
       System.out.println(jedis.get("token"));

    }


}
