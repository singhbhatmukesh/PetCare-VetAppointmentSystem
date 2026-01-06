package com.petcare.main.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.Diagnosis;
import com.petcare.main.entities.Medication;
import com.petcare.main.entities.MedicationStatus;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.Vet;
import com.petcare.main.repository.MedicationRepo;
import com.petcare.main.repository.VetRepo;
import com.petcare.main.service.AppointmentService;
import com.petcare.main.service.DiagnosisService;
import com.petcare.main.service.MedicationService;
import com.petcare.main.service.PetService;
import com.petcare.main.service.VetService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/vet")
public class VetController {
	
	private VetService vservice;
	private AppointmentService aservice;
	private VetRepo vrepo; 
	private MedicationService mservice;
	private DiagnosisService dservice;
	//private PetService pservice;
	//private MedicationRepo mrepo;
	
	
	public VetController(VetService vservice,
						AppointmentService aservice,
						VetRepo vrepo,
						MedicationService mservice,
						DiagnosisService dservice,
						PetService pservice,
						MedicationRepo mrepo) {
		super();
		this.vservice = vservice;
		this.aservice=aservice;
		this.vrepo=vrepo;
		this.mservice=mservice;
		this.dservice=dservice;
		//this.pservice=pservice;
		//this.mrepo=mrepo;
	}

	@GetMapping("/loginPage")
	public String getVetLoginPage(Model model) {
		model.addAttribute("vet", new Vet());
		return "vet/vet-login-page";
	}
	
	@GetMapping("/home")
	public String getVetHomePage() {
		return "vet/vet-home-page";
	}
	
	@GetMapping("/profile")
	public String getProfilePage(Authentication auth, Model model) {
		
		String email = auth.getName();
		Vet vetByEmail = vservice.getVetByEmail(email);
		model.addAttribute("vet", vetByEmail);
		model.addAttribute("scheduledAppointments", aservice.getAppointmentByVetAndStatus(vetByEmail,"Scheduled"));
		model.addAttribute("completedAppointments", aservice.getAppointmentByVetAndStatus(vetByEmail,"Completed"));
		return "vet/view-profile";
	}
	
	@GetMapping("/profile/edit/{id}")
	public String getEditProfilePage(@PathVariable("id") Long id,Model model) {
		Vet vetById = vservice.getVetById(id);
		model.addAttribute("vet", vetById);
		return "vet/edit-profile";
	}
	
	@PostMapping("/profile/edit")
	public String editProfile(@ModelAttribute Vet vet, RedirectAttributes ra) {
		vservice.editVet(vet);
		ra.addFlashAttribute("success", "Profile Edited Successfully");
		return "redirect:/vet/profile";
	}
	
	@GetMapping("/appointments")
	public String getAssignedAppointments(Authentication auth,Model model) {
		Vet vetByEmail = vservice.getVetByEmail(auth.getName());
		model.addAttribute("appointments", aservice.getAppointmentByVetAndStatus(vetByEmail,"Scheduled"));
		model.addAttribute("ongoingAppointments", aservice.getAppointmentByVetAndStatus(vetByEmail, "Ongoing"));
		return "vet/appointments/manage-appointments";
	}
	
	
	
	@GetMapping("/appointments/cancel/{id}")
	public String cancelAppointment(@PathVariable Long id,RedirectAttributes ra) {
		Appointment appointmentById = aservice.getAppointmentById(id);
		aservice.cancelAppointment(appointmentById);
		ra.addFlashAttribute("canceled", 
				"Appointment canceled for "+aservice.getAppointmentById(id).getUser().getName());
		return "redirect:/vet/appointments";
	}
	
	
	@PostMapping("/medications/save")
	public String saveMedication(@ModelAttribute Medication medication,
							     HttpServletRequest req) {
		mservice.addMedication(medication);
		
		return "redirect:"+req.getHeader("Referer");
	}
	
