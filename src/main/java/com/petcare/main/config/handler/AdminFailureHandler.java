package com.petcare.main.config.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminFailureHandler implements AuthenticationFailureHandler {
	
	@Value("${admin.loginPage}")
	private String loginPage ;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
	
			String errorMessage = "Login Failed";
			
			if(exception instanceof UsernameNotFoundException) {
				errorMessage ="Admin not found!!!";
			}
			else if(exception instanceof BadCredentialsException) {
				errorMessage ="Invalid Credentials!!!";
			}
			else if(exception instanceof DisabledException) {
				errorMessage ="Your account is disabled. Please verify first.";
			}
			
			String encoded=URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
			response.sendRedirect(loginPage+"?error="+encoded);
		

		
	}

}
