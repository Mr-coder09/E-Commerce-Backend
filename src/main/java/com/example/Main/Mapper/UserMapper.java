package com.example.Main.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.Main.DTO.UserRegisterRequest;
import com.example.Main.DTO.UserResponse;
import com.example.Main.Entity.User;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		imports = LocalDateTime.class
		
		)
public interface UserMapper {

	
	@Mapping(target = "id" , ignore = true)
	@Mapping(target = "password" , ignore = true)
	@Mapping(target = "role" , constant = "CUSTOMER")
	@Mapping(target = "createdAt" ,expression = "java(LocalDateTime.now())")
	@Mapping(target = "updatedAt" ,expression = "java(LocalDateTime.now())")
	User toEntity(UserRegisterRequest request);
	
	
	UserResponse toResponse(User user);
}
