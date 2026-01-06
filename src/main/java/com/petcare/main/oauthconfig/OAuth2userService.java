package com.petcare.main.oauthconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.User;
import com.petcare.main.repository.UserRepo;

@Service
public class OAuth2userService {
	
	@Autowired
	private UserRepo urepo;

	public void validateOAuthUser(String email, String provider) {
		
		urepo.findByEmail(email).ifPresent(user -> {

	        if ("LOCAL".equals(user.getProvider())) {

	            throw new OAuth2AuthenticationException(
	                new OAuth2Error("provider_mismatch"),
	                "User with this email already exists. Please login using email & password."
	            );
	        }
	    });
	}	
	
	public User saveOAuthUser(OAuth2User oauthUser, String provider) {
		
		String email = oauthUser.getAttribute("email");
		return urepo.findByEmail(email).orElseGet(()->{
		
				 User user = new User();
				 user.setName(oauthUser.getAttribute("name"));
				 user.setEmail(email);
				 user.setPassword(null);
				 user.setRole("ROLE_USER");
				 user.setProvider(provider.toUpperCase());
				 user.setGender(null);
				 user.setEnabled(true);
				 return urepo.save(user);
		});
		
	}
}
