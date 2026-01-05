package com.petcare.main.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String email;
	private String password;
	private String role; // e.g., "pet_owner", "admin"
	private String provider;
	private String gender;
	private boolean enabled;
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
	private Verificationtoken token;
	
	@OneToMany(mappedBy = "owner",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pet> pet;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Appointment> appointments;
 }
