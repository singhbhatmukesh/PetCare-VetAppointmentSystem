package com.petcare.main.dtomapper;

import com.petcare.main.dto.VetDto;
import com.petcare.main.entities.Vet;

public class VetDtoMapper {
	
	public static VetDto toDto(Vet vet) {
		VetDto vdto = new VetDto();
		vdto.setName(vet.getName());
		vdto.setSpecialization(vet.getSpecialization());
		vdto.setEmail(vet.getEmail());
		return vdto;
	}

}
