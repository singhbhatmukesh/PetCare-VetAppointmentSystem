package com.petcare.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.petcare.main.config.handler.VetFailureHandler;

@Configuration
@EnableWebSecurity
public class VetSecurity {

	@Autowired
	private VetFailureHandler failureHandler;
	
	@Bean
	@Order(2)
	public SecurityFilterChain vetChain(HttpSecurity http,
									AuthenticationProvider vetAuthenticationProvider) throws Exception {
		
		http.securityMatcher("/vet/**")
			.authenticationProvider(vetAuthenticationProvider)
			.authorizeHttpRequests(auth->auth
									.requestMatchers(PublicUrls.VET_PUBLIC_URLS).permitAll()
									.anyRequest().hasRole("VET"))
			.formLogin(login->login
						.loginPage("/vet/loginPage")
						.loginProcessingUrl("/vet/loginVet")
						.defaultSuccessUrl("/vet/home", true)
						.failureHandler(failureHandler)
						.permitAll())
			.logout(logout->logout
		    		.logoutUrl("/vet/logout")
		    		.logoutSuccessUrl("/vet/loginPage?logout=true")
		    		.invalidateHttpSession(true)
		    		.deleteCookies("JSESSIONID")
		    		.permitAll());
		
		return http.build();
	}

}
