package com.zr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther zhourui
 * @date 2020/12/9 12:29 上午
 **/
@Controller
public class thymeleafCtrl {

    @RequestMapping("/testThymeleaf")
    public String testThymeleaf(ModelMap map) {
        // 设置属性
        map.addAttribute("name", "enjoy");
        // testThymeleaf：为模板文件的名称
        // 对应src/main/resources/templates/testThymeleaf.html
        return "testThymeleaf";
    }

    @RequestMapping("/hello123")
    @ResponseBody
    public Object sayHello() {
        return "hello-zhourui!";
    }

}
