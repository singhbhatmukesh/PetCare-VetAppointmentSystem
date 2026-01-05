package com.petcare.main.oauthconfig;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.petcare.main.entities.User;

public class CustomOidcUser extends DefaultOidcUser
 {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final User user;

public CustomOidcUser(
    Collection<? extends GrantedAuthority> authorities,
    OidcIdToken idToken,
    OidcUserInfo userInfo,
    String nameAttributeKey,
    User user) {

super(authorities, idToken, userInfo, nameAttributeKey);
this.user = user;
}


public String getDisplayName() {
return user.getName();
}
}
