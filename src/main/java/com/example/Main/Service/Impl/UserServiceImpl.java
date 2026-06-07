package com.example.Main.Service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.DTO.UserRegisterRequest;
import com.example.Main.DTO.UserResponse;
import com.example.Main.Entity.User;
import com.example.Main.Enum.Role;
import com.example.Main.Exceptions.DuplicateException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Mapper.UserMapper;
import com.example.Main.Repository.UserRepository;

import com.example.Main.Service.AuditService;
import com.example.Main.Service.UserService;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
//	@Autowired
//	JwtUtil jwtUtil;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	UserMapper userMapper;

//	@Autowired
//	RefreshTokenService refreshTokenService;
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public UserResponse regsiterUser(UserRegisterRequest userRegisterRequest) {

		if (userRepository.existsByEmail(userRegisterRequest.getEmail())
				|| userRepository.existsByMobileNo(userRegisterRequest.getMobileNo())) {
			throw new DuplicateException("Dupliacte Email or mobileNo");
		}

//		User user = new User();
//		user.setName(userRegisterRequest.getName());
//		user.setEmail(userRegisterRequest.getEmail());
//		user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
//		user.setGender(userRegisterRequest.getGender());
//		user.setMobileNo(userRegisterRequest.getMobileNo());
//		user.setRole(Role.CUSTOMER);
//		user.setCreatedAt(LocalDateTime.now());
//		user.setUpdatedAt(LocalDateTime.now());
		
		User user = userMapper.toEntity(userRegisterRequest);
		user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
		
		
		
		
		
		
		
		User savedUser = userRepository.save(user);

//		UserResponse response = new UserResponse();
//
//		response.setName(savedUser.getName());
//		response.setEmail(savedUser.getEmail());
//		response.setGender(savedUser.getGender());
//		response.setMobileNo(savedUser.getMobileNo());
//		response.setId(savedUser.getId());
		
		
//		UserResponse response = mapToResponse(savedUser);
		UserResponse response = userMapper.toResponse(savedUser);
		
		log.info("User registered successfully: email={}", user.getEmail());
		
		auditService.log(
			    savedUser.getId(),
			    savedUser.getEmail(),
			    savedUser.getRole().name(),
			    "REGISTER",
			    "USER",
			    savedUser.getId(),
			    "SUCCESS",
			    "User registered successfully"
			);

		return response;

	}

	
//	@Transactional
//	@Override
//	public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {
//
//
//		
//		User user = userRepository.findByEmail(userLoginRequest.getEmail())
//	            .orElseThrow(() -> {
//	                log.warn("Login failed: email not found [{}]", userLoginRequest.getEmail());
//	                auditService.log(
//	                	    null,
//	                	    userLoginRequest.getEmail(),
//	                	    null,
//	                	    "LOGIN",
//	                	    "USER",
//	                	    null,
//	                	    "FAILED",
//	                	    "Email not found"
//	                	);
//	                return new UnauthorizedAccessException("Invalid email");
//	            });
//
//		if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
//			
//			
//			log.warn("Login failed: invalid password for email [{}]", userLoginRequest.getEmail());
//			
//			auditService.log(
//				    user.getId(),
//				    user.getEmail(),
//				    user.getRole().name(),
//				    "LOGIN",
//				    "USER",
//				    user.getId(),
//				    "FAILED",
//				    "Invalid password"
//				);
//			
////			throw new RuntimeException("Invalid Password ");
//			throw new UnauthorizedAccessException("Invalid email or password");
//		}
//
//		
//		Long id = user.getId();
//		String email=user.getEmail();
//		String role= user.getRole().name();
//		
//		String accessToken = jwtUtil.generateToken(id, email, role);
//		RefreshToken rt = refreshTokenService.createRefreshToken(id);
//		
//		String refreshToken = rt.getToken();
//		
////		UserResponse response = new UserResponse();
////		response.setName(user.getName());
////		response.setEmail(user.getEmail());
////		response.setGender(user.getGender());
////		response.setMobileNo(user.getMobileNo());
//		
//		UserResponse response = mapToResponse(user);
//		
//		UserLoginResponse userLoginResponse = new UserLoginResponse();
//		userLoginResponse.setAccessToken(accessToken);
//		userLoginResponse.setRefreshToken(refreshToken);
//		userLoginResponse.setUser(response);
//		
//		
//		log.info("User login successful: email={}", user.getEmail());
//		
//		auditService.log(
//			    user.getId(),
//			    user.getEmail(),
//			    user.getRole().name(),
//			    "LOGIN",
//			    "USER",
//			    user.getId(),
//			    "SUCCESS",
//			    "User logged in successfully"
//			);
//
//		return userLoginResponse;
//	}

	@Override
	public UserResponse getMyProfile(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new IdNotFoundException("Email not found"));

//		UserResponse response = new UserResponse();
//
//		response.setName(user.getName());
//		response.setEmail(user.getEmail());
//		response.setGender(user.getGender());
//		response.setMobileNo(user.getMobileNo());
//		response.setId(user.getId());

