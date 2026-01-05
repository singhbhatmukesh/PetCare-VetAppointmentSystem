package com.petcare.main.service;

import java.util.List;

import com.petcare.main.entities.Medication;
import com.petcare.main.entities.Pet;

public interface MedicationService {
	
	public Medication addMedication(Medication med);
	
	public List<Medication> getAllMedications(String vetName);
	
	public List<Medication> getMedicationByPet(Pet pet);
	public Medication getMedicationById(Long id);

}
