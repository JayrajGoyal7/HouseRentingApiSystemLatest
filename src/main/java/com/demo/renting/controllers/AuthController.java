package com.demo.renting.controllers;

import java.util.Optional;

//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.demo.renting.entities.User;
import com.demo.renting.exceptions.ApiException;
//import com.demo.renting.exceptions.ResourceNotFoundException;
import com.demo.renting.payloads.JwtAuthRequest;
//import com.demo.renting.payloads.UserDto;
import com.demo.renting.repositories.UserRepo;
//import com.demo.renting.security.CustomUserDetailService;
import com.demo.renting.security.JwtAuthResponse;
import com.demo.renting.security.JwtTokenHelper;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge=3600, allowCredentials="true" ,allowedHeaders = "*")
@RequestMapping("/api/v1/auth/")
@Validated
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserRepo userRepo;
	
	// token generation with login
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request
			
			
	) throws Exception {

		this.authenticate(request.getUsername(), request.getPassword());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		
		// this object is only for getting the user role.
		Optional<User> opt = userRepo.findByEmail(request.getUsername());
		System.out.println(opt);
		User user = opt.get();
		//System.out.println(user.getRole());

		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setRole(user.getRole());
		response.setMessage("Login successfully");
		response.setSuccess(true);
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	// authentication of the username and password
	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid Credentials/User ....!!");
			throw new ApiException("Invalid Username or Password...!!");
		}

	}
	
	
	
	
	//Use this bean only if the cors policy is not working in the header. Cross origin anotation.
	
//	@Bean
//	public WebMvcConfigurer corsConfiguration() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/*").allowedHeaders("*").allowedOrigins("http://localhost:4200").allowedMethods("*").allowCredentials(true);
//			}
//		};
//	}

}
