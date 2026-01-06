package com.petcare.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.Diagnosis;
import com.petcare.main.entities.Pet;
import com.petcare.main.repository.DiagnosisRepo;
import com.petcare.main.service.DiagnosisService;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {
	
	@Autowired
	private DiagnosisRepo drepo;

	@Override
	public Diagnosis getDiagnosisByPet(Pet pet) {
		Diagnosis byPet = drepo.findByPet(pet);
		return byPet;
	}

	@Override
	public Diagnosis addDiagnosis(Diagnosis diagnosis) {
		
		return drepo.save(diagnosis);
	}

}
