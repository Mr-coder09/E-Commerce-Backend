package com.example.Main.Service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Main.DTO.RecaptchaResponse;
import com.example.Main.Service.CaptchaService;

@Service
public class CaptchaServiceImpl implements CaptchaService{
	
	@Value("${google.recaptcha.secret}")
	private String secretKey;
	
	@Value("${google.recaptcha.verify-url}")
	private String verifyUrl;

	@Override
	public boolean validateCaptcha(String captchaResponse) {
		
		
		String url = verifyUrl
					+ "?secret=" +secretKey
					+ "&response=" + captchaResponse;
		
		RestTemplate restTemplate = new RestTemplate();
		
		
		ResponseEntity<RecaptchaResponse> response = 
				restTemplate.postForEntity(url,null,RecaptchaResponse.class);
		
		
		RecaptchaResponse body = response.getBody();
		
		System.out.println(response.getBody());
		System.out.println(".........");
		System.out.println(response);
		
		System.out.println(body.getErrorCodes());
		
		
		return body!=null && body.isSuccess();
	}

}
