package com.petcare.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.main.entities.Medication;
import com.petcare.main.entities.Pet;

import java.util.List;


public interface MedicationRepo extends JpaRepository<Medication, Long> {
	
	List<Medication> findByPrescribedBy(String prescribedBy);
	
	List<Medication> findByPet(Pet pet);

}
