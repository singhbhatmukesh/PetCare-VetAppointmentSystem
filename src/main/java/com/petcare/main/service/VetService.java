package com.petcare.main.service;
import java.util.List;

import org.springframework.data.domain.Page;
import com.petcare.main.entities.Vet;

public interface VetService {
	//for login
	public Vet getVetByEmail(String email);
	
	//for admin
	public Page<Vet> getAllVets(int page);
	public Vet addVet(Vet vet);
	public Vet getVetById(Long id);
	public void editVet(Vet vet);
	public void deleteVet(Long id);
	
	//for Appointment
	public List<Vet> getAvailableVets();
	
	
	

}
