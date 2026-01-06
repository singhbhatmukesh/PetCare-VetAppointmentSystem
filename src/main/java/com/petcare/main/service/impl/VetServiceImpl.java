package com.petcare.main.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.petcare.main.entities.Vet;
import com.petcare.main.repository.VetRepo;
import com.petcare.main.service.VetService;

@Service
public class VetServiceImpl implements VetService {
	private BCryptPasswordEncoder encoder;
	
	public VetServiceImpl(BCryptPasswordEncoder encoder) {
		super();
		this.encoder = encoder;
	}

	@Autowired
	private VetRepo vrepo;
	
	
	
	@Override
	public Vet getVetByEmail(String email) {
		Vet byEmail = vrepo.findByEmail(email);
		return byEmail;
	}

	@Override
	public Page<Vet> getAllVets(int page) {
		  Pageable pageable= PageRequest.of(page, 5);
		    return vrepo.findAll(pageable);
	}

	@Override
	public Vet addVet(Vet vet) {
		String password = encoder.encode(vet.getPassword());
		vet.setRole("ROLE_VET");
		vet.setPassword(password);
		
		Vet savedVet = vrepo.save(vet);
		return savedVet;
	}

	@Override
	public Vet getVetById(Long id) {
		Vet byId = vrepo.findById(id).orElseThrow(()-> new RuntimeException("Vet not Found"));
		return byId;
	}

	@Override
	public void editVet(Vet vet) {
		Vet byId = vrepo.findById(vet.getId()).orElseThrow(
				()-> new RuntimeException("Vet not Found"));
		byId.setEmail(vet.getEmail());
		byId.setName(vet.getName());
		byId.setSpecialization(vet.getSpecialization());
		if(vet.getPassword()!=null && !vet.getPassword().isBlank()) {
			byId.setPassword(encoder.encode(vet.getPassword()));
		}
		
		vrepo.save(byId);

		
	}

	@Override
	public void deleteVet(Long id) {
		vrepo.deleteById(id);
	}

	@Override
	public List<Vet> getAvailableVets() {
		List<Vet> available = vrepo.findByIsavailable(true);
		return available;
	}

	
	
	
	
	

}
