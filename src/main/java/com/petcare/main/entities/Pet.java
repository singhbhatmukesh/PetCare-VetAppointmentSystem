package com.petcare.main.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String type; // e.g., "dog", "cat"
	private int age;
	private String breed;
	private String medicalHistory;
	private String gender;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Medication>  medication;
	
	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Appointment> appointments;
	
	@OneToOne(mappedBy =  "pet", cascade = CascadeType.ALL,orphanRemoval = true)
	private Diagnosis diagnosis;

}
