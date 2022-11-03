package com.kusitms.backend.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisClient {

  private final RedisTemplate<String, String> redisTemplate;

  public void setValue(String key, String value, Long timeout) {
    ValueOperations<String, String> values = redisTemplate.opsForValue();
    values.set(key, value, Duration.ofMinutes(timeout));
  }

  public String getValue(String key) {
    ValueOperations<String, String> values = redisTemplate.opsForValue();
    return values.get(key);
  }
}