	@GetMapping("/pets")
	public String getAssignedPets(Authentication auth, Model model) {
		Vet vetByEmail = vservice.getVetByEmail(auth.getName());
		List<Pet> assignedPets = aservice.getAssignedPets(vetByEmail);
		model.addAttribute("pets", assignedPets);
		return "vet/pets/assigned-pets";
	}
	
	@PostMapping("/availability")
	public String setAvailability(Authentication auth, HttpServletRequest req) {
		Vet vetByEmail = vservice.getVetByEmail(auth.getName());
		vetByEmail.setIsavailable(!vetByEmail.isIsavailable());
		vrepo.save(vetByEmail);
		String referer = req.getHeader("Referer");
		return "redirect:"+referer;
	}
	
//	@GetMapping("/medications")
//	public String viewMedications(Authentication auth, Model model) {
//		List<Medication> allMedications = mservice.getAllMedications(vservice.getVetByEmail(auth.getName()).getName());
//		model.addAttribute("medications", allMedications);
//		return "vet/medications/view-medication";
//	}
	
	@GetMapping("/appointments/ongoing/{id}")
	public String setOngoingAppointment(@PathVariable Long id) {
		Appointment appointmentById = aservice.getAppointmentById(id);
		//setting appointment as ongoing
		aservice.ongongAppointment(appointmentById);
		return "redirect:/vet/appointments/treatment/"+id;
	}
	
	@GetMapping("/appointments/treatment/{id}")
	public String getOngoingAppointmentPage(@PathVariable Long id, Model model) {
		Appointment appointmentById = aservice.getAppointmentById(id);
		Diagnosis diagnosisByPet = dservice.getDiagnosisByPet(appointmentById.getPet());
		if(diagnosisByPet==null) {
			diagnosisByPet = new Diagnosis();
			diagnosisByPet.setPet(appointmentById.getPet());
		}
		List<Medication> medicationByPet = mservice.getMedicationByPet(appointmentById.getPet());
		
		Medication med = new Medication();
		med.setPet(appointmentById.getPet());
		med.setPrescribedBy(appointmentById.getVet().getName());
		med.setStatus(MedicationStatus.RUNNING);
		model.addAttribute("medications", medicationByPet);
		model.addAttribute("currentdiagnosis", diagnosisByPet);
		model.addAttribute("appointment", appointmentById);
		model.addAttribute("medication", med);
		model.addAttribute("today", LocalDate.now());
		return "vet/appointments/ongoing-appointment";
	}
	
	@PostMapping("/diagnosis/save")
	public String saveDiagnosis(@ModelAttribute("currentdiagnosis") Diagnosis currentdiagnosis, HttpServletRequest req) {
		dservice.addDiagnosis(currentdiagnosis);
		
		return "redirect:"+req.getHeader("Referer");
	}
	
	@GetMapping("/appointments/complete/{id}")
	public String completeAppointment(@PathVariable Long id, RedirectAttributes ra) {
		Appointment appointmentById = aservice.getAppointmentById(id);
		aservice.completeAppointment(appointmentById);
		ra.addFlashAttribute("completed", 
				"Appointment completed for"+aservice.getAppointmentById(id).getUser().getName());
		return "redirect:/vet/appointments";
	}
	
	@GetMapping("/medications")
	public String viewAssignedMedications(Authentication auth, Model model) {

	    Vet vet = vservice.getVetByEmail(auth.getName());

	    // completed appointments only
	    List<Appointment> completedAppointments =
	            aservice.getAppointmentByVetAndStatus(vet, "Completed");
//	    System.out.println("Vet email: " + vet.getEmail());
//	    completedAppointments.forEach(a ->
//	        System.out.println("Appointment vet: " + a.getVet().getEmail())
//	    );


	    // unique pets
	    Set<Pet> pets = completedAppointments.stream()
	            .map(Appointment::getPet)
	            .collect(Collectors.toSet());

	    model.addAttribute("pets", pets);
	    return "vet/medications/view-medication";
	}

	
	


}
