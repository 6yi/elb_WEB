package com.lzheng.elbweb.Config;

import com.lzheng.elbweb.Config.myInterceptors.loginIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfig
 * @Author lzheng
 * @Date 2019/6/29 12:19
 * @Version 1.0
 * @Description:
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private loginIn loginInterceptor;
    // 这个方法用来注册拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        //妈的，我使用拦截全部，然后排除部分的时候有问题！！！只能曲线救国了
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user/recharge","/work/queryResult","/work/queryClass");


    }






}
