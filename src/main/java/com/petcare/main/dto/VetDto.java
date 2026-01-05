package com.petcare.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VetDto {
	
	private String name;
	private String specialization; // e.g., "small animals", "exotic pets"
	private String email;

}
