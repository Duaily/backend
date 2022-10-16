package com.kusitms.backend.util;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisClient {

  private final RedisTemplate<String, String> redisTemplate;

  @Autowired
  public RedisClient(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void setValue(String key, String value, Long timeout) {
    ValueOperations<String, String> values = redisTemplate.opsForValue();
    values.set(key, value, Duration.ofMillis(timeout));
  }

  public String getValue(String key) {
    ValueOperations<String, String> values = redisTemplate.opsForValue();
    return values.get(key);
  }
}
