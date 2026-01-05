package com.petcare.main.controller.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.Pet;
import com.petcare.main.service.AppointmentService;

@Controller
@RequestMapping("/admin")
public class AdminMedicationController {
	private AppointmentService aservice;
	
	
	public AdminMedicationController(AppointmentService aservice) {
		super();
		this.aservice = aservice;
	}


	@GetMapping("/medications")
	public String viewMedications(Model model)
	{
		List<Appointment> appointmentByStatus = aservice.getAppointmentByStatus("Completed");
		Set<Pet> pets= appointmentByStatus.stream()
				      .map(Appointment::getPet)
				      .collect(Collectors.toSet());
		model.addAttribute("pets", pets);
		
		return "admin/medications/view-medications";
	}

}
