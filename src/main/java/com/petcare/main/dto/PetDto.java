package com.petcare.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
	private Long id;
	private String name;
	private String type; // e.g., "dog", "cat"
	private int age;
	private String breed;
	private String medicalHistory;
	private String gender;
	private long ownerId;

}
