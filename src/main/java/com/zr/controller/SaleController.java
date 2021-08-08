package com.zr.controller;

import com.zr.lock.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * @auther zhourui
 * @date 2021/1/24 下午9:55
 **/
@RestController
@Api(tags="用户抢票")
public class SaleController {
    private final Logger logger= LoggerFactory.getLogger(SaleController.class);
    private static long count = 30;
    private CountDownLatch countDownLatch=new CountDownLatch(5);

    @Autowired
    private RedisLock lock;


    @ApiOperation(value="窗口售票", notes="窗口售票")
    @GetMapping("/sale")
    public Long sale(){
        count = 20;
        countDownLatch=new CountDownLatch(5);
        logger.info("开售售票，一共有5个窗口开始售票");
        for(int i=0;i<5;i++){
            new Thread(()->{
                int amount=0;
                logger.info(Thread.currentThread().getName() + "开始售票");
                countDownLatch.countDown();
                if(countDownLatch.getCount()==0){
                    logger.info("售票开始");
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (count>0) {
                    try {
                        lock.lock();
                        if (count > 0) {
                            amount++;
                            count--;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                }
                logger.info(Thread.currentThread().getName() + "总共售出:"+amount);
            }).start();
        }

        return count;
    }



}
