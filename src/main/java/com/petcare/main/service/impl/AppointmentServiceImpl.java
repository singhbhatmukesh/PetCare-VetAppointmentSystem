package com.petcare.main.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.entities.Vet;
import com.petcare.main.repository.AppointmentRepo;
import com.petcare.main.service.AppointmentService;
import com.petcare.main.service.PetService;
import com.petcare.main.service.VetService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	
	

	private PetService pservice;
	private VetService vservice;
	
	
	
	public AppointmentServiceImpl(PetService pservice, VetService vservice) {
		super();
		this.pservice = pservice;
		this.vservice = vservice;
	}

	@Autowired
	private AppointmentRepo arepo;

	@Override
	public List<Appointment> getAllAppointments() {
		List<Appointment> all = arepo.findAll();
		return all;
	}

	@Override
	public Appointment createAppointment(Appointment appt) {
		
		return arepo.save(appt);
	}

	@Override
	public List<Appointment> getScheduledAppointment() {
		String status="Scheduled";
		List<Appointment> byStatus = arepo.findByStatus(status);
		return byStatus;
	}

	@Override
	public List<Appointment> getApointmentHistory() {
		List<Appointment> history = arepo.findByStatusIn(List.of("Completed","Canceled"));
		return history;
	}

	@Override
	public Appointment getAppointmentById(Long id) {
		Appointment existingAppointment = arepo.findById(id).orElseThrow(
				()-> new RuntimeException("Appointment doesnot exist"));
		return existingAppointment;
	}
	

	@Override
	public void cancelAppointment(Appointment appt) {
		
		appt.setStatus("Canceled");
		arepo.save(appt);
	}
	
	@Override
	public void completeAppointment(Appointment appt) {
		appt.setStatus("Completed");
		arepo.save(appt);
		
	}

	@Override
	public void ongongAppointment(Appointment appt) {
		appt.setStatus("Ongoing");
		arepo.save(appt);
		
	}

	@Transactional
	@Override
	public void clearHistory() {
		arepo.deleteHistory();
	}

	@Override
	public void editAppointment(Appointment appt, Long petId, Long vetId) {
		appt.setPet(pservice.getPetById(petId));
		appt.setVet(vservice.getVetById(vetId));
		arepo.save(appt);
		
	}

	@Override
	public List<Appointment> getAppointmentsByUserAndStatus(User user,String status) {
		List<Appointment> byUser = arepo.findByUserAndStatus(user,status);
		
		return byUser;
	}

	@Override
	public List<Appointment> getAppointmentByVetAndStatus(Vet vet, String status) {
		List<Appointment> byVet = arepo.findByVetAndStatus(vet,status);
		return byVet==null?Collections.emptyList():byVet;
	}

	@Override
	public List<Pet> getAssignedPets(Vet vet) {
		List<Pet> assignedPets = arepo.getAssignedPets(vet);
		return assignedPets;
	}

	@Override
	public List<Appointment> getAppointmentByStatus(String status) {
		List<Appointment> byStatus = arepo.findByStatus(status);
		
		return byStatus;
	}

	

	

	

}
