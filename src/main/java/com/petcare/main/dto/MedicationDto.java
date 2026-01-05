package com.petcare.main.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDto {
	
	private String name;//name of the medication
	private String dosage;//eg: 2 times per day
	private LocalDate startDate;
	private LocalDate endDate;
	private long petId;

}
