package com.petcare.main.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;


import com.petcare.main.config.handler.FailureHandler;
import com.petcare.main.oauthconfig.CustomOAuth2UserService;
import com.petcare.main.oauthconfig.CustomOAuthFailureHandler;
import com.petcare.main.oauthconfig.CustomOauthSuccessHandler;

@Configuration
@EnableWebSecurity
public class UserSecurity {
	
	

	
	@Bean
	@Order(3)
	public SecurityFilterChain userChain(HttpSecurity http,
						AuthenticationProvider userAuthenticationProvider,
						FailureHandler failureHandler,
						CustomOAuth2UserService customOAuth2UserService,
						CustomOAuthFailureHandler authFailureHandler,
						CustomOauthSuccessHandler authSuccessHandler) throws Exception {
		
		http.securityMatcher("/user/**","/oauth2/**",
				"/login/oauth2/**")
			.authenticationProvider(userAuthenticationProvider)
		    .authorizeHttpRequests(auth->auth
		    		.requestMatchers(PublicUrls.USER_PUBLIC_URLS).permitAll()
		    		.anyRequest().hasRole("USER"))
		    .formLogin(login->login
		    		.loginPage("/user/loginPage")
		    		.loginProcessingUrl("/user/loginUser")
		    		.defaultSuccessUrl("/user/home", true)
		    		.failureHandler(failureHandler)
		    		.permitAll())
		    
		    .oauth2Login(oauth->
	 		oauth.loginPage("/user/loginPage")
	 		.userInfoEndpoint(user->user
	 				.oidcUserService(customOAuth2UserService))
	 		.failureHandler(authFailureHandler)
	 		.successHandler(authSuccessHandler))
	 		
		    
		    .logout(logout->logout
		    		.logoutUrl("/user/logout")
		    		.logoutSuccessUrl("/user/loginPage?logout=true")
		    		.invalidateHttpSession(true)
		    		.deleteCookies("JSESSIONID")
		    		.permitAll());
		   
		
		return http.build();
	}

}
