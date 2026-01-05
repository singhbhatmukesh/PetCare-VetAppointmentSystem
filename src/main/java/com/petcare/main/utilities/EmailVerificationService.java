package com.petcare.main.utilities;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcare.main.customExceptions.TokenExpiredException;
import com.petcare.main.entities.User;
import com.petcare.main.entities.Verificationtoken;
import com.petcare.main.repository.UserRepo;
import com.petcare.main.repository.VerificationTokenRepo;

@Service
public class EmailVerificationService {
	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private VerificationTokenRepo vrepo;
	
		public void verifyUser(String token) throws TokenExpiredException {
			Verificationtoken byToken = vrepo.findByToken(token);
			
			if(byToken==null) {
				throw new IllegalArgumentException("Invalid Token or User already verified");
			}
			if(byToken.getExpiryDate().isBefore(LocalDateTime.now())) {
				throw new TokenExpiredException("Token Expired");
			}
			
			User user = byToken.getUser();
			user.setEnabled(true);
			urepo.save(user);
			
			byToken.setExpiryDate(null);
			byToken.setStatus("Verified");
			byToken.setToken(null);
			byToken.setUser(user);
			vrepo.save(byToken);
			
			
	}

}
