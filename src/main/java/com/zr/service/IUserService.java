package com.zr.service;

public interface IUserService {
    boolean login(String username, String password);
    boolean register(String username, String password);
    void batchAdd(String username, String password);
}
