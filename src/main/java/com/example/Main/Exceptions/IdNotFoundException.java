package com.example.Main.Exceptions;

public class IdNotFoundException extends RuntimeException{

	

	public IdNotFoundException(Long id) {
		super("Id Not Found "+id);
		// TODO Auto-generated constructor stub
	}

	public IdNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
