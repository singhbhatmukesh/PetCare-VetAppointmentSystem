package com.petcare.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.petcare.main.config.handler.AdminFailureHandler;

@Configuration
@EnableWebSecurity
public class AdminSecurity {
	
	@Autowired
	private AdminFailureHandler adminFailureHandler;
	
	@Bean
	@Order(1)
	public SecurityFilterChain adminChain(HttpSecurity http,
						AuthenticationProvider userAuthenticationProvider) throws Exception {
		http.securityMatcher("/admin/**")
			.authenticationProvider(userAuthenticationProvider)	
		     .authorizeHttpRequests(auth->auth
		    		.requestMatchers(PublicUrls.ADMIN_PUBLIC_URLS).permitAll()
		    		.anyRequest().hasRole("ADMIN"))
		     .formLogin(login->login
		    		 .loginPage("/admin/loginPage")
		    		.loginProcessingUrl("/admin/loginAdmin")
		    		.defaultSuccessUrl("/admin/home", true)
		    		.failureHandler(adminFailureHandler)
		    		.permitAll())
		     .logout(logout->logout
		    		 .logoutUrl("/admin/logout")
		    		.logoutSuccessUrl("/admin/loginPage?logout=true")
		    		.invalidateHttpSession(true)
		    		.deleteCookies("JSESSIONID")
		    		.permitAll());
		     
		return http.build();
	}

}
