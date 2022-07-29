package com.demo.renting.security;




import lombok.Data;

@Data
public class JwtAuthResponse {

	private String message;

	private boolean success;
	
	private String token;
	
	private String role;

}
