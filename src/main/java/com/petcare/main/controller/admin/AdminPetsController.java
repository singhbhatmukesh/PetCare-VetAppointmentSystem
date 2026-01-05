package com.petcare.main.controller.admin;

import java.util.List;

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

import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminPetsController {
	
	@Autowired
	private AdminService aservice;
	
	@GetMapping("/pets")
	public String getPetspage(@RequestParam(defaultValue = "0") int page  ,Model model) {
		Page<Pet> allPets = aservice.getAllPets(page);
		model.addAttribute("pets", allPets.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", allPets.getTotalPages());
		model.addAttribute("pageSize", allPets.getSize());
		return "admin/pets/manage-pets";
	}
	
	@GetMapping("/pets/detail/{id}")
	public String getPetDetailPage(@PathVariable("id") Long id, Model model) {
		Pet pet = aservice.getPetById(id);
		model.addAttribute("pet", pet);
		return "admin/pets/pet-detail";
	}
	
	@GetMapping("/pets/edit/{id}")
	public String getEditPetPage(@PathVariable("id") Long id, Model model)
	{
		Pet pet = aservice.getPetById(id);
		model.addAttribute("pet", pet);
		return "admin/pets/edit-pet";
	}
	
	@PostMapping("/pets/edit")
	public String editPet(@ModelAttribute("pet") Pet pet, RedirectAttributes ra) {
		aservice.editPet(pet);
		ra.addFlashAttribute("success", "Pet updated Successfully");
		return "redirect:/admin/pets";
	}
	
	@GetMapping("/pets/add")
	public String addPetPage(Model model) {
		List<User> users = aservice.allUsers();
		model.addAttribute("users", users);
		model.addAttribute("pet", new Pet());
		return "admin/pets/add-pet";
	}
	
	@PostMapping("/pets/add")
	public String addPet(@ModelAttribute("pet") Pet pet, RedirectAttributes ra) {
		aservice.addPet(pet);
		ra.addFlashAttribute("success", "Pet Created Successfully");
		return "redirect:/admin/pets";
	}
	
	@GetMapping("/pets/delete/{id}")
	public String deletePet(@PathVariable("id") Long id,RedirectAttributes ra) {
		aservice.deletePet(id);
		ra.addFlashAttribute("success", "Pet Deleted Successfully");
		return "redirect:/admin/pets";
	}

}
