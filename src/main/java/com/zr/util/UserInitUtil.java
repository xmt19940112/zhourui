package com.zr.util;

import com.zr.config.UserImportConfig;
import com.zr.model.Users;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInitUtil implements InitializingBean {
    @Autowired
    private UserImportConfig userImportConfig;
    private static Users users;

    @Override
    public void afterPropertiesSet() throws Exception {
        users=new Users(userImportConfig.getSynFromCorn());
        System.out.println("init:"+users.getUsername());
    }


    public static String getName(){
        return users.getUsername();
    }
}
