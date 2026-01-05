package com.petcare.main.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.petcare.main.dto.UserDto;
import com.petcare.main.entities.User;
import com.petcare.main.repository.AdminRepo;
import com.petcare.main.service.AdminService;
import com.petcare.main.service.UserService;
import com.petcare.main.utilities.EmailService;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService uservice;
	
	@Autowired
	private AdminRepo arepo;
	
	@Autowired
	private AdminService aservice;
	
	@Autowired
	private EmailService eservice;
	
	@GetMapping
	public String getIndexPage() {
		return "admin/admin-index";
	}
	
	@GetMapping("/register")
	public String getAdminRegisterPage(Model model) {
		if(!model.containsAttribute("admin")) {
		model.addAttribute("admin", new User());
		}
		return "admin/admin-register-page";
	}
	
	@PostMapping("/registerAdmin")
	public String registerAdmin(@ModelAttribute("admin") User admin,
										RedirectAttributes redatt) {
		try {
		User saveAdmin=aservice.saveAdmin(admin);
		redatt.addFlashAttribute("success","Admin Registered Successfully");
		return"redirect:/admin/register";
		}
		catch (IllegalStateException e) {
			redatt.addFlashAttribute("admin", admin);
			if("Existing_email".equals(e.getMessage())) {
				redatt.addFlashAttribute("existingEmail","Email already registered!!!Please use another one");
			}
			else {
				redatt.addFlashAttribute("error","Admin Registration Failed");
				

			}
		}
		
		return"redirect:/admin/register";
	}
	
	@GetMapping("/loginPage")
	public String getAdminLoginPage() {
		
		return "admin/admin-login-page";
	}
	
	@GetMapping("/home")
	public String getAdminPage(Authentication auth, Model model) {
		String email = auth.getName();
		User admin = arepo.findByEmail(email);
		model.addAttribute("admin", admin);
		return "admin/admin-home";
	}
	
	@GetMapping("/users")
	public String manageUsers(@RequestParam(defaultValue = "0") int page  ,Model model)  {
		Page<UserDto> allUsers = uservice.getAllUsers(page);
		model.addAttribute("users", allUsers);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", allUsers.getSize());
		model.addAttribute("totalPages", allUsers.getTotalPages());
		
		return "admin/manage-users";
	}
	
	
	@GetMapping("/users/createUsers")
	public String getcreateUsersPage(Model model) {
		model.addAttribute("user",new User());
		return "admin/create-users";
	}
	
	@PostMapping("/users/createUser")
	public String createUsers(@ModelAttribute("user") User user,
										RedirectAttributes redatt) throws MessagingException {
		User saveUser=uservice.saveUser(user);
		if(saveUser!=null) {
			redatt.addFlashAttribute("success","User Created Successfully");
			return"redirect:/admin/users/createUsers";
		}
		else {
			redatt.addFlashAttribute("error","User Creation Failed");
			return"redirect:/admin/users/createUsers";

		}
	}
	
	@GetMapping("/users/editUser/{id}")
	public String getEditUserPage(@PathVariable("id") Long userId, Model model) {
		User userById = uservice.getUserById(userId);
		UserDto userDto = new UserDto();
		userDto.setId(userById.getId());
		userDto.setEmail(userById.getEmail());
		userDto.setGender(userById.getGender());
		userDto.setName(userById.getName());
		userDto.setRole(userById.getRole());
		userDto.setProvider(userById.getProvider());
		
		model.addAttribute("user",userDto);
		
		return "admin/edit-user";
	}
	
	@PostMapping("/users/updateUser")
	public String updateUser(@ModelAttribute("user") User user,
									@RequestParam(required = false) String newPassword,
										RedirectAttributes redatt) {
		
		uservice.updateUser(user, newPassword);
		redatt.addFlashAttribute("success","User Updated Successfully");
		
		return "redirect:/admin/users";
		
	}
	@GetMapping("/users/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") Long userId,
										RedirectAttributes redatt) {
		uservice.deleteUser(userId);
		redatt.addFlashAttribute("success","User deleted Successfully");
		return "redirect:/admin/users";
	
	}
	
	@GetMapping("/forgot-password")
	public String getForgotPasswordPage() {
		return "admin/forgot-password";
	}
	
    @PostMapping("/forgot-password")
    public String sendPasswordResetLink(@RequestParam String email,
    									RedirectAttributes ra) throws MessagingException {
    	Optional<User> adminByEmail = aservice.getAdminByEmail(email);
    	if(adminByEmail.isPresent()) {
    		eservice.sendResetPasswordLinkForAdmin(adminByEmail.get());
    		ra.addFlashAttribute("success","Reset Link Sent To Your Email.");
    		return "redirect:/admin/loginPage";
    	}
    	else {
    		ra.addFlashAttribute("error","Email Doesnot Exist!!!");
    		return "redirect:/admin/forgot-password";
    	}
    	
    }
    
    @GetMapping("/resetPassword")
    public String getResetPasswordPage(@RequestParam Long id,
    									Model model) {
    	User adminById = aservice.getAdminById(id);
    	model.addAttribute("admin", adminById);
    	return "admin/reset-password-page";
    }
	
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam Long id,
    							@RequestParam String newPassword,
    							@RequestParam String confirmPassword,
    							RedirectAttributes ra
    									) {
    	try {
    		aservice.resetPassword(id, newPassword, confirmPassword);
    		ra.addFlashAttribute("success","Password Changed");
    		return "redirect:/admin/loginPage";
    		
    	}
    	catch (IllegalArgumentException e) {
    		ra.addFlashAttribute("error",e.getMessage());
    		return "redirect:/admin/resetPassword?id="+id;
		}
    	
    }
}
		


