package com.petcare.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcare.main.dto.PetDto;
import com.petcare.main.dtomapper.PetDtoMapper;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.service.PetService;
import com.petcare.main.service.UserService;

@Controller
@RequestMapping("/user")
public class PetController {
	
	@Autowired
	private UserService uService;
	
	@Autowired
	private PetService pService;
	
	
	@GetMapping("/pets")
	public String getMyPetsPage(Authentication auth, Model model) {
		String email = auth.getName();
		User loggedInUser = uService.getUserByEmail(email);
		List<Pet> pets = pService.getPetsByOwner(loggedInUser);
		List<PetDto> petDtoList= new ArrayList<>();
		for(Pet pet : pets) {
			PetDto pdto = PetDtoMapper.todto(pet);
			
			petDtoList.add(pdto);
		}
		model.addAttribute("pets", petDtoList);
		model.addAttribute("user", loggedInUser.getName());
		
		return "pet/my-pets-page";
	}
	
	@GetMapping("/pets/register")
	public String getPetsRegisterPage(Model model) {
		model.addAttribute("pet", new Pet());
		return "pet/pet-register-page";
	}
	
	@PostMapping("/pets/addPet")
	public String registerPet(@ModelAttribute("pet") Pet pet, RedirectAttributes redatt,Authentication auth)
	{
		String email = auth.getName();
		User userByEmail = uService.getUserByEmail(email);
		boolean petRegister = pService.registerPet(pet,userByEmail);
		if(petRegister) {
			redatt.addFlashAttribute("success", "Pet Registered Successfully");
			return "redirect:/user/pets";
		}
		else {
			redatt.addFlashAttribute("error", "Pet Registered Successfully");
			return "redirect:/user/pets";
		}
	}
	
	@GetMapping("/pets/view-details/{id}")
	public String getPetDetailPage(@PathVariable("id") Long id, Model model) {
		
		Pet pet = pService.getPetById(id);
		PetDto pdto = PetDtoMapper.todto(pet);
		
		model.addAttribute("pet", pdto);
		
		return "pet/pet-details";
	}
	
	@GetMapping("/pets/delete/{id}")
	public String deletePet(@PathVariable("id") Long id, RedirectAttributes redatt) {
		pService.deletePet(id);
		redatt.addFlashAttribute("deleted", "Pet deleted Successfully");
		return "redirect:/user/pets";
	}
	
	@GetMapping("/pets/edit/{id}")
	public String getUpdatePetPage(@PathVariable("id") Long id, Model model) {
		Pet petById = pService.getPetById(id);
		PetDto todto = PetDtoMapper.todto(petById);
		
		model.addAttribute("pet", todto);
		return "pet/edit-pet";
	}
	
	@PostMapping("/pets/update")
	public String updatePet(@ModelAttribute("pet")PetDto petdto, RedirectAttributes ra) {
		pService.updatePet(petdto);
		ra.addFlashAttribute("success", "Pet Updated Successfully");
		return "redirect:/user/pets";
	}

}
