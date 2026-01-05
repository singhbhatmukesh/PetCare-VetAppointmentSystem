package com.petcare.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.Medication;
import com.petcare.main.entities.Pet;
import com.petcare.main.repository.MedicationRepo;
import com.petcare.main.service.MedicationService;

@Service
public class MedicationServiceImpl implements MedicationService {
	
	@Autowired
	private MedicationRepo mrepo;

	@Override
	public Medication addMedication(Medication med) {
		
		return mrepo.save(med);
	}

	@Override
	public List<Medication> getAllMedications(String vetName) {
		List<Medication> byPrescribedBy = mrepo.findByPrescribedBy(vetName);
		return byPrescribedBy;
	}

	@Override
	public List<Medication> getMedicationByPet(Pet pet) {
		List<Medication> byPet = mrepo.findByPet(pet);

		return byPet;
	}

	@Override
	public Medication getMedicationById(Long id) {
		 Medication medication = mrepo.findById(id).orElseThrow(()->
				 						new RuntimeException("No Medication Found"));
		return medication;
	}
	
	

}
