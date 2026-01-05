package com.petcare.main.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.petcare.main.entities.Vet;
import com.petcare.main.repository.VetRepo;
import com.petcare.main.service.AppointmentService;
import com.petcare.main.service.VetService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminVetsController {
	
	@Autowired
	private VetService vservice;
	
	@Autowired
	private AppointmentService apptservice;
	
	@Autowired
	private VetRepo vrepo;
	
	@GetMapping("/vets")
	public String getAllVets(@RequestParam(defaultValue = "0") int page  ,Model model) {
		Page<Vet> allVets = vservice.getAllVets(page);
		model.addAttribute("vets", allVets.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", allVets.getTotalPages());
		model.addAttribute("pageSize", allVets.getSize());
		return "admin/vets/manage-vets";
	}
	
	@GetMapping("/vets/add")
	public String getAddVetPage(Model model) {
		
		model.addAttribute("vet", new Vet());
		return "admin/vets/add-vet";
	}
	
	@PostMapping("/vets/add")
	public String addVet(@ModelAttribute("vet") Vet vet, RedirectAttributes ra) {
		vservice.addVet(vet);
		ra.addFlashAttribute("success", "New Vet Registered");
		return "redirect:/admin/vets";
	}
	
	@GetMapping("/vets/detail/{id}")
	public String getVetDetailPage(@PathVariable("id") Long id, Model model) {
		Vet vetById = vservice.getVetById(id);
		model.addAttribute("vet", vetById);
		return "admin/vets/vet-detail";
	}
	
	@GetMapping("/vets/edit/{id}")
	public String geteditVetDetailsPage(@PathVariable("id") Long id, Model model) {
		Vet vetById = vservice.getVetById(id);
		model.addAttribute("vet", vetById);
		return "admin/vets/edit-vet";
	}
	
	@PostMapping("/vets/edit")
	public String editVet(@ModelAttribute("vet") Vet vet, RedirectAttributes ra) {
		vservice.editVet(vet);
		ra.addFlashAttribute("success","Vet updated!!!");
		return "redirect:/admin/vets";
	}
	
	@GetMapping("/vets/delete/{id}")
	public String deleteVet(@PathVariable("id") Long id,RedirectAttributes ra) {
		vservice.deleteVet(id);
		ra.addFlashAttribute("success","Vet deleted!!!");
		return "redirect:/admin/vets";
	}
	
	@PostMapping("/vets/{id}/availability")
	public String setAvailability(@PathVariable Long id,
								  @RequestParam boolean isavailable) {
		Vet vetById = vservice.getVetById(id);
		vetById.setIsavailable(isavailable);
		vrepo.save(vetById); 
		return "redirect:/admin/vets";
	}
	
	@GetMapping("/vets/{id}/appointments")
	public String viewAppointmentsForVet(@PathVariable Long id, Model model) {
		
		Vet vetById = vservice.getVetById(id);
		
		model.addAttribute("vet", vetById);
		model.addAttribute("appointments", apptservice.getAppointmentByVetAndStatus(vetById, "Scheduled"));
		return "admin/vets/view-appointments-for-vet";
	}
	
	@GetMapping("/appointments/cancel/{id}")
	public String cancelAppointment(@PathVariable Long id, HttpServletRequest req) {
		Appointment appointmentById = apptservice.getAppointmentById(id);
		apptservice.cancelAppointment(appointmentById);
		String referer =req.getHeader("Referer");
		return "redirect:"+referer;
	}
}
