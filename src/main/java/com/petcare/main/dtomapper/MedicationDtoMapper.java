package com.petcare.main.dtomapper;

import com.petcare.main.dto.MedicationDto;
import com.petcare.main.entities.Medication;

public class MedicationDtoMapper {
	
	public static MedicationDto toDto(Medication medication) {
		MedicationDto dto = new MedicationDto();
		dto.setName(medication.getName());
		dto.setDosage(medication.getDosage());
		dto.setStartDate(medication.getStartDate());
		dto.setEndDate(medication.getEndDate());
		dto.setPetId(medication.getPet()!=null?medication.getPet().getId():null);
		return dto;
	}

}
