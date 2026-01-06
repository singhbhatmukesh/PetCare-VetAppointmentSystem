package com.petcare.main.oauthconfig;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.User;

	@Service
	public class CustomOAuth2UserService extends OidcUserService {

		  @Autowired
		    private OAuth2userService oauthUserService;

		 

		    @Override
		    public OidcUser loadUser(OidcUserRequest userRequest)
		            throws OAuth2AuthenticationException {

		        OidcUser oidcUser = super.loadUser(userRequest);

		        String email = oidcUser.getEmail();
		        String provider = userRequest
		                .getClientRegistration()
		                .getRegistrationId()
		                .toUpperCase();

		        // block LOCAL users
		        oauthUserService.validateOAuthUser(email, provider);

		        // save / fetch user
		        User dbUser = oauthUserService.saveOAuthUser(oidcUser, provider);

		        Set<GrantedAuthority> authorities = new HashSet<>();
		        authorities.add(new SimpleGrantedAuthority(dbUser.getRole())); // ROLE_USER

		        return new CustomOidcUser(
		                authorities,
		                oidcUser.getIdToken(),
		                oidcUser.getUserInfo(),
		                "email",
		                dbUser
		        );
		}
	    
	    
    

}

