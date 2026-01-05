package com.petcare.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.main.entities.Diagnosis;
import com.petcare.main.entities.Pet;


public interface DiagnosisRepo extends JpaRepository<Diagnosis, Long> {
	Diagnosis findByPet(Pet pet);

}
