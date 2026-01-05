package com.petcare.main.controller.admin;

import java.util.List;

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
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.entities.Vet;
import com.petcare.main.service.AppointmentService;
import com.petcare.main.service.PetService;
import com.petcare.main.service.UserService;
import com.petcare.main.service.VetService;

@Controller
@RequestMapping("/admin")
public class AdminAppointmentController {
	
	private AppointmentService aservice;
	private UserService uservice;
	private PetService pservice;
	private VetService vservice;
	
	
	public AdminAppointmentController(AppointmentService aservice, UserService uservice,
								VetService vservice,PetService pservice) {
		super();
		this.aservice = aservice;
		this.uservice=uservice;
		this.vservice=vservice;
		this.pservice=pservice;
	}


	@GetMapping("/appointments")
	public String getManageAppointmentPage(Model model) {
		List<Appointment> allAppointments = aservice.getScheduledAppointment();
		model.addAttribute("appointments", allAppointments);
		return "admin/appointments/manage-appointments";
	}
	
	@GetMapping("/appointments/add")
	public String getCreateAppointmentPage(@RequestParam(required = false) Long userId,
									Model model) {
		model.addAttribute("appointment", new Appointment());
		model.addAttribute("users",uservice.getAllUsers());
		model.addAttribute("vets", vservice.getAvailableVets());
		
		if (userId != null) {
	        User user = uservice.getUserById(userId);
	        model.addAttribute("selectedUser", user);
	        model.addAttribute("pets", pservice.getPetsByOwner(user));
	    }
		
		return "admin/appointments/create-appointment";
	}
	
	@PostMapping("/appointments/add")
	public String addAppointment(@ModelAttribute Appointment appt,
								@RequestParam Long petId,
								@RequestParam Long vetId,
								RedirectAttributes ra) {
		Pet petById = pservice.getPetById(petId);
		appt.setPet(petById);
		appt.setUser(petById.getOwner());
		appt.setVet(vservice.getVetById(vetId));
		aservice.createAppointment(appt);
		ra.addFlashAttribute("success","Appointment Created!!" );
		return "redirect:/admin/appointments";
	}
	
	@GetMapping("/appointments/delete/{id}")
	public String cancelAppointments(@PathVariable Long id) {
		aservice.cancelAppointment(aservice.getAppointmentById(id));
		return "redirect:/admin/appointments";
	}
	
	@GetMapping("/appointments/edit/{id}")
	public String getEditAppointmentPage(@PathVariable Long id,Model model) {
		Appointment appointmentById = aservice.getAppointmentById(id);
		List<Pet> petsByOwner = pservice.getPetsByOwner(appointmentById.getUser());
		List<Vet> availableVets = vservice.getAvailableVets();
		model.addAttribute("appointment", appointmentById);
		model.addAttribute("vets", availableVets);
		model.addAttribute("pets", petsByOwner);
		return "admin/appointments/edit-appointment";
	}
	
	@PostMapping("/appointments/update/{id}")
	public String editAppointment(@ModelAttribute Appointment appt,@PathVariable Long id,
								@RequestParam Long petId,
								@RequestParam Long vetId) {
		User user = aservice.getAppointmentById(id).getUser();
		appt.setUser(user);
		aservice.editAppointment(appt, petId, vetId);
		return "redirect:/admin/appointments";
	}
	
	@GetMapping("/appointments/schedule/{petId}")
	public String setScheduleFromPetsPage(@PathVariable("petId") Long id, Model model) {
		Pet petById = pservice.getPetById(id);
		User user = petById.getOwner();
		model.addAttribute("selectedUser", user);
		model.addAttribute("selectedPet", petById);	
		model.addAttribute("pets", pservice.getPetsByOwner(user));
		model.addAttribute("users", uservice.getAllUsers());
		model.addAttribute("appointment", new Appointment());
		model.addAttribute("vets", vservice.getAvailableVets());
		return "admin/appointments/create-appointment";
	}
	
	@GetMapping("/appointments/completed")
	public String getCompletedHistory(Model model) {
		List<Appointment> appointmentByStatus = aservice.getAppointmentByStatus("Completed");
		model.addAttribute("appointments", appointmentByStatus);
		
		return "admin/appointments/appointments-history";
	}
	
	@GetMapping("/appointments/canceled")
	public String getCanceledHistory(Model model) {
		List<Appointment> appointmentByStatus = aservice.getAppointmentByStatus("Canceled");
		model.addAttribute("appointments", appointmentByStatus);
		
		return "admin/appointments/appointments-history";
	}

}
