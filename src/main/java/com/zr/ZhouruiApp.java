package com.zr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zr.dao")
public class ZhouruiApp {

    public static void main(String[] args) {
        SpringApplication.run(ZhouruiApp.class, args);
    }
}
