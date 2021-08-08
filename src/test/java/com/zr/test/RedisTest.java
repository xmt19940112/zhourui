package com.zr.test;

import com.zr.ZhouruiApp;
import com.zr.model.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest(classes={ZhouruiApp.class})
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void testRedis() throws Exception {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("name", "enjoy");
        String value = ops.get("name");
        System.out.println(value);
        Long length = ops.size("name");
        System.out.println(length);

    }

    @Test
    public void testRedisIncr() throws Exception{
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("i", "9");
        ops.increment("i",2);
        System.out.println(ops.get("i"));
        redisTemplate.getConnectionFactory().getConnection().decr("i".getBytes(StandardCharsets.UTF_8) );
        System.out.println(ops.get("i"));
        ops.decrement("i");
        System.out.println(ops.get("i"));
    }

    @Test
    public void testHash() throws Exception{
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        Map<String,String> map=new HashMap<>();
        map.put("name", "zhourui");
        map.put("age", "18");
        ops.putAll("person", map);
        System.out.println(ops.entries("person"));
        ops.put("person", "id", "123");
        System.out.println(ops.entries("person"));
        Collection<Object> list=new ArrayList<>();
        list.add("name");
        list.add("age");
        System.out.println(ops.multiGet("person",list));
        List<byte[]> values = redisTemplate.getConnectionFactory().getConnection().hMGet("person".getBytes(StandardCharsets.UTF_8), "name".getBytes(StandardCharsets.UTF_8));
        for(byte[] value:values){
            System.out.println(new String(value));
        }
    }

    @Test
    public void testList(){
        ListOperations<String, String> ops = redisTemplate.opsForList();
        redisTemplate.delete("list");
        ops.leftPush("list", "node");
        List<String> nodes=new ArrayList<>();
        for(int i=0;i< 5;i++){
            nodes.add("node"+i);
        }
        ops.leftPushAll("list", nodes);
        ops.set("list", 1, "new_node");
        redisTemplate.getConnectionFactory().getConnection().lInsert("list".getBytes(StandardCharsets.UTF_8), RedisListCommands.Position.AFTER,"new_node".getBytes(),"new_node_2".getBytes(StandardCharsets.UTF_8));
        System.out.println(ops.range("list", 0, -1));

    }
    @Test
    public void testSet(){
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        redisTemplate.delete("set1");
        redisTemplate.delete("set2");
        redisTemplate.boundSetOps("set1").add("v1","v2");
        redisTemplate.boundSetOps("set2").add("v3","v4");
        Boolean member = redisTemplate.opsForSet().isMember("set1", "v1");
        System.out.println(member);
        Set<String> set1 = redisTemplate.opsForSet().members("set1");
        System.out.println(set1);
        String set2 = redisTemplate.boundSetOps("set2").pop();
        System.out.println(set2);
        System.out.println(redisTemplate.opsForSet().pop("set2"));
        ops.add("set1", "v1","v2","v3","v4");
        ops.add("set2", "v3","v4","v5","v6");
        Set<String> setList = redisTemplate.opsForSet().members("set2");
        System.out.println(setList);
        String set11 = ops.randomMember("set1");
        System.out.println(set11);
        System.out.println(ops.intersect("set1", "set2"));
        System.out.println(ops.union("set1", "set2"));
        List<String> set12 = ops.randomMembers("set1", 3);
        System.out.println(set12);
        System.out.println(ops.members("set1"));
    }

    @Test
    public void testZset(){
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        redisTemplate.delete("zset1");
        redisTemplate.delete("zset2");
        ops.add("zset1","money",1);
        ops.add("zset2","money",2);
        Set<TypedTuple<String>> zset1 = ops.rangeWithScores("zset1", 0, -1);
        printSet(zset1);
        Set<TypedTuple<String>> list1=new HashSet<>();
        Set<TypedTuple<String>> list2=new HashSet<>();
        int index=9;
        for(int i=0;i<9;i++){
            index--;
            Double score1 = Double.valueOf(i);
            String key1=i%2==0?"x"+i:"y"+i;
            Double score2 = Double.valueOf(index);
            String key2="x"+index;
            TypedTuple<String> typedTuple1 = new DefaultTypedTuple<>(key1, score1);
            list1.add(typedTuple1);
            TypedTuple<String> typedTuple2 = new DefaultTypedTuple<>(key2, score2);
            list2.add(typedTuple2);
        }
        ops.add("zset1",list1);
        ops.add("zset2",list2);
        System.out.println(ops.zCard("zset1"));
        System.out.println(ops.size("zset1"));
        Set<TypedTuple<String>> zset11 = ops.rangeWithScores("zset1", 0, -1);
        printSet(zset11);
        System.out.println(ops.add("zset1", "money1", 2));
        Long size = ops.intersectAndStore("zset1", "zset2", "zset3");
        System.out.println(size);
        Set<TypedTuple<String>> zset3 = ops.rangeWithScores("zset3", 0, -1);
        printSet(zset3);
        printSet(ops.rangeByScoreWithScores("zset4", 0, 5));
        System.out.println("......intersectAndStore max....");
        List<String> collect=new ArrayList<>();
        collect.add("zset2");
        ops.intersectAndStore("zset1",collect, "zset4", RedisZSetCommands.Aggregate.MAX);
        printSet(ops.rangeByScoreWithScores("zset4", 0, 5));
        System.out.println("......intersectAndStore max*....");
        collect.add("zset3");
        ops.intersectAndStore("zset1", collect, "zset5", RedisZSetCommands.Aggregate.MAX, RedisZSetCommands.Weights.fromSetCount(3));
        printSet(ops.rangeByScoreWithScores("zset5", 0, 5));
        RedisZSetCommands.Range range=new RedisZSetCommands.Range();
        range.gt("money");
        Set<String> set = ops.range("zset1", 0, -1);
        System.out.println(set);
        range.lt("x4");
        System.out.println(ops.rangeByLex("zset1", range));

    }

    private void printSet(Set<TypedTuple<String>> set){
        if(set==null||set.isEmpty()){
            return;
        }
        Iterator<TypedTuple<String>> iterator = set.iterator();
        while (iterator.hasNext()){
            TypedTuple<String> tuple = iterator.next();
            System.out.println("key:"+tuple.getValue()+" score:"+tuple.getScore()+"\n");
        }
    }

    @Test
    public void testMulti(){
         SessionCallback sessionCallback = new SessionCallback(){
             @Override
             public Object execute(RedisOperations ops) throws DataAccessException {
                 ops.multi();
                 ops.boundValueOps("james").set("xmt");
                 System.out.println(ops.boundValueOps("james").get());
                 ops.exec();
                 String value =(String)ops.boundValueOps("james").get();
                 return value;
             };
        };
        String value =(String) redisTemplate.execute(sessionCallback);
        System.out.println(value);

    }

    @Test
    public void testPipelinded(){
        SessionCallback sessionCallback = new SessionCallback(){
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                for(int i=0;i<300000;i++){
                    ops.boundValueOps("pipelinde_key"+i).set("pipelinde_value"+i);
                    System.out.println(ops.opsForValue().get("pipelinde_key"+i));
                }
                return null;
            };
        };
        long start = System.currentTimeMillis();
        redisTemplate.executePipelined(sessionCallback);
        System.out.println("耗时:"+(System.currentTimeMillis()-start));
    }

    @Test
    public void testLua(){
        String lua="return redis.call('get',KEYS[1])";
        Object eval = redisTemplate.getConnectionFactory().getConnection().eval("return redis.call('get',KEYS[1])".getBytes(StandardCharsets.UTF_8), ReturnType.VALUE, 1, "evalkey".getBytes(StandardCharsets.UTF_8));
        System.out.println(eval);
        //RedisScript<String> script = RedisScript.of(lua);
        DefaultRedisScript<String> script = new DefaultRedisScript<>(lua,String.class);
        String evalkey = redisTemplate.execute(script, Collections.singletonList("evalkey"));
        System.out.println(evalkey);


    }

    @Test
    public  void testLua2(){
        String lua="redis.call('set',KEYS[1],ARGV[1]) return redis.call('get',KEYS[1])";
        //设置默认脚本封装类
        DefaultRedisScript<Users> script = new DefaultRedisScript<>();
        script.setScriptText(lua);
        List<String> keys=new ArrayList<>();
        keys.add("user1");
        Users users = new Users();
        users.setId(1);
        users.setUsername("xmt");
        users.setPasswd("123");
        script.setResultType(Users.class);
        RedisSerializer serializer = new JdkSerializationRedisSerializer();
        Users u = (Users) redisTemplate.execute(script, serializer,serializer, keys, users);

        System.out.println(u);
    }


}
