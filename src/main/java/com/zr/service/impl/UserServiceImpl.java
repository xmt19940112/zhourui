package com.zr.service.impl;

import com.zr.dao.UsersMapper;
import com.zr.model.Users;
import com.zr.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @auther zhourui
 * @date 2020/11/10 7:30 下午
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UsersMapper usersMapper;



    private final  Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public boolean login(String username, String password) {
        Users users = usersMapper.findByUsernameAndPasswd(username, password);
        System.out.println(1113);
        return users != null;

    }

    @Override
    public boolean register(String username, String password) {
        Users users = new Users();
        users.setUsername(username);
        users.setPasswd(password);
        int cnt = usersMapper.insertSelective(users);
        return cnt>0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchAdd(String username, String password) {
        Users users = new Users();
        users.setUsername(username);
        users.setPasswd(password);
         usersMapper.insertSelective(users);

             int i = 1 / 0;
        users = new Users();
        users.setUsername(username+"i");
        users.setPasswd(password);
        usersMapper.insertSelective(users);
    }
}
