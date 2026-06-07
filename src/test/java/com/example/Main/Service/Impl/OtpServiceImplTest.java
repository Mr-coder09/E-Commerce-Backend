package com.example.Main.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.example.Main.Entity.User;
import com.example.Main.Enum.Role;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Security.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class OtpServiceImplTest {
	
	@Mock
	private RedisTemplate<String, String> redisTemplate;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private OtpServiceImpl otpServiceImpl;
	
	@Test
	void testSendOtp() {

	    String email = "test@gmail.com";
	    
	    ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
	    
	    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
	    
	    System.out.println("valueOperations"+valueOperations);
	    

	    otpServiceImpl.sendOtp(email);
	    
	    verify(valueOperations,atLeastOnce()).set(anyString(), anyString(),any());
	    
	    
	    

	}
	
	@Test
	void testVerifyOtpSuccess() {
		 String email = "test@gmail.com";

		    String otp = "123456";

		    String token = "jwt-token";
		    
		    String wrongOtp = "999999";
		    
		    ValueOperations<String, String> valueOperations =

		            mock(ValueOperations.class);

		    when(redisTemplate.opsForValue())

		            .thenReturn(valueOperations);
		
		    when(valueOperations.get("otp:" + email))

            .thenReturn(otp);
		    
		    
		    User user = new User();

		    user.setId(1L);

		    user.setEmail(email);
		    
		    user.setRole(Role.ADMIN);
		    // Mock repository

		    when(userRepository.findByEmail(email))

		            .thenReturn(Optional.of(user));

		    // Mock JWT generation

		    when(jwtUtil.generateToken(

		            anyLong(),

		            anyString(),

		            anyString()))

		            .thenReturn(token);

		   // Actual method call

		    String result =

		            otpServiceImpl.verifyOtp(email, otp);

		    // Assertion

		    assertEquals(token, result);
		    
		    
		    
		    verify(redisTemplate)
	        .delete("otp:" + email);
		    
//		    verify(jwtUtil , never()).generateToken(anyLong(), anyString(), anyString());
		  

		    
		    
//		    assertThrows(
//
//		            BadRequestException.class,
//
//		            () -> otpServiceImpl.verifyOtp(
//
//		                    email,
//
//		                    wrongOtp
//
//		            )
//
//		    );
		    
		    
	}
	
	@Test

    void testVerifyOtpWrongOtp() {

        String email = "test@gmail.com";

        String storedOtp = "123456";

        String wrongOtp = "999999";

        // Mock Redis ValueOperations

        ValueOperations<String, String> valueOperations =

                mock(ValueOperations.class);

        when(redisTemplate.opsForValue())

                .thenReturn(valueOperations);

        // Mock stored OTP

        when(valueOperations.get("otp:" + email))

                .thenReturn(storedOtp);

        // Exception test

        assertThrows(

                BadRequestException.class,

                () -> otpServiceImpl.verifyOtp(

                        email,

                        wrongOtp

                )

        );

        // Verify JWT never generated

        verify(jwtUtil, never())

                .generateToken(

                        anyLong(),

                        anyString(),

                        anyString()

                );

    

}
	
	
	
	
	
	
	
	
	
	
	
}


