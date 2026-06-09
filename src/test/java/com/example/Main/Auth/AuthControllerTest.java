package com.example.Main.Auth;

import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Main.Service.OtpService;

//@WebMvcTest(AuthController.class)
//@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

	
	@Autowired

    private MockMvc mockMvc;

	@MockBean
    private OtpService otpService;
 
    @MockBean
    private AuthService authService;
    
    @Test
    @Disabled
    void testSendOtp() throws Exception {
    	
    	
    	
    	doNothing().when(otpService).sendOtp("test@gmail.com");
    	
    	mockMvc.perform(
    			
    			 post("/api/v1/auth/send-otp")
    			 .contentType(MediaType.APPLICATION_JSON)
    			 .content("""

                         {

                             "email":""

                         }

                         """)
    			
    			
    			
    			
    			
//    			).andExpect(status().isOk())
//    	.andExpect(content().string("OTP sent successfully"));;
    			 
    			).andExpect(status().isBadRequest());
    	
    	
    	
    	
    }
    
    
    
    
    
    
	
}
