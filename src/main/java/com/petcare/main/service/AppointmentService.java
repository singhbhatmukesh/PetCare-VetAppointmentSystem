package com.petcare.main.service;

import java.util.List;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.entities.Vet;

public interface AppointmentService {
	
	public List<Appointment> getAllAppointments();
	public Appointment createAppointment(Appointment appt);
	
	public List<Appointment> getScheduledAppointment();
	public List<Appointment> getAppointmentByStatus(String status);
	public List<Appointment> getApointmentHistory();
	
	public void cancelAppointment(Appointment appt);
	public void completeAppointment(Appointment appt);
	public void ongongAppointment(Appointment appt);
	public Appointment getAppointmentById(Long id);
	
	public void clearHistory();
	public void editAppointment(Appointment appt, Long petId, Long vetId);
	
	//public List<Appointment> getAppointmentsByUser(User user);
	public List<Appointment> getAppointmentsByUserAndStatus(User user, String status);
	
	public List<Appointment> getAppointmentByVetAndStatus(Vet vet, String status);
	
	public List<Pet> getAssignedPets(Vet vet);

}
