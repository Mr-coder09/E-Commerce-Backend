package com.example.Main.Service.Impl;

import java.time.Duration;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.Main.Entity.User;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Security.JwtUtil;
import com.example.Main.Service.OtpService;







@Service
public class OtpServiceImpl implements OtpService{
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired

	private UserRepository userRepository;

	@Override
	public void sendOtp(String email) {
		
		String cooldownKey = "otp_cooldown:" + email;

	    String requestCountKey = "otp_request_count:" + email;

	    String blockKey = "otp_block:" + email;
		
		
		
	    
	    if (Boolean.TRUE.equals(redisTemplate.hasKey(blockKey))) {
	    	
	    	
	    	System.out.println("....blockey....");
	    	System.out.println(redisTemplate.hasKey(blockKey));
	    	throw new BadRequestException(

	                "Too many OTP requests. Try again later.");
	    	
	    	
		}
		
	    
	    if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
			
	    	System.out.println("....cooldown....");
	    	System.out.println(redisTemplate.hasKey(cooldownKey));
	    	throw new BadRequestException(
	    		    "Please wait 60 seconds before requesting another OTP");
		}
	    
	    
	    
	    String requestCountStr = redisTemplate.opsForValue().get(requestCountKey);
	    
	    
	    System.out.println("....requestCountStr....");
    	System.out.println(requestCountStr);
    	
    	
    	int requestCount = requestCountStr == null?0:Integer.parseInt(requestCountStr);
    	
    	 System.out.println("....requestCount....");
     	System.out.println(requestCount);
	    
     	if (requestCount >= 5) {
			
     		redisTemplate.opsForValue().set(blockKey, "BLOCKED",Duration.ofHours(1));
     		throw new BadRequestException(

                    "Maximum OTP request limit reached");
		}
     	
     	
     	
     	
	    
		Random random = new Random();
		
		String otp = String.format("%06d", random.nextInt(999999));
		
		String key = "otp:" + email;
		
		redisTemplate.opsForValue().set(key, otp,Duration.ofMinutes(2));
		System.out.println("OTP : " + otp);
		
		redisTemplate.opsForValue().set(cooldownKey, "true",Duration.ofSeconds(60));
		
		
		redisTemplate.opsForValue().increment(requestCountKey);
		
		if (requestCount == 0) {
			
			redisTemplate.expire(requestCountKey, Duration.ofHours(1));
		}
		
		
	}

	@Override
	public String verifyOtp(String email, String otp) {
		
		
		
		
		String blockKey = "otp_block:" + email;

	    String attemptKey = "otp_attempts:" + email;
	    
	    
	    if (Boolean.TRUE.equals(redisTemplate.hasKey(blockKey))) {

	        throw new BadRequestException(

	                "Too many failed attempts. Try later.");

	    }
	    

		
		
		String  storedOtp = redisTemplate.opsForValue().get("otp:"+email);
		
		
		if (storedOtp == null) {

	        throw new BadRequestException(

	                "OTP expired or invalid");

	    }

	    

	    // Wrong OTP

	    if (!storedOtp.equals(otp)) {
	    	
	    	
	    	Long attempts = redisTemplate.opsForValue().increment(attemptKey);
	    	
	    	redisTemplate.expire(attemptKey, Duration.ofMinutes(10));
	    	
	    	
	    	
	    	
	    	if (attempts !=null && attempts >=5) {
				
	    		 redisTemplate.opsForValue().set(

	                     blockKey,

	                     "BLOCKED",

	                     Duration.ofMinutes(15)

	             );
	    		 
	    		 
	    		 throw new BadRequestException(

	                     "Too many wrong OTP attempts. Blocked for 15 minutes.");
	    		
			}

	        throw new BadRequestException(

	                "Invalid OTP");

	    }
		
		
		redisTemplate.delete("otp:" + email);
		redisTemplate.delete(attemptKey);

		User user = userRepository.findByEmail(email).
				orElseThrow(() -> new IdNotFoundException("Email not found"));
		
		String token = jwtUtil.generateToken(
		        user.getId(),
		        user.getEmail(),
		        user.getRole().name()
		);
		
		return token;
	}

	
}
