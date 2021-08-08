package com.zr.util;

import com.zr.config.UserImportConfig;
import com.zr.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInitCommandLineRunnerUtil implements CommandLineRunner {
    @Autowired
    private UserImportConfig userImportConfig;
    private  Users users;

    @Override
    public void run(String... args) throws Exception {
        users=new Users(userImportConfig.getSynFromCorn());
        System.out.println("commanLine:"+users.getUsername());
    }
    public  String getName(){
        System.out.println(users);
        return users.getUsername();
    }
}
