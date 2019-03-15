package com.channelsoft.ucds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author sicwen
 * @date 2019/03/14
 */
@Controller
public class LoginController {
    @RequestMapping("/login")
    public ModelAndView login(String username, String password, HttpSession httpSessoin){
        //存储到session
        httpSessoin.setAttribute("username",username);
        httpSessoin.setAttribute("password",password);
        ModelAndView mv = new ModelAndView("operator");
        mv.getModel().put("username",username);
        return mv;
    }
}