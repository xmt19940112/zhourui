package com.zr.util;

import com.zr.config.UserImportConfig;
import com.zr.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class UserInitApplicationRunnerUtil implements ApplicationRunner {
    @Autowired
    private UserImportConfig userImportConfig;
    private static Users users;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        users=new Users(userImportConfig.getSynFromCorn());
        System.out.println("applicaton:"+users.getUsername());
    }

    public static String getName(){
        return users.getUsername();
    }
}
