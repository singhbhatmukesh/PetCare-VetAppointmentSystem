package com.petcare.main.oauthconfig;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.petcare.main.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private OAuth2userService auth2userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		   OAuth2AuthenticationToken token =
	                (OAuth2AuthenticationToken) authentication;

	        Collection<? extends GrantedAuthority> authorities =
	                token.getAuthorities();

	        // 🔍 Debug (remove later)
	        System.out.println("OAuth Authorities: " + authorities);
	        System.out.println("AUTHORITIES = " + authentication.getAuthorities());


	        // 🔁 Redirect based on role
	        if (hasRole(authorities, "ROLE_ADMIN")) {
	            response.sendRedirect("/admin/dashboard");
	        }
	        else if (hasRole(authorities, "ROLE_VET")) {
	            response.sendRedirect("/vet/home");
	        }
	        else {
	            // default ROLE_USER
	            response.sendRedirect("/user/home");
	        }
	    }

	    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
	        return authorities.stream()
	                .anyMatch(auth -> auth.getAuthority().equals(role));
		
	}

}
