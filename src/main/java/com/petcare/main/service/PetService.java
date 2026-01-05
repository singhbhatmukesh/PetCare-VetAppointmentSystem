package com.petcare.main.service;

import java.util.List;

import com.petcare.main.dto.PetDto;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;

public interface PetService {
	
	public List<Pet> getPetsByOwner(User owner);
	public boolean registerPet(Pet pet,User user);
	
	public Pet getPetById(Long id);
	public void deletePet(Long id);
	
	public void updatePet(PetDto petdto);

}
