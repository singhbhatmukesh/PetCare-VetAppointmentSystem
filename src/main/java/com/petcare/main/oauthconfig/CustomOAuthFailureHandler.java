package com.petcare.main.oauthconfig;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuthFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		

        String message = "oauth_login_failed";

        if (exception.getMessage() != null) {
            message = URLEncoder.encode(
                exception.getMessage(),
                StandardCharsets.UTF_8
            );
        }

        response.sendRedirect(
            "/user/loginPage?oautherror=" + message
        );
	}

}
