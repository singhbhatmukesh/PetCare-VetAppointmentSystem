package com.petcare.main.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Medication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;//name of the medication
	private String dosage;//eg: 2 times per day
	private LocalDate startDate;
	private LocalDate endDate;
	private String prescribedBy;
	private MedicationStatus status;
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

}
