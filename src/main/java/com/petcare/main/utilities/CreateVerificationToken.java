package com.petcare.main.utilities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.petcare.main.entities.Verificationtoken;

@Service
public class CreateVerificationToken {

	
   public Verificationtoken createToken() {
		Verificationtoken vt = new Verificationtoken();
		vt.setStatus("Not Verified");
		vt.setToken(UUID.randomUUID().toString());
		vt.setExpiryDate(LocalDateTime.now().plusHours(24));
		
	   
	   return vt;
   }

}
