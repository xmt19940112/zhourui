package com.zr.lock;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @auther zhourui
 * @date 2021/1/23 下午3:54
 **/
@Service
public class RedisLock implements Lock {
    private static final String KEY="ZHOURUI_KEY";
    private ThreadLocal<String> local=new ThreadLocal<>();
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public void lock() {
        if(tryLock()){
               return;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        String value= UUID.randomUUID().toString();
        //Jedis jedis=(Jedis)jedisConnectionFactory.getConnection().getNativeConnection();
        //String result = jedis.set(KEY, value, "NX", "PX", 1000);
//        String result = getJedis().set(KEY, value, "NX", "PX", 1000);
//        if("OK".equals(result)){
//            local.set(value);
//            return true;
//        }
//        return false;
        Boolean result = redisTemplate.opsForValue().setIfAbsent(KEY, value, 1, TimeUnit.SECONDS);
        if(result){

            local.set(value);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        return false;
    }

    @Override
    public void unlock() {
        String value = redisTemplate.opsForValue().get(KEY);
        if(value.equals(local.get())){
            redisTemplate.delete(KEY);
        }
//        String script = FileUtils.getScript("unlock.lua");
//        Jedis jedis=getJedis();
//        jedis.eval(script, Arrays.asList(KEY),Arrays.asList(local.get()));
    }

    @Override
    public Condition newCondition() {
        return null;
    }

//    private Jedis getJedis(){
//        Jedis jedis=(Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
//        return jedis;
//    }
}
