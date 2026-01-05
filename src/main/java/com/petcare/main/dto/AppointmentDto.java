package com.petcare.main.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
	
	private LocalDate date; // e.g., "2023-10-15"
	private LocalTime time;
	private String status;
	private long petId;
	private long vetId;
	private long userId;
	

}
