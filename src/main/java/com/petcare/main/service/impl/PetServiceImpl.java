package com.petcare.main.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcare.main.dto.PetDto;
import com.petcare.main.dtomapper.PetDtoMapper;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.repository.PetRepo;
import com.petcare.main.service.PetService;

@Service
public class PetServiceImpl implements PetService {
	
	@Autowired
	private PetRepo petRepo;

	@Override
	public List<Pet> getPetsByOwner(User owner) {
		List<Pet> byOwner = petRepo.findByOwner(owner);
		
		return byOwner;
	}

	@Override
	public boolean registerPet(Pet pet,User user) {
		boolean petRegistered=false;
		pet.setOwner(user);
		Pet savePet = petRepo.save(pet);
		
		if(savePet==null) {
			petRegistered=false;
			return petRegistered;
		}
		petRegistered=true;
		return petRegistered;
	}


	@Override
	public Pet getPetById(Long id) {
		Pet byId = petRepo.findById(id).orElseThrow(()-> new RuntimeException("Pet not found"));
		return byId;
	}

	@Override
	public void deletePet(Long id) {
		petRepo.deleteById(id);		
	}

	@Override
	public void updatePet(PetDto petdto) {
		Pet byId = petRepo.findById(petdto.getId()).orElseThrow(()-> new RuntimeException("Pet not found"));
		Pet updateEntity = PetDtoMapper.updateEntity(petdto, byId);
		petRepo.save(updateEntity);
		
	}

}
