package com.example.Main.Security.RedisService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static final String PREFIX = "blacklist:";
	
	public void blacklistToken(String jti,long expiryInmillis) {
		String key= PREFIX + jti;
		
		redisTemplate.opsForValue().set(key, "revoked",expiryInmillis,TimeUnit.MILLISECONDS);
	}
	
	public boolean isBlacklisted(String jti) {
		String key= PREFIX + jti;
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
	
	
}
