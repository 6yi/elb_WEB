package com.lzheng.elbweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lzheng.elbweb.entities.Msg;
import com.lzheng.elbweb.service.UserService;
import com.lzheng.elbweb.service.workService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName workCon
 * @Author lzheng
 * @Date 2019/6/28 13:06
 * @Version 1.0
 * @Description:
 */

@Controller
public class workCon {
    @Autowired
    private workService service;

    @Autowired
    private UserService uservice;

    //查询水电==========================================================================================================
    @GetMapping("/work/query")
    public void cx(@RequestParam("loudong")String loudong,@RequestParam("sushe")String sushe ,HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("ajax有反应了！！"+loudong+"xx"+sushe);
        String tokne=(String)request.getSession().getAttribute("token");
        String userid=(String)request.getSession().getAttribute("userid");
        List<String> list=new ArrayList<>();
        if (tokne==null){
             list=uservice.query("lzheng","13650010553");
            tokne=list.get(0);
            userid=list.get(1);
        }
        String bulid=service.query(loudong,sushe);
        request.getSession().setAttribute("msg",bulid);
        PrintWriter out = response.getWriter();
        out.print(bulid);
//        return bulid;
    }

    @GetMapping("/work/queryResult")
    public String queryResult(HttpServletRequest request,HttpServletResponse response){
       String result[]= service.queryResult((String) request.getSession().getAttribute("token"),
                (String) request.getSession().getAttribute("userid"));
       String results[][]=new String[result.length-1][4];
       //这里是切割获取的字符串
       for (int i=1;i<result.length;i++){
            int z=1;
           for(String strs:result[i].split(",")){
                   results[i-1][z]=strs;
                   z++;
            }
            String obj[]=results[i-1][1].split(":");
            results[i-1][0]=obj[0];
            results[i-1][1]=obj[1];
       }
//       result[0]="亲爱的"+request.getSession().getAttribute("username")+"以下是你本学期的成绩噢！";
       request.getSession().setAttribute("result",results);
       request.getSession().setAttribute("hl","亲爱的"+request.getSession().getAttribute("username")+"以下是你本学期的成绩噢！");
       request.getSession().setAttribute("l","jw");
       return "result";
    }

    @GetMapping("/work/queryClass")
    public String queryClass(HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<String[]> list=service.queryClass((String) request.getSession().getAttribute("token"),
                (String) request.getSession().getAttribute("userid"));
        request.getSession().setAttribute("class",list);
        return "class";
    }

}
