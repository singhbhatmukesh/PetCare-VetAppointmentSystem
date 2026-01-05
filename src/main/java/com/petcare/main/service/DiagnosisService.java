package com.petcare.main.service;

import com.petcare.main.entities.Diagnosis;
import com.petcare.main.entities.Pet;

public interface DiagnosisService {
	
	public Diagnosis getDiagnosisByPet(Pet pet);
	public Diagnosis addDiagnosis(Diagnosis diagnosis);

}
