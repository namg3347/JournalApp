package net.engineeringdigest.journalApp.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisServiceTest {
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @Disabled
    public void testRedis() {
        //redisTemplate.opsForValue().set("name", "rajat");
        Object val = redisTemplate.opsForValue().get("salary");
        int a =0;
    }
}
