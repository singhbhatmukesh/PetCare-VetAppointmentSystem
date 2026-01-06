package com.petcare.main.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	
	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Autowired
	private CustomVetDetailService vetDetailService;
	
	
	@Bean
	public AuthenticationProvider userAuthenticationProvider(BCryptPasswordEncoder passwordEncoder ) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setHideUserNotFoundExceptions(false);
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationProvider vetAuthenticationProvider(BCryptPasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(vetDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setHideUserNotFoundExceptions(false);
		return authenticationProvider;
	}
	
	@Bean
	@Order(4)
	public SecurityFilterChain securityFilterChain(HttpSecurity http
			) throws Exception {
		
		http
		.authorizeHttpRequests(auth->auth
				.requestMatchers(PublicUrls.HOME_PUBLIC_URLS).permitAll()
				.anyRequest().authenticated()
				);
		
		
		
		return http.build();
	}
	


}
