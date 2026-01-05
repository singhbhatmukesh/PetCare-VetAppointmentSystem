package com.petcare.main.service.impl;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.repository.AdminRepo;
import com.petcare.main.repository.PetRepo;
import com.petcare.main.repository.UserRepo;
import com.petcare.main.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private AdminRepo arepo;
	
	@Autowired
	private PetRepo prepo;
	
	@Autowired
	private UserRepo urepo;

	@Override
	public User saveAdmin(User admin) {
		if(urepo.findByEmail(admin.getEmail()).isPresent()) {
			throw new IllegalStateException("Existing_email");
		}
		if(admin.getRole()==null) {
			admin.setRole("ROLE_ADMIN");
		}
		if(admin.getProvider()==null) {
			admin.setProvider("LOCAL");
		}
		admin.setEnabled(true);
		admin.setPassword(encoder.encode(admin.getPassword()));
		return arepo.save(admin);
	}

	@Override
	public Page<Pet> getAllPets(int page) {
		 Pageable pageable = PageRequest.of(page, 5);
		    return prepo.findAll(pageable);
	}
	@Override
	public Pet getPetById(Long id) {
		Pet byId = prepo.findById(id).orElseThrow(()-> new RuntimeException("Pet Not Found"));
		
		return byId;
	}

	@Override
	public void editPet(Pet pet) {
		Pet existingpet = prepo.findById(pet.getId()).orElseThrow(()-> new RuntimeException("Pet Not Found"));
		existingpet.setAge(pet.getAge());
		existingpet.setBreed(pet.getBreed());
		existingpet.setGender(pet.getGender());
		existingpet.setMedicalHistory(pet.getMedicalHistory());
		existingpet.setName(pet.getName());
		existingpet.setType(pet.getType());
		
		prepo.save(existingpet);

		
	}

	@Override
	public List<User> allUsers() {
		List<User> allusers = urepo.findAll();
		return allusers;
	}

	@Override
	public void addPet(Pet pet) {
		prepo.save(pet);
		
	}

	@Override
	public void deletePet(Long id) {
		prepo.deleteById(id);
		
	}

	@Override
	public Optional<User> getAdminByEmail(String email) {
		Optional<User> byEmail = urepo.findByEmail(email);
		
		return byEmail;
	}

	@Override
	public User getAdminById(Long id) {
		User user = urepo.findById(id).get();
		return user;
	}

	@Override
	public void resetPassword(Long id, String newPassword, String confirmPassword) {
		User adminById = getAdminById(id);
		String currentPassword = adminById.getPassword();
		
		if(!newPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException("New and Confirm password must be same.");
		}
		if(encoder.matches(newPassword, currentPassword)) {
			throw new IllegalArgumentException("New password must be different from old one.");
			}
		adminById.setPassword(encoder.encode(newPassword));
		urepo.save(adminById);
		
	}
	
	
	

	
}
