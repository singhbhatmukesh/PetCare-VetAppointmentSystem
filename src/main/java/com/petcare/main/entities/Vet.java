package com.petcare.main.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String specialization; // e.g., "small animals", "exotic pets"
	private String email;
	private String password;
	private String role;
	private boolean isavailable;
	
	@OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Appointment> appointments;

}
