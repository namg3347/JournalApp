package net.engineeringdigest.journalApp.services;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            log.error("Exception", e);
            return null;
        }

    }


    public void set(String key, Object o, Long ttl) {
        try {
            String json = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,json, ttl,TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception", e);
        }

    }
}
