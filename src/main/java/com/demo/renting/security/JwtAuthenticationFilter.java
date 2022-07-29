package com.demo.renting.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper; 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException , NullPointerException{
		
		// get token form request
		String requestToken = request.getHeader("Authorization");
		
		//Bearer 14124rrf23r54gfdgher
		System.out.println(requestToken);
		
		String username= null;
		
		String token= null;
		
		if(requestToken !=null && requestToken.startsWith("Bearer")) {
			 
			token = requestToken.substring(7);
			//System.out.println(token);
			try {
				username = this.jwtTokenHelper.getUsernameFromToken(token);
			}catch(IllegalArgumentException e) {
				System.out.println("Unable to get JWT token");
			}catch(ExpiredJwtException e) {
				System.out.println("JWT token has been expired.");
			}catch(MalformedJwtException e) {
				System.out.println("Invalid JWT.");
			}catch(NullPointerException e){
				System.out.println("User is not found.");
			}
			
		}else {
			System.out.println("JWT token does not begin with Bearer.");
		}
		
		// validating the token, after getting the token.
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
			
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				
				//every things seem to be good.
				//authenticating the token
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}else {
				System.out.println("Invalid jwt token, JWT token is not valid.");
			}
			
			
			
		}else {
			System.out.println("Username is null or context holder is null.");
		}
		
		filterChain.doFilter(request, response);
		
	}

}
