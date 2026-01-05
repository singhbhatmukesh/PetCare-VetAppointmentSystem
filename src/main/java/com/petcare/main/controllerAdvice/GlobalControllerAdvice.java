package com.petcare.main.controllerAdvice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.petcare.main.config.CustomVetDetail;
import com.petcare.main.entities.Vet;
import com.petcare.main.service.VetService;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@Autowired
	private VetService vs;

	@ModelAttribute("loggedVet")
	public Vet addVet(Authentication auth) {
		if(auth==null)return null;
		Object principal=auth.getPrincipal();
		if (principal instanceof CustomVetDetail vd) {
			
			return vs.getVetByEmail(vd.getUsername());
		} else {
			return null;
		}
		
	}
}
