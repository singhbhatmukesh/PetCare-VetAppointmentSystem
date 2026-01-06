package com.petcare.main.controller;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcare.main.config.CustomUserDetail;
import com.petcare.main.dto.UserDto;
import com.petcare.main.dtomapper.UserDtoMapper;
import com.petcare.main.entities.Medication;
import com.petcare.main.entities.MedicationStatus;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.repository.MedicationRepo;
import com.petcare.main.repository.UserRepo;
import com.petcare.main.resendmailservice.ResendEmailService;
import com.petcare.main.service.MedicationService;
import com.petcare.main.service.PetService;
import com.petcare.main.service.UserService;
import com.petcare.main.utilities.EmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//private EmailService eservice;
	private UserRepo urepo;
	private MedicationRepo mrepo;
	private MedicationService mservice;
	private UserService uService;
	private PetService pservice;
	private ResendEmailService rservice;
	
	
	
	

	public UserController(EmailService eservice, UserRepo urepo, MedicationRepo mrepo, MedicationService mservice,
			UserService uService, PetService pservice, ResendEmailService rservice) {
		super();
		//this.eservice = eservice;
		this.urepo = urepo;
		this.mrepo = mrepo;
		this.mservice = mservice;
		this.uService = uService;
		this.pservice = pservice;
		this.rservice = rservice;
	}

	@GetMapping("/registerPage")
	public String getRegisterPage(Model	model) {
		//checks if user object is present or not OR first time registering than new User()
		//redirected due to error than existing user object with entered fields
		if(!model.containsAttribute("user")) {
		model.addAttribute("user", new User());
		}
		return "user/user-register-page";
	}
	
	@PostMapping("/registerUser")
	public String registerUser(@ModelAttribute("user") User user, 
										RedirectAttributes redatt) throws MessagingException 
										
	{
		try {
		uService.saveUser(user);
		redatt.addFlashAttribute("success", "User Registered Successfully!");
		return "redirect:/user/registerPage";
		}
		catch (IllegalStateException e) {
			redatt.addFlashAttribute("user",user);
			if("Existing_email".equals(e.getMessage())) {
				
				redatt.addFlashAttribute("existingEmail", "Email alredy exists!!Please use another one.");
			}
			else {
				redatt.addFlashAttribute("error", "Error!!!please try again");
			}
		}
		return "redirect:/user/registerPage";
		
	}
	@GetMapping("/loginPage")
	public String getUserLoginpage(Model model)
	{
		model.addAttribute("user", new User());
		return "user/user-login-page";
	}
	
	@GetMapping("/home")
	public String getHomePage(Authentication auth, Model model) {
		String email = auth.getName();
		User loggedInUser = urepo.findByEmail(email).orElseThrow(()->
												new RuntimeException("User doesnot exist"));;
		UserDto userdto = UserDtoMapper.toDto(loggedInUser);
		model.addAttribute("user", userdto);
		return "user/user-home";
	}
	
	@GetMapping("/profile")
	public String getUserProfilePage(Authentication auth, Model model) {
		String email = auth.getName();
		User userByEmail = uService.getUserByEmail(email);
		model.addAttribute("user", userByEmail);
		return "user/user-profile";
	}
	
	@GetMapping("/profile/edit")
	public String GetUserProfileEditPage(Authentication auth,Model model) {
		String email = auth.getName();
		User userByEmail = uService.getUserByEmail(email);
		model.addAttribute("user", userByEmail);
		return "user/user-editProfile";
	}
	
	@PostMapping("/profile/edit")
	public String editProfile(@ModelAttribute User user,Authentication auth, RedirectAttributes ra) {
		String newPassword=null;
		uService.updateUser(user, newPassword);
		User userById = uService.getUserById(user.getId());
		CustomUserDetail updatedUserDetail = new CustomUserDetail(userById);
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(updatedUserDetail, 
														auth.getCredentials(), 
														updatedUserDetail.getAuthorities());
		//to show changed info(like name) in current session
		SecurityContextHolder.getContext().setAuthentication(token);
		ra.addFlashAttribute("success","Profile Updated");
		return "redirect:/user/profile";
	}
	
	@GetMapping("/profile/change-password")
	public String getChangePasswordPage(Authentication auth, Model model) {
		String email = auth.getName();
		User userByEmail = uService.getUserByEmail(email);
		if(userByEmail.getPassword()==null) {
			return "user/set-password";
		}
		
		return "user/change-password";
	}
	
	@PostMapping("/profile/set-password")
	public String setPassword(@RequestParam String newPassword,
							  @RequestParam String confirmPassword,
							  Authentication auth,
							  RedirectAttributes ra) {
		try {
			uService.setPassword(auth.getName(), newPassword, confirmPassword);
			ra.addFlashAttribute("success", "Password has been set.");
			return "redirect:/user/profile";
		}
		catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		
		return "redirect:/user/profile/change-password";
	}
	
	@PostMapping("/profile/change-password")
	public String changePassword(@RequestParam String currentPassword,
								@RequestParam String newPassword,
								@RequestParam String confirmPassword,
								Authentication auth,
								RedirectAttributes ra) {
		try {
		uService.changePassword(auth.getName(), 
							    currentPassword, 
							    newPassword, 
							    confirmPassword);
		ra.addFlashAttribute("success","Password changed successfully");
		}
		catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		
		
		return "redirect:/user/profile/change-password";
	}
	
	@GetMapping("/medications")
	public String getMedicationPage(Authentication auth, Model model) {
		User userByEmail = uService.getUserByEmail(auth.getName());
		List<Pet> petsByOwner = pservice.getPetsByOwner(userByEmail);
		model.addAttribute("pets", petsByOwner);
		return "user/medications/view-medication";
	}
	
	@GetMapping("/medications/complete/{id}")
	public String completeMedicaton(@PathVariable Long id,
									HttpServletRequest req) {
		Medication medicationById = mservice.getMedicationById(id);
		medicationById.setStatus(MedicationStatus.COMPLETED);
		mrepo.save(medicationById);
		
		return "redirect:"+ req.getHeader("Referer");
	}
	
	@GetMapping("/forgot-password")
	public String getAccountRecoveryPage() {
		return "user/forgot-password-page";
	}
	
	@PostMapping("/searchAccount")
	public String searchAccount(@RequestParam String email, RedirectAttributes ra,Model model) {
		
		try{
			User userByEmail = uService.getUserByEmail(email);
		if(userByEmail!=null && userByEmail.getRole().equals("ROLE_USER")) {
			model.addAttribute("user", userByEmail);
			return "user/reset-password";
		}}catch (RuntimeException e) {
			ra.addFlashAttribute("error", e.getMessage());
			
		}
		
		return "redirect:/user/forgot-password";
	}
	
	@PostMapping("/send-reset-link")
	public String sendResetLink(@RequestParam Long userId) throws MessagingException {
		User userById = uService.getUserById(userId);
		rservice.sendResetPasswordLink(userById);
		//eservice.sendResetPasswordLink(userById);
		return "user/resetpassword-message";
	}
	
}
