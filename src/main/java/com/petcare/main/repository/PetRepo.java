package com.petcare.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;

import java.util.List;
import java.util.Optional;


public interface PetRepo extends JpaRepository<Pet, Long> {

	List<Pet> findByOwner(User owner);
	
	Optional<Pet> findById(Long id);
	
	
	}
