package com.petcare.main.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.petcare.main.entities.Vet;

public class CustomVetDetail implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vet vet;

	public CustomVetDetail(Vet vet) {
		super();
		this.vet = vet;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(vet.getRole());
		return Arrays.asList(authorities) ;
	}

	@Override
	public String getPassword() {
		
		return vet.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return vet.getEmail();
	}
	
	public Vet vet() {
		return vet;
	}

}
