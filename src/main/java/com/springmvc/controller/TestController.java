package com.springmvc.controller;
import com.springmvc.dao.TabUserMapper;
import com.springmvc.entity.CLS_VO_Result;
import com.springmvc.entity.TabUser;
import com.springmvc.service.LoginService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {

    @Resource
    TabUserMapper tabUserMapper;

    @Autowired
    LoginService loginService;

    @RequestMapping("/showIndex")
    public String showIndex( HttpServletRequest request,ServletResponse response, Model model) {
        String id = request.getParameter("id");
        TabUser test = this.tabUserMapper.selectByPrimaryKey(id);

        if(test != null){
            System.out.println(test.getsName());
        }

        return "success";
    }

    @RequestMapping("/login")
    @ResponseBody
    public  void login(HttpServletRequest request, HttpServletResponse resp, String userName, String password) throws IOException {
        System.out.println(userName);
        System.out.println(password);
        resp.getWriter().print(JSONObject.fromObject(loginService.login(userName,password)));
    }
}