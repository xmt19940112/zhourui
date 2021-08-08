package com.zr.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @auther zhourui
 * @date 2020/11/10 8:44 下午
 **/
@Configuration
@ConfigurationProperties(prefix = UserImportConfig.PREFIX)
public class UserImportConfig {
    static final String PREFIX="com.zr.user.import";
    private boolean enable;
    private String synFromCorn;

    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSynFromCorn() {
        return synFromCorn;
    }

    public void setSynFromCorn(String synFromCorn) {
        this.synFromCorn = synFromCorn;
    }
}
