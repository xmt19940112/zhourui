package com.zr.config;

import com.zr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @auther zhourui
 * @date 2020/11/10 8:48 下午
 **/
@Component
@Configuration
@EnableScheduling
public class UserImportScheduleTask implements SchedulingConfigurer {

    @Autowired
    UserImportConfig userImportConfig;

    @Autowired
    private IUserService iUserService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if(userImportConfig.isEnable()){
            CronTrigger cronTrigger = new CronTrigger(userImportConfig.getSynFromCorn());
            taskRegistrar.addTriggerTask(()->iUserService.login("jack","123")
            ,cronTrigger);
        }
    }
}
