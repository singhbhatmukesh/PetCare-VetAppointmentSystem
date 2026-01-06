package com.petcare.main.dtomapper;

import com.petcare.main.dto.PetDto;
import com.petcare.main.entities.Pet;

public class PetDtoMapper {
	


	public static PetDto todto(Pet pet) {
		PetDto dto = new PetDto();
		dto.setId(pet.getId());
		dto.setName(pet.getName());
		dto.setType(pet.getType());
		dto.setAge(pet.getAge());
		dto.setBreed(pet.getBreed());
		dto.setMedicalHistory(pet.getMedicalHistory());
		dto.setGender(pet.getGender());
		dto.setOwnerId(pet.getOwner()!=null ?pet.getOwner().getId():null);
		
		return dto;
	}
	
	public static Pet updateEntity(PetDto dto, Pet pet) {
		   

		    // update allowed fields
		    pet.setName(dto.getName());
		    pet.setType(dto.getType());
		    pet.setAge(dto.getAge());
		    pet.setBreed(dto.getBreed());
		    pet.setMedicalHistory(dto.getMedicalHistory());
		    pet.setGender(dto.getGender());
		    
		    return pet;

	}
}
