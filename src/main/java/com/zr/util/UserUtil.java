package com.zr.util;

import com.zr.config.UserImportConfig;
import com.zr.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class UserUtil {

    public  static Users users;
    @Autowired
    public void setConfig(UserImportConfig config) {
        users=new Users(config.getSynFromCorn());
        System.out.println(users.getUsername());
    }
    

    public static String getName(){
        return users.getUsername();
    }

}
