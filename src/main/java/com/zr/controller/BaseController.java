package com.zr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zhourui
 * @date 2020/11/22 10:38 下午
 **/
@RestController
public class BaseController {
    @RequestMapping("/404.do")
    public Object error_404() {
        return "你要找的页面，被lison偷吃了！";
    }

}
