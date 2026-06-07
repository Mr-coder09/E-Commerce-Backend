package com.example.Main.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RecaptchaResponse {
	
	private boolean success;

//	@JsonProperty("error-Code")
	 @JsonProperty("error-codes")
  	private List<String> errorCodes;
	
	
	
}
