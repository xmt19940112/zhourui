package com.zr.controller;

import com.zr.service.IUserService;
import com.zr.util.UserInitApplicationRunnerUtil;
import com.zr.util.UserInitCommandLineRunnerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * @auther zhourui
 * @date 2020/10/22 6:38 下午
 **/

@RestController
@Api(tags="用户登陆接口")
public class HelloController {
    @Resource
    private IUserService iUserService;
    @Resource
    UserInitCommandLineRunnerUtil userInitCommandLineRunnerUtil;
    @RequestMapping("/hello")
    public Object sayHello() {
        return "hello-zhourui!";
    }

    @ApiOperation(value="用户登陆", notes="用户登陆")
    @GetMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", name = "passwd", value = "用户密码", required = true)
    })
    public String login(@RequestParam  String username, @RequestParam  String passwd) {
        boolean login = iUserService.login(username, passwd);
        if(login) {
            return "登陆成功";
        }else {
            return  "登陆失败";
        }
    }

    @RequestMapping("/register")
    public String register(String username,String passwd) {
        boolean login = iUserService.register(username, passwd);
        if(login) {
            return "注册成功";
        }else {
            return  "注册失败";
        }
    }

    @RequestMapping("/batchAdd")
    public String batchAdd(String username, String passwd, HttpServletResponse response){
        iUserService.batchAdd(username, passwd);
        return "成功";
    }

    @RequestMapping("/getName")
    public String getName(){
        return userInitCommandLineRunnerUtil.getName();
    }






}