//		UserResponse response = mapToResponse(user);
		UserResponse response = userMapper.toResponse(user);
		return response;

	}

	@Transactional
	@Override
	public UserResponse updateMyProfile(UserRegisterRequest userRegisterRequest, Long id, String email) {

		User authenticatedUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new IdNotFoundException("Email not found"));

		User userToUpdate;

		if (authenticatedUser.getRole() == Role.CUSTOMER) {

			if (!authenticatedUser.getId().equals(id)) {
				throw new UnauthorizedAccessException("Access Denied");

			}

			userToUpdate = authenticatedUser;

		}

		else {
			User targetUser = userRepository.findById(id).orElseThrow(() -> new IdNotFoundException("User not found"));

			userToUpdate = targetUser;

		}

		String newEmailString = userRegisterRequest.getEmail();


		if (newEmailString != null && !newEmailString.equals(userToUpdate.getEmail())) {

			Optional<User> existingUserEmail = userRepository.findByEmail(newEmailString);

			if (existingUserEmail.isPresent()) {

				throw new DuplicateException("Email Already Exists ");
			}

			userToUpdate.setEmail(newEmailString);

		}


		if (userRegisterRequest.getMobileNo() != null &&
			    !userRegisterRequest.getMobileNo().equals(userToUpdate.getMobileNo())) {

			if (userRepository.existsByMobileNo(userRegisterRequest.getMobileNo())) {
				throw new DuplicateException("Mobile No Already Exists");
			}

			userToUpdate.setMobileNo(userRegisterRequest.getMobileNo());

		}

		if (authenticatedUser.getRole() == Role.CUSTOMER
		        && userRegisterRequest.getPassword() != null) {
		    userToUpdate.setPassword(
		        passwordEncoder.encode(userRegisterRequest.getPassword())
		    );
		}

		userToUpdate.setName(userRegisterRequest.getName());

		userToUpdate.setGender(userRegisterRequest.getGender());

		userToUpdate.setUpdatedAt(LocalDateTime.now());

		
		
		User userSaved = userRepository.save(userToUpdate);

//		UserResponse response = mapToResponse(userSaved);
		UserResponse response = userMapper.toResponse(userSaved);
		
		
		
		auditService.log(
			    authenticatedUser.getId(),
			    authenticatedUser.getEmail(),
			    authenticatedUser.getRole().name(),
			    "UPDATE_PROFILE",
			    "USER",
			    userSaved.getId(),
			    "SUCCESS",
			    "User profile updated"
			);

		return response;

	}

//	@Override
//	public List<UserResponse> getAllProfiles(String email) {
//
//		User userData = userRepository.findByEmail(email).orElseThrow(() -> new IdNotFoundException("Email not found"));
//
//		if (userData.getRole() == Role.ADMIN) {
//
//			List<User> users = userRepository.findAll();
//
//			List<UserResponse> responseList = new ArrayList<UserResponse>();
//			for (User user : users) {
//
////				UserResponse response = new UserResponse();
////
////				response.setId(user.getId());
////				response.setName(user.getName());
////				response.setEmail(user.getEmail());
////				response.setGender(user.getGender());
////
////				response.setMobileNo(user.getMobileNo());
//				
//				UserResponse response = userMapper.toResponse(user);
//				
//				
//				responseList.add(response);
//
//			}
//			
//			
//			auditService.log(
//				    userData.getId(),
//				    userData.getEmail(),
//				    userData.getRole().name(),
//				    "VIEW_ALL_USERS",
//				    "USER",
//				    null,
//				    "SUCCESS",
//				    "Admin fetched all users"
//				);
//			return responseList;
//			
//			
//		}
//
//		else {
//			throw new UnauthorizedAccessException("Access Denied");
//		}
//
//	}

	@Transactional
	@Override
	public void delteProfile(Long id, String email) {

		User userData = userRepository.findByEmail(email).orElseThrow(() -> new IdNotFoundException("Email not found"));

		User targetUser = userRepository.findById(id)
		        .orElseThrow(() -> new IdNotFoundException("User not found"));
		
		
		
		if (userData.getRole() == Role.ADMIN) {
			
			if (!userRepository.existsById(id)) {
			    throw new IdNotFoundException("User not found");
			}
			
			userRepository.deleteById(id);
			
			

//			targetUser.setDeleted(true);
//			targetUser.setDeletedAt(LocalDateTime.now());
//			userRepository.save(targetUser);
			
			
		
			
			
			auditService.log(
				    userData.getId(),
				    userData.getEmail(),
				    userData.getRole().name(),
				    "DELETE_USER",
				    "USER",
				    id,
				    "SUCCESS",
				    "Admin deleted user account"
				);
			return ;
		}

		else {
			if (userData.getId().equals(id)) {
				
				
				userRepository.deleteById(id);
				
//
//				targetUser.setDeleted(true);
//				targetUser.setDeletedAt(LocalDateTime.now());
//				userRepository.save(targetUser);
				
				
			
				auditService.log(
					    userData.getId(),
					    userData.getEmail(),
					    userData.getRole().name(),
					    "DELETE_USER",
					    "USER",
					    id,
					    "SUCCESS",
					    "User deleted own account"
					);
			} else {
				throw new UnauthorizedAccessException("Access denied");
			}
		}

	}

	
//	private UserResponse mapToResponse(User user) {
//		
//		UserResponse response = new UserResponse();
//
//		response.setName(user.getName());
//		response.setEmail(user.getEmail());
//		response.setGender(user.getGender());
//		response.setMobileNo(user.getMobileNo());
//		response.setId(user.getId());
//		return response;
//		
//		
//	}
	
	
	
	
}
