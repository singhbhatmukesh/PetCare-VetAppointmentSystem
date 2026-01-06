package com.petcare.main.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.petcare.main.entities.Vet;
import com.petcare.main.service.VetService;

@Component
public class CustomVetDetailService implements UserDetailsService {
	
	private VetService vservice;
	

	public CustomVetDetailService(VetService vservice) {
		super();
		this.vservice = vservice;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Vet vetByEmail = vservice.getVetByEmail(email);
		if(vetByEmail==null) {
			throw new UsernameNotFoundException("Vet not found");
		}
		return new CustomVetDetail(vetByEmail);
	}

}
