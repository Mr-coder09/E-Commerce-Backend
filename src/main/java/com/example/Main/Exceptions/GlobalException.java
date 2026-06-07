package com.example.Main.Exceptions;



import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice

public class GlobalException  {
	
	
//	private ErrorResponse getErrorResponse(ErrorResponse errorResponse) {
//
//		ErrorResponse errorMSg= new ErrorResponse();
//		
//		errorMSg.setStatus("Failed ");
//		errorMSg.setMessage(ex.getMessage());
//		errorMSg.setTimestamp(System.currentTimeMillis());
//		
//		return null;
//		
//		
//	}
	
	
	private static final Logger log = LoggerFactory.getLogger(GlobalException.class);
	

	
	
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleIdNotFound(
	        IdNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse error = new ErrorResponse();
	    error.setStatus(HttpStatus.NOT_FOUND.value());
	    error.setError(HttpStatus.NOT_FOUND.name());
	    error.setMessage(ex.getMessage());
	    error.setPath(request.getRequestURI());
	    error.setTimestamp(LocalDateTime.now());

	    log.warn("Business exception: {}", ex.getMessage());
	    
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	
	
	
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatEntity(DuplicateException ex
			,HttpServletRequest request){
		
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setError(HttpStatus.CONFLICT.name());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		error.setTimestamp(LocalDateTime.now());
		
		
		log.warn("Business exception: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
		
	}
	
	
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex
			,HttpServletRequest request){
		
		
		ErrorResponse error= new ErrorResponse();
		error.setStatus(HttpStatus.FORBIDDEN.value());
		error.setError(HttpStatus.FORBIDDEN.name());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		error.setTimestamp(LocalDateTime.now());
		
		log.warn("Business exception: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse>  handleBadRequest(BadRequestException ex
			,HttpServletRequest request){
		
		
		ErrorResponse error= new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setError(HttpStatus.BAD_REQUEST.name());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		error.setTimestamp(LocalDateTime.now());
		
		log.warn("Business exception: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		
		
		
		
	}
	
	
	@ExceptionHandler(OptimisticLockException.class)
	public ResponseEntity<String> handleOptimisticLock(OptimisticLockException ex) {
	    return ResponseEntity
	            .status(HttpStatus.CONFLICT)
	            .body("Resource was updated by another request. Please retry.");
	}
	
	
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException ex
			,HttpServletRequest request){
		
		ErrorResponse error= new ErrorResponse();
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		error.setTimestamp(LocalDateTime.now());
		
		log.error("Unhandled exception occurred", ex);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	
	
	
	
	
}
