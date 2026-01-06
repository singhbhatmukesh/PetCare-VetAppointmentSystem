package com.petcare.main.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.User;
import com.petcare.main.resendmailservice.ResendEmailService;
import com.petcare.main.service.AppointmentService;
import com.petcare.main.service.PetService;
import com.petcare.main.service.UserService;
import com.petcare.main.service.VetService;

@Controller
@RequestMapping("/user")
public class AppointmentController {
	
	private AppointmentService aservice;
	private PetService pservice;
	private VetService vservice;
	private UserService uservice;
	//private EmailService eservice;
	private ResendEmailService rservice;
	
	
	
	public AppointmentController(AppointmentService aservice,
								PetService pservice, 
								VetService vservice,
								UserService uservice,
								ResendEmailService rservice) {
		super();
		this.aservice = aservice;
		this.pservice = pservice;
		this.vservice = vservice;
		this.uservice=uservice;
		this.rservice=rservice;
	}



	@GetMapping("/appointments")
	public String getAllAppointments(Model model, Authentication auth) {
		User userByEmail = uservice.getUserByEmail(auth.getName());
		List<Appointment> appointmentsByUserandstatus = aservice.getAppointmentsByUserAndStatus(userByEmail,"Scheduled");
		model.addAttribute("appointments", appointmentsByUserandstatus);
		
		return "user/appointments/manage-appointments";
	}
	
	@GetMapping("/appointments/schedule")
	public String getScheduleAppointmentsPage(Authentication auth, Model model) {
		User userByEmail = uservice.getUserByEmail(auth.getName());
		model.addAttribute("pets", pservice.getPetsByOwner(userByEmail));
		model.addAttribute("vets", vservice.getAvailableVets());
		model.addAttribute("appointment", new Appointment());
		return "user/appointments/schedule-appointment";
	}
	
	@PostMapping("/appointments/schedule")
	public String scheduleAppointment(Authentication auth,
									  @ModelAttribute Appointment appointment,
									  @RequestParam Long petId,
									  @RequestParam Long vetId,
									  RedirectAttributes ra) {
		User userByEmail = uservice.getUserByEmail(auth.getName());
		appointment.setStatus("Scheduled");
		appointment.setUser(userByEmail);
		appointment.setPet(pservice.getPetById(petId));
		appointment.setVet(vservice.getVetById(vetId));
		aservice.createAppointment(appointment);
		rservice.sendAppointmentNotification(appointment, userByEmail);
		ra.addFlashAttribute("success", "Appointment Scheduled");
		return "redirect:/user/appointments";
	}
	
	@GetMapping("/appointments/cancel/{id}")
	public String cancelAppointment(@PathVariable Long id,RedirectAttributes ra) {
		Appointment appointmentById = aservice.getAppointmentById(id);
		aservice.cancelAppointment(appointmentById);
		ra.addFlashAttribute("cancel", "Appointment Cancelled");
		return "redirect:/user/appointments";
	}

}
